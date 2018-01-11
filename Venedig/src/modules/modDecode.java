package modules;

public class modDecode extends modCopy{

	public modDecode(String in, String out)
	{
		super(in, out);
	}
	public String processLine(String line)
	{	
		if(line == null) return null;
		String[] code = line.split(" ");
		String text = "";
		for(int i=0;i<code.length;i++)
		{
			System.out.println("code = "+code[i]);
			if(code[i].compareTo("")==0) continue;
			char ch = (char) Integer.parseInt(code[i]);
			text = text+ch;
		}
		return text;
	}
	
}
