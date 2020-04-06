from grest import GRest
from models import Office


class OfficeRestView(GRest):
    """(/rest/office)"""
    __model__ = {"primary": Office}
    __selection_field__ = {"primary": "uid"}
    route_prefix = "/rest"
