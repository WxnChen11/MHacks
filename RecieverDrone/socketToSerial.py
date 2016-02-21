import socket               # Import socket module
import serial
import threading

try:
    ser = serial.Serial('/dev/ttyACM0', 9600)
except:
    try:
        ser = serial.Serial('/dev/ttyACM1', 9600)
    except:
        print(st + ",Cannot find Arduino. Please manually enter the address EX: ttyACM0")
        addr = raw_input("Address: ")
        ser = serial.Serial('/dev/' + addr, 9600)



soc = socket.socket()         # Create a socket object
host = "35.2.103.164" # Get local machine name
port = 1338                # Reserve a port for your service.
soc.bind((host, port))       # Bind to the port
soc.listen(5)                 # Now wait for client connection.
while True:
    conn, addr = soc.accept()     # Establish connection with client.
    msg = conn.recv(1024)
    print (msg)
    ser.write(msg)
    

