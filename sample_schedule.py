#!/usr/bin/env python3
import time
import json

currentDayTime = time.localtime(time.time())

x = {
    "Monday": [{"startTime": 800 , "endTime": 1200}, {"startTime": 1600 , "endTime": 1800}],
    "Tuesday": [{"startTime": 800 , "endTime": 1200}, {"startTime": 1600 , "endTime": 1800}],
    "Wednesday": [{"startTime": 800 , "endTime": 1200}, {"startTime": 1600 , "endTime": 1800}],
    "Thursday": [{"startTime": 800 , "endTime": 1200}, {"startTime": 1600 , "endTime": 1800}],
    "Friday": [{"startTime": 800 , "endTime": 1200}, {"startTime": 1600 , "endTime": 1800}],
    "Saturday": [{"startTime": 800 , "endTime": 1200}, {"startTime": 1600 , "endTime": 1800}],
    "Sunday": [{"startTime": 800 , "endTime": 1200}, {"startTime": 1600 , "endTime": 1800}],
}


y = json.dumps(x) 

today = currentDayTime[6]
currentTime = currentDayTime[]

if today == 0:
   '''Do stuff'''
   startTime = x["Monday"][0]["startTime"]
   if 

elif today == 1:
    '''Do stuff'''
    x["Tuesday"]
elif today == 2:
    '''Do stuff'''
    x["Wednesday"]
elif today == 3:
    '''Do stuff'''
    x["Thursday"]
elif today == 4:
    '''Do stuff'''
    x["Friday"]
elif today == 5:
    '''Do stuff'''
    x["Saturday"]
elif today == 6:
    '''Do stuff'''
    x["Sunday"]