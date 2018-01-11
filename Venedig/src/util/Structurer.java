package util;
import java.util.*;

public class Structurer {
	//unit of separation = " ";
	String separator_ = null;
	public Structurer(String sep)
	{
		separator_=sep;
	}
	public Structurer()
	{
		separator_=" ";
	}

	public Vector<String> String2StringVector(String str)
	{
		if(str==null) return null;		
		Vector<String> v = new Vector<String>();
		String[] a = str.split(separator_);
		v=StringArray2StringVector(a);
		return v;
	}
	
	public String StringVector2String(Vector<String> v)
	{
		if(v==null)return null;
		String str = null;
		str = "";
		for(int i=0;i<v.size()-1;i++)
		{
			str=str+v.elementAt(i)+separator_;
		}
		str=str+v.elementAt(v.size()-1);
		return str;
	}

	public String[] String2StringArray(String str)
	{
		if(str==null) return null;
		String[] a = str.split(separator_);
		return a;
	}
	public String StringArray2String(String[] a)
	{
		if(a == null) return null;
		String str = null;
		for(int i=0;i<a.length-1;i++)
		{
			str = str + a[i] + separator_;
		}
		str = str + a[a.length-1];
		return str;
	}

	public Vector<String> StringArray2StringVector (String[] a)
	{
		if(a == null) return null;
		Vector<String> v = new Vector<String>();		
		for(int i=0;i<a.length;i++)
		{
			v.add(a[i]);
		}
		return v;
	}
	public String[] StringVector2StringArray (Vector<String> v)
	{
		if(v==null) return null;
		String [] a = new String[v.size()];
		for(int i=0;i<v.size();i++)
		{
			a[i]=v.elementAt(i);
		}
		return a;
	}
	
	
	
}
