package app;
import util.*;
import modules.*;
import java.util.*;

public class appPipeliner {

	static HashMap<Object,String> modhash_ = null;
	static Vector<Vector<Object>> grouplist_ = null;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		System.out.println("pipeline is running");
		Configurator c = new Configurator("here comes the config descriptor, e.g. filename");
		modhash_ = c.getModHash();
		grouplist_ = c.getGroupList();
		
		while(true)
		{
			cycle();
			if(isFinished())break;
			
		}
		System.out.println("yeah pipeline finished !");
		
	}
	
	public static boolean isFinished()
	{
		boolean f = true;		
		// go through all groups, go through all modules per group
		// and check if module is finished
		for(int i=0; i< grouplist_.size(); i++)
		{
			if(isGroupFinished(grouplist_.elementAt(i))==false)
				{f=false;break;}
		}
		return f;
	}

	public static boolean isGroupFinished
		(Vector<Object> modlist)
	{
		boolean f = true;
		for(int j=0; j< modlist.size(); j++)
			{			
				if(modhash_.get(modlist.elementAt(j)).compareTo("modCopy")==0)
				{
					modCopy cmod = (modCopy)modlist.elementAt(j);
					if(!cmod.isFinished()) 
						{
							f = false;break; 
						}
				}
			}
		return f;
	}

	
	
	public static void cycle()
	{
		// go through all groups, go through all modules per group
		// and check if module is finished
		for(int i=0; i< grouplist_.size(); i++)
		{
			Vector<Object> modlist = grouplist_.elementAt(i);
			initGroup(modlist);
				//sequential model
			while(isGroupFinished(modlist)==false)
			{
					cycleGroup(modlist);
			} 
			closeGroup(modlist);
		}
	}

	public static void initGroup(Vector<Object> modlist)
	{
		for(int j=0; j< modlist.size(); j++)
		{			
				modCopy cmod = (modCopy)modlist.elementAt(j);
				cmod.init();
		}
	}
	public static void closeGroup(Vector<Object> modlist)
	{
		for(int j=0; j< modlist.size(); j++)
		{			
			modCopy cmod = (modCopy)modlist.elementAt(j);
			cmod.close();
		}
	}

	public static void cycleGroup(Vector<Object> modlist)
	{
		for(int j=0; j< modlist.size(); j++)
		{			
				modCopy cmod = (modCopy)modlist.elementAt(j);
				cmod.cycle();
		}
	}


//	int c=0;
//	while(m.ready())
//	{
//		System.out.println("copying line "+c);c++;
//		m.cycle();
//	}
//	m.finish();
//	System.out.println("finished copy process");


}
