import java.io.FileWriter;
import java.io.IOException;  // Import the IOException class to handle errors
public class Output {
	String path;
	int num;
	String cost;
	double time;
	int with_time;

	public Output(String p, int n, int c, double t,int w_t)
	{
		path=p;
		num=n;
		cost=String.valueOf(c);
		time=t;
		with_time=w_t;
	}
	public Output(String p, int n)
	{
		path=p;
		num=n;
		cost=" ";
		with_time=1;
		time=0;
	}


	public  void writeFile() throws IOException {
		try {
			FileWriter writer = new FileWriter("output.txt", true);
			writer.write(path.substring(0, path.length() - 1));
			writer.write("\r\n");   // write new line
			writer.write(String.valueOf("Num: "+num));
			writer.write("\r\n");   // write new line
			if(!cost.equals(" "))
			{
				writer.write("Cost: "+cost);
				writer.write("\r\n");   // write new line
			}
			if(with_time==0)
			{
				writer.write(String.valueOf(time));
				writer.write("\r\n");
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		

	}


}
