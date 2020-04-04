
from grest import GRest
from models import Connector


class ConnectorRestView(GRest):
    """Person's View (/rest/connector)"""
    __model__ = {"primary": Connector}
   # __selection_field__ = {"primary": "name"} -- nu stiu ce sa-i pun, preia nume de la waypoint automat si-l pun aici?
    route_prefix = "/rest"
