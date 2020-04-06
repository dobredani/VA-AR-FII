from flask_classful import FlaskView
from flask import jsonify
from webargs import fields
from webargs.flaskparser import use_args
from models import Building, Connector, Group, Waypoint, StudiesIn, Teacher, ClassRoom, Office
import neomodel


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

    @use_args({"name": fields.Str(required=True)})
    def post(self, args):
        try:
            Building(name=args["name"]).save()
            return "Building created successfully", 200
        except neomodel.UniqueProperty:
            return "Building already exists", 409

    def patch(self, args, uid):
        return "Update a building"
