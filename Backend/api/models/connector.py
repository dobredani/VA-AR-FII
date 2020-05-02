# -*- coding: utf-8 -*-

from models import Waypoint
from neomodel import IntegerProperty, RelationshipTo, ZeroOrMore
from webargs import fields
from models import Floor, GoesTo


class Connector(Waypoint):
    """Connector model"""

    def pre_save(self):
        Floor.nodes.get(buildingName=self.buildingName, level=self.floorLevel)
        self.building_unique_waypoint = f'building_{self.buildingName}_floor_level_{self.floorLevel}_waypoint_name_{self.name}'
        self.building_unique_marker = f'building_{self.buildingName}_marker_id_{self.markerId}'

    def post_save(self):
        Floor.nodes.get(buildingName=self.buildingName,
                        level=self.floorLevel).waypoints.connect(self)
        otherFloorConnectors = Connector.nodes.filter(
            buildingName=self.buildingName, floorLevel__ne=self.floorLevel, name=self.name)
        for otherFloorConn in otherFloorConnectors:
            otherFloorConn.neighbors.connect(self, {
                'direction': GoesTo.DIRECTIONS['Straight']})
