package sqs.core.utils;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import static sqs.core.constants.Constants.DO_NOTHING;
import static sqs.core.constants.Constants.EXCEPTION_ON;
import static sqs.core.utils.Utilities.getCallerMethodName;

public class ExcelUtilities {

	private ExcelUtilities(){

	}
	private static Logger logger=Logger.getLogger(ExcelUtilities.class);
	static DataFormatter dataFormatter =new DataFormatter();

	public static String getCellValue(XSSFSheet sheet,int rowIndex,int columnIndex)
	{
		String cellValue="";
		Cell currentCell=null;
		Row currentRow=null;
		try
		{
			currentRow =sheet.getRow(rowIndex);
		}catch(Exception e){logger.debug(DO_NOTHING);}
		if(currentRow!=null)
		{			
			DataFormatter formatter = new DataFormatter();
			try
			{
				currentCell=currentRow.getCell(columnIndex);
			}catch(Exception e){
				logger.debug(DO_NOTHING);
			}
			if(currentCell!=null)
			{
				cellValue=Utilities.trimString(formatter.formatCellValue(currentCell));
						
			}
		}
		return cellValue;
	}
	public static void updateValueInCell( XSSFSheet sheet,int row, int col,String valueToUpdate)   {
		try {
		
			Cell cell;
	
			XSSFRow sheetrow = sheet.getRow(row);
			if(sheetrow == null)
			{
				sheetrow = sheet.createRow(row);
			}
	
			cell = sheetrow.getCell(col);
			if(cell == null)
			{
				cell = sheetrow.createCell(col);
			}
			String existingValue=getCellValue(cell);
			if(existingValue.equalsIgnoreCase("Failed") && valueToUpdate.equalsIgnoreCase("Failed"))
			{
				valueToUpdate="Hold";
			}
			
			if(valueToUpdate.equalsIgnoreCase("UNDEFINED"))
			{
				valueToUpdate="Hold";
			}
			
			if(!existingValue.equalsIgnoreCase("Passed"))
			{
				cell.setCellValue(valueToUpdate);
				logger.debug("Execution status '"+valueToUpdate+"' has been updated in Excel.");
			}			
			
		} 
		catch (Exception exception)
		{
			logger.error(EXCEPTION_ON+getCallerMethodName()+" :",exception);
		}
	}
	public static String getCellValue(Cell cell)
	{
		String cellValue="";
		try
		{
		cellValue=Utilities.trimString(dataFormatter.formatCellValue(cell));
		}
		catch(Exception exception)
		{
			logger.debug(DO_NOTHING);
		}
		return cellValue;
	}
}
