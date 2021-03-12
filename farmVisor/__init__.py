from flask import Flask
app = Flask(__name__)
app.secret_key = 'hello world'
app.config['UPLOAD_FOLDER'] = 'uploads/'
app.config['ALLOWED_EXTENSIONS'] = set(['xlsx','xls'])
from .database import db_session


@app.teardown_appcontext
def shutdown_session(exception=None):
    db_session.remove()

from farmVisor import views, models