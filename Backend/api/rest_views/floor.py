
from grest import GRest
from models import Floor


class FloorRestView(GRest):
    """Person's View (/rest/floor)"""
    __model__ = {"primary": Floor}
    __selection_field__ = {"primary": "uid"}
    route_prefix = "/rest"
