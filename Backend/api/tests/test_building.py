import json
import os

import pytest
from ..main import create_app
from flask import url_for
from neomodel import clear_neo4j_database, db

mock_building = open('mock_building.json').read()


def test_api_index(client):
    """BuildingView:index"""
    global mock_building
    res = client.get("/building", data="{}")
    assert res.status_code == 200
    assert res.json == []
    client.post("/building",
                data=mock_building,
                headers={'content-type': 'application/json'})
    res = client.get("/building", data="{}")
    assert res.status_code == 200
    assert res.json == [json.loads(mock_building)["name"]]


def test_api_get(client):
    """BuildingView:get"""
    global mock_building
    buildingName = json.loads(mock_building)["name"]
    res = client.get(f"/building/{buildingName}")
    assert res.status_code == 404
    client.post("/building",
                data=mock_building,
                headers={'content-type': 'application/json'})
    res = client.get(f"/building/{buildingName}")
    assert res.status_code == 200
    assert res.json["name"] == buildingName


def test_api_post(client):
    """BuildingView:post"""
    global mock_building
    res = client.post("/building",
                      data=mock_building,
                      headers={'content-type': 'application/json'})
    assert res.status_code == 200
    assert res.json["name"] == json.loads(mock_building)["name"]


def test_api_waypoint(client):
    """BuildingView:waypoint"""
    global mock_building
    buildingName = json.loads(mock_building)["name"]
    res = client.get(f"/building/{buildingName}/waypoints")
    assert res.status_code == 404
    client.post("/building",
                data=mock_building,
                headers={'content-type': 'application/json'})
    res = client.get(f"/building/{buildingName}/waypoints")
    assert res.status_code == 200
    assert len(res.json) > 0


@pytest.fixture
def app():
    app = create_app()
    clear_neo4j_database(db)
    app.debug = True
    app.threaded = True
    return app
