import json
import os

import pytest
from ..main import create_app
from flask import url_for
from neomodel import clear_neo4j_database, db

mock_building = open('mock_building.json').read()


def test_api_waypoint(client):
    """BuildingView:waypoint"""
    global mock_building
    buildingName = json.loads(mock_building)["name"]
    res = client.get(
        f"/building/{buildingName}/waypoints?start=1&destination=7")
    assert res.status_code == 404
    client.post("/building",
                data=mock_building,
                headers={'content-type': 'application/json'})
    res = client.get(
        f"/building/{buildingName}/waypoints?start=1&destination=7")
    assert res.status_code == 200
    assert len(res.json) > 0


@pytest.fixture
def app():
    app = create_app()
    clear_neo4j_database(db)
    app.debug = True
    app.threaded = True
    return app
