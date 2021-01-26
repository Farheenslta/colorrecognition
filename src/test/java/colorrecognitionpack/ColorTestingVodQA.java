package colorrecognitionpack;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Scanner;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.offset.ElementOption;

public class ColorTestingVodQA
{
	public static void main(String[] args) throws Exception
	{
		//Enter color name from keyboard
		Scanner sc=new Scanner(System.in);
		System.out.println("Enter expected color name as red/green/blue/black");
		String ecn=sc.nextLine();
		ecn=ecn.toLowerCase();
		Color ec=null;
		switch(ecn)
		{
			case "red":
				ec=Color.RED;
				break;
			case "green":
				ec=new Color(0,128,0);
				break;
			case "blue":
				ec=Color.BLUE;
				break;
			case "black":
				ec=Color.BLACK;
				break;
			default:
				System.out.println("Wrong color name");
				System.exit(0);
		}
		//Details of app and device(AVD)
		DesiredCapabilities dc=new DesiredCapabilities();
		dc.setCapability(CapabilityType.BROWSER_NAME,"");
		dc.setCapability("deviceName","emulator-5554");
		dc.setCapability("platformName","android");
		dc.setCapability("platformVersion","5.1.1");
		dc.setCapability("appPackage","com.vodqareactnative");
		dc.setCapability("appActivity","com.vodqareactnative.MainActivity");
		//Start appium server
		Runtime.getRuntime().exec("cmd.exe /c start cmd.exe /k \"appium -a 0.0.0.0 -p 4723\"");
		//Get address of appium Server
		URL u=new URL("http://0.0.0.0:4723/wd/hub");
		//Create driver object
		AndroidDriver driver;
		while(2>1)
		{
			try
			{
				driver=new AndroidDriver(u,dc);
				break;
			}
			catch(Exception ex)
			{
			}
		}
		//Automation
		try
		{
			WebDriverWait wait=new WebDriverWait(driver,20);
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@text='LOG IN']")));
			driver.findElement(By.xpath("//*[@text='LOG IN']")).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@text='Back']")));
			TouchAction ta=new TouchAction(driver);
			//Swipe for Wheel picker option (bottom to top)
			while(2>1)
			{
				try
				{
					driver.findElement(By.xpath("//*[@text='Wheel Picker']")).click();
					break;
				}
				catch(Exception ex)
				{
					int w=driver.manage().window().getSize().getWidth();
					int h=driver.manage().window().getSize().getHeight();
					int x1=w/2;
					int y1=(int) (h*0.7);
					int x2=w/2;
					int y2=(int) (h*0.5);
					ta.press(ElementOption.point(x1,y1)).moveTo(ElementOption.point(x2,y2)).release().perform();
				}
			}
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@class='android.widget.Spinner']")));
			driver.findElement(By.xpath("//*[@class='android.widget.Spinner']")).click();
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@text='"+ecn+"']")));
			driver.findElement(By.xpath("//*[@text='"+ecn+"']")).click();
			//go to color area element
			WebElement e=driver.findElement(By.xpath("//*[@class='android.view.ViewGroup'][@instance='2']"));
			//Get the location, width and height of element on the app screen
			int x=e.getLocation().getX();
			int y=e.getLocation().getY();
			int w=e.getSize().getWidth();
			int h=e.getSize().getHeight();
			//Get screenshot of full screen
			File fullimg=driver.getScreenshotAs(OutputType.FILE);
			BufferedImage bfull=ImageIO.read(fullimg);
			//Get screenshot of color area element
			BufferedImage bele=bfull.getSubimage(x, y, w, h);
			int count=0;
			//Check image color to validate
			for(int i=0;i<w;i++)
			{
				for(int j=0;j<h;j++)
				{
					Color ac=new Color(bele.getRGB(i,j));
					if(ac.getRed()==ec.getRed() && ac.getGreen()==ec.getGreen() && ac.getBlue()==ec.getBlue())
					{
						count=count+1;
					}
				}
			}
			System.out.println("Total pixels are: "+(w*h));
			System.out.println("Expected color pixel count:"+count);
			double percentage=(count*100.0)/(w*h);
			System.out.println(percentage);
			if(percentage>=85)
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
		//Close app
		driver.closeApp();
		//Stop appium server
		Runtime.getRuntime().exec("taskkill /F /IM node.exe");
		Runtime.getRuntime().exec("taskkill /F /IM cmd.exe");
	}
}
