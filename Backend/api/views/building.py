import neomodel
from flask import jsonify
from flask_classful import FlaskView
# se poate folosi si marshmallow ca se se creeze o schema pentru args
from models import Building
from webargs import fields
from webargs.flaskparser import use_args


class BuildingView(FlaskView):
    base_args = ['args']
    excluded_methods = []  # metode care nu corespund unei rute

    # findAll
    def index(self):
        return jsonify(list(map(lambda building: building.__properties__, Building.nodes.all()))), 200

    def get(self, uid):  # trebuie schimbat sa returneze toate datele despre o cladire/ eventual cautand dupa nume
        building = Building.nodes.get_or_none(uid=uid)
        if building:
            return jsonify(building.__properties__)
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
