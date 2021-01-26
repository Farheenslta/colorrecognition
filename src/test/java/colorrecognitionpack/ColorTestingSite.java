package colorrecognitionpack;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class ColorTestingSite 
{
	public static void main(String[] args) throws Exception
	{
		//Provide expected color details with default color name available in java class or in terms of RGB values
		//Color ecn=Color.WHITE;
		//Color ecn=new Color(0,0,0);
		//Enter color name from keyboard
		Scanner sc=new Scanner(System.in);
		System.out.println("Enter expected color's Red value in RGB");
		int r=sc.nextInt();
		System.out.println("Enter expected color's Green value in RGB");
		int g=sc.nextInt();
		System.out.println("Enter expected color's Blue value in RGB");
		int b=sc.nextInt();
		Color ecn=new Color(r,g,b);
		sc.close();
		
		//Open browser
		WebDriverManager.chromedriver().setup();
		ChromeDriver driver=new ChromeDriver();
		//Maximize
		driver.manage().window().maximize();
		//Launch site
		driver.get("https://www.w3schools.com/colors/colors_rgb.asp");
		//Create wait object
		WebDriverWait wait=new WebDriverWait(driver,20);
		WebElement e1=driver.findElement(By.xpath("//*[text()='RGB Calculator']"));
		wait.until(ExpectedConditions.visibilityOf(e1));
		driver.executeScript("arguments[0].scrollIntoView();",e1);
		driver.findElement(By.id("r01")).clear();
		driver.findElement(By.id("r01")).sendKeys(""+r+"");
		driver.findElement(By.id("g01")).clear();
		driver.findElement(By.id("g01")).sendKeys(""+g+"");
		driver.findElement(By.id("b01")).clear();
		driver.findElement(By.id("b01")).sendKeys(""+b+"");
		//Automation
		try
		{
			WebElement e2=driver.findElement(By.id("result01"));
			//Get the location, width and height of element on the app screen
			int x=e2.getLocation().getX();
			int y=e2.getLocation().getY();
			int w=e2.getSize().getWidth();
			int h=e2.getSize().getHeight();
			File src=driver.findElement(By.id("result01")).getScreenshotAs(OutputType.FILE);
			BufferedImage bele=ImageIO.read(src);
			int count=0;
			//Check image color to validate
			for(int i=0;i<w;i++)
			{
				for(int j=0;j<h;j++)
				{
					Color acn=new Color(bele.getRGB(i,j));
					if(acn.getRed()==ecn.getRed() && acn.getGreen()==ecn.getGreen() && acn.getBlue()==ecn.getBlue())
					{
						count=count+1;
					}
				}
			}
			System.out.println("Total pixels are: "+(w*h));
			System.out.println("Expected color pixel count:"+count);
			double percentage=(count*100.0)/(w*h);
			System.out.println(percentage);
			if(percentage>=95)
			{
				System.out.println("Color test passed");
			}
			else
			{
				System.out.println("Color test failed");
			}			
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		Thread.sleep(3000);
		//Close site
		driver.close();
	}
}