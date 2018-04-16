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
            	String myLine= arList.get(k).toString();
            	
            	String data[] = myLine.split(",");
            	
            	//ArrayList ardata = (ArrayList);
                HSSFRow row = sheet.createRow((short) 0+k);
                for(int p=0;p<data.length;p++)
                {
                    HSSFCell cell = row.createCell((short) p);
                    //String data = ardata.get(p).toString();
                    /*if(data.startsWith("=")){
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
                    cell.setCellValue(data[p]);
                }
            }
            
            myFecha = new Date();
    		SimpleDateFormat ft2 = new SimpleDateFormat ("YYYY_MM_DD_HHmmss"); 
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
    	final String extension = ".csv";	
    	File f = new File(directorio);
		File[] ficheros = f.listFiles((File pathname) -> pathname.getName().endsWith(extension));
        ArrayList al = new ArrayList();
        String myLine=null;
        int i=0;
        //a�ade la cabecera
        arList.add("par de torsi�n perno_exterior_izq,�ngulo perno_exterior_izq,par de torsi�n perno_interior_izq,�ngulo perno_interior_izq,par de torsi�n perno_interior_drch,�ngulo perno_interior_drch,par de torsi�n perno_exterior_drch,�ngulo perno_exterior_drch,ruta archivo");
        for (int x = 0; x < ficheros.length; x++) {
			if (ficheros[x].isDirectory()) {
				System.out.println(ficheros[x].getName());
				LeerD(ficheros[x].getPath());
			} else {
				System.out.println(ficheros[x].getName() +" peso: "+ficheros[x].length());
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
		            
		            String strar[] = thisLine.split(";");
		               //usar astring y split
		            if (strar[0].equals("par de torsi�n perno_exterior_izq")) {
		            	myLine=strar[1]+",";
		            }
		            if (strar[0].equals("�ngulo perno_exterior_izq")) {
			            myLine=myLine+strar[1]+",";
		            }
		            if (strar[0].equals("par de torsi�n perno_interior_izq")) {
		            	myLine=myLine+strar[1]+",";
		            }
		            if (strar[0].equals("�ngulo perno_interior_izq")) {
		            	myLine=myLine+strar[1]+",";
		            }
		            if (strar[0].equals("par de torsi�n perno_interior_drch")) {
		            	myLine=myLine+strar[1]+",";
		            }
		            if (strar[0].equals("�ngulo perno_interior_drch")) {
		            	myLine=myLine+strar[1]+",";
		            }
		            if (strar[0].equals("par de torsi�n perno_exterior_drch")) {
		            	myLine=myLine+strar[1]+",";
		            }
		            if (strar[0].equals("�ngulo perno_exterior_drch")) {
		            	myLine=myLine+strar[1]+",";
		            }
		            
		            i++;
		        }
		       myLine = myLine + ficheros[x].getAbsolutePath() +",";
			}
	        arList.add(myLine);        
	        //al.clear();
        }
        
        statuslog=statuslog + "\n "+count+" Archivos Correctos - Tama�o: "+countSize+ " Bytes";
   }
}
