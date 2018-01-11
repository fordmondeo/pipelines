package util;

import modules.*;
import data.*;

import java.util.*;
public class Configurator {

	// module can work in seq mode (group = module)
	// and in parallel mode (group of modules)
	// it might make sense to create an abstract class Group of
	// modules - for configuration and for monitoring the
	// progress of the group, as well as for assigning 
	// distributed ressources for the group members!
	// Streams may operate over TCP/IP sockets !, to let
	// a compute cluster execute a groups work
	
	//sorted list of modules 
	//(order is order of execution in sequential mode)
	Vector<Vector<Object>> grouplist_ = null;
	
	//map of module object to know its originating class
	HashMap<Object,String> modhash_ = null;
	
	public Configurator(String config)
	{
		
		modhash_ = new HashMap<Object,String>();
		grouplist_ = new Vector<Vector<Object>>();
		
		
		//configuration coming from a file
		//1. file to string
		//2. string to configuration
		Data configData = new Data("module modCopy in path0 out path1 ;");
		Parametrizer p = new Parametrizer(configData);
		//parametrizer gives list of modules with module params
		
		String pathIn = "C:\\Users\\Peter\\Desktop\\mailSheetPlainInbox-Total.csv";
		String pathOut = "C:\\Users\\Peter\\Desktop\\mailSheetPlainInbox-Total-Processed.csv";
		modCopy m;
		//testcase file streams		
		m = new modExtractGmail("f "+pathIn,"m out");
		addModule(m,"modCopy");
		
		if(false)
		{
		//configuration (in future to be read from a config file)
		String path0 = "C:\\Users\\Peter\\order\\assets\\collectionHtml\\T.txt";
		String path1 = "C:\\Users\\Peter\\order\\assets\\collectionHtml\\T1.txt";
		String path2 = "C:\\Users\\Peter\\order\\assets\\collectionHtml\\T2.txt";
		String path3 = "C:\\Users\\Peter\\order\\assets\\collectionHtml\\T3.txt";
		String path4 = "C:\\Users\\Peter\\order\\assets\\collectionHtml\\T4.txt";
		String path5 = "C:\\Users\\Peter\\order\\assets\\collectionHtml\\T5.txt";
		String path6 = "C:\\Users\\Peter\\order\\assets\\collectionHtml\\T6.txt";

		//testcase file streams
		
		m = new modCopy("f "+path0,"f "+path1);
		addModule(m,"modCopy");
		
		m = new modCopy("f "+path1,"f "+path2);
		addModule(m,"modCopy");
				
		m = new modEncode("f "+path2,"f "+path3);
		addModule(m,"modCopy");
		
		//testcase mem streams
		m = new modCopy("f "+path3,"m mem1");
		addModule(m,"modCopy");

		m = new modCopy("m mem1","m mem2");
		addModule(m,"modCopy");

		m = new modCopy("m mem2","m mem3a");
		addModule(m,"modCopy");

		m = new modCopy("m mem2","m mem3b");
		addModule(m,"modCopy");
		m = new modCopy("m mem2","m mem3c");
		addModule(m,"modCopy");
		
		m = new modCopy("m mem2","m mem4");
		addModule(m,"modCopy");

		//m = new modLineLength("m mem4","f "+path4);
		//addModule(m,"modCopy");
		
		Vector<Object> grp = new Vector<Object>();
		m = new modCopy("m mem4","b buf1");
		addToGroup(grp,m,"modCopy");
		m = new modDecode("b buf2","b buf3");
		addToGroup(grp,m,"modCopy");
		m = new modCopy("b buf1","b buf2");
		addToGroup(grp,m,"modCopy");
		m = new modCopy("b buf3","f "+path5);
		addToGroup(grp,m,"modCopy");
		grouplist_.add(grp);
		
		m = new modFileSystemReader("C:\\Users\\Peter\\order","f "+path6);
		addModule(m,"modFileSystemReader");
		}

	}

	public void addModule(Object m, String moduleName)
	{
		modhash_.put(m,moduleName);
		Vector<Object> modlist = new Vector<Object>();
		modlist.add(m);
		grouplist_.add(modlist);
	}
	public void addToGroup(
			Vector<Object> group,
			Object m, String moduleName)
	{
		// modules in modlist of a group are running semi-parallel
		//
		modhash_.put(m,moduleName);
		group.add(m);
	}
	
	
	public Vector<Vector<Object>> getGroupList()
	{
		return grouplist_;
	}

	public HashMap<Object,String> getModHash()
	{
		return modhash_;
	}
	
}
