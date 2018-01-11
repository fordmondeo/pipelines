package modules;
import util.*;

public class modCopy {
	
	static int instances_ = 0;
	static int cnt_ = 0;
	//descriptors of streams
	String in_ = null;
	String out_ = null;
	int delay_ = 1; 
	
	//the streams
	Streamer strIn_ = null;
	Streamer strOut_ = null;
	
	public modCopy(String in, String out)
	{
		this.in_ = in;
		this.out_ = out;
		instances_++;
		System.out.println("creating modCopy ("+instances_+") in = ["+in+"] out = ["+out+"]");
	}
	public void init()
	{
		//open
		strIn_ = new Streamer(in_);
		strOut_ = new Streamer(out_);
		strIn_.setDebug(true);
		strOut_.setDebug(true);
		strIn_.open("r");
		strOut_.open("w");
		
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
		String line = strIn_.readLine();
		line = processLine(line);
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
		return (strIn_.ready() && strOut_.ready());
	}
	
	public void close()
	{
		//close
		strIn_.close();
		strOut_.close();
	}
	
	public boolean isFinished()
	{
		//report to scheduler if this module has
		//finished its work
		if (strIn_.isAtEnd() && strOut_.isAtEnd())
			return true;
		return false;
	}
	
}
