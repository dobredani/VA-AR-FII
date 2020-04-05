# API

## Technologies used

We are using a boilerplate called gREST which utilizes Flask-classful for building REST APIs and Neo4j for databases, interacting with it using Neomodel.

gRest provides functionality for bulding REST APIs with Neo4j very quickly by automatically creating CRUD endpoints for a Neomodel model by creating a GRest view and assigning it the desired model.

## Install Neo4j

We need neo4j version 3.3 for neomodel to work. To install that I recommend using docker.

```bash
cd neo4j
docker build -t neo4j3 .
docker run -d --name neo4j --publish=7474:7474 --publish=7687:7687  --volume=$HOME/neo4j/data:/data neo4j3
```

If you can't use docker and you are using windows you can easily install using chocolatey.
https://chocolatey.org/packages/neo4j-community/3.3.1

After installing make sure that the created container is running.

If it is running you can access the Neo4j browser on http://localhost:7474/

```bash
docker start neo4j
```

When the container is running you can access the neo4j browser by going to http://localhost:7474/

## Install dependencies

For installing dependencies, use the following command:

```bash
pip install -r requirements.txt
```

## Settings configuration

For development configuration the dotenv package is used to read settings from .env file and use them in settings.py.
So create an .env file with help of the .env.dist file.

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
from grest.models import Node

# noinspection PyAbstractClass
class ExampleNode(StructuredNode, Node):
    """ExampleNode model"""
    __validation_rules__ = {
        "uid": fields.Str(),
    }

    uid = UniqueIdProperty()
```

### rest_views folder

In this folder we will create a new file to define a gRest views for all the models.
Here you can import a model from the models folder to use it for a GRest view.

```/rest_views/example.py
from grest import GRest
from models import ExampleNode

class ExampleView(GRest):
    """Example View (/rest/example)"""
    __model__ = {"primary": Example}
    __selection_field__ = {"primary": "uid"}
```

### views folder

In this folder we will create a new file to define a custom view.
For this use flask, flask_classful, webargs and marshmallow. The view should inherit from FlaskView from flask_classful.
You can define normal methods which will wap to a HTTP method or create custom methods with custom routes using @route() from flask_classful.
You can also define helper methods which will not correspond to any route, but be sure to include them in excluded_methods.
To validate arguments from a request you can use @use_args from webargs.flaskparser and specify the fields using the fields from webargs or create a schema for the arguments using marshmallow and attach the schema to a method using use_args.

```/views/example.py
rom flask_classful import FlaskView
from webargs import fields
from webargs.flaskparser import use_args

class ExampleView(FlaskView):
    base_args = ['args']
    excluded_methods = [helperMethod]

    # GET /route
    def index(self):
        return "Hello world", 200

    # GET /route/:id
    def get(self, id):
        return "I will get it"

    def helperMethod(self):
        return "I help"

```

### main.py

This folder is responsible for setting up the config variables (database config), installing all the labels from the models, registering the views and starting the app.
Import the created views and register them at the end of the `create_app` function.

```main.py
...
from views import ExampleView
...
def create_app():
    ...
    ExampleView.register(app, route_base="/example", trailing_slash=False)
    return app
...
```
