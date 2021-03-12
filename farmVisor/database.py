from sqlalchemy import create_engine
from sqlalchemy.orm import scoped_session, sessionmaker
from sqlalchemy.ext.declarative import declarative_base
from models import Bid,Crop,Farmer,Job,Land,Equipment,Investment,Requests
from flask import jsonify
import datetime
import requests

#import pandas as pd
#from pandas_highcharts import core

engine = create_engine('sqlite:///farmVisor.db', convert_unicode=True)
db_session = scoped_session(sessionmaker(autocommit=False,
                                         autoflush=True,
                                         bind=engine))
Base = declarative_base()
Base.query = db_session.query_property()

def init_db():
    # import all modules here that might define models so that
    # they will be registered properly on the metadata.  Otherwise
    # you will have to import them first before calling init_db()
    Base.metadata.create_all(bind=engine)
    
def serialize_job(jobs):
    serializeJobs=[]
    for job in jobs:
        jobDictionary={}
        jobDictionary['jobId']=job.id
        jobDictionary['jobTitle']=job.title
        jobDictionary['jobContact']=job.contact
        jobDictionary['jobDescription']=job.description
        jobDictionary['jobPayment']=job.payment
        jobDictionary['farmer_id']=job.farmer_id
        serializeJobs.append(jobDictionary)
    return serializeJobs
    
# def serialize_crop(crops):
#     serializeCrops=[]
#     for crop in crops:
#         cropDictionary={}
#         cropDictionary['cropId']=crop.id
#         cropDictionary['cropName']=crop.name
#         cropDictionary['cropPrice']=crop.price
#         cropDictionary['cropDescription']=crop.description
#         cropDictionary['cropQuantity']=crop.quantity
#         cropDictionary['farmer_id']=crop.farmer_id
#         serializeCrops.append(cropDictionary)
#     return serializeCrops

def serialize_crop(crops):
    serializeCrops=[]
    for crop in crops:
        cropDictionary={}
        cropDictionary['cropId']=crop.id
        cropDictionary['farmer_id']=crop.farmer_id
        cropDictionary['cropName']=crop.name
        cropDictionary['cropPrice']=crop.price
        cropDictionary['cropDescription']=crop.description
        cropDictionary['cropQuantity']=crop.quantity
        cropDictionary['bid_date']=crop.bid_date
        cropDictionary['cropUrl']=crop.image
        
        
        serializeCrops.append(cropDictionary)
    return serializeCrops

    
def serialize_bid(bids):
    serializeBids=[]
    for bid in bids:
        bidDictionary={}
        bidDictionary['bidId']=bid.id
        bidDictionary['cropUrl']=bid.crop.image
        bidDictionary['bidPrice']=bid.price_quote
        bidDictionary['bidDate'] = bid.bid_date.strftime('%d/%m/%Y')
        bidDictionary['cropName']=bid.crop.name
        bidDictionary['farmer']=bid.farmer.fname
        bidDictionary['status']=bid.status
        
  
        serializeBids.append(bidDictionary)
    return serializeBids


    
def serialize_bids_for_crop(bids):
    serializeBids=[]
    for bid in bids:
        bidDictionary={}
        bidDictionary['bidId']=bid.id
        bidDictionary['cropPrice']=bid.crop.price
        bidDictionary['bidPrice']=bid.price_quote
        bidDictionary['bidDate'] = bid.bid_date.strftime('%d/%m/%Y')
        bidDictionary['farmerName']=bid.farmer.fname
        bidDictionary['farmerPhone']=bid.farmer.contact
        bidDictionary['status']=bid.status
        
  
        serializeBids.append(bidDictionary)
    return serializeBids


def serialize_land(lands):
    serializeLands=[]
    for land in lands:
        landDictionary={}
        landDictionary['id']=land.id
        landDictionary['farmer_id']=land.farmer_id
        landDictionary['area_size']=land.area_size
        landDictionary['soil_type']=land.soil_type
        landDictionary['land_mark']=land.land_mark
        landDictionary['cost_year']=land.cost_year
        landDictionary['years']=land.years
        landDictionary['contact']=land.contact
        landDictionary['description']=land.description
        landDictionary['coords']=land.coords
        serializeLands.append(landDictionary)
    return serializeLands

def serialize_invest(invests):
    serializeInvests=[]
    for invest in invests:
        investDictionary={}
        investDictionary['id']=invest.id
        investDictionary['farmer_id']=invest.farmer_id
        investDictionary['area_size']=invest.area_size
        investDictionary['soil_type']=invest.soil_type
        investDictionary['land_mark']=invest.land_mark
        investDictionary['investments']=invest.investments
        investDictionary['years']=invest.years
        investDictionary['contact']=invest.contact
        investDictionary['description']=invest.description
        serializeInvests.append(investDictionary)
    return serializeInvests
        
