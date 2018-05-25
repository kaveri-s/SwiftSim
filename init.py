import subprocess,os,sys

path = "/home/kaveri/Swift/swift_map"
os.system("rm -rf "+path)
os.system("mkdir "+path)
command = "python new_swift.py 1 Get . . . ."
output=subprocess.check_output(command,shell=True).decode("UTF-8")
print("OUTPUT")
c = output.split('\n')
print(c)

if(c[0]=='0'):
    print("Empty Acoount")
    sys.exit()
c.pop(0)
print(c)

for container in c:
    if(container=='0'):
        break
    os.system("mkdir "+path+"/"+container)
    command = "python new_swift.py 2 Get "+container+" . . ."
    output=subprocess.check_output(command,shell=True).decode("UTF-8")
    o = output.split('\n')
    if(o[0]=='0'):
        print(container+" is empty")
        continue
    o.pop(0)
    for obj in o:
        print(obj)
        if(obj=='0'):
            break
        objparts = obj.split('\t')
        print(objparts)
        os.system("echo "+objparts[1]+" >"+path+"/"+container+"/"+objparts[0]+".txt")