# -*- coding: utf-8 -*-

from grest.models import Node
from neomodel import StringProperty, StructuredNode, UniqueIdProperty, RelationshipTo, ZeroOrMore
from webargs import fields


class Building(StructuredNode, Node):
    """Building model"""
    __validation_rules__ = {
        "uid": fields.Str(),
        "name": fields.Str(required=True),
    }

    uid = UniqueIdProperty()
    name = StringProperty(unique_index=True, required=True)
    floors = RelationshipTo('models.Floor', 'HAS', cardinality=ZeroOrMore)
