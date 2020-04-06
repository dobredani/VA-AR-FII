from grest import GRest
from models import Teacher, Office


class TeacherRestView(GRest):
    """(/rest/teacher)"""
    __model__ = {"primary": Teacher,
                 "secondary": {
                     "office": Office
                 }}
    __selection_field__ = {"primary": "uid",
                           "secondary": {
                               "office": "uid"
                           }}
    route_prefix = "/rest"
