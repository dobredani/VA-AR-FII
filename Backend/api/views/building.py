from flask_classful import FlaskView, route
from flask import jsonify
from webargs import fields
from webargs.flaskparser import use_args
from models import Building, Floor, Waypoint, Room, Connector, Group, Teacher, ClassRoom, Office
from neomodel import db

BuildingSchema = {
    'name': fields.Str(required=True),
    'floors': fields.List(fields.Nested({
        'level': fields.Int(required=True),
        'waypoints': fields.List(fields.Nested({
            'name': fields.Str(required=True),
            'markerId': fields.Int(required=False),
            'type': fields.Str(required=False),
            'schedule': fields.List(fields.Nested({
                'group': fields.Str(required=True),
                'course': fields.Str(required=True),
                'dayOfWeek': fields.Str(required=True),
                'startTime': fields.Str(required=True),
                'finishTime': fields.Str(required=True)}), required=False),
            'professors': fields.List(fields.Str(), required=False),
            'neighbors': fields.List(fields.Nested({
                'name': fields.Str(required=True),
                'direction': fields.Str(required=True)
            }))}), required=True)}), required=True),
    'connectors': fields.List(fields.Nested({
        'name': fields.Str(required=True),
        'accessibleFloors': fields.List(fields.Int, required=True)
    }), required=True)
}


