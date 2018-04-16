package geo.conekta.minth;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;


public class ReadCsv {
    static ArrayList arList = null;
    static String statuslog;

    public void processCsv(String myPath) throws Exception
    {
    	Date myFecha = new Date();
		SimpleDateFormat ft = new SimpleDateFormat ("hh:mm:ss a");
		statuslog="Hora Inicial: "+ft.format(myFecha);
    	MainForm.setLabelText(statuslog);
    	
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
            
            myFecha = new Date();
    		SimpleDateFormat ft2 = new SimpleDateFormat ("YYYY_MM_DD_HHmmss"); //Extracto_YYYY_MM_DD_HHmmss
    		ft2.format(myFecha);
    		String fileN="\\Extracto_"+ft2.format(myFecha)+".xls";
            FileOutputStream fileOut = new FileOutputStream(myPath+fileN);
            hwb.write(fileOut);
            fileOut.close();
            System.out.println("Your excel file has been generated");    
            myFecha = new Date();
            statuslog=statuslog + "\nHora Final: "+ft.format(myFecha);   
            statuslog=statuslog + "\nArchivo generado: "+fileN;
            MainForm.setLabelText(statuslog);
            
        } catch ( Exception ex ) {
            ex.printStackTrace();
        } //main method ends

    }

    public static void LeerD(String directorio) throws Exception{
    	int count=0;
    	int countSize=0;
		File f = new File(directorio);
		File[] ficheros = f.listFiles();
        ArrayList al = null;
        int i=0;
        for (int x = 0; x < ficheros.length; x++) {
			if (ficheros[x].isDirectory()) {
				System.out.println(ficheros[x].getName());
				LeerD(ficheros[x].getPath());
			} else {
				System.out.println(ficheros[x].getName() +"peso: "+ficheros[x].length());
				count++;
				countSize=countSize+ (int) (ficheros[x].length());        	
	            MainForm.setLabelFile(ficheros[x].getAbsolutePath());
				//CSV
		        String fName = ficheros[x].getPath();
		        String thisLine;
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
        statuslog=statuslog + "\n "+count+" Archivos Correctos - Tamaño: "+countSize+ " Bytes";
   }
}
