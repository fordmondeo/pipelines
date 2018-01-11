package modules;

public class modLineLength extends modCopy{

	public modLineLength(String in, String out)
	{
		super(in, out);
	}
	public String processLine(String line)
	{	
		return ""+line.length();
	}
	
}
