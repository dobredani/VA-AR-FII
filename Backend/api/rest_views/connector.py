from grest import GRest
from models import Connector


class ConnectorRestView(GRest):
    """(/rest/connector)"""
    __model__ = {"primary": Connector}
    __selection_field__ = {"primary": "uid"}
    route_prefix = "/rest"
