# -*- coding: utf-8 -*-

from neomodel import RelationshipFrom, ZeroOrMore
from models import Waypoint


class ClassRoom(Waypoint):
    """ClassRoom model"""

    groups = RelationshipFrom(
        'models.Group', 'STUDIES_IN', cardinality=ZeroOrMore)
