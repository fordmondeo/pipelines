package modules;

public class modEncode extends modCopy{

	public modEncode(String in, String out)
	{
		super(in, out);
	}
	public String processLine(String line)
	{	
		if(line == null) return null;
		String code = "";
		for(int i=0;i<line.length();i++)
		{
			int id = (int)line.charAt(i);
			code+=id+" ";
		}
		return code;
	}
	
}
