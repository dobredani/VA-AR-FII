from flask_classful import FlaskView
from flask import jsonify
from webargs import fields
from webargs.flaskparser import use_args
from marshmallow import Schema
from models import Building, Connector, Group, Waypoint, StudiesIn, Teacher, ClassRoom, Office
import neomodel
from neomodel import db
from neomodel import Q


class NeighborsSchema(Schema):
    name: fields.Str(required=True)
    direction: fields.Str(required=True)


class ScheduleSchema(Schema):
    group: fields.Str(required=True)
    course: fields.Str(required=True)
    dayOfWeek: fields.Str(required=True)
    startTime: fields.Str(required=True)
    finishTime: fields.Str(required=True)


class WayPointSchema(Schema):
    name: fields.Str(required=True)
    type: fields.Str(required=False)
    schedule: fields.List(fields.Nested(ScheduleSchema), required=False)
    professors: fields.List(fields.Str, required=False)
    neighbors: fields.List(fields.Nested(NeighborsSchema))


class ConnectorSchema(Schema):
    name: fields.Str(required=True)
    accesibleFloors: fields.List(fields.Int, required=True)


class FloorSchema(Schema):
    level: fields.Int(required=True)
    wayPoints: fields.List(fields.Nested(WayPointSchema), required=True)


class BuildingSchema(Schema):
    name: fields.Str(required=True)
    floors: fields.List(fields.Nested(FloorSchema), required=True)
    connectors: fields.List(fields.Nested(ConnectorSchema))


class BuildingView(FlaskView):
    base_args = ['args']
    excluded_methods = []

    # findAll
    def index(self):
        return jsonify(list(map(lambda building: building.__properties__, Building.nodes.all()))), 200

    def get(self, name):
        building = Building.nodes.get_or_none(name=name)
        if building:
            map = {"name": building.name, "uid": building.uid,
                   "floors": [], "connectors": []}
            for floor in building.floors:
                map["floors"].append({"level": floor.level, "waypoints": []})
                for waypoint in floor.waypoints:
                    map["floors"][-1]["waypoints"].append(
                        {"uid": waypoint.uid, "name": waypoint.name})
                    if ClassRoom.__name__ in waypoint.labels():
                        map["floors"][-1]["waypoints"][-1]["type"] = "classRoom"
                        map["floors"][-1]["waypoints"][-1]["schedule"] = []
                        for group in [group for group in Group.nodes.has(classes=True) if group.classes.relationship(waypoint)]:
                            classes = group.classes.all_relationships(
                                waypoint)
                            for cclass in classes:
                                map["floors"][-1]["waypoints"][-1]["schedule"].append(
                                    {"group": group.name, "course": cclass.course, "dayOfWeek": cclass.dayOfWeek,
                                     "startTime": cclass.startTime, "finishTime": cclass.finishTime})
                    if Office.__name__ in waypoint.labels():
                        map["floors"][-1]["waypoints"][-1]["type"] = "office"
                        map["floors"][-1]["waypoints"][-1]["professors"] = []
                        for teacher in Teacher.nodes.has(office=True):
                            for office in teacher.office:
                                if office.name == waypoint.name:
                                    map["floors"][-1]["waypoints"][-1]["professors"].append(
                                        teacher.name)
                    if Connector.__name__ in waypoint.labels():
                        map["floors"][-1]["waypoints"][-1]["type"] = "connector"
                    map["floors"][-1]["waypoints"][-1]["neighbors"] = []
                    for neighbor in waypoint.neighbors:
                        map["floors"][-1]["waypoints"][-1]["neighbors"].append(
                            {"name": neighbor.name})
            for conn in Connector.nodes.all():
                map["connectors"].append({"name": conn.name, "floors": []})
                for floor in conn.floors:
                    map["connectors"][-1]["floors"].append(floor.level)

            return jsonify(map)
        else:
            return "Not Found", 404

    @use_args(BuildingSchema())
    def post(self, args):
        db.begin()
        try:
            Building(name=args["name"]).save()
            for floor in args["floors"]:
                Floor(level=floor["level"],
                      buildingName=args["name"]).save()
                for wayPoint in floor["wayPoints"]:
                    if (wayPoint["type"] == "classRoom"):
                        classRoom = ClassRoom(
                            name=wayPoint["name"], buildingName=args["name"], floorLevel=floor["level"]).save()
                        for orar in wayPoint["schedule"]:
                            group = Group(name=orar["group"]).save()
                            group.classes.connect(classRoom, {course: orar["course"],
                                                              dayOfWeek: orar["dayOfWeek"],
                                                              startTime: orar["startTime"],
                                                              finishTime: orar["finishTime"]})
                    elif (wayPoint["type"] == "connector"):
                        Connector(
                            name=wayPoint["name"], buildingName=args["name"], floorLevel=floor["level"]).save()
                    elif (wayPoint["type"] == "office"):
                        office = Office(
                            name=wayPoint["name"], buildingName=args["name"], floorLevel=floor["level"]).save()
                        for prof in wayPoint["professors"]:
                            teacher = Teacher(
                                name=professors["name"]).save()
                            teacher.office.connect(office)
                    else:
                        Waypoint(
                            name=wayPoint["name"], buildingName=args["name"], floorLevel=floor["level"]).save()

            for floor in args["floor"]:
                for wayPoint in floor["wayPoint"]:
                    base = Waypoint.nodes.get(
                        name=wayPoint["name"], buildingName=args["name"], floorLevel=floor["level"])
                    for neighbour in wayPoint["neighbours"]:
                        base.neighbors.connect(
                            Waypoint.nodes.get(
                                name=neighbour["name"], buildingName=args["name"], floorLevel=floor["level"]), direction=neighbour["direction"])
            db.commit()
            return self.get(args["name"]), 200
        except neomodel.UniqueProperty:
            return "Building already exists", 409
        except Exception as e:
            db.rollback()
            print(e)
            return str(e), 500

    def patch(self, args, uid):
        if not args.is_json:
            return jsonify({"err": "No JSON content received."}), 400
        else:
            return "Update a building"
