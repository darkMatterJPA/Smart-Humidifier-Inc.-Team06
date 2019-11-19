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
RelFour = 0x04
OneStat = 0x05
TwoStat = 0x06
ThreeStat = 0x07
FourStat = 0x08
TOG_ALL_OFF = 0x0A
TOG_ALL_ON = 0x0B
TOG_ALL = 0x0C

RELAY_OFF = 0
RELAY_ON = 15

#RH Sensor
sensor = Adafruit_DHT.DHT22
pin = 23
GoalRH = 75

#Variables
RH = 0
Temp = 0
Schedule = 0
Device_State = 0
ScheduleState = 0
ManualState = 0

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
	maxIn = 0x0FA0
	minIn = 0x02FF
	rangeIn = maxIn - minIn
	print("Raw Water:",hex(rawWater))
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
	time.sleep(0.1)
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

OldSetting = 0
def SetPwrLvl():
	global Device_State
	global RH
	global GoalRH
	global OldSetting
	high = RelFour
	medium = RelThree
	low = RelTwo
	error = GoalRH - RH

	if error > 15:
		setting = high
	elif error > 10:
		setting = medium
	else:
		setting = low
	if(OldSetting != setting):
		relays = CheckRelay()
		print(relays)
		if relays[setting - 1] == 0:
			AllRelayOff()
			bus.write_byte(RELAY_ADDR, RelOne)
			time.sleep(0.1)
			bus.write_byte(RELAY_ADDR, setting)
			time.sleep(0.1)
			Device_State = 1
			OldSetting = setting
	return 1

def AllRelayOff():
	global TOG_ALL_OFF
	global OldSetting
	OldSetting = 0;
	bus.write_byte(RELAY_ADDR, TOG_ALL_OFF)
	time.sleep(0.1)
	Device_State = 0
	return 1

#@return array of 4
def CheckRelay():
	ret = [0,0,0,0]
	ret[0] = bus.read_byte_data(RELAY_ADDR, OneStat)
	time.sleep(0.1)
	ret[1] = bus.read_byte_data(RELAY_ADDR, TwoStat)
	time.sleep(0.1)
	ret[2] = bus.read_byte_data(RELAY_ADDR, ThreeStat)
	time.sleep(0.1)
	ret[3] = bus.read_byte_data(RELAY_ADDR, FourStat)
	time.sleep(0.1)
	return ret

#Control
def CheckSchedule():
	global GoalRH
	global RH
	global ScheduleState
	global Schedule
	global ManualState
	if Schedule == 0:
		return

	currentDayTime = time.localtime(time.time())
	today = currentDayTime[6]
	currentHour = currentDayTime[3]
	currentMinute = currentDayTime[4]
	z = int(str(currentHour)+str(currentMinute))

	days = ("Monday", "Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday")
	print(today);
	print(len(Schedule[days[today]]))
	for i in range(len(Schedule[days[today]])):
		startTime = int(Schedule[days[today]][i]["startTime"])
		endTime = int(Schedule[days[today]][i]["endTime"])
		if z >= startTime and z <= endTime and ManualState == 0:
			ScheduleState = 1
			GoalRH = Schedule[days[today]][i]["humidityLevel"]
			return 1
		else:
			ScheduleState = 0
			return 0
def CheckError():
	global RH
	global Device_State


def ReadTxt(txt):
	fileTemp = open(txt,'r')
	
	fileTemp.close()
	return 1
    
def WriteTxt(txt, str):
	fileTemp = open(txt,'r+')
	
	fileTemp.close()
	return 1

#Socket.IO event listeners
#@sio.on('schedule-to-pi')
#def on_schedule(data):
#	global Schedule
#	Schedule = data

#@sio.on('power-status-to-pi')
#@sio.on('powerStatusToPi')
@sio.event
def powerStatusToPi(data):
	print('inPowerFunc', data)
	global ManualState
	if data['power'] == 'On':
		print('Message Recieved')
		SetPwrLvl()
		ManualState = 1
	if data['power'] == 'Off':
                DeviceOff()
                ManualState = 0
	print("In Power Status Command")
#def on_powerStatusToPi(data):

@sio.event
def humiditySettingToPi(data):
	global GoalRH
	GoalRH =int( data["Humidity"])
	print("Recieved Humidity Command")

@sio.event
def scheduleToPi(data):
	global Schedule
	Schedule = data
	print("Recived Schedule")

@sio.event
def connect():
	print('Connected To Server')

###############################################################
def MainLoop():
	global sio
	sio.connect('https://polar-meadow-51053.herokuapp.com/')

	#initialize
	global RH
	global Temp
	global Device_State
	global GoalRH
	global ScheduleState
	global ManualState
	oldTime = 0
	oldWaterTime = 0
	Device_State = 0
	fullTime = 0
	emptyTime = 0
	DeviceOff()
	waterFlag75 = 0
	waterFlag50 = 0
	waterFlag25 = 0
	while True:
		#Read Hardware#####
		GetRhTemp()
		water = GetWater()

		if not CheckSchedule():
			ScheduleState = 0
			print("Not in Schedule State")
		if RH < (GoalRH - 2) and (ScheduleState == 1 or ManualState == 1):
			SetPwrLvl()
			print("SetPowerLVl")
		if RH > (GoalRH + 2) or (ManualState == 0 and ScheduleState == 0):
			DeviceOff()
		#Handle###########

		#Water level Handling
		if water == 100:
			waterFlag75 = 0
			waterFlag50 = 0
			waterFlag25 = 0
		elif water == 75 or water == 50:
			jWater = {"WaterLevel": water}
			z = json.dumps(jWater)
			sio.emit('waterLvl-Warning', z)
			if water == 50:
				waterFlag50 = 1
			else:
				waterFlag75 = 1
		elif water == 25:
			newTime = time.time()
			if(newTime - oldWaterTime) >= (30 * 60) or waterFlag25 == 0:
				message = {"Waterlvl": "Water level under 25%"}
				jm = json.dumps(message)
				sio.emit('refill-flag', jm)
				waterFlag25 = 1
				oldWaterTime = time.time()

		#Error Handling
		#CheckError()

		newRH = "%.2f" % RH
		print("Humidity:", newRH)
		print("GoalHumidity:", GoalRH)
		print("Water Percent", water)
		#send data to server#####
		newTime = time.time()
		if((newTime - oldTime) >= 120):
			jWater = {"WaterLevel": water}
			jRh = {"Humidity": newRH}
			y = json.dumps(jWater)
			z = json.dumps(jRh)
			sio.emit('waterLevel-from-pi',y)
			sio.emit('humidityLevel-from-pi', z)
			oldTime = time.time()
		time.sleep(1)

MainLoop()
