import os
from dotenv import load_dotenv, find_dotenv
load_dotenv(find_dotenv())

os.system("neomodel_install_labels main.py models --db " + os.getenv("DB_URL"))
