package geo.conekta.minth;

import java.awt.Desktop;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Stream;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
      String[] linea = new String[8];	  
	  
	    static ArrayList arList = new ArrayList();
	    static String statuslog;
	    static int progressC;
	    String myLine="";
	    int totalCount;
	     int count=0;
	     int countSize=0;
	  private Button btnProcesar;  
	     private Button btnCambiarRuta;
	    
	    public ReadCsv(Display display, ProgressBar progressBar,String myPath, Label myOutput,Text text1, Button btnSalir, Label lblArchivoLeido, Button btnProcesar, Button btnCambiarRuta ) {
	        this.display = display;
	        this.progressBar = progressBar;
	        this.myPath=myPath;
	        this.myOutput=myOutput;
	        this.text1=text1;
	        this.btnSalir = btnSalir;
	        this.lblArchivoLeido = lblArchivoLeido;
	        this.btnProcesar = btnProcesar;
	        this.btnCambiarRuta = btnCambiarRuta;
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
	            Thread.sleep(1);
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
	                      //String fNameComp = ficheros[x].getAbsolutePath();
	                      //FileInputStream fis;
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
	        //añade la cabecera
	        if (count == 0) {
	          arList.add("par de torsión perno_exterior_izq,ángulo perno_exterior_izq,par de torsión perno_interior_izq,ángulo perno_interior_izq,par de torsión perno_interior_drch,ángulo perno_interior_drch,par de torsión perno_exterior_drch,ángulo perno_exterior_drch,ruta archivo");
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
	                  linea[0] = ",";linea[1] = ",";linea[2] = ",";linea[3] = ",";linea[4] = ",";linea[5] = ",";linea[6] = ",";linea[7] = ",";
	                  DataInputStream myInput = new DataInputStream(fis);
	                  while ((thisLine = myInput.readLine()) != null){
	                    String strar[] = thisLine.split(";");
	                    if (strar.length > 1) {
	                      //usar astring y split
	                      if (strar[0].equals("par de torsión perno_exterior_izq") || strar[0].equals("torque dowel pin_outside_left") || strar[0].equals("Drehmoment Stehbolzen_Aussen_Li") ) {
	                        linea[0]=strar[1]+",";
	                      }
	                      if (strar[0].equals("ángulo perno_exterior_izq") || strar[0].equals("angle dowel pin_outside_left") || strar[0].equals("Winkel Stehbolzen_Aussen_Li")) {
	                        linea[1]=strar[1]+",";
	                      }
	                      if (strar[0].equals("par de torsión perno_interior_izq") || strar[0].equals("torque dowel pin_inside_left") || strar[0].equals("Drehmoment Stehbolzen_Innen_Li")) {
	                          linea[2]=strar[1]+",";
	                        }
	                        if (strar[0].equals("ángulo perno_interior_izq") || strar[0].equals("angle dowel pin_inside_left") || strar[0].equals("Winkel Stehbolzen_Innen_Li")) {
	                          linea[3]=strar[1]+",";
	                        }
	                        if (strar[0].equals("par de torsión perno_interior_drch") || strar[0].equals("torque dowel pin_inside_right") || strar[0].equals("Drehmoment Stehbolzen_Innen_Re")) {
	                          linea[4]=strar[1]+",";
	                        }
	                        if (strar[0].equals("ángulo perno_interior_drch") || strar[0].equals("angle dowel pin_inside_right") || strar[0].equals("Winkel Stehbolzen_Innen_Re")) {
	                          linea[5]=strar[1]+",";
	                        }
	                        if (strar[0].equals("par de torsión perno_exterior_drch") || strar[0].equals("torque dowel pin_outside_right") || strar[0].equals("Drehmoment Stehbolzen_Aussen_Re")) {
	                          linea[6]=strar[1]+",";
	                        }
	                        if (strar[0].equals("ángulo perno_exterior_drch") || strar[0].equals("angle dowel pin_outside_right") || strar[0].equals("Winkel Stehbolzen_Aussen_Re")) {
	                          linea[7]=strar[1]+",";
	                        }
	                        i++;
	                      }
	                    }
	                    myLine = myLine.concat(linea[0]).concat(linea[1]).concat(linea[2]).concat(linea[3]).concat(linea[4]).concat(linea[5]).concat(linea[6]).concat(linea[7]); 
	                  } catch (IOException e) {
	                    e.printStackTrace();
	                  }
	                //if (!myLine.equals("")) {
	                myLine = myLine.concat(fNameComp).concat(",");
	                arList.add(myLine);
	              //}
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
	                         int hojas = 1;
	                         int rows = 0;
	                       if (arList.size()>1)
	                       {
	     
	                         XSSFWorkbook hwb = new XSSFWorkbook();
	                         XSSFSheet sheet = hwb.createSheet("Sheet" + hojas);
	                         
	                         for(int k=0;k<arList.size();k++)
	                         {
	                             if (cancel) {
	                                 break;
	                             }
	                           String myLine= arList.get(k).toString();              
	                           String data[] = myLine.split(",");

	                               //crea una nueva hoja si llega al limite
	                             if (rows == 1048576) {
	                               hojas++;
	                                 sheet = hwb.createSheet("Sheet " + hojas);
	                                 rows = 0;
	                             }
	                             XSSFRow row = sheet.createRow((short) 0+rows);
	                             rows++;
	                                  
	                             for(int p=0;p<data.length;p++)
	                             {
	                                 XSSFCell cell = row.createCell((short) p);
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
	                           
	                           int hora = (int) (diferencia / 3600);
	                           int minu = (int) ((diferencia - (3600 * hora)) / 60);
	                           int seg = (int) (diferencia - ((hora * 3600)+(minu * 60)));
	                           
	                           tiempo = hora + "horas " + minu + "minutos " + seg + "segundos.";
	                           
	                           /*if (diferencia / (1000 * 60) > 0){
	                             tiempo= (diferencia / (1000 * 60)) + " minutos transcurridos";
	                           } else {
	                             tiempo= (diferencia / 1000) + " segundos transcurridos";
	                           }*/
	                         
	                         
	                         String fileN="/Extracto_"+ft2.format(myFecha)+".xlsx";
	                             FileOutputStream fileOut = new FileOutputStream(myPath+fileN);
	                             hwb.write(fileOut);
	                             fileOut.close();
	                             //hwb.close();
	                             System.out.println("Your excel file has been generated"); 
	                             try{ 
	                               //definiendo la ruta en la propiedad file
	                               Desktop.getDesktop().open(new File(myPath+fileN));                         
	                              }catch(IOException e){
	                                 e.printStackTrace();
	                              } 
	                              
	                              myFecha = new Date();
	                              String finalSize=size(countSize);
	                              statuslog=statuslog + "\n "+count+" Archivos Procesados - Tamaño: "+finalSize;
	                              statuslog=statuslog + "\nHora Final: "+ft.format(myFecha);   
	                              statuslog=statuslog + "\nTiempo de Ejecución: "+ tiempo  ;
	                              statuslog=statuslog + "\nArchivo generado: "+myPath+fileN;
	                              myOutput.setText(statuslog);
	                          }
	                          else 
	                          {
	                              Date myFecha = new Date();
	                              statuslog=statuslog + "\n - Ningun archivo procesado / sin información";
	                          SimpleDateFormat ft = new SimpleDateFormat ("hh:mm:ss a");
	                              statuslog=statuslog + "\nHora Final: "+ft.format(myFecha);   
	                              myOutput.setText(statuslog);
	                          }
	                       
	                       
	                       progressBar.setVisible(false);
	               text1.setVisible(false);
	               lblArchivoLeido.setVisible(false);  
	               btnSalir.setVisible(false);
	               btnProcesar.setEnabled(true);
	               btnCambiarRuta.setEnabled(true);
	               
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
	             btnProcesar.setEnabled(true);
	             btnCambiarRuta.setEnabled(true);
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