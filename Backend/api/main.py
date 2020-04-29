# -*- coding: utf-8 -*-

import neomodel
from flask import Flask
import settings
from rest_views import *
from views import *
import models



def create_app():
    app = Flask(__name__)

    @app.route('/')
    def index():
        return "Hello World"

    neomodel.config.DATABASE_URL = settings.DB_URL
    neomodel.config.AUTO_INSTALL_LABELS = False
    neomodel.config.ENCRYPTED_CONNECTION = False
    neomodel.config.FORCE_TIMEZONE = True

    app.ext_logger = app.logger

    # /rest endpoints
    BuildingRestView.register(
        app, route_base="/building", trailing_slash=False)
    FloorRestView.register(
        app, route_base="/floor", trailing_slash=False)
    WaypointRestView.register(
        app, route_base="/waypoint", trailing_slash=False)
    ClassRoomRestView.register(
        app, route_base="/classroom", trailing_slash=False)
    ConnectorRestView.register(
        app, route_base="/connector", trailing_slash=False)
    OfficeRestView.register(
        app, route_base="/office", trailing_slash=False)
    GroupRestView.register(
        app, route_base="/group", trailing_slash=False)
    TeacherRestView.register(
        app, route_base="/teacher", trailing_slash=False)

    # custom endpoints
    BuildingView.register(
        app, route_base="/building", trailing_slash=False)
    RouteView.register(
        app, route_base="/route", trailing_slash=False)

    return app


if __name__ == '__main__':
    app = create_app()
    app.run(debug=settings.DEBUG,
            host=settings.IP_ADDRESS,
            port=settings.PORT,
            threaded=True)
