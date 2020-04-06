from flask_classful import FlaskView
from flask import jsonify
from webargs import fields
from webargs.flaskparser import use_args
# se poate folosi si marshmallow ca se se creeze o schema pentru args
from models import Building, Connector, Group, Waypoint, StudiesIn, Teacher
import neomodel


def test():
    A = Group(name='A3', buildingName='Building0').save()
    B = Waypoint.nodes.get_or_none(name='testRoom3')
    C = A.classes.connect(B, {'course': 'BD', 'dayOfWeek': 'Monday', 'startTime': '12:00', 'finishTime': '14:00'})
    pass


class BuildingView(FlaskView):
    base_args = ['args']
    excluded_methods = [test]  # metode care nu corespund unei rute

    # findAll
    def index(self):
        return jsonify(list(map(lambda building: building.__properties__, Building.nodes.all()))), 200

    def get(self, name):  # trebuie schimbat sa returneze toate datele despre o cladire/ eventual cautand dupa nume
        building = Building.nodes.get_or_none(name=name)
        map = {"name": building.name, "uid": building.uid, "floors": [], "connectors": []}
        if building:
            for floor in building.floors:
                map["floors"].append({"level": floor.level, "waypoints": []})
                for nodes in floor.waypoints:
                    map["floors"][-1]["waypoints"].append(
                        {"uid": nodes.uid, "floorLevel": nodes.floorLevel, "name": nodes.name})
                    if "ClassRoom" in nodes.labels():
                        map["floors"][-1]["waypoints"][-1]["type"] = "ClassRoom"
                        map["floors"][-1]["waypoints"][-1]["schedule"] = []
                        for group in Group.nodes.has(classes=True):
                            classes = group.classes.relationship(nodes)
                            if isinstance(classes, StudiesIn):
                                classes = group.classes.all_relationships(nodes)
                                for current in classes:
                                    map["floors"][-1]["waypoints"][-1]["schedule"].append(
                                        {"group": group.name, "course": current.course, "dayOfWeek": current.dayOfWeek,
                                         "startTime": current.startTime, "finishTime": current.finishTime})
                    if "Office" in nodes.labels():
                        map["floors"][-1]["waypoints"][-1]["type"] = "Office"
                        map["floors"][-1]["waypoints"][-1]["professors"] = []
                        for teacher in Teacher.nodes.has(office=True):
                            for office in teacher.office:
                                if office.name == nodes.name:
                                   map["floors"][-1]["waypoints"][-1]["professors"].append(teacher.name)
                    if "Connector" in nodes.labels():
                        map["floors"][-1]["waypoints"][-1]["type"] = "Connector"
                    map["floors"][-1]["waypoints"][-1]["neighbors"] = []
                    for neighbor in nodes.neighbors:
                        map["floors"][-1]["waypoints"][-1]["neighbors"].append({"name": neighbor.name})
            for conn in Connector.nodes.all():
                map["connectors"].append({"name": conn.name, "floors": []})
                for floor in conn.floors:
                    map["connectors"][-1]["floors"].append(floor.level)

            return jsonify(map)
        else:
            return "Not Found", 404

    @use_args({"name": fields.Str(required=True)})
    def post(self, args):  # trebuie schimbat sa primeasca toate datele si sa le salveze
        try:
            Building(name=args["name"]).save()
            return "Building created successfully", 200
        except neomodel.UniqueProperty:
            return "Building already exists", 409

    def patch(self, args, uid):
        return "Update a building"
