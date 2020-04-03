import json

from models import Building

building = Building.nodes.get_or_none(name="Building0")

print(building)
