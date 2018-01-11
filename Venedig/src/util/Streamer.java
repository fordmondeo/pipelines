package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;


public class Streamer {
	//this is an adapter class, which 
	//offers stream functionality independent of
	//the underlying platform. Currently, only java streams
	//are supported as underlying platform

	//stream descriptor = "[type] [name]"
	//"f a.txt", 
	//"m mymem", 
	//"b mybuf"
	//"r myram"  // complete free memory address, asynchr. writing and reading as in nature fcfs
	//"s <string with content>"
	//"p linux-pipe"
	//"j java-object-i/o"
	String descriptor_ = null;
	String name_ = null; // "a.txt"
	String type_ = null; // "f"
	String mode_ = null;
	Status status_ = null;

	//status of stream, used by module to know if it
	//work is finished
	boolean isClosed_ = false;
	boolean isAtEnd_ = false;
	boolean debug_ = false;
	
	//internal stream
	Stream<String> stream_ = null;
	
	//file
	BufferedReader br_ = null;
	BufferedWriter bw_ = null;
	//memfile, global class variable, storing all memfiles 
	static private final HashMap<String,String> memfiles_ = new HashMap<String,String>();

	//local object variable
	Vector<String> memfileLines_ = null;
	int memfilePointer_=0;
	
	static private final HashMap <String,String> buffer_ = new HashMap<String,String>();
	static private final HashMap <String,Integer> bufferReaderUsage_ = new HashMap<String,Integer>();
	static private final HashMap <String,Integer> bufferWriterUsage_ = new HashMap<String,Integer>();
	
	public Streamer(String str)
	{
		
		descriptor_=str;
		stream_ = new Stream<String>(str);
	}
	public void setDebug(boolean flag)
	{
		debug_=flag;
	}
	public void setEnd(boolean flag)
	{
		isAtEnd_=flag;
	}
	
	//opening and closing the stream
	
	public boolean open(String mode)
	{
		boolean success = false;
		mode_=mode;
		if(mode.compareTo("r")==0) 
			{
				success=openRead();
			}

		if(mode.compareTo("w")==0) 
			{
				success=openWrite();
			}
		return success;
	}

	public void close()
	{
		if(type_.compareTo("f")==0) closeFile();
		if(type_.compareTo("m")==0) closeMemfile();
		if(type_.compareTo("b")==0) closeBuffer();
		if(type_.compareTo("z")==0) closeStream();
	}
	public void closeStream()
	{
		stream_.close();
	}
	public void closeBuffer()
	{
//		writeLine("[EOS-1123581321345589]");
		if(mode_.compareTo("r")==0)
		{
			if(bufferReaderUsage_.containsKey(name_)==false)
				return;
			
			Integer u = bufferReaderUsage_.get(name_);
			if(u.intValue() > 0) 
			{
				bufferReaderUsage_.put(name_,new Integer(u.intValue()-1));
				if(debug_)System.out.println("closing attempt buffer "+name_+ " in mode "+mode_+ " at usage "+u);
			}
		}
		if(mode_.compareTo("w")==0)
		{
			if(bufferWriterUsage_.containsKey(name_)==false)
				return;
			Integer u = bufferWriterUsage_.get(name_);
			if(u.intValue() > 0) 
			{
				bufferWriterUsage_.put(name_,new Integer(u.intValue()-1));
				if(debug_)System.out.println("closing attempt buffer "+name_+ " in mode "+mode_+ " at usage "+u);
			}
		}
		if(bufferWriterUsage_.get(name_).intValue()+bufferReaderUsage_.get(name_).intValue() == 0) 
		{
			destroyBuffer(name_);
		}

	}

	public void closeMemfile()
	{
//		writeLine("[EOS-1123581321345589]");		
		String content = "";
		for(int i=0;i<memfileLines_.size();i++)
		{
			content += memfileLines_.elementAt(i);
			content += System.lineSeparator();
		}
		memfiles_.put(name_,content);
		if(debug_)System.out.println("closing memfile "+name_+ " in mode "+mode_);

	}
	
	public void closeFile()
	{
		try{
				if(br_ != null)
					{
						br_.close();
						br_=null;
					}
				if(bw_ != null)
					{
						bw_.close();
						bw_=null;
					}
			} catch (IOException e) {
			e.printStackTrace();
		}
		isClosed_=true;
	}
	public String[] parseDescriptor(String descr)
	{
		return descr.split(" ");		
	}
	public void createBuffer(String name)
	{
		bufferReaderUsage_.put(name, new Integer(0));
		bufferWriterUsage_.put(name, new Integer(0));
		buffer_.put(name,null);
		if(debug_)System.out.println("creating buffer "+name_+ " in mode "+mode_);
	}
	public void destroyBuffer(String name)
	{
		buffer_.remove(name_);
		bufferReaderUsage_.remove(name_);
		bufferWriterUsage_.remove(name_);
		if(debug_)System.out.println("destruction of buffer "+name_+ " in mode "+mode_);
	}
	
