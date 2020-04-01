# -*- coding: utf-8 -*-

from neomodel import StringProperty, StructuredNode, UniqueIdProperty, RelationshipTo, StructuredRel, ZeroOrMore
from webargs import fields
from grest.models import Node


class StudiesIn(StructuredRel):
    course = StringProperty(required=True)
    dayOfWeek = StringProperty(required=True)
    startTime = StringProperty(required=True)
    finishTime = StringProperty(required=True)


class Group(StructuredNode, Node):
    """Waypoint model"""
    __validation_rules__ = {
        "uid": fields.Str(),
        "name": fields.Str(required=True),
        "buildingName": fields.Str(required=True),
    }

    uid = UniqueIdProperty()
    name = StringProperty(required=True)
    buildingName = StringProperty(required=True)
    building_unique_group = StringProperty(required=True, unique_index=True)
    classes = RelationshipTo(
        'models.ClassRoom', 'STUDIES_IN', cardinality=ZeroOrMore, model=StudiesIn)

    def pre_save(self):
        self.building_unique_group = f'building_{self.buildingName}_group_name_{self.name}'
