# -*- coding: utf-8 -*-

from neomodel import RelationshipFrom, OneOrMore
from models import Waypoint


class Connector(Waypoint):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        delattr(self, 'floor')
    floors = RelationshipFrom('models.Floor', 'HAS', cardinality=OneOrMore)
    pass
