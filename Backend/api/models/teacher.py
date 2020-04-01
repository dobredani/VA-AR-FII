# -*- coding: utf-8 -*-

from neomodel import StringProperty, StructuredNode, UniqueIdProperty, RelationshipTo, One
from webargs import fields
from grest.models import Node


class Teacher(StructuredNode, Node):
    """Teacher model"""
    __validation_rules__ = {
        "uid": fields.Str(),
        "name": fields.Str(required=True),
        "buildingName": fields.Str(required=True),
    }

    uid = UniqueIdProperty()
    name = StringProperty(required=True)
    buildingName = StringProperty(required=True)
    building_unique_teacher = StringProperty(required=True, unique_index=True)
    office = RelationshipTo('models.Office', 'HAS', cardinality=One)

    def pre_save(self):
        self.building_unique_teacher = f'building_{self.buildingName}_teacher_name_{self.name}'
