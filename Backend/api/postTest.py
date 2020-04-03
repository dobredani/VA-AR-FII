import requests

URL = "http://localhost:5000/building"

data = {"name" : "Building2"}

r = requests.post(URL, data)
