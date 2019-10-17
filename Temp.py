#!/usr/bin/env python3
import socketio

sio = socketio.Client()
sio.connect('https://polar-meadow-51053.herokuapp.com/')

print('my sid is: ', sio.sid)

@sio.on('news')
def on_message(data):
	print('I recieved a Message: ', data['hello'])

