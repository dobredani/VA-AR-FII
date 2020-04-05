# -*- coding: utf-8 -*-

from flask import Flask

import settings
from rest_views import *
from views import *


def create_app():
    app = Flask(__name__)

    @app.route('/')
    def index():
        
        return "Hello World", 200

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

    # custom endpoints
    BuildingView.register(
        app, route_base="/building", trailing_slash=False)

    return app


if __name__ == '__main__':
    app = create_app()
    app.run(debug=settings.DEBUG,
            host=settings.IP_ADDRESS,
            port=settings.PORT,
            threaded=True)
