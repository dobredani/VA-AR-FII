from grest import GRest
from models import Floor, Waypoint


class FloorRestView(GRest):
    """(/rest/floor)"""
    __model__ = {"primary": Floor}
    __selection_field__ = {"primary": "uid"}
    route_prefix = "/rest"
