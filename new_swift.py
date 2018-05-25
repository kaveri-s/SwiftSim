import json
import subprocess,os,sys
#from .networkTime import netDelay
import networkTime

c = 'ssh -t ubuntu@192.168.122.54 "cd ~/mystack && . admin-openrc && swift stat | grep AUTH"'
acc_data=subprocess.check_output(c,shell=True).decode("UTF-8")
acc_data=acc_data.strip()
temp,account_id=acc_data.split(":")
account_id=account_id.strip()


publicURL="http://192.168.122.54:8080/v1/"

# print(publicURL+account_id)

def get_token():
	command = 'ssh -t ubuntu@192.168.122.54 "cd ~/mystack && . admin-openrc && openstack token issue -f json"'
	data = subprocess.check_output(command, shell=True).decode("UTF-8")
	json_data = json.loads(data)
	token = json_data["id"]
	return token


def parse_output_a(output):
	container_list=[]
	i=0
	while(output[i]!="["):
		i=i+1
	str_output=output[i:]
	json_output=json.loads(str_output)
	for j in range(0,len(json_output)):
		ustr=json_output[j]['name']
		astr=ustr.encode("ascii","replace")
		container_list.append(astr)
        
	return container_list


def parse_output_c(output):
	object_list=[]
	i=0
	while(output[i]!="["):
		i=i+1
	str_output=output[i:]
	json_output=json.loads(str_output)
	j=0
	n=len(json_output)
	while(j<n):
		temp_list=[]
		ustr=json_output[j]['name']
		astr=ustr.encode("ascii","replace")
		temp_list.append(astr)
		ustr=json_output[j]['bytes']
		temp_list.append(ustr)
		ustr=json_output[j]['content_type']
		astr=ustr.encode("ascii","replace")
		temp_list.append(astr)
		j=j+1
		object_list.append(temp_list)
	return object_list

token=get_token()

param1=sys.argv[1]
param2=sys.argv[2]
param3=sys.argv[3]
param4=sys.argv[4]
param5=sys.argv[5]
param6=sys.argv[6]

if(param1=="1"):  #account
	if(param2=="Get"):
		command = "curl -i " + publicURL+account_id +"?format=json -X GET -H \"X-Auth-Token: "+token+"\""
		output=subprocess.check_output(command,shell=True)
		http_response=int(output[8:12])
		if(http_response!=404):
			print("1")
			l=parse_output_a(output)
			for w in l:
				print(w)
			print('0')
		else:
			print("0")
		numContainers=len(l)
		networkTime.netDelay(numContainers,3,0)
                
elif(param1=="2"): #container
	if(param2=="Get"):
		command="curl -i " + publicURL+account_id+"/"+param3+"?format=json -X GET -H \"X-Auth-Token: "+token+"\""
		output=subprocess.check_output(command,shell=True)
		http_response=int(output[8:12])
		if(http_response==200 or http_response==204):
			l=parse_output_c(output)
			print("1")
			if(l==[]):
				print("Empty")
			else:
				for x in l:
					size, name = x[0].split(":")
					sys.stdout.write(name + '\t' + size + '\t' + x[2])
					sys.stdout.write('\n')
					print('0')
		elif(http_response==404):
			print("0")
		#numObjects=len([name for name in os.listdir('.') if os.path.isfile(name)])
		numObjects=len(l)
		networkTime.netDelay(numObjects,2,0)
		
			
	elif(param2=="Put"):
		flag=1
		command="curl -i "+publicURL+account_id+"/"+param3+" -X PUT -H \"Content-Length:0\" -H \"X-Auth-Token:"+token+"\""
		output=subprocess.check_output(command,shell=True)
		http_response=int(output[8:12])
		if(http_response==201 or http_response==202):
			os.system("mkdir swift_map/"+param3)
			flag=1
		elif(http_response==404 or http_response==400):
			flag=0
			print(flag)
	#deletion ofcontainer is possible only if the container is empty. If it has even a single object in it, then deleting it will result in 409 #Conflict	
	elif(param2=="Delete"):
		command="curl -i "+publicURL+account_id+"/"+param3+" -X DELETE -H \"X-Auth-Token: "+token+"\""
		output=subprocess.check_output(command,shell=True)
		http_response=int(output[8:12])
		if(http_response==204):
			os.system("rmdir swift_map/"+param3)
			flag=1
		elif(http_response==404):
			flag=0
		elif(http_response==409):
			flag=2
		print(flag)

elif(param1=="3"):  #object
	fileName=os.path.basename(param4)
	fileName=fileName+".txt"
	if(param2=="Get"):
		flag=1
		fil=open('swift_map/'+param3+'/'+fileName,"r+")
		fileSize=fil.read()
		networkTime.netDelay(fileSize,1,0)
		command="curl -i "+publicURL+account_id+"/"+param3+"/"+fileSize+":"+param4+" -X GET -H \"X-Auth-Token: "+token+"\""
		output=subprocess.check_output(command,shell=True)
		print(output)
		http_response=int(output[8:12])
		if(http_response==200 or http_response==204):
			flag=1
		elif(http_response==404):
			flag=0
		print(flag)
	
	elif(param2=="Put"):
		l = param4.split('/')
		flag=1
		fileSize=0
		if(param5=="w"):
			#fileSize = param6
			#networkTime.netDelay(fileSize,1,1)
			os.system("python workload.py")
		else:
			fileSize = str(os.path.getsize(param4))
			networkTime.netDelay(fileSize,1,0)
		command="curl -i "+publicURL+account_id+"/"+param3+"/"+fileSize+":"+l[-1]+" -X PUT -H \"X-Auth-Token: "+token+"\" -H \"Content-Length:0\""
		output=subprocess.check_output(command,shell=True)
		http_response=int(output[8:12])
		if(http_response==201):
			fil=open('swift_map/'+param3+'/'+fileName,"w+")
			fil.write(fileSize)
			flag=1
		elif(http_response==404):
			flag=0
		elif(http_response==408):
			flag=2 #request time out
		print(flag)
	
	elif(param2=="Delete"):
		flag=1
		fil=open('swift_map/'+param3+'/'+fileName,"r+")
		fileSize=fil.read()
		command="curl -i "+publicURL+account_id+"/"+param3+"/"+fileSize+":"+param4+" -X DELETE -H \"X-Auth-Token: "+token+"\""
		output=subprocess.check_output(command,shell=True)
		http_response=int(output[8:12])
		if(http_response==204):
			flag=1
		elif(http_response==404):
			flag=0
		print(flag)
	
	#fil.close()


