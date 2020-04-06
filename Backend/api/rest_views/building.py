from grest import GRest
from models import Building, Floor


class BuildingRestView(GRest):
    """(/rest/building)"""
    __model__ = {"primary": Building,
                 "secondary": {
                     "floors": Floor
                 }}
    __selection_field__ = {"primary": "name",
                           "secondary": {
                               "floors": "uid"
                           }}
    route_prefix = "/rest"
