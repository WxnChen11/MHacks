from flask import Flask, render_template

app = Flask(__name__)


@app.route('/')
def root():
     return render_template("demo.html")

if __name__ == '__main__':
    app.run(host = "35.2.59.247", port = 5001)