from flask import Flask, render_template, url_for, redirect, request, session,jsonify,send_from_directory

from database import *
import requests
import os
from werkzeug.utils import secure_filename
app = Flask(__name__)
UPLOAD_FOLDER = os.path.join(os.getcwd(),"uploads")
ALLOWED_EXTENSIONS = set(['txt', 'pdf', 'png', 'jpg', 'jpeg', 'gif'])
def allowed_file(filename):
    return '.' in filename and \
           filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS


app.secret_key="1234"

app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER

def send_message(number, msg):
    url = "https://www.fast2sms.com/dev/bulk"
    querystring={"authorization":"nBE5KxNmDbZv2JQsfX1UAhIP4L7wakgGWM6qrVdyR3OFjecSYCHhSilYgX7DJ4Q6CqVWOwKj8dn5TUFZ","sender_id":"FSTSMS","message":f"{msg}","language":"english","route":"p","numbers":f"{number}"}
    headers = {
    'cache-control': "no-cache"
	}
    response = requests.request("GET", url, headers=headers, params=querystring)
    print(response.text)

@app.route('/')
def root():
    return "success working"

@app.route('/farmvisor/farmer_registration',methods=['POST'])
def farmer_registration():
    fname=request.form['fname']
    lname=request.form['lname']
    
    contact=request.form['contact']
    password=request.form['password']
    kisan_number = request.form['kisan_number']
    return add_farmer(fname,lname,contact,password, kisan_number)

@app.route("/farmvisor/admin/farmer_verify")
def farmer_verify():
    farmer_id = request.args.get('farmer_id',0)
    status = request.args.get('status',0)

    if farmer_id:
        status = admin_verify_farmer(farmer_id, status)
        #return jsonify(status)
        
    return render_template("adminHome.html", farmers = get_unverified_farmers())

@app.route("/admin")
def admin_home():
    msg = request.args.get('msg')
    return render_template("index.html",msg=msg)
    
@app.route('/admin/login',methods=['POST'])
def admin_login():

    username = request.form['username']
    password = request.form['password']

    if username == 'admin' and password == 'admin':
        # session['admin'] = 'admin'
        return redirect(url_for('farmer_verify'))
    else:
        return redirect(url_for('admin_home',msg="Wrong username/password"))

@app.route('/admin/logout')
def admin_logout():
    # session.pop('admin')
    return redirect(url_for('home',msg="Successfully logged out"))
    

@app.route('/farmvisor/farmer_login',methods=['POST'])
def farmer_login():
    username=request.form['username']
    password=request.form['password']
    farmer=verify_farmer(username,password)
    if farmer and type(farmer) != type(""):
        return jsonify({'farmer_id':farmer.id,'login':'OK','contact':farmer.contact,'name':farmer.fname})
    else:
        return jsonify({'login':'Null',"msg":farmer})
        
@app.route('/farmvisor/add_job',methods=['POST'])
def add_job():
    title=request.form['title']
    farmer_id=request.form['farmer_id']
    contact=request.form['contact']
    description=request.form['description']
    payment=request.form['payment']
    return addJob(title,farmer_id,contact,description,payment)
    
@app.route('/farmvisor/addCrops',methods=['POST'])
def add_crop():
    name=request.form['name']
    farmer_id=request.form['farmer_id']
    price=request.form['price']
    description=request.form['description']
    quantity=request.form['quantity']
    bid_date = request.form['bid_date']
    image = request.files['image']
    filename = secure_filename(image.filename)
    filename.rsplit('.', 1)[1].lower()
    image.save(os.path.join(app.config['UPLOAD_FOLDER'], filename))
    print("date ",bid_date)
    
    return addCrop(name,price,quantity,description,farmer_id,filename,bid_date)


@app.route('/farmvisor/getjobs/<farmer_id>',methods=['GET'])
def getJob(farmer_id):
    return getAllJobs(farmer_id)

@app.route('/getImage/<filename>')
def uploaded_file(filename):
    return send_from_directory(app.config['UPLOAD_FOLDER'],
                               filename)


@app.route('/farmvisor/getAuctions/<int:farmer_id>',methods=['GET'])
def get_auctions(farmer_id):
    return getBidCrops(farmer_id,mine=False)

@app.route('/farmvisor/getMyCrops/<int:farmer_id>',methods=['GET'])
def get_Crops(farmer_id):
    return getBidCrops(farmer_id)

@app.route('/farmvisor/getBids/<int:farmer_id>',methods=['GET'])
def get_bids(farmer_id):
    return getBids(farmer_id)


