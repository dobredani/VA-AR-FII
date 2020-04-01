# -*- coding: utf-8 -*-

from neomodel import RelationshipFrom, OneOrMore
from models import Waypoint


class Connector(Waypoint):
    def __init__(self):
        delattr(self.__class__, 'floor')
    floors = RelationshipFrom('models.Floor', 'HAS', cardinality=OneOrMore)
    pass
