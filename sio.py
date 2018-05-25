import json
from math import ceil
import sys
import networkTime
with open('workload.json') as w:
	workloadData=json.load(w)
	
racks=workloadData["racks"]
servers=workloadData["servers"]
partition=[]
serverNum=[]
i=1
iorate=500 *(10**6)
eachobjTime=[]
while(i<=racks):
	for j in range(0,servers):
		serverNum.append(i)
	i=i+1
print(serverNum)
partition = [serverNum[x:x+3] for x in range(0, len(serverNum), 3)]
print(partition)
if(len(partition)>3):
	temp=partition.pop()
	partition[-1].append(temp[0])
	if(len(temp)==2):
		partition[-1].append(temp[1])

print(partition)	
def ioTime(pos,size):
	pos=pos-1
	
	part=partition[pos]
	uniqueServers=len(set(part))
	size=size*(10**9)
	noOfPackets=networkTime.numOfPacket(size)
	
	totalTime=0
	#for j in range(0,int(noOfPackets)):
	#	time=1500.0*uniqueServers/iorate
	#	totalTime+=time
	totalTime=(1500.0*noOfPackets*uniqueServers)/(iorate * len(part))
	eachobjTime.append(totalTime)
	print(eachobjTime)
	
#print(uniqueServers)
#print(part)

	
