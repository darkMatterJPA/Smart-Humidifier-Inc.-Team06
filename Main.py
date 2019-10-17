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
TOG_ALL_OFF = 0xA
TOG_ALL_ON = 0xB
TOG_ALL = 0xC

RELAY_OFF = 0
RELAY_ON = 15

#RH Sensor
sensor = Adafruit_DHT.DHT22
pin = 23

#Variables
RH = 0
Temp = 0

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

def GetWater():
	bus.write_word_data(ADC_ADDR, 0x01, 0xC383)
	time.sleep(0.1)
	data = bus.read_word_data(ADC_ADDR, 0x00)
	#Data is inverted and 
	return data

def DeviceOn():
	status = bus.read_byte_data(RELAY_ADDR, OneStat)
	if status is not None:
		return 1
	bus.write_byte_data(RELAY_ADDR, RelTwo)
	time.sleep(0.1)
	return 1

def DeviceOff():
	AllRelayOff()
	return 1

def SetPwrLvl(setting):
	AllRelayOff()
	bus.write_byte_data(RELAY_ADDR, RelOne)
	time.sleep(0.1)
	bus.write_byte_data(RELAY_ADDR, setting)
	time.sleep(0.1)
	return 1

def AllRelayOff():
	status = bus.read_byte_data(RELAY_ADDR, OneStat)
	if status is not None:
		bus.write_byte(RELAY_ADDR, RelOne)
		time.sleep(0.1)
	status = bus.read_byte_data(RELAY_ADDR, TwoStat)
	if status is not None:
		bus.write_byte(RELAY_ADDR, RelTwo)
		time.sleep(0.1)
	status = bus.read_byte_data(RELAY_ADDR, ThreeStat)
	if status is not None:
		bus.write_byte(RELAY_ADDR, RelThree)
		time.sleep(0.1)
	status = bus.read_byte_data(RELAY_ADDR, FourStat)
	if status is not None:
		bus.write_byte(RELAY_ADDR, RelFour)
		time.sleep(0.1)
	return 1

def ReadTxt(txt):
	return 1
def WriteTxt(txt, str):
	return 1

def MainLoop():
	global _RH
	global _Temp	
	AllRelayOff()
	while True:
		GetRhTemp()
		water = GetWater()
		print('Humidity, Temp, Water')
		print(RH, Temp, water)
		time.sleep(2)

MainLoop()
