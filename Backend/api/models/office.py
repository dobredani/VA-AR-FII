# -*- coding: utf-8 -*-

from neomodel import RelationshipFrom, ZeroOrMore
from models import Room


class Office(Room):
    """Office model"""

    teachers = RelationshipFrom(
        'models.Teacher', 'HAS', cardinality=ZeroOrMore)
