from flask import *
from DBConnection import Db

app = Flask(__name__)

app.secret_key = "dgdgh"
from flask import Flask
from flask_mail import Mail, Message

app.config['MAIL_SERVER']='smtp.gmail.com'
app.config['MAIL_PORT'] = 465

app.config['MAIL_USERNAME'] = 'godseyepvtldt@gmail.com'
app.config['MAIL_PASSWORD'] = 'jdigfycpltpqlfph'
app.config['MAIL_USE_TLS'] = False
app.config['MAIL_USE_SSL'] = True

# -------------------Website----------------------
@app.route('/')
def login():
    return render_template('my_index.html')

@app.route('/log_out')
def log_out():
    session['lid']=0
    return redirect('/')

@app.route('/passreco')
def passreco():
    return render_template('forgot-password.html')

@app.route('/passreco_post', methods=['post'])
def passreco_post():
    email=request.form['reco_email']
    db=Db()
    qry="select * from login where username='"+ email +"'"
    res=db.selectOne(qry)

    mail = Mail(app)

    import  random
    r=str(random.randint(1222,35235352352352))
    qry1="update login set password='"+r+"' where username='"+email+"'"
    db=Db()
    db.update(qry1)


    msg = Message('Hello from the other side!', sender='from@gmail.com', recipients=[res['username']])
    msg.body = "your password is" +r
    mail.send(msg)

    return "ok"



@app.route('/login_post', methods=['post'])
def login_post():
    u_name = request.form['Name']
    passw = request.form['pass']
    db = Db()
    qry = "select * from login where username='" + u_name + "' and password='" + passw + "'"
    res = db.selectOne(qry)
    if res is None:

        return '''<script>alert("Invalid Username Or Password");window.location="/"</script>'''
    else:
       session["lid"]= str(res['lid'])
       return redirect("/dash")


@app.route('/reg')
def reg():
    return render_template('registration.html')


@app.route('/reg_post', methods=['post'])
def reg_post():
    u_name = request.form['name']
    e_mail = request.form['mail']
    phone = request.form['phone']
    dob = request.form['DOB']
    gender = request.form['gender']
    imei = request.form['imei']
    password = request.form['pass']
    confirm = request.form['c_pass']
    db = Db()
    if password == confirm:
        qry1 = "insert into login (username,password,type) values('" + e_mail + "','" + password + "','user')"
        res1 = db.insert(qry1)
        lid = str(res1)
        qry = "INSERT INTO reg_page (lid,Name,E_Mail,Phone_number,Date_Of_Birth,Gender,imei) VALUES ('" + lid + "','" + u_name + "','" + e_mail + "','" + phone + "','" + dob + "','" + gender + "','" + imei + "')"
        res = db.insert(qry)
        return "ok"
    else:
        return redirect('/reg')




@app.route('/lock_update',methods=['post'])
def lock_update():
    type=request.form["lock"]
    if type=="Lock":
        qry="update reg_page set touch='Disabled' where lid='"+str(session["lid"])+"' "
        db=Db()
        res=db.update(qry)
        print(qry)
    else:
        qry = "update reg_page set touch='Enabled' where lid='" + str(session["lid"]) + "' "
        db = Db()
        res = db.update(qry)
        print(qry)

        if session['lid'] == "0":
            return redirect('/')
    return "ok"

@app.route('/alarm_update',methods=['post'])
def alarm_update():
    type=request.form["alarm"]
    if type=="alarm":
        qry="update reg_page set alarm_enable='disabled' were lid='"+str(session["lid"])+"'"
        db=Db()
        res=db.update(qry)
        print(qry)
    else:
        qry="update reg_page set alarm_enable='enabled' where lid='"+str(session["lid"]) + " ' "
        db=Db()
        res=db.update(qry)
        print(qry)

        if session['lid'] == "0":
            return redirect('/')
    return "ok"

@app.route('/alarm')
def alarm():
    qry = "select * from reg_page where lid='" + str(session['lid']) + "'"
    db = Db()
    res = db.selectOne(qry)
    return render_template('alarm.html', data=res)





@app.route('/sim')
def sim():
    qry = "select * from sim"
    db = Db()
    res = db.select(qry)
    return render_template('sim.html', data=res)


@app.route('/call')
def call():
    qry = "SELECT * FROM call_logs where ULid='"+str(session['lid'])+"'"
    db = Db()
    res = db.select(qry)
    if session['lid'] == "0":
        return redirect('/')
    return render_template("call.html", data=res)


@app.route('/loc')
def loc():
    qry = "SELECT * FROM location where ULid='"+str(session['lid'])+"'"
    db = Db()
    res = db.select(qry)
    return render_template("loc.html", data=res)


@app.route('/lock')
def lock():
    qry="select * from reg_page where lid='"+str(session['lid'])+"'"
    db=Db()
    res=db.selectOne(qry)
    return render_template('lock.htm',data=res)



@app.route('/msg')
def msg():
    qry = "select * from msg  where ULid='"+str(session['lid'])+"'"
    db = Db()
    res = db.select(qry)
    return render_template('msg.htm', data=res)


