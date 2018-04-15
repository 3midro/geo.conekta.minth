package geo.conekta.minth;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.sql.SQLException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;


public class ReadCsv {
    static ArrayList arList = null;

    public void processCsv(String myPath) throws Exception
    {
    	System.out.print("Iniciando..."  + myPath);
        arList = new ArrayList();
        LeerD(myPath);
        //Excel
        try
        {
            HSSFWorkbook hwb = new HSSFWorkbook();
            HSSFSheet sheet = hwb.createSheet("new sheet");
            for(int k=0;k<arList.size();k++)
            {
                ArrayList ardata = (ArrayList)arList.get(k);
                HSSFRow row = sheet.createRow((short) 0+k);
                for(int p=0;p<ardata.size();p++)
                {
                    HSSFCell cell = row.createCell((short) p);
                    String data = ardata.get(p).toString();
                    if(data.startsWith("=")){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        data=data.replaceAll("\"", "");
                        data=data.replaceAll("=", "");
                        cell.setCellValue(data);
                    }else if(data.startsWith("\"")){
                        data=data.replaceAll("\"", "");
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        cell.setCellValue(data);
                    }else{
                        data=data.replaceAll("\"", "");
                        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        cell.setCellValue(data);
                    }
                    //*/
                    cell.setCellValue(ardata.get(p).toString());
                }
            }
            FileOutputStream fileOut = new FileOutputStream(myPath+"test2.xls");
            hwb.write(fileOut);
            fileOut.close();
            System.out.println("Your excel file has been generated");
        } catch ( Exception ex ) {
            ex.printStackTrace();
        } //main method ends

    }

    public static void LeerD(String directorio) throws Exception{
		File f = new File(directorio);
		File[] ficheros = f.listFiles();
        ArrayList al = null;
        int i=0;
        for (int x = 0; x < ficheros.length; x++) {
			if (ficheros[x].isDirectory()) {
				System.out.println(ficheros[x].getName());
				LeerD(ficheros[x].getPath());
			} else {
				System.out.println(ficheros[x].getName());
				//CSV
		        String fName = ficheros[x].getPath();
		        String thisLine;
		        int count=0;
		        FileInputStream fis = new FileInputStream(fName);
		        DataInputStream myInput = new DataInputStream(fis);
		        while ((thisLine = myInput.readLine()) != null)
		        {
		            al = new ArrayList();
		            String strar[] = thisLine.split(";");
		            System.out.println(thisLine);
		            for(int j=0;j<strar.length;j++)
		            {
		                al.add(strar[j]);
		            }
		            arList.add(al);
		            i++;
		        }
			}
		}
   }
}
