#!/usr/bin/env python3
import time
import json

currentDayTime = time.localtime(time.time())

x = {
    "Monday": [{"startTime": 800 , "endTime": 1200}, {"startTime": 1600 , "endTime": 1800}],
    "Tuesday": [{"startTime": 800 , "endTime": 1200}, {"startTime": 1600 , "endTime": 1800}],
    "Wednesday": [{"startTime": 800 , "endTime": 1200}, {"startTime": 1600 , "endTime": 1800}],
    "Thursday": [{"startTime": 1621 , "endTime": 1622}, {"startTime": 1700 , "endTime": 1800}],
    "Friday": [{"startTime": 800 , "endTime": 1200}, {"startTime": 1600 , "endTime": 1800}],
    "Saturday": [{"startTime": 800 , "endTime": 1200}, {"startTime": 1600 , "endTime": 1800}],
    "Sunday": [{"startTime": 800 , "endTime": 1200}, {"startTime": 1600 , "endTime": 1800}],
}


y = json.dumps(x) 

today = currentDayTime[6]
currentHour = currentDayTime[3]
currentMinute = currentDayTime[4]

z = int(str(currentHour)+str(currentMinute))
print(z)

days = ("Monday", "Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday")

for i in range(len(x[days[today]][0])):
    startTime = x[days[today]][i]["startTime"]
    endTime = x[days[today]][i]["endTime"]
    if z >= startTime and z <= endTime:
        print('''ON''')
    else:
        print('''oFF''')
