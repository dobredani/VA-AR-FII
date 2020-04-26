from flask_classful import FlaskView, route
from flask import jsonify
from neomodel import db
from webargs import fields
from webargs.flaskparser import use_args
from models import Building, Room, Waypoint, Connector


class RouteView(FlaskView):
    base_args = ['args']
    excluded_methods = ['findRoute', 'formatRoute']

    def findRoute(self, buildingName, startMarkerId, destinationMarkerId):
        result, meta = db.cypher_query(
            f"MATCH (start:Waypoint {{markerId:{startMarkerId}, buildingName: '{buildingName}'}}), (end:Waypoint {{markerId:{destinationMarkerId}, buildingName: '{buildingName}'}}), p=shortestPath((start)-[:GOES_TO*]-(end)) RETURN p")
        waypoints = []
        for record in result:
            nodes = record[0].nodes
            # Convert neo4j nodes to neomodel nodes
            waypoints = [Waypoint.inflate(node) for node in nodes]

        return waypoints
        pass

    def formatRoute(self, route):
        formatted_route = []
        for index in range(len(route)):
            if index != len(route) - 1:
                current_dir = route[index].neighbors.relationship(route[index + 1]).direction
                floor_level = route[index].neighbors.relationship(route[index + 1]).floorLevel
            else:
                room = Room.nodes.get(name=route[index].name, buildingName=route[index].buildingName)
                formatted_route.append({'name': room.name, 'markerId': room.markerId})
                return formatted_route
            if Connector.__name__ in route[index].labels():
                look_ahead = 0
                while Connector.__name__ in route[index + look_ahead].labels():
                    look_ahead += 1
                room = Room.nodes.get_or_none(name=route[index + look_ahead].name,
                                              buildingName=route[index + look_ahead].buildingName)
                formatted_route.append({'name': route[index].name, 'direction': current_dir,
                                        'floor': floor_level})
            else:
                room = Room.nodes.get(name=route[index].name, buildingName=route[index].buildingName)

                formatted_route.append(
                    {'name': room.name, 'markerId': room.markerId, 'direction': current_dir,
                     'floor': floor_level})

        pass

    @route('<buildingName>')
    @use_args({'start': fields.Int(required=True), 'destination': fields.Int(required=True)})
    def route(self, args, buildingName):
        building = Building.nodes.get_or_none(name=buildingName)
        if building is not None:
            route = self.findRoute(buildingName, args['start'], args['destination'])
            return jsonify(self.formatRoute(route)), 200
        else:
            return "Building not found", 404
