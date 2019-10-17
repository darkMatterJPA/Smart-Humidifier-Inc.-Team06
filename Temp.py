#!/usr/bin/env python3
import socketio
import json

sio = socketio.Client()
sio.connect('https://polar-meadow-51053.herokuapp.com/')

print('my sid is: ', sio.sid)

@sio.on('news')
def on_message(data):
	print('I recieved a Message: ', data['hello'])

@sio.on('power-status-and-humidity-Setting-to-pi')
def on_powerStatusHumidity(data):
	''' Do some stuff '''

@sio.on('schedule-to-pi')
def on_schedule(data):
	''' Do some stuff '''

# sio.emit('waterLevel-from-pi',''' some data ''')