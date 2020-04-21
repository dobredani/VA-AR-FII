# -*- coding: utf-8 -*-

from models import Waypoint
from neomodel import IntegerProperty, RelationshipTo, ZeroOrMore
from webargs import fields
from models import Floor


class Connector(Waypoint):
    """Connector model"""

    floors = RelationshipTo('models.Floor', 'GOES_TO', cardinality=ZeroOrMore)