@app.route('/farmvisor/getBidsForCrop/<int:crop_id>',methods=['GET'])
def get_bids_for_crop(crop_id):
    return get_bids_of_crop(crop_id)

@app.route('/farmvisor/save_bid',methods=['POST'])
def save_bid():
    print(request.form);farmer_id=request.form['farmer_id']
    crop_id=request.form['crop_id']
    bid_price=request.form['bid_price']
    return saveBid(farmer_id,crop_id,bid_price)    

@app.route('/farmvisor/make_payment',methods=['POST'])
def pay():
    print(request.form);farmer_id=request.form['farmer_id']
    phone=get_farmer_id(farmer_id).contact
    bid_price=request.form['bid_price']
    bid_id=request.form['bid_id']
    send_message(phone, f"Payment of {bid_price} made. Thank You")
    msg, fphone = payBid(bid_id)
    print(f"Farmer phone: {fphone}")
    send_message(fphone, f"Bid amount paid from trader with contact num {phone}")    
    return msg

@app.route('/farmvisor/add_land',methods=['POST'])
def add_land():
    farmer_id=request.form['farmer_id']
    area_size=request.form['area_size']
    soil_type=request.form['soil_type']
    land_mark=request.form['land_mark']
    cost_year=request.form['cost_year']
    years=request.form['years']
    contact=request.form['contact']
    coords=request.form['coords']
    description=request.form['description']
    return addLand(farmer_id,area_size,soil_type,land_mark,cost_year,years,contact,description,coords)
    
@app.route('/farmvisor/getLands/<farmer_id>',methods=['GET'])
def getLands(farmer_id):
    return getAllLands(farmer_id)


@app.route('/farmvisor/addInvest',methods=['POST'])
def add_Invest():
    farmer_id=request.form['farmer_id']
    area_size=request.form['area_size']
    soil_type=request.form['soil_type']
    land_mark=request.form['land_mark']
    invest=request.form['cost_year']
    years=request.form['years']
    contact=request.form['contact']
    description=request.form['description']
    return addInvest(farmer_id,area_size,soil_type,land_mark,invest,years,contact,description)
    
@app.route('/farmvisor/getInvests/<farmer_id>',methods=['GET'])
def getInvests(farmer_id):
    return getAllInvests(farmer_id)
    
@app.route('/farmvisor/addEquipment',methods=['POST'])
def add_Equipment():
    name=request.form['name']
    farmer_id=request.form['farmer_id']
    model=request.form['model']
    rent_price=request.form['rent_price']
    contact=request.form['contact']
    description=request.form['description']
    return addEquipment(name,farmer_id,model,rent_price,contact,description)
    
@app.route('/farmvisor/getEquipments/<farmer_id>',methods=['GET'])
def getEquipments(farmer_id):
    return getAllEquipments(farmer_id)
    
@app.route('/farmvisor/forget_password',methods=['POST'])
def forget_password():
    phone=request.form['phone']
    farmer=get_farmer(phone)
    if farmer:
        password=farmer.password
        send_message(phone,str(password)+" this is your password")
        return jsonify({"status":'OK'})
    else:
        return jsonify({'status':'Null'})
        
@app.route('/farmvisor/apply_request',methods=['POST'])
def apply_requests():
    request_for=request.form['request_for']
    rfarmer_id=request.form['rfarmer_id']
    farmer_id=request.form['farmer_id']
    object_id=request.form['object_id']
    return addRequest(request_for,rfarmer_id,farmer_id,object_id)

@app.route('/getMyRequests/<farmer_id>',methods=['GET'])
def get_my_requests(farmer_id):
    return getMyRequests(farmer_id)

@app.route('/get_details',methods=['POST'])
def get_detail():
    object_for=request.form['object_for']
    object_id=request.form['object_id']
    print(object_for)
    print(object_for)
    status=get_details(object_for,object_id)
    print(str(status))
    return status
    
@app.route('/accept_request',methods=['POST'])
def accept_requests():
    request_id=request.form['request_id']
    status=accept_request(request_id)
    if status=='success':
        return jsonify({'status':'success'})
    else:
        return jsonify({'status':status})

@app.route('/farmvisor/accept_bid',methods=['POST'])
def accept_bid():
    bid_id=request.form['bid_id']
    status=acceptBid(bid_id)
    return status

@app.route('/delete_request',methods=['POST'])
def delete_requests():
    request_id=request.form['request_id']
    status=delete_request(request_id)
    if status=='success':
        return jsonify({'status':'success'})
    else:
        return jsonify({'status':status})

app.run(debug=True,threaded=False,port=5333,host='0.0.0.0')
    
