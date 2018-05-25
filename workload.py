import pymysql
import json, string
from math import ceil, floor
import random
import subprocess, sys
import networkTime
import sio
import six
#json data
data = {}

#store objects being created
objstore = []

#list of common extensions for files
extensions = ['txt', 'mp3', 'png', 'mp4', 'sql', 'json']

#connect to mysql to store object partition info for workflow simulations
hostname = 'localhost'
username = 'swiftsim'
password = 'ubuntu'
database = 'swift_map'

conn = pymysql.connect( host=hostname, user=username, passwd=password, db=database)

stmt = 'delete from mypartition';
cursor = conn.cursor()
result = cursor.execute(stmt)
conn.commit()

def converttob(size, given):
	if(given=='gb'):
		return size*1024*1024*1024
	if(given=='mb'):
		return size*1024*1024
	if(given=='kb'):
		return size*1024

def ascii_encode_dict(data):
	ascii_encode = lambda x: x.encode('ascii') if isinstance(x, bytes) else x
	return dict(map(ascii_encode, pair) for pair in data.items())

#change size associated with each type or define own types
def size_gen(stype):
	if(stype=='min'):
		return random.randint(1, 50)*0.1
	if(stype=='mid'):
		return random.randint(10, 30)*0.5
	if(stype=='max'):
		return random.randint(15, 50)

def name_gen(size=10, chars=string.ascii_uppercase + string.ascii_lowercase):
	return ''.join(random.choice(chars) for _ in range(size))

#main code begins here
with open('workload.json') as f:
	data = json.load(f, object_hook=ascii_encode_dict)

	print("\nJson Loaded")
	for key, value in data.items():
		print(key, ":", value)

if(data["delete"]>data["create"] or data["read"]>data["create"]):
	print("Invalid Workload")
	sys.exit(0)

totser = data["servers"]*data["racks"]
sppf = totser/3.0
spp = [floor(sppf), floor(sppf), ceil(sppf)]

#initialise DB
for x in range(1, 4):
	stmt = 'insert into mypartition values (%s, %s, %s, %s)';
	args = (str(x), str(spp[x-1]), str(data["storage"]*spp[x-1]), str(data["storage"]*spp[x-1]))
	cursor = conn.cursor()
	cursor.execute(stmt, args)
	conn.commit()
	
#create commands
for c in range(0, data["create"]):
	size = size_gen(data["type"])
	ext = random.randint(0,5)
	objname = name_gen()+'.'+extensions[ext]
	print("################################################################################")
	command = 'python new_swift.py 3 PUT test '+objname+' w '+str(size)
	print(command)
	# os.system(command)
	stmt = 'select * from mypartition order by rem desc';
	cursor = conn.cursor()
	cursor.execute(stmt)
	result = cursor.fetchone()
	newsize = result[3]-size
	#newsize=1
	if(newsize>=0):
		stmt = 'update mypartition set rem='+str(newsize)+' where name='+str(result[0]);
		cursor = conn.cursor()
		cursor.execute(stmt)
		conn.commit()
		#subprocess.call(command, shell=True)
		objstore.append([objname, size, result[0]])
		#calc network
		#print(size)
		print("\nNetwork Time:")
		networkTime.netDelay(size,c,1)
		#calc io
		print("\nIO Time: ")
		sio.ioTime(2,size)
	else:
		print('No space. Object not stored')
		if data["delete"]==data["create"]:
			data["delete"]-=1
		if data["read"]==data["create"]:
			data["read"]-=1
	print("################################################################################")
#print("Current list of objects: ")
#print(objstore)			

#read commands
for r in range(0, data["read"]):
	print("################################################################################")	
	command = 'python new_swift.py 3 GET test '+objstore[r][0]+' w '+str(objstore[r][1])
	print(command)
	# os.system(command)

	#calc network
	print("\nNetwork Time:")
	networkTime.netDelay(size,r+data["create"],1)
	#io.ioTime(int(result[0]),size)
	print("\nIO Time: ")
	sio.ioTime(2,size)
	#calc io
	print("################################################################################")

#delete commands
for d in range(0, data["delete"]):
	print("################################################################################")
	command = 'python new_swift.py 3 DELETE test '+objstore[d][0]+' w '+str(objstore[d][1])
	print(command)
	# os.system(command)
	stmt = 'update mypartition set rem=rem+'+str(objstore[d][1]/3.0)+' where name='+str(objstore[d][2]);
	cursor = conn.cursor()
	cursor.execute(stmt)
	conn.commit()
	l = objstore[d]
	objstore.remove(l)
	print("################################################################################")

#print(objstore)
	
#conn.close()
