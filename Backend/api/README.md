# API

## Technologies used

We are using a boilerplate called gREST which utilizes Flask-classful for building REST APIs and Neo4j for databases, interacting with it using Neomodel.

gRest provides functionality for bulding REST APIs with Neo4j very quickly by automatically creating CRUD endpoints for a Neomodel model by creating a GRest view and assigning it the desired model.

## Install Neo4j

The easiest way is to use docker with the provided image from neo4j

```bash
docker pull neo4j
docker run -d --name neo4j --publish=7474:7474 --publish=7687:7687  --volume=$HOME/neo4j/data:/data neo4j
```

After installing make sure that the created container is running

```bash
docker start neo4j
```

When the container is running you can access the neo4j browser by going to http://localhost:7474/

## Install dependencies

For installing dependencies, use the following command:

```bash
pip install -r requirements.txt
```

## Running application

For running the application, use the following command:

```bash
python main.py
```

The app will be accessible on http://localhost:PORT/, PORT being 5000 by default.

## Project structure

### models folder

In this folder create a new file to define each model used in the app.
Define the model using neomodel and grest.models

```/models/example.py
from neomodel import StringProperty, StructuredNode, UniqueIdProperty
from webargs import fields
from grest import models

# noinspection PyAbstractClass
class ExampleNode(StructuredNode, models.Node):
    """ExampleNode model"""
    __validation_rules__ = {
        "uid": fields.Str(),
    }

    uid = UniqueIdProperty()
```

### views folder

In this folder create a new file to define each view (endpoint).
Here you can import a model from the models folder to use it for a GRest view or in a custom view.

```/views/example.py
from grest import GReest
from models.example import ExampleNode

class ExampleView(GRest):
    """Example View (/example)"""
    __model__ = {"primary": Example}
    __selection_field__ = {"primary": "uid"}
```

### main.py

This folder is responsible for setting up the config variables (database config), registering the views and starting the app.
Import the creates views from the views folder and register them at the end of the `create_app` function.

```main.py
...
from views.example import ExampleView
...
def create_app():
    ...
    ExampleView.register(app, route_base="/example", trailing_slash=False)
    return app
...
```
