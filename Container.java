package org.opensim.examples;
import java.util.*;
import java.io.*;
//import java.util.regex.Pattern;

public class Container {
    String conName;
    String objName;
    int size;
    String type;
    String timestamp;
    
    public String mypath = "/home/kaveri/Swift/";
    //private static final Pattern TAB_SPLITTER = Pattern.compile("\t");
    
    public static List<String[]> objList = new ArrayList<String[]>();
    
    public List<String> readFile(String f) {
    	String s= null;
    	List<String> list = new ArrayList<String>();
		try {
    			Process p = Runtime.getRuntime().exec("python /home/kaveri/Swift/new_swift.py "+f);
    			p.waitFor();
    			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
    			while((s=in.readLine())!=null){
    				list.add(s);
    			}
    	}
    	catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
		catch (InterruptedException ex) {
            System.err.println(ex.getMessage());
        }
    	
    	return list;
    }
    
    public String[] getAccount(){
    	List<String> list = readFile("1 Get . .");
    	String[] stringArr = list.toArray(new String[0]);
    	String[] result = Arrays.copyOfRange(stringArr, 1, stringArr.length);
    	System.out.println(Arrays.toString(result));
    	return result;
    }
    
    public List<String[]> getContainer(String conName) {
    	List<String> list = readFile("2 Get "+conName+" .");
    	List<String[]> objlist = new ArrayList<String[]>();
    	String[] stringArr = list.toArray(new String[1]);
    	if(!stringArr[1].equals("Empty")) {
    		for(int i=1;i<stringArr.length;i++) {
    			String[] bits = stringArr[i].split("\\t");
    			objlist.add(bits);
    		}
    	}
    	return objlist;
    }
    
    public String getObject(String conName, String objName) {
    	List<String> list = readFile("3 Get "+conName+" "+objName);
    	String result = String.join("\n", list);
    	return result;
    }
    
    public int createContainer(String conName) {
    	List<String> list = readFile("2 Put "+conName+" .");
    	String[] stringArr = list.toArray(new String[0]);
    	int result = Integer.parseInt(stringArr[0]);
    	return result;
    }
    
    public int createObject(String conName, String filePath) {
    	List<String> list = readFile("3 Put "+conName+" "+filePath);
    	String[] stringArr = list.toArray(new String[0]);
    	int result = Integer.parseInt(stringArr[0]);
    	return result;
    }
    
    public int deleteContainer(String conName) {
    	List<String> list = readFile("2 Delete "+conName+" .");
    	String[] stringArr = list.toArray(new String[0]);
    	int result = Integer.parseInt(stringArr[0]);
    	return result;
    }
    
    public int deleteObject(String conName, String objName) {
    	List<String> list = readFile("3 Delete "+conName+" "+objName);
    	String[] stringArr = list.toArray(new String[0]);
    	int result = Integer.parseInt(stringArr[0]);
    	return result;
    }
    
}