@app.route('/brow')
def bro():
    qry = "SELECT * FROM ` brow`  where ULid='"+str(session['lid'])+"'"
    db = Db()
    res = db.select(qry)
    return render_template("browsing.htm", data=res)




@app.route('/home')
def home():
    return render_template("index.html")


@app.route('/erase')
def erase():
    qry = "select * from reg_page where lid='" + str(session['lid']) + "'"
    db = Db()
    res = db.selectOne(qry)
    return render_template("erase.html",data=res)


@app.route('/file_update',methods=['post'])
def file_update():
    type=request.form["file_erase"]
    if type=="erase":
        qry="update reg_page set file_erase='enabled' were lid='"+str(session["lid"])+"'"
        db=Db()
        res=db.update(qry)
        print(qry)
    else:
        qry="update reg_page set file_erase='disabled' where lid='"+str(session["lid"]) + " ' "
        db=Db()
        res=db.update(qry)
        print(qry)

        if session['lid'] == "0":
            return redirect('/')
    return "ok"

@app.route('/dash')
def dash():
    return render_template("dashboard.html")

@app.route('/add_zone')
def add_zone():


    if session['lid']=="0":
        return redirect('/')

    return render_template("add_zone.html")

@app.route('/add_zonepost', methods=['post'])
def add_zonepost():
    zone=request.form['zone']
    qry="INSERT INTO `zone`(zone,lid,status) VALUES('"+zone+"','"+str(session['lid'])+"','Pending')"
    db=Db()
    db.insert(qry)

    if session['lid']=="0":
        return redirect('/')
    return render_template("add_zone.html")

@app.route('/zonedefault/<zoneid>')
def zonedefault(zoneid):
    qry="UPDATE zone set status='Active' WHERE zoneid='"+zoneid+"'"
    db=Db()
    db.update(qry)

    qry = "UPDATE zone set status='Active' WHERE zoneid!='" + zoneid + "' and lid='"+str(session['lid'])+"'"
    db.update(qry)
    if session['lid']=="0":
        return redirect('/')
    return "ok"

@app.route('/erase_post', methods=['post'])
def erase_post():
    if session['lid']=="0":
        return redirect('/')
    return "ok"


@app.route('/zone')
def zone():
    qry = "select * from zone"
    db = Db()
    res = db.select(qry)
    return render_template('zone.html', data=res)


@app.route('/main')
def maininin():
    return render_template('main.html')


@app.route('/zone_change_post',methods=['post'])
def zone_change_post():
    userid = request.form['lid']
    qry="SELECT *  FROM `zone` WHERE userid='"+ userid +"' and Status='Active'"
    db=Db()
    res=db.selectOne(qry)
    if session['lid']=="0":
        return redirect('/')
    if res is not None:
        return jsonify(status="ok")
    else:
        return jsonify(status="no")



# -----------------------------------android---------------------------

@app.route('/inslocation', methods=['post'])
def location_insert_post():
    lat = request.form['latitude']
    lon = request.form['longitude']
    imei = request.form['imei']
    db=Db()
    qryd = "SELECT * FROM `reg_page` WHERE imei='" + imei + "'"
    ff = db.selectOne(qryd)
    if ff is not None:
        touch = ff["touch"]
    else:
        touch = "Enabled"
    print("imei",imei)
    db = Db()
    qry = "SELECT User_id FROM reg_page WHERE imei='" + imei + "' "
    res = db.selectOne(qry)
    print(res)
    if res is not None:
        qry = "INSERT INTO `location`(DATE,TIME,Lattitude,longitude,ULid)VALUES(CURDATE(),CURTIME(),'" + lat + "','" + lon + "','" + str(
            res['User_id']) + "')"
        db.insert(qry)

        print(qry)


        qry = "SELECT *  FROM `zone` WHERE lid='" + str( res['User_id']) + "' and Status='Active'"
        db = Db()
        resw = db.selectOne(qry)
        if resw is not None:
            return jsonify(status="ok",zone=resw["zone"],Phone_number=res["Phone_number"],touch=touch)
        else:
            return jsonify(status="no")
    else:
        return jsonify(status="ok",zone='',touch=touch)


@app.route('/call_insert_post', methods=['post'])
def call_insert_post():
    phone = request.form['Phone_number']
    date = request.form['Date']
    time = request.form['Time']
    type = request.form['Call_type']
    duration = request.form['Duration']
    imei = request.form['imei']

    print(imei)
    db = Db()
    qry = "SELECT User_id FROM reg_page WHERE imei='" + imei + "' "
    res = db.selectOne(qry)
    if res is not None:
        qry = "INSERT INTO `call_logs`(phone_number,DATE,TIME,call_type,duration,ULid)VALUES('" + phone + "',CURDATE(),CURTIME(),'" + type + "','" + duration + "','" + str(
        res['User_id']) + "')"

        print(qry,"hfffffffffffffffffffffffffff")
        db.insert(qry)
    return jsonify(status="ok")


