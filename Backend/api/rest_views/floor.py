from grest import GRest
from models import Floor, Waypoint


class FloorRestView(GRest):
    """Person's View (/rest/floor)"""
    __model__ = {"primary": Floor,
                 "secondary": {
                     "waypoints": Waypoint
                 }}
    __selection_field__ = {"primary": "uid",
                           "secondary": {
                               "waypoints": "uid"
                           }}
    route_prefix = "/rest"
