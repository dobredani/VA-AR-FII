
from grest import GRest
from models import Group, ClassRoom


class GroupRestView(GRest):
    """Person's View (/rest/group)"""
    __model__ = {"primary": Group,
                 "secondary": {
                     "classroom": ClassRoom
                 }}
    __selection_field__ = {"primary": "uid",
                           "secondary": {
                               "classroom": "uid"
                           }}
    route_prefix = "/rest"
