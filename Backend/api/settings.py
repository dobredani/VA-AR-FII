# -*- coding: utf-8 -*-
import os
from dotenv import load_dotenv, find_dotenv
load_dotenv(find_dotenv())


DEBUG = os.getenv("DEBUG")

IP_ADDRESS = os.getenv("IP_ADDRESS")
PORT = os.getenv("PORT")

VERSION = os.getenv("VERSION", "0.0.1")
SECRET_KEY = os.getenv("SECRET_KEY", "")
DB_URL = os.getenv(
    "DB_URL", "bolt://neo4j:admin@localhost:7687")
VERSION = os.getenv("VERSION")
SECRET_KEY = os.getenv("SECRET_KEY")
DB_URL = os.getenv("DB_URL")

X_AUTH_TOKEN = os.getenv("X_AUTH_TOKEN")
ENABLE_DELETE_ALL = os.getenv("ENABLE_DELETE_ALL")
