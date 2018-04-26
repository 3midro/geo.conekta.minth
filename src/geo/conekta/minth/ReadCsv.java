package geo.conekta.minth;
import java.awt.Desktop;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Stream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
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
	private Button btnSalir;
	private Label lblArchivoLeido;
	private Date myFechaIni;
	private boolean cancel;
	
    static ArrayList arList = new ArrayList();
    static String statuslog;
    static int progressC;
    String myLine="";
    int totalCount;
   	int count=0;
   	int countSize=0;  
    
    public ReadCsv(Display display, ProgressBar progressBar,String myPath, Label myOutput,Text text1, Button btnSalir, Label lblArchivoLeido) {
        this.display = display;
        this.progressBar = progressBar;
        this.myPath=myPath;
        this.myOutput=myOutput;
        this.text1=text1;
        this.btnSalir = btnSalir;
        this.lblArchivoLeido = lblArchivoLeido;
    }

    @Override
    public void run() {
        if (display.isDisposed()) {
            return;
        }
        this.updateGUIWhenStart();
    	totalCount = 0;
    	getCountFile (myPath);
    	getFiles(myPath);
        this.updateGUIWhenFinish();
    }
    
    private void copy(File file) {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
        }
    }
    
    private void updateGUIWhenStart() {
        display.asyncExec(new Runnable() {
 
            @Override
            public void run() {
            	arList = new ArrayList();
        		progressBar.setSelection(0);
            	myFechaIni = new Date();
        		SimpleDateFormat ft = new SimpleDateFormat ("hh:mm:ss a");
        		statuslog="Hora Inicial: "+ft.format(myFechaIni);
        		myOutput.setText(statuslog);
            }
        });
        

    }

    private void updateGUIInProgress(String file, int value, int count) {
   	 	display.asyncExec(new Runnable() {

   	 		@Override
            public void run() {
            	
   	 			lblArchivoLeido.setText("Abriendo: " + file);
   	 			progressBar.setMaximum(count);
   	 			progressBar.setSelection(value);
            }
    	});
    }
 
	private void getFiles(String directorio)
	{
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
		progressC=totalCount+2;
		//progressBar.setMaximum(progressC);
		
	    //ArrayList al = new ArrayList();
	    int i=0;
	    //a�ade la cabecera
	    if (count == 0) {
	    	arList.add("par de torsi�n perno_exterior_izq,�ngulo perno_exterior_izq,par de torsi�n perno_interior_izq,�ngulo perno_interior_izq,par de torsi�n perno_interior_drch,�ngulo perno_interior_drch,par de torsi�n perno_exterior_drch,�ngulo perno_exterior_drch,ruta archivo");
	    }
	    for (int x = 0; x < ficheros.length; x++) {
	        if (cancel) {
	            break;
	        }
	    	if (ficheros[x].isDirectory()) {
	    		System.out.println(ficheros[x].getName());
	    		this.getFiles(ficheros[x].getPath());
	    	} else {
	    		this.copy(ficheros[x]);
	    		String fName = ficheros[x].getPath();
	            this.updateGUIInProgress(fName,count,progressC);
	            
	    		//progressBar.setSelection(progressBar.getSelection() + 1);
	    		//MainForm.text1.setText(ficheros[x].getAbsolutePath());
	    		count++;
	    		countSize=countSize + (int)(ficheros[x].length());        	
			
	    		//CSV
	    		
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
	}
 
    private void updateGUIWhenFinish() {
        display.asyncExec(new Runnable() {
 
            @Override
            public void run() {
                //Excel
            	if (!cancel) {
	                try
	                {
	                	progressBar.setSelection(progressBar.getSelection() + 2);
	                	if (arList.size()>1)
	                	{
	
		                	HSSFWorkbook hwb = new HSSFWorkbook();
		                    HSSFSheet sheet = hwb.createSheet("new sheet");
		                    for(int k=0;k<arList.size();k++)
		                    {
		                        if (cancel) {
		                            break;
		                        }
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
		            		
		            		java.util.GregorianCalendar jCal = new java.util.GregorianCalendar();
	            		    java.util.GregorianCalendar jCal2 = new java.util.GregorianCalendar();
	            		    
	            		    jCal.setTime(myFechaIni);
	            		    jCal2.setTime(myFecha);
	            		    
	            		    String tiempo;
	            		    long diferencia = jCal2.getTime().getTime()-jCal.getTime().getTime();
	            		    if (diferencia / (1000 * 60) > 0){
	            		    	tiempo= (diferencia / (1000 * 60)) + " minutos transcurridos";
	            		    } else {
	            		    	tiempo= (diferencia / 1000) + " segundos transcurridos";
	            		    }
		            		
		            		
		            		String fileN="/Extracto_"+ft2.format(myFecha)+".xls";
		                    FileOutputStream fileOut = new FileOutputStream(myPath+fileN);
		                    hwb.write(fileOut);
		                    fileOut.close();
		                    System.out.println("Your excel file has been generated"); 
		                   try{ 
		                     //definiendo la ruta en la propiedad file
		                	   Desktop.getDesktop().open(new File(myPath+fileN));	                       
		                     }catch(IOException e){
		                        e.printStackTrace();
		                     } 
		                    
		                    myFecha = new Date();
		                    String finalSize=size(countSize);
    	                	statuslog=statuslog + "\n "+count+" Archivos Procesados - Tama�o: "+finalSize;
		                    statuslog=statuslog + "\nHora Final: "+ft.format(myFecha);   
		                    statuslog=statuslog + "\nTiempo de Ejecuci�n: "+ tiempo  ;
		                    statuslog=statuslog + "\nArchivo generado: "+myPath+fileN;
		                    myOutput.setText(statuslog);
	                	}
	                	else 
	                	{
		                    Date myFecha = new Date();
		                    statuslog=statuslog + "\n - Ningun archivo procesado / sin informaci�n";
		            		SimpleDateFormat ft = new SimpleDateFormat ("hh:mm:ss a");
		                    statuslog=statuslog + "\nHora Final: "+ft.format(myFecha);   
		                    myOutput.setText(statuslog);
	                	}
	
	
	                    progressBar.setVisible(false);
	    				text1.setVisible(false);
	    				lblArchivoLeido.setVisible(false);	
	    				btnSalir.setVisible(false);
	    			} catch ( Exception ex ) {
	                    ex.printStackTrace();
	                } //main method ends
            	}            	
	            if (cancel) {
	            	Date myFecha = new Date();
                    statuslog=statuslog + "\n - Proceso Cancelado";
            		SimpleDateFormat ft = new SimpleDateFormat ("hh:mm:ss a");
                    statuslog=statuslog + "\nHora Final: "+ft.format(myFecha);   
                    myOutput.setText(statuslog);
                    progressBar.setVisible(false);
    				text1.setVisible(false);
    				lblArchivoLeido.setVisible(false);	
    				btnSalir.setVisible(false);
	            }  
            }
        });
    }
    
    public static String size(int size){
	    String hrSize = "";
	    int k = size;
	    double m = size/1024;
	    double g = size/1048576;
	    double t = size/1073741824;
	    
	    DecimalFormat dec = new DecimalFormat("0.00");
	    
	    if (k>0)
	    {
	        hrSize = dec.format(k).concat(" byte");
	    }
	    if (m>0)
	    {
	        hrSize = dec.format(m).concat(" KB");
	    }
	    if (g>0)
	    {
	        hrSize = dec.format(g).concat(" MB");
	    }
	    if (t>0)
	    {
	        hrSize = dec.format(t).concat(" GB");
	    }
	    return hrSize;
    }
    
    private void getCountFile(String dirPath) {
        File f = new File(dirPath);
        File[] files = f.listFiles();
        if (files != null)
        for (int i = 0; i < files.length; i++) {
            File file = files[i];         
            String extension = "";
            int c = file.getName().lastIndexOf('.');
            if (c > 0) {
                extension = file.getName().substring(c+1);
            }
            if (extension.equals("csv"))
            	totalCount++;            
            if (file.isDirectory()) {   
            	getCountFile(file.getAbsolutePath()); 
            }
        }
    }
    


    public void cancel() {
        this.cancel = true;
    }

}
