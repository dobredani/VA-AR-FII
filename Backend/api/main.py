# -*- coding: utf-8 -*-

import logging
import os

import neomodel
from flask import Flask

import config

def create_app():
    app = Flask(__name__)

    @app.route('/')
    def index():
        return "Hello World"

    neomodel.config.DATABASE_URL = config.DB_URL
    neomodel.config.AUTO_INSTALL_LABELS = True
    neomodel.config.FORCE_TIMEZONE = True

    app.ext_logger = app.logger

    return app


if __name__ == '__main__':
    app = create_app()
    app.run(debug=config.DEBUG,
            host=config.IP_ADDRESS,
            port=config.PORT,
            threaded=True)
