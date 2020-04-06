# -*- coding: utf-8 -*-

from models import Waypoint
from neomodel import RelationshipFrom, OneOrMore


class Connector(Waypoint):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        delattr(self, 'floor')

    floors = RelationshipFrom('models.Floor', 'HAS', cardinality=OneOrMore)
