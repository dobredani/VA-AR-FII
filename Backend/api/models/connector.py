# -*- coding: utf-8 -*-

from models import Waypoint
from neomodel import IntegerProperty, RelationshipFrom, ZeroOrMore
from webargs import fields
from models import Floor


class Connector(Waypoint):
    """Connector model"""

    floors = RelationshipFrom('models.Floor', 'HAS', cardinality=ZeroOrMore)