@app.route('/brow_insert_post', methods=['post'])
def brow_insert_post():
    date = request.form['Date']
    time = request.form['Time']
    bro_hist = request.form['bro_hist']
    imei = request.form['imei']
    db = Db()
    qry = "SELECT User_id FROM reg_page WHERE imei='" + imei + "'"
    res = db.selectOne(qry)
    if res is not None:
        qry = "INSERT INTO ` brow`(DATE,TIME,bro_hist)VALUES(CURDATE(),CURTIME(),'" + bro_hist + "','" + str(
            res['User_id']) + "')"
        db.insert(qry)
    return jsonify(status="ok")


@app.route('/msg_insert_post', methods=['post'])
def msg_insert_post():
    phone = request.form['Phone']
    imei = request.form['imei']
    db = Db()

    qry = "SELECT User_id FROM reg_page WHERE imei='" + imei + "'"
    res = db.selectOne(qry)
    if res is not None:
        message = request.form['Message']
        type = request.form['type']
        qry = "INSERT INTO`msg`(Phone_Number,DATE,TIME,Message,type,ULid)VALUES('" + phone + "',curdate(),curtime(),'" + message + "','" + type + "','" + str(
            res['User_id']) + "')"
        db.insert(qry)
    return jsonify(status="ok")
@app.route('/and_alarm', methods=['post'])
def and_alarm():
    imei = request.form['imei']
    print(imei)
    db = Db()

    qry = "SELECT User_id FROM reg_page WHERE imei='" + imei + "'"
    print(qry)
    res = db.selectOne(qry)
    if res is not None:
        qry="select alarm_enable,Phone_number from reg_page where User_id='"+str(res['User_id'])+"' "
        data=db.selectOne(qry)
        print(data)
        return jsonify(status="ok",res= data['alarm_enable'] ,Phone_number=data['Phone_number'])
    else:
        return jsonify(status="no")

@app.route('/and_file', methods=['post'])
def and_file():
    imei = request.form['imei']
    print(imei)
    db = Db()

    qry = "SELECT User_id FROM reg_page WHERE imei='" + imei + "'"

    print(qry)
    res = db.selectOne(qry)
    if res is not None:
        qry="select file_erase, Phone_number from reg_page where User_id='"+str(res['User_id'])+"' "

        data=db.selectOne(qry)

        print(qry)
        print(data)
        return jsonify(status="ok",res= data['file_erase'])
    else:
        return jsonify(status="no")


@app.route('/sim_insert_post', methods=['post'])
def sim_insert_post():
    imei = request.form['imei']
    db = Db()

    print(imei)

    qry = "SELECT User_id FROM reg_page WHERE imei='" + imei + "'"
    res = db.selectOne(qry)
    if res is not None:

        simserialno = request.form['simserialno']
        simno = request.form['simno']
        deviceid = request.form['deviceid']
        ulid=str(res['User_id'])

        qry="select * from sim where ulid='"+str(ulid)+"'"
        resk= db.selectOne(qry)

        if resk is  None:
            qry = "INSERT INTO `sim` (`simserialno`,`simno`,`deviceid`,`ulid`) values ('"+simserialno+"','"+simno+"','"+deviceid+"','"+ulid+"')"
            db = Db()
            db.insert(qry)
        else:
            qry="UPDATE `sim` SET `simserialno`='"+simserialno+"',`simno`='"+simno+"',`deviceid`='"+deviceid+"' WHERE `ulid`='"+ulid+"'"
            db.update(qry)


    return jsonify(status="ok")


@app.route('/zone_insert_post', methods=['post'])
def zone_insert_post():

    imei = request.form['imei']
    db=Db()
    qry = "SELECT User_id FROM reg_page WHERE imei='" + imei + "'"
    res = db.selectOne(qry)
    if res is not None:
        qry = "INSERT INTO `zone_alert`(date,time,userlid)VALUES(curdate(),curtime(),'" + str(res["User_id"]) + "')"
        db = Db()
        db.insert(qry)
        return jsonify(status="ok")
    else:
        return jsonify(status="no")


@app.route('/zonealert_insert_post', methods=['post'])
def zonealert_insert_post():
    zaid = request.form['zaid']
    userlid = request.form['userlid']
    date = request.form['date']
    time = request.form['time']
    qry = "INSERT INTO `zone_alert`(ZAID,USERLID,DATE,TIME)VALUES('" + zaid + "','" + userlid + "','" + date + "','" + time + "')"
    db = Db()
    db.insert(qry)
    return jsonify(status="ok")


@app.route('/brow_hist_insert_post' , methods=['post'])
def brow_hist_insert_post():
    date = request.form['Date']
    time = request.form['Time']
    bro_hist = request.form['bro_hist']
    userid = request.form['ULid']
    qry = "INSERT INTO ` brow`(DATE,TIME,BRO_HIST,USERLID)VALUES('" + date + " ' ,' " + time + " ',' " + bro_hist + " ' , ' " + userid + "')"
    db = Db()
    db.insert(qry)
    return jsonify(status="ok")







if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=5000)
