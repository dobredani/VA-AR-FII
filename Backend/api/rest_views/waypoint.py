
from grest import GRest
from models import Waypoint


class WaypointRestView(GRest):
    """Person's View (/rest/waypoint)"""
    __model__ = {"primary": Waypoint}
    __selection_field__ = {"primary": "name"}
    route_prefix = "/rest"
