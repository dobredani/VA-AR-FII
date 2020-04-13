# -*- coding: utf-8 -*-

from neomodel import RelationshipFrom, ZeroOrMore
from models import Room


class ClassRoom(Room):
    """ClassRoom model"""

    groups = RelationshipFrom(
        'models.Group', 'STUDIES_IN', cardinality=ZeroOrMore)
