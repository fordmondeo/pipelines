package modules;

import java.util.HashMap;

public class modExtractGmail extends modCopy{
	// a different module type is required
	// or you need a stat feature which writes stat at the end
	
	private HashMap<String, String> gmail_ = null; 
	String lineType_;
	int lineNumber_=0;
	
	public modExtractGmail(String in, String out)
	{
		super(in, out);
	}
	public String processLine(String line)
	{	
		if(line == null) return null;
		String[] slots = line.split(",");
		lineNumber_++;
		lineType_ = "text";
		if(slots.length < 3)
			lineType_ = "text";
		else
			if(slots[2].compareTo("BATCH")==0)
				lineType_ = "batch";
			else
				if(slots[2].compareTo("THREAD")==0)
					lineType_ = "thread";
				else
					if(slots[2].compareTo("MESSAGE")==0)
						lineType_ = "message";
			
		if(lineType_.compareTo("message")==0)
		{
			String source = slots[1];
			String id = slots[5];
			String date = slots[6];
			System.out.println
			("line "+lineNumber_+" type = "+lineType_+" "
			+source+" "+date+" "+id);
		}
//		else 
//			System.out.println("line "+lineNumber_+" type = "+lineType_);

		line = "...";
		return line;
	}
	
}
