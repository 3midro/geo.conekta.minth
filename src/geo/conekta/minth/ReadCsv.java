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
    static int progressC;

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
			MainForm.setPBSel(progressC+1);
        	HSSFWorkbook hwb = new HSSFWorkbook();
            HSSFSheet sheet = hwb.createSheet("new sheet");
            for(int k=0;k<arList.size();k++)
            {
            	String myLine= arList.get(k).toString();            	
            	String data[] = myLine.split(",");
                HSSFRow row = sheet.createRow((short) 0+k);
                for(int p=0;p<data.length;p++)
                {
                    HSSFCell cell = row.createCell((short) p);
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
            MainForm.setPBSel(progressC+2);
            
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
		progressC=ficheros.length+2;
		MainForm.setPBMax(progressC);
        ArrayList al = new ArrayList();
        String myLine=null;
        int i=0;
        //añade la cabecera
        arList.add("par de torsión perno_exterior_izq,ángulo perno_exterior_izq,par de torsión perno_interior_izq,ángulo perno_interior_izq,par de torsión perno_interior_drch,ángulo perno_interior_drch,par de torsión perno_exterior_drch,ángulo perno_exterior_drch,ruta archivo");
        for (int x = 0; x < ficheros.length; x++) {
			if (ficheros[x].isDirectory()) {
				System.out.println(ficheros[x].getName());
				LeerD(ficheros[x].getPath());
			} else {
				System.out.println(ficheros[x].getName() +" peso: "+ficheros[x].length());
				MainForm.setPBSel(x+1);
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
		            if (strar[0].equals("par de torsión perno_exterior_izq")) {
		            	myLine=strar[1]+",";
		            }
		            if (strar[0].equals("ángulo perno_exterior_izq")) {
			            myLine=myLine+strar[1]+",";
		            }
		            if (strar[0].equals("par de torsión perno_interior_izq")) {
		            	myLine=myLine+strar[1]+",";
		            }
		            if (strar[0].equals("ángulo perno_interior_izq")) {
		            	myLine=myLine+strar[1]+",";
		            }
		            if (strar[0].equals("par de torsión perno_interior_drch")) {
		            	myLine=myLine+strar[1]+",";
		            }
		            if (strar[0].equals("ángulo perno_interior_drch")) {
		            	myLine=myLine+strar[1]+",";
		            }
		            if (strar[0].equals("par de torsión perno_exterior_drch")) {
		            	myLine=myLine+strar[1]+",";
		            }
		            if (strar[0].equals("ángulo perno_exterior_drch")) {
		            	myLine=myLine+strar[1]+",";
		            }
		            
		            i++;
		        }
		       if (!myLine.equals(""))
		        	myLine = myLine + ficheros[x].getAbsolutePath() +",";
			}
			if (!myLine.equals(""))
				arList.add(myLine);  
	        myLine="";
        }
        
        statuslog=statuslog + "\n "+count+" Archivos Correctos - Tamaño: "+countSize+ " Bytes";
   }
}
