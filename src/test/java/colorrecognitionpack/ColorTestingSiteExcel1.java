package colorrecognitionpack;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

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

public class ColorTestingSiteExcel1 
{
	public static void main(String[] args) throws Exception
	{
		//Connect to excel file and take read permission
		File f=new File("rgbvaluesforcolors.xlsx");
		FileInputStream fi=new FileInputStream(f);
		Workbook wb=WorkbookFactory.create(fi);
		Sheet sh=wb.getSheet("Sheet1");
		int nour=sh.getPhysicalNumberOfRows();
		int nouc=sh.getRow(0).getLastCellNum();
		//Open browser
		WebDriverManager.chromedriver().setup();
		ChromeDriver driver=new ChromeDriver();
		//Maximize browser
		driver.manage().window().maximize();
		//Launch site
		driver.get("https://www.w3schools.com/colors/colors_rgb.asp");
		//Create wait object
		WebDriverWait wait=new WebDriverWait(driver,20);
		WebElement e1=driver.findElement(By.xpath("//*[text()='RGB Calculator']"));
		wait.until(ExpectedConditions.visibilityOf(e1));
		driver.executeScript("arguments[0].scrollIntoView();", e1);
		//Data  driven
		for(int i=1;i<nour;i++)
		{
			//Read data from excel
			DataFormatter df=new DataFormatter();
			int r=Integer.parseInt(df.formatCellValue(sh.getRow(i).getCell(0)));
			int g=Integer.parseInt(df.formatCellValue(sh.getRow(i).getCell(1)));
			int b=Integer.parseInt(df.formatCellValue(sh.getRow(i).getCell(2)));
			//Provide expected color details with default color name available in java class or in terms of RGB values
			Color ecn=new Color(r,g,b);
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
				for(int j=0;j<w;j++)
				{
					for(int k=0;k<h;k++)
					{
						Color acn=new Color(bele.getRGB(j, k));
						if(acn.getRed()==ecn.getRed() && acn.getGreen()==ecn.getGreen() && acn.getBlue()==ecn.getBlue())
						{
							count=count+1;
						}
					}
				}
				System.out.println("Total pixels are: "+(w*h));
				System.out.println("Expected color pixel count: " + count);
				double percentage=(count*100)/(w*h);
				System.out.println(percentage);
				if(percentage>=95)
				{
					sh.getRow(i).createCell(nouc).setCellValue("colour came");
					//System.out.println("Color test passed");
				}
				else
				{
					sh.getRow(i).createCell(nouc).setCellValue("color did not come");
					//System.out.println("Color test failed");
				}
				
			}
			catch(Exception ex)
			{
				System.out.println(ex.getMessage());
			}			
		}
		sh.autoSizeColumn(nouc);
		//Save data back to excel
		FileOutputStream fo=new FileOutputStream(f);
		wb.write(fo);
		fi.close();
		fo.close();
		wb.close();
		
		Thread.sleep(3000);
		//close
		driver.close();
	}
}
