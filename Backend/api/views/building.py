from flask_classful import FlaskView, route
from flask import jsonify
from webargs import fields, validate
from webargs.flaskparser import use_args
from models import Building, Floor, Waypoint, GoesTo, Connector, Group, Teacher, ClassRoom, Office
from neomodel import db

WAYPOINT_TYPES = {"ClassRoom": "classRoom",
                  "Office": "office", "Connector": "connector"}

BuildingSchema = {
    'name': fields.Str(required=True),
    'floors': fields.List(fields.Nested({
        'level': fields.Int(required=True),
        'waypoints': fields.List(fields.Nested({
            'name': fields.Str(required=True),
            'markerId': fields.Int(required=True),
            'type': fields.Str(required=False, validate=validate.OneOf(list(WAYPOINT_TYPES.values()))),
            'schedule': fields.List(fields.Nested({
                'group': fields.Str(required=True),
                'course': fields.Str(required=True),
                'dayOfWeek': fields.Str(required=True),
                'startTime': fields.Str(required=True),
                'finishTime': fields.Str(required=True)}), required=False),
            'professors': fields.List(fields.Str(), required=False),
            'neighbors': fields.List(fields.Nested({
                'name': fields.Str(required=True),
                'direction': fields.Str(required=True, validate=validate.OneOf(list(GoesTo.DIRECTIONS.values())))
            }))}), required=True)}), required=True),
}


class BuildingView(FlaskView):
    base_args = ['args']
    excluded_methods = ['createBuilding']

    # findAll
    def index(self):
        return jsonify(list(map(lambda building: building.name, Building.nodes.all()))), 200

    def get(self, name):
        try:
            building = Building.nodes.get_or_none(name=name)
            if building is not None:
                map = {"name": building.name,
                       "floors": []}
                for floor in building.floors:
                    map["floors"].append(
                        {"level": floor.level, "waypoints": []})
                    for waypoint in floor.waypoints:
                        map["floors"][-1]["waypoints"].append(
                            {"name": waypoint.name, "markerId": waypoint.markerId})
                        if ClassRoom.__name__ in waypoint.labels():
                            map["floors"][-1]["waypoints"][-1]["type"] = WAYPOINT_TYPES["ClassRoom"]
                            map["floors"][-1]["waypoints"][-1]["schedule"] = []
                            for group in [group for group in Group.nodes.filter(buildingName=name).has(classes=True) if group.classes.relationship(waypoint)]:
                                classes = group.classes.all_relationships(
                                    waypoint)
                                for cclass in classes:
                                    map["floors"][-1]["waypoints"][-1]["schedule"].append(
                                        {"group": group.name, "course": cclass.course, "dayOfWeek": cclass.dayOfWeek,
                                         "startTime": cclass.startTime, "finishTime": cclass.finishTime})
                        elif Office.__name__ in waypoint.labels():
                            map["floors"][-1]["waypoints"][-1]["type"] = WAYPOINT_TYPES["Office"]
                            map["floors"][-1]["waypoints"][-1]["professors"] = []
                            for teacher in [teacher for teacher in Teacher.nodes.filter(buildingName=name).has(office=True) if teacher.office.relationship(waypoint)]:
                                map["floors"][-1]["waypoints"][-1]["professors"].append(
                                    teacher.name)
                        elif Connector.__name__ in waypoint.labels():
                            map["floors"][-1]["waypoints"][-1]["type"] = WAYPOINT_TYPES["Connector"]

                        map["floors"][-1]["waypoints"][-1]["neighbors"] = []
                        for neighbor in waypoint.neighbors:
                            if waypoint.name != neighbor.name:
                                direction = waypoint.neighbors.relationship(
                                    neighbor).direction
                                map["floors"][-1]["waypoints"][-1]["neighbors"].append(
                                    {"name": neighbor.name, "direction": direction})
                return jsonify(map)
            else:
                return "Not Found", 404

        except Exception as e:
            return str(e), 500

    def createBuilding(self, building):
        db.begin()
        try:
            Building(name=building["name"]).save()
            for floor in building["floors"]:
                Floor(level=floor["level"],
                      buildingName=building["name"]).save()
                for waypoint in floor["waypoints"]:
                    if ("type" in waypoint):
                        if (waypoint["type"] == WAYPOINT_TYPES["ClassRoom"]):
                            classRoom = ClassRoom(
                                name=waypoint["name"], markerId=waypoint["markerId"], buildingName=building["name"], floorLevel=floor["level"]).save()
                            for schedule in waypoint["schedule"]:
                                group = Group(
                                    name=schedule["group"], buildingName=building["name"]).save()
                                group.classes.connect(classRoom, {'course': schedule["course"],
                                                                  'dayOfWeek': schedule["dayOfWeek"],
                                                                  'startTime': schedule["startTime"],
                                                                  'finishTime': schedule["finishTime"]})
                        elif (waypoint["type"] == WAYPOINT_TYPES["Office"]):
                            office = Office(
                                name=waypoint["name"], markerId=waypoint["markerId"], buildingName=building["name"], floorLevel=floor["level"]).save()
                            for prof in waypoint["professors"]:
                                teacher = Teacher(
                                    name=prof, buildingName=building["name"]).save()
                                teacher.office.connect(office)
                        elif (waypoint["type"] == WAYPOINT_TYPES["Connector"]):
                            Connector(
                                name=waypoint["name"], markerId=waypoint["markerId"], buildingName=building["name"], floorLevel=floor["level"]).save()
                        else:
                            Waypoint(
                                name=waypoint["name"], markerId=waypoint["markerId"], buildingName=building["name"], floorLevel=floor["level"]).save()
                    else:
                        Waypoint(
                            name=waypoint["name"], markerId=waypoint["markerId"], buildingName=building["name"], floorLevel=floor["level"]).save()

            for floor in building["floors"]:
                for waypoint in floor["waypoints"]:
                    base = Waypoint.nodes.get(
                        name=waypoint["name"], floorLevel=floor["level"], buildingName=building["name"])
                    for neighbor in waypoint["neighbors"]:
                        base.neighbors.connect(
                            Waypoint.nodes.get(
                                name=neighbor["name"], floorLevel=floor["level"], buildingName=building["name"]), {'direction': neighbor["direction"]})

            db.commit()
            return self.get(building["name"]), 200
        except Exception as e:
            db.rollback()
            return str(e), 500

    @use_args(BuildingSchema)
    def post(self, args):
        return self.createBuilding(args)

    def delete(self, name):
        try:
            building = Building.nodes.get_or_none(name=name)
            if building is not None:
                db.begin()
                db.cypher_query(
                    f"MATCH (n) WHERE n.buildingName = '{name}' DETACH DELETE n")
                building.delete()
                db.commit()
                return 'Building successfully deleted', 200
            else:
                return "Not Found", 404
        except Exception as e:
            db.rollback()
            return str(e), 500

    @use_args(BuildingSchema)
    def patch(self, args):
        res, status_code = self.delete(args["name"])
        if (status_code != 200):
            return res, status_code
        return self.createBuilding(args)

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
