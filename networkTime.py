
import json
from math import ceil
import sys
hops_o=4
hops_ac=2
linkSpeed=125000000.0 #1Gbps  or 125000000Bytesps    /bandwidth .The number of bytes that are put onto the link in one second
processSpeed=1*(10**9)
velocity=200000000.0 # 2*10^8 m/s				 
MSS=1460
MTU=1500
distance=100
headerSize=1*(10**3)

with open('workload.json') as w:
	workloadData=json.load(w)
#size=sys.argv[1]
createCom=workloadData["create"]
readCom=workloadData["read"]
deleteCom=workloadData["delete"]
jsonObjectSize=300.0 #bytes josn object returned for each object on container get
jsonContainerSize = 200.0
l=0
r=0
wl=[]
def numOfPacket(size):
	return ceil(int(size)/1460.0)
	
def transmissionDelay(tSize):
	#print(tSize)
	return (tSize/linkSpeed)

def propagationDelay():
	return (distance/velocity)	

def netDelay(size,op,method):
	if(method==0):
		print("From GUI")
		if(op==1):       #put or get object
		
			numOfPackets=int(numOfPacket(size))
			totalSize=int(size)+(40*numOfPackets)
	
			td=transmissionDelay(totalSize)
	
			pd=propagationDelay()
	
			totalNetDelay=hops_o*(td+pd)
			print(totalNetDelay)
	
		elif(op==2):	#get container
			numOfObjects=size
			size=numOfObjects*(headerSize+jsonObjectSize)
			numOfPackets=int(numOfPacket(size))
			totalSize=int(size)+(40*numOfPackets)
	
			td=transmissionDelay(totalSize)
	
			pd=propagationDelay()
	
			totalNetDelay=hops_ac*(td+pd)
			print(totalNetDelay)
		
		elif(op==3):
			numOfContainers=size
			size=numOfContainers*(headerSize+jsonContainerSize)
			numOfPackets=int(numOfPacket(size))
			totalSize=int(size)+(40*numOfPackets)
	
			td=transmissionDelay(totalSize)
	
			pd=propagationDelay()
	
			totalNetDelay=hops_ac*(td+pd)
			print(totalNetDelay)

	elif(method==1):
		print("From Workload")
		if(op<createCom):
			size=size*(10**9)
			numOfPackets=int(numOfPacket(size))
			totalSize=int(size)+(40*numOfPackets)
			
			td=transmissionDelay(totalSize)

			global l
			l=l+td
			pd=propagationDelay()
			if(op==0):
				totalNetDelay=hops_o*(td+pd)
				wl.append(totalNetDelay)
			else:

				totalNetDelay=hops_o*(td+pd+(l*((linkSpeed/processSpeed)**op))) #add the transmission delay of previous request 
				wl.append(totalNetDelay)
		
		if(op>=createCom and op<(createCom+readCom)):
			size=size*(10**9)
			numOfPackets=int(numOfPacket(size))
			totalSize=int(size)+(40*numOfPackets)
			#print(totalSize)
			td=transmissionDelay(totalSize)

			global r
			#l=l+td
			if(op==createCom):
				r=0
			else:
				r=r+td
			#print(r)
			pd=propagationDelay()
			if(op==0):
				totalNetDelay=hops_o*(td+pd)
			else:

				totalNetDelay=hops_o*(td+pd+(r*((linkSpeed/processSpeed)**op))) #add the transmission delay of previous request 
				wl.append(totalNetDelay)
				
		#print(totalNetDelay)
		print(wl)


	

	


	
#netDelay(size,1,1)	

