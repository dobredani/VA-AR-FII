# -*- coding: utf-8 -*-

from neomodel import RelationshipFrom, ZeroOrMore
from models import Waypoint


class Office(Waypoint):
    """Office model"""

    teachers = RelationshipFrom(
        'models.Teacher', 'HAS', cardinality=ZeroOrMore)