def serialize_equipment(equipments):
    serializeEquipments=[]
    for equipment in equipments:
        equipmentDictionary={}
        equipmentDictionary['id']=equipment.id
        equipmentDictionary['name']=equipment.name
        equipmentDictionary['farmer_id']=equipment.farmer_id
        equipmentDictionary['model']=equipment.model
        equipmentDictionary['rent_price']=equipment.rent_price
        equipmentDictionary['contact']=equipment.contact
        equipmentDictionary['description']=equipment.description
        serializeEquipments.append(equipmentDictionary)
    return serializeEquipments
    
def serialize_request(requests):
    s_requests=[]
    for request in requests:
        d={}
        d['id']=request.id
        d['request_for']=request.request_for
        d['rfarmer_id']=request.rfarmer_id
        d['farmer_name']=request.farmer.fname+" " +request.farmer.lname
        d['farmer_contact']=request.farmer.contact
        d['object_id']=request.object_id
        s_requests.append(d)
    return s_requests
    
def add_farmer(fname,lname,contact,password, kisan_number):
    try:
        db_session.add(Farmer(fname=fname, lname=lname,contact=contact,password=password, kisan_number=kisan_number))
        db_session.commit()
        return jsonify({'register':'OK'})
    except Exception as e:
        print(e)
        return str(e)

def verify_farmer(username, password):
    try:
        former = db_session.query(Farmer).filter(Farmer.contact == username, Farmer.password==password, Farmer.status == 'accepted').one()
        
        return former
    except Exception as e:
        print(e);return " either username/password is wrong or your registration has not been approved"

def admin_verify_farmer(farmer_id, status):
    try:
        farmer = db_session.query(Farmer).filter(Farmer.id == farmer_id).one()
        farmer.status = status
        db_session.commit()
        return {'status':'success'}
    except Exception as e:
        return {'status':"failure"}

def get_unverified_farmers():
    farmers = db_session.query(Farmer).filter(Farmer.status == "pending").all()
    return farmers

def get_farmer_id(id):
    # print([e.id for e in db_session.query(Farmer).all()])
    return db_session.query(Farmer).filter(Farmer.id == id).one()

def get_bids_of_crop(crop_id):
    try:
        bids = db_session.query(Bid).filter(Bid.crop_id == crop_id).order_by(Bid.price_quote.desc())
        
        return jsonify({'bids':serialize_bids_for_crop(bids)})
    except Exception as e:
        print(e)
        return []

    

def get_farmer(username):
    try:
        former = db_session.query(Farmer).filter(Farmer.contact == username).one()
        db_session.commit()
        return former
    except Exception as e:
        print(str(e))
        return None
    
def addJob(title,farmer_id,contact,description,payment):
    try:
        db_session.add(Job(title=title,farmer_id=farmer_id,contact=contact,description=description,payment=payment))
        db_session.commit()
        return jsonify({'job':'added'})
    except Exception as e:
        return str(e)

def acceptBid(bid_id):
    try:
        for bid in db_session.query(Bid).filter(Bid.id == bid_id ):
                bid.status = 'accepted'
        db_session.commit()
        return jsonify({'status':"success"})
    except Exception as e:
        print(e)
        return jsonify({'status':'failure'})

def payBid(bid_id):
    fphone = None
    try:
        for bid in db_session.query(Bid):
            print(bid.id, bid_id)
            if bid.id == eval(bid_id):
                print("inside!!")
                bid.status = 'paid'
                fphone = bid.crop.farmer.contact
            else:
                bid.status = "rejected"
        db_session.commit()
        return jsonify({'status':"success"}),fphone
    except Exception as e:
        print(e)
        return jsonify({'status':'failure'})
                
def saveBid(farmer_id,crop_id,bid_price):
    try:
        db_session.add(Bid(farmer_id=farmer_id,crop_id=crop_id,price_quote=bid_price,bid_date=datetime.date.today()))
        db_session.commit()
        return jsonify({'status':'success'})
    except Exception as e:
        print(e)
        return str(e)

        
def addCrop(name,price,quantity,description,farmer_id,image,bid_date):
    try:
        db_session.add(Crop(name=name,farmer_id=farmer_id,price=price,description=description,quantity=quantity,image=image,bid_date=datetime.datetime.strptime(bid_date,"%d/%m/%Y").date()))
        db_session.commit()
        return jsonify({'job':'added'})
    except Exception as e:
        print(e)
        return str(e)
    
def getAllJobs(farmer_id):
    try:
        jobs=db_session.query(Job).filter(Job.farmer_id!=farmer_id).all()
        return jsonify({'jobs':serialize_job(jobs)})
    except Exception as e:
        return str(e)

    
def addLand(farmer_id,area_size,soil_type,land_mark,cost_year,years,contact,description,coords):
    try:
        db_session.add(Land(farmer_id=farmer_id,area_size=area_size,soil_type=soil_type,land_mark=land_mark,cost_year=cost_year,years=years,contact=contact,status="no",description=description,coords=coords))
        db_session.commit()
        return jsonify({'land':'added'})
    except Exception as e:
        print(e)
        return str(e)

