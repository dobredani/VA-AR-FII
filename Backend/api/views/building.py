from flask_classful import FlaskView
from flask import jsonify
from webargs import fields
from webargs.flaskparser import use_args
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
    startTime: fields.Str(required=True)  # este string?
    finishTime: fields.Str(required=True)  # same :))


class wayPointSchema(Schema):
    name: fields.Str(required=True)
    type: fields.Str(required=True, validate=validate.OneOf(
        ["office", "classRoom", "connector"]))
    schedule: fields.List(fields.Nested(ScheduleSchema), required=False)
    professors: fields.List(fields.Str, required=False)
    neighbors: fields.List(fields.Nested(NeighborsSchema))


class ConnectorSchema(Schema):
    name: fields.Str(required=True)
    accesibleFloors: fields.List(fields.Int, required=True)


class FloorSchema(Schema):
    level: fields.Int(required=True)
    wayPoints: fields.List(fields.Nested(wayPointSchema), required=True)  # ?


class BuildingSchema(Schema):
    name: fields.Str(required=True)
    floors: fields.List(fields.Nested(FloorSchema))
    connectors: fields.List(fields.Nested(ConnectorSchema))


class BuildingView(FlaskView):
    base_args = ['args']
    # metode care nu corespund unei rute
    excluded_methods = ['reverse_direction']

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

    def reverse_direction(direction):
        switcher = {
            "Left": "Right",
            "LEFT": "RIGHT",
            "Right": "Left",
            "RIGHT": "LEFT"
        }
        return switcher.get(direction, "Straight")

    @use_args({"name": fields.Str(required=True)})
    @use_args(BuildingSchema())
    def post(self, args):  # trebuie schimbat sa primeasca toate datele si sa le salveze

        if not args.is_json:
            return jsonify({"err": "No JSON content received."}), 400
        else:
            db.begin()  # incepem tranzactia
            try:
                Building(name=args["name"]).save()

                # primul loop creeaza toate nodurile din json
                for floor in args["floors"]:
                    for wayPoint in floor["wayPoints"]:
                        if (wayPoint["type"] == "classRoom"):
                            classRoom(name=wayPoint["name"]).save()
                            for orar in wayPoint["schedule"]:
                                # duplicate la grupa?
                                group(name=orar["group"]).save()

                        elif (wayPoint["type"] == "connector"):
                            connector(name=wayPoint["name"]).save()

                        elif (wayPoint["type"] == "office"):
                            office(name=wayPoint["name"]).save()
                            for prof in wayPoint["professors"]:
                                teacher(name=professors["name"]).save()

                        else:
                            return "Invalid type", 400  # ok?
                    Floor(level=floor["level"]).save()

                # introducem in BD relatiile dintre noduri
                for floor in args["floor"]:
                    etaj = floor.nodes.get(
                        Q(level=floor["level"]), Q(buildingName=args["name"]))
                    # cypher query pt nodurile cu label floor, level=floor["level"] si din building-ul args["name"]

                    for wayPoint in floor["wayPoint"]:

                        if(wayPoint["type"] == "classRoom"):
                            clasa = classRoom.nodes.get(name=wayPoint["name"])
                            # relatie floor->classRoom
                            etaj.waypoints.connect(clasa)

                            for schedule in wayPoint["schedule"]:
                                grupa = group.nodes.get(name=schedule["group"])
                                grupa.classes.connect(clasa, {course: schedule["course"],
                                                              dayOfWeek: schedule["dayOfWeek"],
                                                              startTime: schedule["startTime"],
                                                              finishTime: schedule["finishTime"]})  # relatia group->classRoom
                                # de verificat connect -relatia (daca face o relatie noua?)

                            for neighbour in wayPoint["neighbours"]:
                                vecin = neomodel.node.get(
                                    name=neighbour["name"])
                                clasa.neighbors.connect(
                                    vecin, direction=neighbour["direction"])
                                # relatia clasa->vecin
                                vecin.neighbors.connect(
                                    clasa, direction=reverse_direction(neighbour["direction"]))
                                # relatia vecin->clasa cu directia inversata

                        elif (wayPoint["type"] == "connector"):
                            conector = connector.nodes.get(
                                name=wayPoint["name"])  # ? e ok
                            # relatia floor->connector
                            etaj.floors.connect(conector)
                            # e ok relatia facuta asa sau: etaj.waypoints.connect(conector) ?

                            for neighbour in wayPoint["neighbours"]:
                                vecin = neomodel.node.get(
                                    name=neighbour["name"])
                                conector.neighbors.connect(
                                    vecin, direction=neighbour["direction"])
                                # relatia clasa->vecin
                                vecin.neighbors.connect(
                                    conector, direction=reverse_direction(neighbour["direction"]))
                                # relatia vecin->clasa cu directia inversata

                        elif(wayPoint["type"] == "office"):
                            cabinet = office.nodes.get(name=wayPoint["name"])
                            # relatia floor->office
                            etaj.waypoints.connect(cabinet)
                            for professors in wayPoint["professors"]:
                                prof = teacher.nodes.get(
                                    name=professors["name"])
                                # relatia teacher->office
                                prof.office.connect(cabinet)

                            for neighbour in wayPoint["neighbours"]:
                                vecin = neomodel.node.get(
                                    name=neighbour["name"])
                                cabinet.neighbors.connect(
                                    vecin, direction=neighbour["direction"])
                                # relatia clasa->vecin
                                vecin.neighbors.connect(
                                    cabinet, direction=reverse_direction(neighbour["direction"]))
                                # relatia vecin->clasa cu directia inversata

                return "Building created successfully", 200
            except Exception as e:
                db.rollback()
            except neomodel.UniqueProperty:
                return "Building already exists", 409

    def patch(self, args, uid):
        if not args.is_json:
            return jsonify({"err": "No JSON content received."}), 400
        else:
            return "Update a building"
