
from grest import GRest
from models import Building


class BuildingRestView(GRest):
    """Person's View (/rest/building)"""
    __model__ = {"primary": Building}
    __selection_field__ = {"primary": "name"}
    route_prefix = "/rest"
