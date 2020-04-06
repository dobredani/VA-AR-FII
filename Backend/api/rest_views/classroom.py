from grest import GRest
from models import ClassRoom


class ClassRoomRestView(GRest):
    """(/rest/classroom)"""
    __model__ = {"primary": ClassRoom}
    __selection_field__ = {"primary": "uid"}
    route_prefix = "/rest"