class BuildingView(FlaskView):
    base_args = ['args']
    excluded_methods = []

    # findAll
    def index(self):
        return jsonify(list(map(lambda building: building.name, Building.nodes.all()))), 200

    def get(self, name):
        try:
            building = Building.nodes.get_or_none(name=name)
            if building is not None:
                map = {"name": building.name,
                       "floors": [], "connectors": []}
                for floor in building.floors:
                    map["floors"].append(
                        {"level": floor.level, "waypoints": []})
                    for waypoint in floor.waypoints:
                        if Connector.__name__ in waypoint.labels():
                            map["floors"][-1]["waypoints"].append(
                                {"name": waypoint.name, "type": "connector"})
                        else:
                            map["floors"][-1]["waypoints"].append(
                                {"name": waypoint.name, "markerId": waypoint.markerId})
                            if ClassRoom.__name__ in waypoint.labels():
                                map["floors"][-1]["waypoints"][-1]["type"] = "classRoom"
                                map["floors"][-1]["waypoints"][-1]["schedule"] = []
                                for group in [group for group in Group.nodes.filter(buildingName=name).has(classes=True) if group.classes.relationship(waypoint)]:
                                    classes = group.classes.all_relationships(
                                        waypoint)
                                    for cclass in classes:
                                        map["floors"][-1]["waypoints"][-1]["schedule"].append(
                                            {"group": group.name, "course": cclass.course, "dayOfWeek": cclass.dayOfWeek,
                                             "startTime": cclass.startTime, "finishTime": cclass.finishTime})
                            elif Office.__name__ in waypoint.labels():
                                map["floors"][-1]["waypoints"][-1]["type"] = "office"
                                map["floors"][-1]["waypoints"][-1]["professors"] = []
                                for teacher in [teacher for teacher in Teacher.nodes.filter(buildingName=name).has(office=True) if teacher.office.relationship(waypoint)]:
                                    for office in teacher.office:
                                        if office.name == waypoint.name:
                                            map["floors"][-1]["waypoints"][-1]["professors"].append(
                                                teacher.name)
                            map["floors"][-1]["waypoints"][-1]["neighbors"] = []
                            for neighbor in [neighbor for neighbor in waypoint.neighbors if waypoint.neighbors.relationship(
                                    neighbor).floorLevel == floor.level]:
                                direction = waypoint.neighbors.relationship(
                                    neighbor).direction
                                map["floors"][-1]["waypoints"][-1]["neighbors"].append(
                                    {"name": neighbor.name, "direction": direction})
                for conn in Connector.nodes.filter(buildingName=name):
                    map["connectors"].append(
                        {"name": conn.name, "accessibleFloors": []})
                    for floor in conn.floors:
                        map["connectors"][-1]["accessibleFloors"].append(
                            floor.level)
                    map["connectors"][-1]["accessibleFloors"].sort()

                return jsonify(map)
            else:
                return "Not Found", 404

        except Exception as e:
            return str(e), 500

    @use_args(BuildingSchema)
    def post(self, args):
        db.begin()
        try:
            Building(name=args["name"]).save()
            for floor in args["floors"]:
                Floor(level=floor["level"],
                      buildingName=args["name"]).save()
                for waypoint in floor["waypoints"]:
                    if ("type" in waypoint):
                        if (waypoint["type"] == "classRoom"):
                            classRoom = ClassRoom(
                                name=waypoint["name"], markerId=waypoint["markerId"], buildingName=args["name"], floorLevel=floor["level"]).save()
                            for schedule in waypoint["schedule"]:
                                group = Group(
                                    name=schedule["group"], buildingName=args["name"]).save()
                                group.classes.connect(classRoom, {'course': schedule["course"],
                                                                  'dayOfWeek': schedule["dayOfWeek"],
                                                                  'startTime': schedule["startTime"],
                                                                  'finishTime': schedule["finishTime"]})
                        elif (waypoint["type"] == "office"):
                            office = Office(
                                name=waypoint["name"], markerId=waypoint["markerId"], buildingName=args["name"], floorLevel=floor["level"]).save()
                            for prof in waypoint["professors"]:
                                teacher = Teacher(
                                    name=prof, buildingName=args["name"]).save()
                                teacher.office.connect(office)
                        elif (waypoint["type"] == "connector"):
                            connector = Connector.nodes.get_or_none(
                                name=waypoint["name"], buildingName=args["name"])
                            if connector is None:
                                Connector(
                                    name=waypoint["name"], buildingName=args["name"]).save()
                        else:
                            Room(
                                name=waypoint["name"], markerId=waypoint["markerId"], buildingName=args["name"], floorLevel=floor["level"]).save()
                    else:
                        Room(
                            name=waypoint["name"], markerId=waypoint["markerId"], buildingName=args["name"], floorLevel=floor["level"]).save()

            for floor in args["floors"]:
                for waypoint in floor["waypoints"]:
                    base = Waypoint.nodes.get(
                        name=waypoint["name"], buildingName=args["name"])
                    for neighbour in waypoint["neighbors"]:
                        base.neighbors.connect(
                            Waypoint.nodes.get(
                                name=neighbour["name"], buildingName=args["name"]), {'floorLevel': floor["level"],
                                                                                     'direction': neighbour["direction"]})

            for conn in args["connectors"]:
                connector = Connector.nodes.get(
                    name=conn["name"], buildingName=args["name"])
                for floorLevel in conn["accessibleFloors"]:
                    connector.floors.connect(Floor.nodes.get(
                        level=floorLevel, buildingName=args["name"]))

            db.commit()
            return self.get(args["name"]), 200
        except Exception as e:
            db.rollback()
            return str(e), 500

    # noinspection PyUnreachableCode
    @route('<buildingName>/waypoints')
    def waypoint(self, buildingName):
        building = Building.nodes.get_or_none(name=buildingName)
        if building is not None:
            floors = []
            for floor in building.floors:
                floors.append({"floorLevel": floor.level, "waypoints": list(map(
                    lambda waypoint: waypoint.name, floor.waypoints))})
            return jsonify(floors), 200
        else:
            return "Building not found", 404

    @use_args(BuildingSchema)
    def patch(self, args):
        self.delete(args)
        self.post(args)

    @use_args(BuildingSchema)
    def delete(self, args):
        db.begin()
        building = Building.nodes.get_or_none(name=args["name"])
        if building is None:
            return "Building not found", 404
        else:
            Building(name=args["name"]).delete()
            for floor in args["floors"]:
                Floor(level=floor["level"],
                      buildingName=args["name"]).delete()
                for waypoint in floor["waypoints"]:
                    if ("type" in waypoint):
                        if (waypoint["type"] == "classRoom"):
                            classRoom = ClassRoom(
                                name=waypoint["name"], markerId=waypoint["markerId"], buildingName=args["name"],
                                floorLevel=floor["level"]).delete()
                            for schedule in waypoint["schedule"]:
                                group = Group(
                                    name=schedule["group"], buildingName=args["name"]).delete()
                        elif (waypoint["type"] == "office"):
                            office = Office(
                                name=waypoint["name"], markerId=waypoint["markerId"], buildingName=args["name"],
                                floorLevel=floor["level"]).delete()
                            for prof in waypoint["professors"]:
                                teacher = Teacher(
                                    name=prof, buildingName=args["name"]).delete()
                        elif (waypoint["type"] == "connector"):
                            connector = Connector.nodes.get_or_none(
                                name=waypoint["name"], buildingName=args["name"])
                            if connector is None:
                                Connector(
                                    name=waypoint["name"], buildingName=args["name"]).delete()
                        else:
                            Room(
                                name=waypoint["name"], markerId=waypoint["markerId"], buildingName=args["name"],
                                floorLevel=floor["level"]).delete()
                    else:
                        Room(
                            name=waypoint["name"], markerId=waypoint["markerId"], buildingName=args["name"],
                            floorLevel=floor["level"]).delete()
        db.commit()
        if self.get_or_none(args["name"]) is None:
            return "The building was removed", 200
