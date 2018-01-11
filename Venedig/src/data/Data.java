package data;
import java.util.*;
import util.Structurer;

public class Data {
	String data_ = null;
	Vector<String> dataVector_ = null;
	String[] dataArray_ = null;
	Structurer s = new Structurer();
	public Data (){}
	public Data (String d){set(d);}
	public Data (Vector<String> v){set(v);}
	
	public Data clone()
	{
		Data c = new Data();
		c.set(get());
		return c;
	}
	
	public void set(String d){data_=d;}
	public void set(Vector<String> v)
	{
		data_=s.StringVector2String(v);
	}
	public void set(String[] a)
	{
		data_=s.StringArray2String(a);
	}
	
	public String get(){return data_;}
	public Vector<String> getVector()
	{
		if(dataVector_ == null)
			dataVector_ = s.String2StringVector(data_);
		return dataVector_;
	}
	public String[] getArray()
	{
		if(dataArray_ == null)
			dataArray_ = s.String2StringArray(data_);
		return dataArray_;
	}
	
	public Vector<String> grepVector(String tag)
	{
		if(dataVector_ == null)
			dataVector_ = s.String2StringVector(data_);
		Vector<String> v = new Vector<String>();
		for(int i=0;i<dataVector_.size();i++)
		{
			String line = dataVector_.elementAt(i);
			String[] w = line.split(" ");
			if(w[0].compareTo(tag)==0)v.add(line);
		}
		if(v.size()==0) return null;
		return v;
	}
	
}
