from flask_classful import FlaskView, route
from flask import jsonify
from neomodel import db
from webargs import fields
from webargs.flaskparser import use_args
from models import Building, Room, Waypoint


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
        last_dir = 'null'
        formatted_route = []
        for index in range(len(route) - 1):
            # get the direction between the current node and the next
            current_dir = route[index].neighbors.relationship(route[index + 1]).direction
            # if the last direction and the current one aren't Straight
            if (last_dir != 'Straight') or (current_dir != 'Straight'):
                # add to the list of waypoints
                formatted_route.append({route[index].name: current_dir})
            last_dir = current_dir
        return formatted_route
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