	public boolean openRead()
	{
		String[] format = parseDescriptor(descriptor_);
		type_ = format[0];
		name_ = format[1];
		
		if(type_.compareTo("f")==0) return openReadFile(name_);
		if(type_.compareTo("m")==0) return openReadMemfile(name_);
		if(type_.compareTo("b")==0) return openReadBuffer(name_);
		return false;
	}
	
	public boolean openReadBuffer(String name)
	{
		if(bufferReaderUsage_.containsKey(name_)==false)
		{
			createBuffer(name);
		}
		Integer u = bufferReaderUsage_.get(name_);
		bufferReaderUsage_.put(name_,new Integer(u.intValue()+1));
		u = bufferReaderUsage_.get(name_);
		if(debug_)System.out.println("registering user no. "+u+" for buffer "+name_+ " in mode "+mode_);
		return true;
	}
	
	public boolean openReadMemfile(String name)
	{
		// access hash with Strings as Memory
		if(debug_)System.out.println("opening memfile "+name_+" for reading");
		if(memfiles_.containsKey(name)) 
			{
				String content = memfiles_.get(name);
				String[] lines = content.split(System.lineSeparator());
				memfileLines_ = new Vector<String>();
				for(int i=0;i<lines.length;i++)
				{
					memfileLines_.add(lines[i]);
				}
				if(debug_)System.out.println("opening memfile "+name+" for reading, lines found "+memfileLines_.size());
				memfilePointer_=0;
				return true;
			}
		else
			if(debug_)System.out.println("Warning could not find memfile "+name);
		return false;
	}
	public boolean openReadFile(String name)
	{
		try{
		FileInputStream fstream = new FileInputStream(name);
		DataInputStream in = new DataInputStream(fstream);
		br_ = new BufferedReader(new InputStreamReader(in));
		} catch (IOException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	public boolean openWrite()
	{
		String[] format = parseDescriptor(descriptor_);
		type_ = format[0];
		name_ = format[1];
		
		//only stream type "f ..." currently supported
		
		if(type_.compareTo("f")==0) return openWriteFile(name_);
		if(type_.compareTo("m")==0) return openWriteMemfile(name_);
		if(type_.compareTo("b")==0) return openWriteBuffer(name_);
		return false;
	}

	public boolean openWriteBuffer(String name)
	{
		if(bufferWriterUsage_.containsKey(name_)==false)
		{
			createBuffer(name);
		}
		Integer u = bufferWriterUsage_.get(name_);
		bufferWriterUsage_.put(name_,new Integer(u.intValue()+1));
		u = bufferWriterUsage_.get(name_);
		if(debug_)System.out.println("registering user no. "+u+" for buffer "+name_+ " in mode "+mode_);
		if(u.intValue()>1)
		{
			System.out.println("Error fatal: multiple writers for buffer");
			System.exit(0);
		}
		return true;
	}

	public boolean openWriteFile(String name)
	{	
		try{
			File file = new File(name);
			if (!file.exists()) 
				{file.createNewFile();}
			else
			{
				if(debug_)System.out.println("Warning in Streamer: file exists: "+name_);
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			bw_ = new BufferedWriter(fw);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	public boolean openWriteMemfile(String name)
	{
		if(memfiles_.containsKey(name)) 
		{
			if(debug_)System.out.println("Error Memfile "+name+" already exists");
			return false;
		}
		if(debug_)System.out.println("opening memfile "+name+" for writing ");
		name_ = name;
		memfileLines_ = new Vector<String>(0);
		memfilePointer_=0;
		return true;		
	}

	//reading and writing the stream	
	public String readLine()
	{
		String str = null;
		if(type_.compareTo("f")==0) str = readLineFile();
		if(type_.compareTo("m")==0) str = readLineMemfile();
		if(type_.compareTo("b")==0) str = readLineBuffer();
		return str;
	}	
	
	public String readLineFile(){	
		String line = null;
		try {
		line = br_.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return line;
	}
	public String readLineMemfile(){
		if(memfileLines_ == null)
		{
			if(debug_)System.out.println("Error reading line from memfile "+name_+" line: "+memfilePointer_);
			System.exit(0);
		}
		if(memfilePointer_ >= memfileLines_.size())
			return null;
		
		String line = memfileLines_.elementAt(memfilePointer_);
		memfilePointer_++;
		return line;
	}

	public String readLineBuffer(){
		if(buffer_.containsKey(name_) == false
				|| buffer_.get(name_)==null)
		{
			if(debug_)System.out.println("Error reading line from buffer "+name_);
		}
		System.out.println("reading from buffer "+name_+": "+buffer_.get(name_));
		String line = buffer_.get(name_);
		buffer_.put(name_,null);
		return line;
	}

	public boolean writeLine(String line)
	{
		if(type_.compareTo("f")==0) return writeLineFile(line);
		if(type_.compareTo("m")==0) return writeLineMemfile(line);
		if(type_.compareTo("b")==0) return writeLineBuffer(line);
		

		return false;
	}	
	
	public boolean writeLineBuffer(String line){
		if(buffer_.containsKey(name_) == false
				|| buffer_.get(name_)!=null)
		{
			
			if(debug_)
				{
					System.out.println("Error writing line to buffer "+name_);
					System.out.println("buffer contains string: "+buffer_.get(name_));
					System.out.println("cannot write string: "+line);
					
				}
			return false;
		}
		System.out.println("writing to buffer "+name_+": "+line);
		buffer_.put(name_,""+line);
		return true;
	}

	
	
	public boolean writeLineFile(String line)
	{
		try {
			String wline = line+System.lineSeparator();
			bw_.write(wline);			
			} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	public boolean writeLineMemfile(String line){
		
		if(memfileLines_==null) return false;
		if(line == null) return false;
		memfileLines_.add(line);
		memfilePointer_ = memfileLines_.size();
		System.out.println(" writing line to "+name_+": "+line);
		return true;
	}

	
	//stream status
	public boolean ready()
	{
		if(type_.compareTo("f")==0) return readyFile();
		if(type_.compareTo("m")==0) return readyMemfile();
		if(type_.compareTo("b")==0) return readyBuffer();
		return false;
	}	
	public boolean readyBuffer()
	{
		if(mode_.compareTo("r") == 0)
			if(buffer_!=null)
				if(buffer_.containsKey(name_))
					if(buffer_.get(name_)!=null)
						return true;
		if(mode_.compareTo("w") == 0)
			if(buffer_!=null)
				if(buffer_.containsKey(name_))
					if(buffer_.get(name_)==null)
						return true;
		
		if(debug_)
			{
			System.out.println("WARNING buffer "+name_+" is not ready for operation in mode "+mode_);
			}
		return false;
	}	

	public boolean readyMemfile()
	{
		if(mode_.compareTo("r")==0)
			if(memfileLines_!=null)
				if(memfilePointer_>=0 && memfilePointer_<memfileLines_.size())
					return true;
		if(mode_.compareTo("w")==0)
			if(memfileLines_!=null)
				if(memfilePointer_==memfileLines_.size())
					return true;
		if(debug_)System.out.println("WARNING memfile "+name_+" is not ready in mode "+mode_);
		return false;
	}	
	public boolean readyFile()
	{
		//ready for next line ?
		boolean r = false;
		if(br_ != null)
			try{
			r=br_.ready();
			} catch (IOException e) {
				// TODO Auto-generated catch block
					e.printStackTrace();
			}
		if(bw_ != null) r = true;
		// writer is always ready if(bw_ != null)return bw_.ready();
		return r;
	}
	
	
	public boolean isAtEnd()
	{

		if(type_.compareTo("f")==0) return isAtEndFile();
		if(type_.compareTo("m")==0) return isAtEndMemfile();
		if(type_.compareTo("b")==0) return isAtEndBuffer();
		return false;
	}	
	public boolean isAtEndBuffer()
	{
		
		if(buffer_.get(name_)==null)return true;
		return false;
	}
	
	public boolean isAtEndMemfile()
	{
		if(memfileLines_!=null)
			if(memfileLines_.size() == memfilePointer_)			
					return true;
		
		return false;
	}
	
	//
	public boolean isAtEndFile()
	{
		boolean f = true;
		try{
			if(type_.compareTo("f")==0)
				{
				    // in file mode the following logic is for the while being defined by us
					if(bw_ != null) f = true;
					if(br_ != null) f = !br_.ready();		
				}
			}	 catch (IOException e) {
				e.printStackTrace();
			}
		return f;
	}
}
