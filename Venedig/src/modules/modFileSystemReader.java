package modules;
import util.*;
import java.io.*;

public class modFileSystemReader extends modCopy{
	
	static int instances_ = 0;
	static int cnt_ = 0;
	//descriptors of streams
	String in_ = null;
	String out_ = null;
	int delay_ = 1; 
	
	//the streams
	Streamer strOut_ = null;
	File folder_ = null;
	
	public modFileSystemReader(String in, String out)
	{
		super(in,out);
		in_=in;
		out_=out;
		System.out.println("creating modCopy ("+instances_+") in = ["+in+"] out = ["+out+"]");
	}
	public void init()
	{
		//open
		strOut_ = new Streamer(out_);
		strOut_.setDebug(true);
		strOut_.open("w");
		
		folder_ = new File(in_);
		listFilesForFolder(folder_);	
		System.exit(0);
		
	}
	
	public void listFilesForFolder(final File folder) {
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            listFilesForFolder(fileEntry);
	        } else {
	            System.out.println(fileEntry.getName());
	        }
	    }
	}

	public void cycle()
	{
		// read one line, write it to output
		if(!ready()) 
			{
				System.out.println("module not ready "+cnt_++);
				return;
			}
		cnt_=0;

		
		String line = processLine(null);
		strOut_.writeLine(line);
		//strOut_.setEnd(false);
		
		
	}
	public String processLine(String line)
	{	
		return line;
	}

	public boolean ready()
	{
		// ready for next line ?
		return (strOut_.ready());
	}
	
	public void close()
	{
		//close
		strOut_.close();
	}
	
	public boolean isFinished()
	{
		//report to scheduler if this module has
		//finished its work
		if (strOut_.isAtEnd())
			return true;
		return false;
	}
	
}
