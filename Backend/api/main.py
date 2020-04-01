# -*- coding: utf-8 -*-

import logging
import os

import neomodel
from flask import Flask
import settings
import models
from rest_views import *


def create_app():
    app = Flask(__name__)

    @app.route('/')
    def index():
        return "Hello World"

    neomodel.config.DATABASE_URL = settings.DB_URL
    neomodel.config.ENCRYPTED_CONNECTION = False
    neomodel.config.FORCE_TIMEZONE = True

    app.ext_logger = app.logger

    # rest endpoints
    BuildingRestView.register(
        app, route_base="/building", trailing_slash=False)
    FloorRestView.register(
        app, route_base="/floor", trailing_slash=False)
    # custom endpoints

    return app


if __name__ == '__main__':
    app = create_app()
    neomodel.install_all_labels()
    app.run(debug=settings.DEBUG,
            host=settings.IP_ADDRESS,
            port=settings.PORT,
            threaded=True)
