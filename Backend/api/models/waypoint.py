# -*- coding: utf-8 -*-

from grest.models import Node
from neomodel import StringProperty, IntegerProperty, StructuredNode, UniqueIdProperty, RelationshipTo, StructuredRel, ZeroOrMore
from webargs import fields
from models import Building


class GoesTo(StructuredRel):
    DIRECTIONS = {'Straight': 'Straight', 'Left': 'Left', 'Right': 'Right'}
    REVERSE_DIRECTIONS = {'Straight': 'Straight',
                          'Left': 'Right', 'Right': 'Left'}
    floorLevel = IntegerProperty(required=True)
    direction = StringProperty(required=True, choices=DIRECTIONS)

    def post_save(self):
        if not self.end_node().neighbors.is_connected(self.start_node()):
            self.end_node().neighbors.connect(self.start_node(), {
                'floorLevel': self.floorLevel,
                'direction': self.REVERSE_DIRECTIONS.get(self.direction)})


class Waypoint(StructuredNode, Node):
    """Waypoint model"""
    __validation_rules__ = {
        "uid": fields.Str(),
        "name": fields.Str(required=True),
        "buildingName": fields.Str(required=True),
    }

    uid = UniqueIdProperty()
    name = StringProperty(required=True)
    buildingName = StringProperty(required=True)
    building_unique_waypoint = StringProperty(required=True, unique_index=True)
    neighbors = RelationshipTo(
        'Waypoint', 'GOES_TO', cardinality=ZeroOrMore, model=GoesTo)

    def pre_save(self):
        Building.nodes.get(name=self.buildingName)
        self.building_unique_waypoint = f'building_{self.buildingName}_waypoint_name_{self.name}'
