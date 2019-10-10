
#!/usr/bin/env python3
import smbus
import time
from enum import Enum

#Constants and Enums
RelayControl = Enum('RelayControl','RelOne RelTwo RelThree RelFour OneStat TwoStat ThreeStat FourStat' )
TOG_ALL_OFF = 0xA
TOG_ALL_ON = 0xB
TOG_ALL = 0xC
bus = smbus.SMBus(1)
ADC_ADDR = 0x48
RELAY_ADDR = 0x6D

while True:
	bus.write_word_data(ADC_ADDR, 0x01,0xC383)
	time.sleep(0.5)
	data = bus.read_word_data(ADC_ADDR,0x00)
	dataH = hex(data)
	print(dataH)
	time.sleep(0.5)

def GetRhTemp():
	
	return 1
def GetWater():
	bus.write_word_data(ADC_ADDR, 0x01, 0xC383)
	time.sleep(0.1)
	data = bus.read_word_data(ADC_ADDR, 0x00)
	
	return data

def DeviceOn():
	
	return 1

def DeviceOff():
	return 1

def SetPwrLvl(setting):
	return 1

def WriteToRelay(relay,setting):
	return 1
def ReadRelay(relay):
	return 1
def ReadI2c(addr,reg):
	return 1
def WriteI2c(addr,reg,data):
	return 1

def ReadTxt(txt):
	return 1
def WriteTxt(txt, str):
	return 1
