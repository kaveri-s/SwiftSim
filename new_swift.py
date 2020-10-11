import json
import subprocess,os,sys


c = 'ssh -t ubuntu@192.168.122.54 "cd ~/mystack && . admin-openrc && swift stat | grep AUTH"'
acc_data=subprocess.check_output(c,shell=True).decode("UTF-8")
acc_data=acc_data.strip()
temp,account_id=acc_data.split(":")
account_id=account_id.strip()
#print(account_id)


publicURL="http://192.168.122.54:8080/v1/"


def get_token():
    command = 'ssh -t ubuntu@192.168.122.54 "cd ~/mystack && . admin-openrc && openstack token issue -f json"'
    #command = "openstack token issue -f json"
    data = subprocess.check_output(command, shell=True).decode("UTF-8")
    json_data = json.loads(data)
    token = json_data["id"]
    #print(token)
    return token


def parse_output_a(output):
    container_list=[]
    i=0
    while(output[i]!="["):
        i=i+1
    str_output=output[i:]
    #print(str_output)
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
        #print(json_output)
        #for j in range(0,len(json_output)):
        j=0
        n=len(json_output)
        while(j<n):
            temp_list=[]
            ustr=json_output[j]['name']
            astr=ustr.encode("ascii","replace")
            temp_list.append(astr)
            ustr=json_output[j]['bytes']
            #astr=ustr.encode("ascii","replace")
            temp_list.append(ustr)
            ustr=json_output[j]['content_type']
            astr=ustr.encode("ascii","replace")
            temp_list.append(astr)
            j=j+1
            object_list.append(temp_list)
        return object_list

	
token=get_token()    
f=open("write_output.txt","w+") 
    
# first parameter is for account(1),container(2),object(3)
# second parameter is to specify what action it is like for example , get , put etc
#third parameter is the name of the container
#fourth parameter is the object to be put  
param1=sys.argv[1]
param2=sys.argv[2]
param3=sys.argv[3]
param4=sys.argv[4]


if(param1=="1"):  #account
	if(param2=="Get"):
		command = "curl -i " + publicURL+account_id +"?format=json -X GET -H \"X-Auth-Token: "+token+"\""
    		#os.system(command)
                output=subprocess.check_output(command,shell=True)
                #l=[[]]
                http_response=int(output[8:12])
                if(http_response!=404):
                    print("1") #f.write("1" + '\n')
                    l=parse_output_a(output)
                    for w in l:
			    print(w) #f.write(w + '\n')
                else:
                    print("0") #f.write("0")
                #print(output)
                #print(l)
                #print(output[1])

                #print(type(output))
                
elif(param1=="2"): #container
	if(param2=="Get"):
		command="curl -i " + publicURL+account_id+"/"+param3+"?format=json -X GET -H \"X-Auth-Token: "+token+"\""
    		output=subprocess.check_output(command,shell=True)
    		http_response=int(output[8:12])
    		if(http_response==200 or http_response==204):
    			l=parse_output_c(output)
    			#flag=1
                        print("1") #f.write('1' + '\n')
                        if(l==[]):
			    print("Empty")
                        for x in l:
			    le = len(l[0])
                            for y in range(0,le):
                                sys.stdout.write(str(x[y]) + '\t') #f.write(str(x[y]) + "\t")
                            sys.stdout.write('\n') #f.write('\n')
  		elif(http_response==404):
    			#flag=0
                        print("0") #f.write('0')
                #print(l)	
	elif(param2=="Put"):
		flag=1
		command="curl -i "+publicURL+account_id+"/"+param3+" -X PUT -H \"Content-Length:0\" -H \"X-Auth-Token:"+token+"\""
        	output=subprocess.check_output(command,shell=True)
        	http_response=int(output[8:12])
    		if(http_response==201 or http_response==202):
    			flag=1
    		elif(http_response==404 or http_response==400):
    			flag=0
                print(flag) #f.write(str(flag))
#deletion ofcontainer is possible only if the container is empty. If it has even a single object in it, then deleting it will result in 409 #Conflict	
	elif(param2=="Delete"):
		command="curl -i "+publicURL+account_id+"/"+param3+" -X DELETE -H \"X-Auth-Token: "+token+"\""
            	output=subprocess.check_output(command,shell=True)
            	http_response=int(output[8:12])
    		if(http_response==204):
    			flag=1
    		elif(http_response==404):
    			flag=0
    		elif(http_response==409):
    			flag=2
	        print(flag) #f.write(str(flag))

elif(param1=="3"):  #object
	if(param2=="Get"):
		flag=1
		command="curl -i "+publicURL+account_id+"/"+param3+"/"+param4+" -X GET -H \"X-Auth-Token: "+token+"\""
        	output=subprocess.check_output(command,shell=True)
        	http_response=int(output[8:12])
    		if(http_response==200 or http_response==204):
    			#l=parse_output_o(output)
    			flag=1
    			#print(output)
    		elif(http_response==404):
    			flag=0
                print(flag) #f.write(str(flag))
	
	elif(param2=="Put"):
		l = param4.split('/')
		flag=1
		command="curl -i "+publicURL+account_id+"/"+param3+"/"+l[-1]+" -X PUT -H \"X-Auth-Token: "+token+"\" -T "+param4 
        	output=subprocess.check_output(command,shell=True)
        	http_response=int(output[8:12])
    		if(http_response==201):
    			flag=1
    		elif(http_response==404):
    			flag=0
    		elif(http_response==408):
    			flag=2 #request time out
        	print(flag) #f.write(str(flag))
	
	elif(param2=="Delete"):
		flag=1
		command="curl -i "+publicURL+account_id+"/"+param3+"/"+param4+" -X DELETE -H \"X-Auth-Token: "+token+"\""
        	output=subprocess.check_output(command,shell=True)
        	http_response=int(output[8:12])
    		if(http_response==204):
    			flag=1
    		elif(http_response==404):
    			flag=0
                print(flag) #f.write(str(flag))

f.close()

