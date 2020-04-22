from flask_classful import FlaskView, route
from flask import jsonify
from webargs import fields
from webargs.flaskparser import use_args
from models import Building, Room


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
        pass

    @route('<buildingName>')
    @use_args({'start': fields.Int(required=True), 'destination': fields.Int(required=True)})
    def route(self, args, buildingName):
        building = Building.nodes.get_or_none(name=buildingName)
        if building is not None:
            route = findRoute(buildingName, args['start'], args['destination'])
            return jsonify(formatRoute(route)), 200
        else:
            return "Building not found", 404