def getBids(farmer_id):
    try:
        bids = db_session.query(Bid).filter(Bid.farmer_id == farmer_id)
        return jsonify({'bids':serialize_bid(bids)})
    except Exception as e:
        print(e)
        return jsonify({'bids':[]})

def getBidCrops(farmer_id=None,mine=True):
    try:
        if farmer_id and mine:
            crops=db_session.query(Crop).filter(Crop.farmer_id==farmer_id).all()    
            c = {'crops':serialize_crop(crops)}
            
        else:
            crops=db_session.query(Crop).filter(Crop.farmer_id!=farmer_id,Crop.bid_date >= datetime.date.today()).all()

            farmer_bid_crops = (bid.crop for bid in db_session.query(Bid) if bid.farmer_id == farmer_id)
            crops = (crop for crop in crops if crop not in farmer_bid_crops)
            c = {'auctions':serialize_crop(crops)}
        print(c)
        return jsonify(c)
    except Exception as e:
        print(e)
        return ""

def getAllLands(farmer_id):
    try:
        lands=db_session.query(Land).filter(Land.farmer_id!=farmer_id).all()
        return jsonify({'lands':serialize_land(lands)})
    except Exception as e:
        return str(e)
        
def addEquipment(name,farmer_id,model,rent_price,contact,description):
    try:
        db_session.add(Equipment(name=name,farmer_id=farmer_id,model=model,rent_price=rent_price,contact=contact,status='no',description=description))
        db_session.commit()
        return jsonify({'equipment': 'added'})
    except Exception as e:
        return str(e)
        
def getAllEquipments(farmer_id):
    try:
        equipments=db_session.query(Equipment).filter(Equipment.farmer_id!=farmer_id).all()
        return jsonify({'equipment':serialize_equipment(equipments)})
    except Exception as e:
        return str(e)
        
def addInvest(farmer_id,area_size,soil_type,land_mark,investments,years,contact,description):
    try:
        db_session.add(Investment(farmer_id=farmer_id,area_size=area_size,soil_type=soil_type,land_mark=land_mark,investments=investments,years=years,contact=contact,status="no",description=description))
        db_session.commit()
        return jsonify({'investment':'added'})
    except Exception as e:
        return str(e)

def getAllInvests(farmer_id):
    try:
        invests=db_session.query(Investment).filter(Investment.farmer_id!=farmer_id).all()
        return jsonify({'invests':serialize_invest(invests)})
    except Exception as e:
        return str(e)
    
def addRequest(request_for,rfarmer_id,farmer_id,object_id):
    try:
        db_session.add(Requests(request_for=request_for,rfarmer_id=rfarmer_id,farmer_id=farmer_id,object_id=object_id,status='pending'))
        db_session.commit()
        farmer=db_session.query(Farmer).filter(Farmer.id==rfarmer_id).one()
        contact=farmer.contact
        url="http://smslane.com/vendorsms/pushsms.aspx?user=roopa&password=G'TECH&msisdn=91"+contact+"&sid=gtech&msg="+"You have new request for your post"+"&fl=0"
        r = requests.get(url)
        return jsonify({'status':'success'})
    except Exception as e:
        print(str(e))
        return jsonify({'status':'failed'})
def getMyRequests(farmer_id):
    try:
        requests=db_session.query(Requests).filter(Requests.rfarmer_id==farmer_id,Requests.status=='pending').all()
        db_session.commit
        return jsonify({'list':serialize_request(requests)})
    except Exception as e:
        print(str(e))
        return str(e)

def get_details(object_for,object_id):
    try:
        if object_for=='land':
            lands=db_session.query(Land).filter(Land.id==object_id).all()
            return jsonify({'status':'success','list':serialize_land(lands)})
        if object_for=='investment':
            invests=db_session.query(Investment).filter(Investment.id==object_id).all()
            return jsonify({'status':'success','list':serialize_invest(invests)})
        if object_for=='job':
            jobs=db_session.query(Job).filter(Job.id==object_id).all()
            return jsonify({'status':'success','list':serialize_job(jobs)})
        if object_for=='equipment':
            equipments=db_session.query(Equipment).filter(Equipment.id==object_id).all()
            return jsonify({'status':'success','list':serialize_equipment(equipments)})
    except Exception as e:
        print(str(e))
        return jsonify({'status':'failed'})
            
def accept_request(request_id):
    try:
        request=db_session.query(Requests).filter(Requests.id==request_id).one()
        request.status='success'
        db_session.commit()
        return 'success'
    except Exception as e:
        print(str(e))
        return 'failed'
    
def delete_request(request_id):
    try:
        db_session.query(Requests).filter(Requests.id==request_id).delete()
        db_session.commit()
        return 'success'
    except Exception as e:
        print(str(e))
        return 'failed'
