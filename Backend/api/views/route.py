from flask_classful import FlaskView, route
from flask import jsonify
from neomodel import db
from webargs import fields
from webargs.flaskparser import use_args
from models import Building, Waypoint, Connector


class RouteView(FlaskView):
    base_args = ['args']
    excluded_methods = ['findRoute', 'formatRoute']

    def findRoute(self, buildingName, startMarkerId, destinationMarkerId):
        [[[route]]], meta = db.cypher_query(
            f"MATCH (start:Waypoint {{markerId:{startMarkerId}, buildingName: '{buildingName}'}}), (end:Waypoint {{markerId:{destinationMarkerId}, buildingName: '{buildingName}'}}), p=shortestPath((start)-[:GOES_TO*]-(end)) RETURN NODES(p)",
            resolve_objects=True)
        return route

    def formatRoute(self, route):
        formattedRoute = []
        lastDirection = ''
        for index in range(len(route) - 1):
            currWaypoint = route[index]
            nextWaypoint = route[index + 1]
            direction = currWaypoint.neighbors.relationship(
                nextWaypoint).direction
            if direction == 'right':
                direction = 'left'
            elif direction == 'left':
                direction = 'right'
            if lastDirection != 'straight' or lastDirection != direction or Connector.__name__ in currWaypoint.labels():
                formattedRoute.append(
                    {'markerId': currWaypoint.markerId, 'name': currWaypoint.name,
                     'floor': currWaypoint.floorLevel, 'direction': direction})
            lastDirection = direction
        finalWaypoint = route[-1]
        formattedRoute.append(
            {'markerId': finalWaypoint.markerId, 'name': finalWaypoint.name,
             'floor': finalWaypoint.floorLevel, })
        return formattedRoute

    @route('<buildingName>')
    @use_args({'start': fields.Int(required=True), 'destination': fields.Int(required=True)})
    def route(self, args, buildingName):
        building = Building.nodes.get_or_none(name=buildingName)
        if building is not None:
            route = self.findRoute(
                buildingName, args['start'], args['destination'])
            return jsonify(self.formatRoute(route)), 200
        else:
            return "Building not found", 404
