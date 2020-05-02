# -*- coding: utf-8 -*-

import neomodel
from flask import Flask
import settings
from rest_views import *
from views import *
import models
from flask_swagger_ui import get_swaggerui_blueprint


def create_app():
    app = Flask(__name__)

    @app.route('/')
    def index():
        return "Hello World"

    @app.route('/static/<path:path>')
    def send_static(path):
        return send_from_directory('static', path)

    SWAGGER_URL = '/swagger'
    API_URL = '/static/swagger.json'
    swaggerui_blueprint = get_swaggerui_blueprint(
        SWAGGER_URL,
        API_URL,
        config={
            'app_name': "IP-A3-2020-Orientation-mobile-APP"
        }
    )
    app.register_blueprint(swaggerui_blueprint, url_prefix=SWAGGER_URL)

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
