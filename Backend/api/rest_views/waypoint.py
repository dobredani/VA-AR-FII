from grest import GRest
from models import Waypoint


class WaypointRestView(GRest):
    """(/rest/waypoint)"""
    __model__ = {"primary": Waypoint,
                 "seconday": {
                     "waypoints": Waypoint
                 }}
    __selection_field__ = {"primary": "uid",
                           "secondary": {
                               "waypoints": "uid"
                           }}
    route_prefix = "/rest"
