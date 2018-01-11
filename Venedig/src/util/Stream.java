package util;

import java.util.*;

public class Stream<T> {
	String name_ = null;
	Vector<T> data_ = null;
	String mode_ = null;
	Status status_ = null;
	
	
	public Stream(String name){
		data_ = new Vector<T>();
		mode_ = "c"; // created
		if(name_ == null) name_ = "nemo";// use id generator here
		status_ = new Status();
		status_.update("name",name_);
		status_.update("mode","c");
		status_.update("size",0);
		}
	public void open(String mode)
	{
		if(mode.compareTo("w")==0)
		{
			openWrite();
		}
		if(mode.compareTo("r")==0)
		{
			openRead();
		}
	}
	private boolean openWrite()
	{
		if(status_.check("mode","r")==false)
			{
			mode_="w";	
			status_.update("mode","w");
			return true;
		}
		else 
			return false;
	}
	private void openRead()
	{
		mode_="r";
		status_.update("mode","r");
	}
	public void put(T element)
	{
		//put element on top of stream
		data_.add(element);
		status_.update("size", status_.toInteger("size")+1);
	}
	public T get()
	{
		//take out element at bottom of stream
		T element = null;
		if(data_.size()>0) element = data_.elementAt(0);
		data_.remove(0);
		status_.update("size", status_.toInteger("size")-1);
		return element;
	}
	public Status status()
	{
		return status_;
	}
	public void close(){}
	
	
}
