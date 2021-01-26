package colorrecognitionpack;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class ColorTesting 
{
	public static void main(String[] args) throws Exception
	{
		//Provide expected color details with default color name available in java class or in terms of RGB values
		Color ec=Color.WHITE;
		//Color ec=new Color(255,0,0);
		File f=new File("code.png");
		BufferedImage bim=ImageIO.read(f);
		//Check image pixel by pixel
		int w=bim.getWidth();
		int h=bim.getHeight();
		int count=0;
		for(int i=0;i<w;i++)
		{
			for(int j=0;j<h;j++)
			{
				Color ac=new Color(bim.getRGB(i,j));
				if(ac.getRed()==ec.getRed() && ac.getGreen()==ec.getGreen() && ac.getBlue()==ec.getBlue())
				{
					count=count+1;
				}
			}
		}
		System.out.println("Total pixels are: "+(w*h));
		System.out.println("Expected color pixel count:"+count);
		double percentage=(count*100)/(w*h);
		System.out.println(percentage);
	}
}