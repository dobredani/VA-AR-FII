# -*- coding: utf-8 -*-

from neomodel import StringProperty, IntegerProperty, StructuredNode, UniqueIdProperty, Relationship, StructuredRel, RelationshipFrom, One
from webargs import fields
from grest.models import Node


class GoesTo(StructuredRel):
    DIRECTIONS = {'STRAIGHT': 'Straight', 'LEFT': 'Left', 'RIGHT': 'Right'}
    direction = StringProperty(required=True, choices=DIRECTIONS)


class Waypoint(StructuredNode, Node):
    """Waypoint model"""
    __validation_rules__ = {
        "uid": fields.Str(),
        "name": fields.Str(required=True),
        "buildingName": fields.Str(required=True),
        "floorLevel": fields.Int(required=True),
    }

    uid = UniqueIdProperty()
    name = StringProperty(required=True)
    buildingName = StringProperty(required=True)
    floorLevel = IntegerProperty(required=True)
    building_unique_waypoint = StringProperty(required=True, unique_index=True)
    floor = RelationshipFrom('models.Floor', 'HAS', cardinality=One)
    neighbors = Relationship(
        'Waypoint', 'GOES_TO', cardinality=One, model=GoesTo)

    def pre_save(self):
        self.building_unique_waypoint = f'building_{self.buildingName}_waypoint_name_{self.name}'

    def post_save(self):
        self.floor.connect(Floor.nodes.get(
            buildingName=self.buildingName, level=self.floorLevel))
