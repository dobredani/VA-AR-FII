from flask_classful import FlaskView
from flask import jsonify
from webargs import fields
from webargs.flaskparser import use_args
from marshmallow import Schema,validate
# se poate folosi si marshmallow ca se se creeze o schema pentru args
from models import Building
import neomodel
from neomodel import db
from neomodel import Q

class NeighborsSchema(Schema):
    name:fields.Str(required=True)
    direction: fields.Str(required=True)

class ScheduleSchema(Schema):
    group: fields.Str(required=True)
    course: fields.Str(required=True)
    dayOfWeek: fields.Str(required=True)
    startTime: fields.Str(required=True) #este string?
    finishTime:fields.Str(required=True) #same :))

class wayPointSchema(Schema):
    name: fields.Str(required=True)
    type: fields.Str(required=True,validate=validate.OneOf(["office", "classRoom", "connector"])) 
    schedule: fields.List(fields.Nested(ScheduleSchema),required=False)
    professors: fields.List(fields.Str,required=False)
    neighbors: fields.List(fields.Nested(NeighborsSchema))

class ConnectorSchema(Schema):
    name: fields.Str(required=True)
    accesibleFloors: fields.List(fields.Int,required=True)

class FloorSchema(Schema):
    level:fields.Int(required=True)
    wayPoints: fields.List(fields.Nested(wayPointSchema),required=True) #?

class BuildingSchema(Schema):
    name:fields.Str(required=True)
    floors:fields.List(fields.Nested(FloorSchema))
    connectors: fields.List(fields.Nested(ConnectorSchema))


class BuildingView(FlaskView):
    base_args = ['args']
    excluded_methods = ['reverse_direction']  # metode care nu corespund unei rute

    # findAll
    def index(self):
        return jsonify(list(map(lambda building: building.__properties__, Building.nodes.all()))), 200

    def get(self, uid):  # trebuie schimbat sa returneze toate datele despre o cladire/ eventual cautand dupa nume
        building = Building.nodes.get_or_none(uid=uid)
        if building:
            return jsonify(building.__properties__)
        else:
            return "Not Found", 404


    def reverse_direction(direction):
        switcher={
            "Left":"Right",
            "LEFT":"RIGHT",
            "Right":"Left",
            "RIGHT":"LEFT"
        }
        return switcher.get(direction,"Straight")

    @use_args({"name": fields.Str(required=True)})
    @use_args(BuildingSchema())
    def post(self, args):  # trebuie schimbat sa primeasca toate datele si sa le salveze

        if not args.is_json:
            return jsonify({"err": "No JSON content received."}), 400
        else:
            db.begin() #incepem tranzactia
            try: 
                Building(name=args["name"]).save()

                #primul loop creeaza toate nodurile din json
                for floor in args["floors"]:
                    for wayPoint in floor["wayPoints"]:
                        if (wayPoint["type"] == "classRoom"):
                                classRoom(name=wayPoint["name"]).save() 
                                for orar in wayPoint["schedule"]:
                                    group(name = orar["group"]).save() #duplicate la grupa?
                        
                        elif (wayPoint["type"] == "connector"): 
                                connector(name=wayPoint["name"]).save() 

                        elif (wayPoint["type"] == "office"):
                                office(name=wayPoint["name"]).save()
                                for prof in wayPoint["professors"]:
                                    teacher(name=professors["name"]).save()
     
                        else:
                            return "Invalid type", 400 # ok?
                    Floor(level=floor["level"]).save()

                #introducem in BD relatiile dintre noduri
                for floor in args["floor"]:
                    etaj=floor.nodes.get( Q(level=floor["level"]), Q(buildingName=args["name"]) )
                    #cypher query pt nodurile cu label floor, level=floor["level"] si din building-ul args["name"]
                    
                    for wayPoint in floor["wayPoint"]:

                        if(wayPoint["type"] == "classRoom"):
                            clasa=classRoom.nodes.get(name=wayPoint["name"])
                            etaj.waypoints.connect(clasa) #relatie floor->classRoom

                            for schedule in wayPoint["schedule"]:
                                grupa=group.nodes.get(name=schedule["group"])
                                grupa.classes.connect(clasa,{course : schedule["course"],
                                    dayOfWeek : schedule["dayOfWeek"],
                                    startTime : schedule["startTime"],
                                    finishTime : schedule["finishTime"]}) #relatia group->classRoom
                                    #de verificat connect -relatia (daca face o relatie noua?)

                            for neighbour in wayPoint["neighbours"]:
                                vecin=neomodel.node.get(name=neighbour["name"])
                                clasa.neighbors.connect(vecin,direction = neighbour["direction"]) 
                                #relatia clasa->vecin
                                vecin.neighbors.connect(clasa,direction = reverse_direction(neighbour["direction"]))
                                #relatia vecin->clasa cu directia inversata
                        

                        elif (wayPoint["type"] == "connector"): 
                            conector=connector.nodes.get(name=wayPoint["name"]) #? e ok 
                            etaj.floors.connect(conector) #relatia floor->connector
                            # e ok relatia facuta asa sau: etaj.waypoints.connect(conector) ?

                            for neighbour in wayPoint["neighbours"]:
                                vecin=neomodel.node.get(name=neighbour["name"])
                                conector.neighbors.connect(vecin,direction = neighbour["direction"]) 
                                #relatia clasa->vecin
                                vecin.neighbors.connect(conector,direction = reverse_direction(neighbour["direction"]))
                                #relatia vecin->clasa cu directia inversata
                                

                        elif(wayPoint["type"]=="office"):
                            cabinet=office.nodes.get(name=wayPoint["name"])
                            etaj.waypoints.connect(cabinet); #relatia floor->office
                            for professors in wayPoint["professors"]:
                                prof=teacher.nodes.get(name=professors["name"])
                                prof.office.connect(cabinet) #relatia teacher->office

                            for neighbour in wayPoint["neighbours"]:
                                vecin=neomodel.node.get(name=neighbour["name"])
                                cabinet.neighbors.connect(vecin,direction = neighbour["direction"]) 
                                #relatia clasa->vecin
                                vecin.neighbors.connect(cabinet,direction = reverse_direction(neighbour["direction"]))
                                #relatia vecin->clasa cu directia inversata

                return "Building created successfully", 200
            except Exception as e:
                db.rollback()
            except neomodel.UniqueProperty:
                return "Building already exists", 409


    def patch(self, args, uid):
        if not args.is_json:
            return jsonify({"err": "No JSON content received."}), 400
        else:
            return "Update a building"
