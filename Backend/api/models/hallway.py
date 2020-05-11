# -*- coding: utf-8 -*-

from grest.models import Node
from neomodel import StringProperty, IntegerProperty, FloatProperty, StructuredNode, UniqueIdProperty, RelationshipFrom, One
from webargs import fields
from models import Building, Floor


class Hallway(StructuredNode, Node):
    """Hallway model"""
    __validation_rules__ = {
        "uid": fields.Str(),
        "name": fields.Str(required=True),
        'markerId': fields.Int(required=True),
        "buildingName": fields.Str(required=True),
        "floorLevel": fields.Int(required=True),
        'shapeType': fields.Str(required=False),
        'color': fields.Str(required=False),
        'width': fields.Float(required=False),
        'length': fields.Float(required=False),
        'x': fields.Float(required=False),
        'y': fields.Float(required=False),
    }

    uid = UniqueIdProperty()
    name = StringProperty(required=True, index=True)
    markerId = IntegerProperty(required=True, index=True)
    buildingName = StringProperty(required=True, index=True)
    floorLevel = IntegerProperty(required=True, index=True)
    shapeType = StringProperty(required=False)
    color = StringProperty(required=False)
    width = FloatProperty(required=False)
    length = FloatProperty(required=False)
    x = FloatProperty(required=False)
    y = FloatProperty(required=False)
    floor = RelationshipFrom('models.Floor', 'HAS', cardinality=One)

    def pre_save(self):
        Floor.nodes.get(buildingName=self.buildingName, level=self.floorLevel)

    def post_save(self):
        Floor.nodes.get(buildingName=self.buildingName,
                        level=self.floorLevel).hallways.connect(self)
