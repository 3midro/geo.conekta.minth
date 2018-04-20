package geo.conekta.minth;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Stream;
import java.text.SimpleDateFormat;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;

public class ReadCsv extends Thread{
	
	private Display display;
	private ProgressBar progressBar;
	private Label myOutput;
	private Text text1;
	private String myPath;
    static ArrayList arList = null;
    static String statuslog;
    static int progressC;
    String myLine="";
    
    public ReadCsv(Display display, ProgressBar progressBar,String myPath, Label myOutput,Text text1) {
        this.display = display;
        this.progressBar = progressBar;
        this.myPath=myPath;
        this.myOutput=myOutput;
        this.text1=text1;
    }

    @Override
    public void run() {
        if (display.isDisposed()) {
            return;
        }
        this.updateGUIWhenStart();
    	
        this.updateGUIInProgress(myPath);

        this.updateGUIWhenFinish();
    }

    private void updateGUIInProgress(String directorio) {
    	 try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	
    	 	display.asyncExec(new Runnable() {
 
           	int count=0;
           	int countSize=0;  	

            @Override
            public void run() {
            	if (progressBar.isDisposed())
                    return;
            	getFiles(directorio);
                if (arList.size()==1)
                	statuslog=statuslog + "\n - Ningun archivo procesado / sin información";
                else
               		statuslog=statuslog + "\n "+count+" Archivos Procesados - Tamaño: "+countSize+ " Bytes";
            }
            
            public void getFiles(String directorio) {
            	final String extension = ".csv";	
            	File dir = new File("dir");
           
        		FileFilter directoryFilter = new FileFilter() {
        			public boolean accept(File file) {
        				return file.isDirectory();
        			}
        		};
        		
            	File f = new File(directorio);
        		File[] ar1 =f.listFiles((File pathname) -> pathname.getName().endsWith(extension));
        		File[] ar2 =f.listFiles(directoryFilter);
        		File[] ficheros = Stream.of(ar1, ar2).flatMap(Stream::of).toArray(File[]::new);
        		progressC=ficheros.length+2;
        		progressBar.setMaximum(progressC);
        		
                //ArrayList al = new ArrayList();
                int i=0;
                //añade la cabecera
                if (count == 0) {
                	arList.add("par de torsión perno_exterior_izq,ángulo perno_exterior_izq,par de torsión perno_interior_izq,ángulo perno_interior_izq,par de torsión perno_interior_drch,ángulo perno_interior_drch,par de torsión perno_exterior_drch,ángulo perno_exterior_drch,ruta archivo");
                }
                for (int x = 0; x < ficheros.length; x++) {
                	if (ficheros[x].isDirectory()) {
                		System.out.println(ficheros[x].getName());
                		this.getFiles(ficheros[x].getPath());
                	} else {
                		progressBar.setSelection(progressBar.getSelection() + 1);
                		text1.setText(ficheros[x].getAbsolutePath());
                		count++;
                		countSize=countSize + (int)(ficheros[x].length());        	
    				
                		//CSV
                		String fName = ficheros[x].getPath();
                		String fNameComp = ficheros[x].getAbsolutePath();
                		String thisLine;
                		FileInputStream fis;
                		try {
                			System.out.println(fName);
                			fis = new FileInputStream(fName);
                			DataInputStream myInput = new DataInputStream(fis);
                			while ((thisLine = myInput.readLine()) != null){
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
                		} catch (IOException e) {
                			// TODO Auto-generated catch block
                			e.printStackTrace();
                		}
               			if (!myLine.equals("")) {
               				myLine = myLine.concat(fNameComp).concat(",");
               				arList.add(myLine);
               			}
         		       	myLine="";
                	}
                }
    	}});
    }
 
    private void updateGUIWhenStart() {
        display.asyncExec(new Runnable() {
 
            @Override
            public void run() {
            	Date myFecha = new Date();
        		SimpleDateFormat ft = new SimpleDateFormat ("hh:mm:ss a");
        		statuslog="Hora Inicial: "+ft.format(myFecha);
        		myOutput.setText(statuslog);
            	
                arList = new ArrayList();
            }
        });
    }
 
    private void updateGUIWhenFinish() {
        display.asyncExec(new Runnable() {
 
            @Override
            public void run() {
                //Excel
                try
                {
                	progressBar.setSelection(progressBar.getSelection() + 1);

                	if (arList.size()>1)
                	{
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
	                    Date myFecha = new Date();
	            		SimpleDateFormat ft2 = new SimpleDateFormat ("YYYY_MM_DD_HHmmss"); 
	            		SimpleDateFormat ft = new SimpleDateFormat ("hh:mm:ss a");
	            		ft2.format(myFecha);
	            		String fileN="\\Extracto_"+ft2.format(myFecha)+".xls";
	                    FileOutputStream fileOut = new FileOutputStream(myPath+fileN);
	                    hwb.write(fileOut);
	                    fileOut.close();
	                    System.out.println("Your excel file has been generated");    
	                    myFecha = new Date();
	                    statuslog=statuslog + "\nHora Final: "+ft.format(myFecha);   
	                    statuslog=statuslog + "\nArchivo generado: "+fileN;
	                    myOutput.setText(statuslog);
                	}
                	else 
                	{
	                    Date myFecha = new Date();
	            		SimpleDateFormat ft = new SimpleDateFormat ("hh:mm:ss a");
	                    statuslog=statuslog + "\nHora Final: "+ft.format(myFecha);   
	                    myOutput.setText(statuslog);
                	}

                    progressBar.setSelection(progressBar.getSelection() + 1);
                 
                } catch ( Exception ex ) {
                    ex.printStackTrace();
                } //main method ends
            }
        });
    }

   /* public void cancel() {
        this.cancel = true;
    }*/

}
