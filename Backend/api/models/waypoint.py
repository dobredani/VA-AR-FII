# -*- coding: utf-8 -*-

from grest.models import Node
from neomodel import StringProperty, IntegerProperty, FloatProperty, StructuredNode, UniqueIdProperty, RelationshipFrom, One, RelationshipTo, StructuredRel, ZeroOrMore
from webargs import fields
from models import Building, Floor


class GoesTo(StructuredRel):
    DIRECTIONS = {'Straight': 'Straight', 'Left': 'Left', 'Right': 'Right'}
    REVERSE_DIRECTIONS = {'Straight': 'Straight',
                          'Left': 'Right', 'Right': 'Left'}
    direction = StringProperty(required=True, choices=DIRECTIONS)

    def post_save(self):
        if not self.end_node().neighbors.is_connected(self.start_node()):
            self.end_node().neighbors.connect(self.start_node(), {
                'direction': self.REVERSE_DIRECTIONS.get(self.direction)})


class Waypoint(StructuredNode, Node):
    """Waypoint model"""
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
    building_unique_waypoint = StringProperty(required=True, unique_index=True)
    building_unique_marker = StringProperty(required=True, unique_index=True)
    neighbors = RelationshipTo(
        'Waypoint', 'GOES_TO', cardinality=ZeroOrMore, model=GoesTo)

    def pre_save(self):
        Floor.nodes.get(buildingName=self.buildingName, level=self.floorLevel)
        self.building_unique_waypoint = f'building_{self.buildingName}_waypoint_name_{self.name}'
        self.building_unique_marker = f'building_{self.buildingName}_marker_id_{self.markerId}'

    def post_save(self):
        Floor.nodes.get(buildingName=self.buildingName,
                        level=self.floorLevel).waypoints.connect(self)
