package util;

import modules.*;
import data.*;
import java.util.*;
public class Parametrizer {

	//get list of parameters from module
	//extract parameters from parameter file
	Data params_ = null;
	
	//
	HashMap <Data,String> moduleList_ = null;
	
	public Parametrizer(Data params)
	{
		params_ = params.clone();
	}
	public void buildGroupList()
	{
		Vector<String> v = params_.grepVector("group");
		for(int i=0; i<v.size();i++)
		{
			
		}
	}
	public void buildModuleList()
	{
		Vector<String> v = params_.grepVector("module");
		for(int i=0; i<v.size();i++)
		{
			
		}
	}
	
	
	
}
