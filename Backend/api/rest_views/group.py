
from grest import GRest
from models import Group


class GroupRestView(GRest):
    """Person's View (/rest/group)"""
    __model__ = {"primary": Group}
    __selection_field__ = {"primary": "name"}
    route_prefix = "/rest"
