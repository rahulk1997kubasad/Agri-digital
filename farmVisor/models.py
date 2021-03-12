from sqlalchemy import create_engine
from sqlalchemy import Column,String,Integer, Date, ForeignKey,Float,Boolean
from sqlalchemy.orm import sessionmaker, relationship
# from api import * 
from sqlalchemy.ext.declarative import declarative_base

engine = create_engine("sqlite:///farmVisor.db",convert_unicode=True)
Base = declarative_base()
Session = sessionmaker(bind=engine)

session = Session()

class Farmer(Base):
    __tablename__ = 'farmers'
    id = Column(Integer,primary_key=True)
    fname = Column(String(30))
    lname = Column(String(30))    
    
    contact = Column(String(20),unique=True)
    kisan_number = Column(String(20), unique=True)
    password = Column(String(30))
    status = Column(String(20),default="pending")
    jobs= relationship('Job', back_populates='farmer' )
    lands= relationship('Land', back_populates='farmer' )
    crops= relationship('Crop', back_populates='farmer' )
    bids = relationship('Bid', back_populates='farmer')
    equipments= relationship('Equipment', back_populates='farmer' )
    invests= relationship('Investment', back_populates='farmer' )
    requests=relationship('Requests',back_populates='farmer')
    def __repr__(self):
        return "Former: "+self.fname


class Job(Base):
    __tablename__='jobs'
    id=Column(Integer,primary_key=True)
    title=Column(String(50))
    farmer_id=Column(Integer,ForeignKey('farmers.id'))
    contact=Column(String(15))
    description=Column(String(100))
    payment=Column(String(5))
    farmer= relationship('Farmer', back_populates='jobs' )
    def __repr__(self):
        return "Job :"+self.title

# class CropCenter(Base):
#     __tablename__ = "cropcenter"
#     id=Column(Integer,primary_key=True)
#     name = String(50)
#     longitude = Column(Float)    
#     latitude = Column(Float)
        
class Crop(Base):
    __tablename__='crops'
    id=Column(Integer,primary_key=True)
    name=Column(String(50))
    farmer_id=Column(Integer,ForeignKey('farmers.id'))
    price=Column(Integer)
    description=Column(String(100))
    quantity = Column(Integer)
    image=Column(String(100))
    status = Column(String(10),default="Open")
    farmer= relationship('Farmer', back_populates='crops' )
    bid_date = Column(Date)
    bids = relationship('Bid', back_populates='crop')
    
    # crop_center_id = Column(Integer,ForeignKey('cropcenter.id'))
    # crop_center= relationship('CropCenter', back_populates='crops')
    
    def __repr__(self):
        return "Crop :"+self.name

class Bid(Base):
    __tablename__ = 'bids'
    id = Column(Integer,primary_key=True)
    farmer_id=Column(Integer,ForeignKey('farmers.id'))
    
    bid_date = Column(Date)
    price_quote = Column(Float)
    crop_id = Column(Integer, ForeignKey("crops.id"))
    crop = relationship('Crop',back_populates="bids")
    farmer= relationship('Farmer', back_populates='bids' )
    status = Column(String(10),default="pending")
    def set_status(self, status):
        self.status = status


class Land(Base):
    __tablename__='lands'
    id=Column(Integer,primary_key=True)
    farmer_id=Column(Integer,ForeignKey('farmers.id'))
    area_size=Column(String(10))
    soil_type=Column(String(30))
    land_mark=Column(String(50))
    cost_year=Column(String(7))
    years=Column(Integer)
    contact=Column(String(20))
    coords=Column(String(300))
    description=Column(String(100))
    status=Column(String(20))
    farmer= relationship('Farmer', back_populates='lands' )
    def __repr__(self):
        return "Land Soil :"+self.soil_type
 
class Investment(Base):
    __tablename__='investments'
    id=Column(Integer,primary_key=True)
    farmer_id=Column(Integer,ForeignKey('farmers.id'))
    area_size=Column(String(10))
    soil_type=Column(String(30))
    land_mark=Column(String(50))
    investments=Column(String(7))
    years=Column(Integer)
    contact=Column(String(20))
    status=Column(String(20))
    description=Column(String(100))
    farmer= relationship('Farmer', back_populates='invests' )
    def __repr__(self):
        return "Area Size :"+self.area_size
    

class Equipment(Base):
    __tablename__='equipments'
    id=Column(Integer,primary_key=True)
    name=Column(String(20))
    farmer_id=Column(Integer,ForeignKey('farmers.id'))
    model=Column(String(50))
    rent_price=Column(String(7))
    contact=Column(String(20))
    status=Column(String(20))
    description=Column(String(100))
    farmer= relationship('Farmer', back_populates='equipments' )

class Requests(Base):
    __tablename__='requests'
    id=Column(Integer,primary_key=True)
    request_for=Column(String(50))
    rfarmer_id=Column(Integer)
    farmer_id=Column(Integer,ForeignKey('farmers.id'))
    object_id=Column(Integer)
    status=Column(String(30))
    farmer= relationship('Farmer', back_populates='requests' )

if __name__ == '__main__':
    Base.metadata.create_all(engine)
    # session.add(CropCenter(name="center1", latitude=15.4130, longitude=75.0055))
    # session.add(CropCenter(name="center2", latitude=15.3798, longitude=75.1129))
    # session.add(CropCenter(name="center3", latitude=15.4307, longitude=75.0146))
    
    session.commit()
    session.close()


