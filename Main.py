#!/usr/bin/env python3
import smbus
import time
import Adafruit_DHT

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

#Variables
RH = 0
Temp = 0

#Humidity
def GetRhTemp():
	print("in func")
	i = 0
	global RH
	global Temp

	while i < 10:
		humidity, temperature = Adafruit_DHT.read_retry(sensor, pin)
		if humidity is not None and temperature is not None:
			print("in if")
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
	time.sleep(0.1)
	data = bus.read_word_data(ADC_ADDR, 0x00)
	#Data is inverted and 
	return data

#Relay
def DeviceOn():
    global RelOne
	status = bus.read_byte_data(RELAY_ADDR, OneStat)
	if status is not None:
		return 1
	bus.write_byte_data(RELAY_ADDR, RelOne)
	time.sleep(0.1)

def DeviceOff():
	if AllRelayOff():
        global Device_State
		Device_State = 0

def SetPwrLvl(setting):
	relays = CheckRelay()
	bus.write_byte_data(RELAY_ADDR, 0x01)
	time.sleep(0.1)
	bus.write_byte_data(RELAY_ADDR, setting)
	time.sleep(0.1)
	return 1

def AllRelayOff():
    global TOG_ALL_OFF
    bus.write_byte(RELAY_ADDR, TOG_ALL_OFF)
    time.sleep(0.1)

#@return array of 4
def CheckRelay():
    ret = [0,0,0,0]
	for x in ret
		ret[x] = bus.read_byte_data(RELAY_ADDR, 0x5 + x)
	return ret

#Control
def ReadTxt(txt):
	fileTemp = open(txt,'r')
	
	fileTemp.close()
	return 1
    
def WriteTxt(txt, str):
	fileTemp = open(txt,'r+')
	
	fileTemp.close()
	return 1

def MainLoop():
	#initialize
	global RH
	global Temp
	global Device_State
    GoalRH = 0
	Device_State = 0
	pwrLvl = 0
	DeviceOff()

	while True:
		#Read Txt and check for new commands

		#Read Hardware
		CheckRelay()
		GetRhTemp()

		#Handle
        if RH > GoalRH:
            DeviceOff()
		else if Device_State == 0
			SetPwrLvl(pwrLvl)
		water = GetWater()
        
		print('Humidity, Temp, Water')
		print(RH, Temp, water)
		time.sleep(2)

MainLoop()