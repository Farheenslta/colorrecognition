package colorrecognitionpack;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;

import javax.imageio.ImageIO;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class ColorTestingSiteExcel2
{
	public static void main(String[] args) throws Exception
	{
		//Connect to excel file and take read permission
		File f=new File("255255255.xlsx");
		FileInputStream fi=new FileInputStream(f);
		Workbook wb=WorkbookFactory.create(fi);
		Sheet sh=wb.getSheet("Sheet1");
		int nour=sh.getPhysicalNumberOfRows();
		int nouc=sh.getRow(0).getLastCellNum();
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
		int c=0;
		//Data driven
		for(int i=1;i<nour;i++)
		{
			//Read data from excel
			DataFormatter df=new DataFormatter();
			int r=Integer.parseInt(df.formatCellValue(sh.getRow(i).getCell(0)));
			for(int j=1;j<nour;j++)
			{
				int g=Integer.parseInt(df.formatCellValue(sh.getRow(j).getCell(1)));
				for(int k=1;k<nour;k++)
				{
					int b=Integer.parseInt(df.formatCellValue(sh.getRow(k).getCell(2)));
					//Provide expected color details with default color name available in java class or in terms of RGB values
					Color ecn=new Color(r,g,b);
					driver.findElement(By.id("r01")).clear();
					driver.findElement(By.id("r01")).sendKeys(""+r+"");
					driver.findElement(By.id("g01")).clear();
					driver.findElement(By.id("g01")).sendKeys(""+g+"");
					driver.findElement(By.id("b01")).clear();
					driver.findElement(By.id("b01")).sendKeys(""+b+"");
					c=c+1;
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
						for(int p=0;p<w;p++)
						{
							for(int q=0;q<h;q++)
							{
								Color acn=new Color(bele.getRGB(p,q));
								if(acn.getRed()==ecn.getRed() && acn.getGreen()==ecn.getGreen() && acn.getBlue()==ecn.getBlue())
								{
									count=count+1;
								}
							}
						}
						//System.out.println("Total pixels are: "+(w*h));
						//System.out.println("Expected color pixel count:"+count);
						double percentage=(count*100.0)/(w*h);
						//System.out.println(percentage);
						if(percentage>=95)
						{
							//sh.getRow(i).createCell(nouc).setCellValue("Rangu paduddhi");
							//System.out.println("Color test passed");
						}
						else
						{
							//sh.getRow(i).createCell(nouc).setCellValue("Rangu padadhu");
							//System.out.println("Color test failed");
						}			
					}
					catch(Exception e)
					{
						System.out.println(e.getMessage());
					}
				}
			}
		}
		
		sh.autoSizeColumn(nouc);
		
		//Save data back to excel
		//FileOutputStream fo=new FileOutputStream(f);
		//wb.write(fo);
		fi.close();
		//fo.close();
		wb.close();
		
		System.out.println(c);
		Thread.sleep(3000);
		//Close site
		driver.close();

	}

}
