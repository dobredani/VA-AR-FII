
from grest import GRest
from models import ClassRoom


class ClassRoomRestView(GRest):
    """Person's View (/rest/classroom)"""
    __model__ = {"primary": ClassRoom}
    #__selection_field__ = {"primary": "name"} ca n-are fields
    route_prefix = "/rest"
