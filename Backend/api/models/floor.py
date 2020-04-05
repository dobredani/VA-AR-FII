# -*- coding: utf-8 -*-

from neomodel import IntegerProperty, StringProperty, StructuredNode, UniqueIdProperty, RelationshipTo, ZeroOrMore, RelationshipFrom, One
from webargs import fields
from grest.models import Node
from models import Building


class Floor(StructuredNode, Node):
    """Floor model"""
    __validation_rules__ = {
        "uid": fields.Str(),
        "level": fields.Int(required=True),
        "buildingName": fields.Str(required=True),
    }

    uid = UniqueIdProperty()
    level = IntegerProperty(required=True)
    buildingName = StringProperty(required=True)
    building_unique_floor = StringProperty(required=True, unique_index=True)
    building = RelationshipFrom('models.Building', 'HAS', cardinality=One)
    waypoints = RelationshipTo(
        'models.Waypoint', 'HAS', cardinality=ZeroOrMore)

    def pre_save(self):
        Building.nodes.get(name=self.buildingName)
        self.building_unique_floor = f'building_{self.buildingName}_floor_level_{self.level}'

    def post_save(self):
        self.building.connect(Building.nodes.get(name=self.buildingName))
