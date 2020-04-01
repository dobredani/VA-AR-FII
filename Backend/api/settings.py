# -*- coding: utf-8 -*-

import os

DEBUG = os.getenv("DEBUG", True)

IP_ADDRESS = os.getenv("IP_ADDRESS", "localhost")
PORT = os.getenv("PORT", 5000)

VERSION = os.getenv("VERSION", "0.0.1")
SECRET_KEY = os.getenv("SECRET_KEY", "")
DB_URL = os.getenv(
    "DB_URL", "bolt://neo4j:password@localhost:7687")

X_AUTH_TOKEN = "X-Auth-Token"
ENABLE_DELETE_ALL = os.getenv(
    "ENABLE_DELETE_ALL", "False")
