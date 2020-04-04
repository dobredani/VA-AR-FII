
from grest import GRest
from models import Teacher


class TeacherRestView(GRest):
    """Person's View (/rest/teacher)"""
    __model__ = {"primary": Teacher}
    __selection_field__ = {"primary": "name"}
    route_prefix = "/rest"
