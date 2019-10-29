#!/usr/bin/env python3
import smbus
import time
import Adafruit_DHT
import socketio
import json

#Client socket.IO
sio = socketio.Client()

#Constants

#I2C
bus = smbus.SMBus(1)
ADC_ADDR = 0x48
RELAY_ADDR = 0x6D

#Relay
RelOne = 0x01
RelTwo = 0x02
RelThree = 0x03
RelFour = 0x03
OneStat = 0x04
TwoStat = 0x05
ThreeStat = 0x06
FourStat = 0x07
TOG_ALL_OFF = 0x0A
TOG_ALL_ON = 0x0B
TOG_ALL = 0x0C

RELAY_OFF = 0
RELAY_ON = 15

Device_State = 0

#RH Sensor
sensor = Adafruit_DHT.DHT22
pin = 23
GoalRH = 100

#Variables
RH = 0
Temp = 0

#Humidity
def GetRhTemp():
	i = 0
	global RH
	global Temp

	while i < 10:
		humidity, temperature = Adafruit_DHT.read_retry(sensor, pin)
		if humidity is not None and temperature is not None:
			RH = humidity
			Temp = temperature
			return 1
	else:
		i = i + 1
	print('Failed to get reading. Try again!')
	return 0

#Water Level
def GetWater():
	bus.write_word_data(ADC_ADDR, 0x01, 0xC383)
	time.sleep(0.3)
	data = bus.read_word_data(ADC_ADDR, 0x00)
	temp1 = data >> 8
	temp2 = (data & 0x0F) << 8
	newDat = temp1 + temp2
	return MapWater(newDat)

def MapWater(rawWater):
	#Constants
	maxIn = 0x0950
	minIn = 0x02FF
	rangeIn = maxIn - minIn
	maxOut = 100
	minOut = 25
	rangeOut = 75

	#Calculations
	temp = rawWater - minIn
	if temp < 0:
		temp = 0
	temp = temp/rangeIn
	#print(temp)
	out = rangeOut * temp
	#print(out)
	out = out + minOut

	#Checks
	if out >= maxOut:
		return maxOut
	elif out <= minOut:
		return minOut
	return out

#Relay
def DeviceOn():
	global RelOne
	global Device_State
	status = bus.read_byte_data(RELAY_ADDR, OneStat)
	if status is not None:
		return 1
	bus.write_byte(RELAY_ADDR, RelOne)
	time.sleep(0.1)
	SetPwrLvl()
	Device_State = 1

def DeviceOff():
	if AllRelayOff():
		global Device_State
		Device_State = 0

def SetPwrLvl():
	global Device_State
	global RH
	global GoalRH
	high = 4
	medium = 3
	low = 2
	
	error = GoalRh - RH
	if error > 15:
		setting = high
	elif error > 10:
		setting = medium
	else:
		setting = low
	
	relays = CheckRelay()
	if relays[0] == 0:
		bus.write_byte(RELAY_ADDR, 0x01)
		time.sleep(0.1)
	if relays[setting -1] == 0:
	
		bus.write_byte(RELAY_ADDR, setting)
		time.sleep(0.1)
	Device_State = 1
	return 1

def AllRelayOff():
    global TOG_ALL_OFF
    bus.write_byte(RELAY_ADDR, TOG_ALL_OFF)
    time.sleep(0.1)

#@return array of 4
def CheckRelay():
	ret = [0,0,0,0]
	for x in ret:
		ret[x] = bus.read_byte_data(RELAY_ADDR, 0x4 + x)
		time.sleep(0.1)
	return ret

#Control
def CheckSchedule()
	global GoalRH
	global RH

	y = json.dumps(x) 
	today = currentDayTime[6]
	currentHour = currentDayTime[3]
	currentMinute = currentDayTime[4]
	z = int(str(currentHour)+str(currentMinute))

	days = ("Monday", "Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday")

	for i in range(len(x[days[today]][0])):
		startTime = x[days[today]][i]["startTime"]
		endTime = x[days[today]][i]["endTime"]
		if z >= startTime and z <= endTime:
			GetRhTemp()
			if RH < GoalRH:
				DeviceOff()
			else:
				DeviceOn()
		else:
			DeviceOff()

def ReadTxt(txt):
	fileTemp = open(txt,'r')
	
	fileTemp.close()
	return 1
    
def WriteTxt(txt, str):
	fileTemp = open(txt,'r+')
	
	fileTemp.close()
	return 1

#Socket.IO event listeners
@sio.on('schedule-to-pi')
def on_schedule(data):
        ''' Do some stuff '''

@sio.on('power-status-and-humidity-Setting-to-pi')
def on_powerStatusHumidity(data):
        ''' Do some stuff '''


''' Sample way of sending an event to the server
	sio.emit('waterLevel-from-pi',' some data ')

'''


def MainLoop():
	global sio
	sio.connect('https://polar-meadow-51053.herokuapp.com/')
	#initialize
	global RH
	global Temp
	global Device_State
	global GoalRH
	Device_State = 0
	fullTime = 0
	emptyTime = 0
	DeviceOff()
	
	while True:
		#Read and handle Server

		#Read Hardware
		GetRhTemp()
		water = GetWater()
		print('Humidity, Temp, Water')
		print(RH, Temp, water)
		
		#Handle
		CheckSchedule()
		if water == 100:
			fullTime = time.clock_gettime
		elif water < 25:
			#notify server
			emptyTime = time.clock_gettime

		#send data to server

		time.sleep(2)

MainLoop()
