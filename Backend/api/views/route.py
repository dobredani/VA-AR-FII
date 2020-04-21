from flask_classful import FlaskView, route
from flask import jsonify
from webargs import fields
from webargs.flaskparser import use_args
from models import Building, Room


class RouteView(FlaskView):
    base_args = ['args']
    excluded_methods = ['findRoute', 'formatRoute']

    def findRoute(self, buildingName, startMarkerId, destinationMarkerId):
        # startWaypoint = Room.nodes.get(
        #     buildingName=buildingName, markerId=startMarkerId)
        # destinationWaypoint = Room.nodes.get(
        #     buildingName=buildingName, markerId=destinationMarkerId)
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
