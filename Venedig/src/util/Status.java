package util;

import java.util.*;

public class Status {
	HashMap<String,String> features_ = null;
	public Status ()
	{
		features_ = new HashMap<String,String>();
	}
	public void update(String feature, String value)
	{
		features_.put(feature, value);
	}
	public void update(String feature, Integer value)
	{
		features_.put(feature, ""+value);
	}
	public void update(String feature, Boolean value)
	{
		features_.put(feature, ""+value);
	}
	public boolean check(String feature, String value)
	{
		if(features_.get(feature).compareTo(value)==0)
			return true;
		else return false;
	}
	public boolean check(String feature, Integer value)
	{
		if(Integer.parseInt(features_.get(feature))==value)
			return true;
		else return false;
	}
	public boolean check(String feature, Boolean value)
	{
		if(Boolean.parseBoolean(features_.get(feature))==value)
			return true;
		else return false;
	}

	public String get(String feature)
	{
		return toString(feature);
	}
	public String toString(String feature)
	{
		if(features_.containsKey(feature))
			return features_.get(feature);
		return null;
	}
	public Integer toInteger(String feature)
	{
		if(features_.containsKey(feature))
			return Integer.parseInt(features_.get(feature));
		return null;
	}
	public Boolean toBoolean(String feature)
	{
		if(features_.containsKey(feature))
			return Boolean.parseBoolean(features_.get(feature));
		return null;
	}
	
	public HashMap<String,String> report()
	{
		if(features_ == null) return null;
		HashMap<String,String> f = new HashMap<String,String>();
		for(String s : f.keySet())f.put(s, features_.get(s));
		return f;
	}
}
