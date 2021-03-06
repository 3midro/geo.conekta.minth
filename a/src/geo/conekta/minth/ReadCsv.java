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

import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;

public class ReadCsv extends Thread {

	private Display display;
	private ProgressBar progressBar;
	private Label myOutput;
	private Text text1;
	private String myPath;
	private Button btnSalir;
	private Label lblArchivoLeido;
	private Date myFechaIni;
	private boolean cancel;
	private String[] linea;
	private boolean flagHeader;

	static ArrayList arList = new ArrayList();
	static String statuslog;
	static int progressC;
	String myLine = "";
	int totalCount;
	int count = 0;
	int countSize = 0;
	private Button btnProcesar;
	private Button btnCambiarRuta;

	public ReadCsv(Display display, ProgressBar progressBar, String myPath, Label myOutput, Text text1, Button btnSalir,
			Label lblArchivoLeido, Button btnProcesar, Button btnCambiarRuta) {
		this.display = display;
		this.progressBar = progressBar;
		this.myPath = myPath;
		this.myOutput = myOutput;
		this.text1 = text1;
		this.btnSalir = btnSalir;
		this.lblArchivoLeido = lblArchivoLeido;
		this.btnProcesar = btnProcesar;
		this.btnCambiarRuta = btnCambiarRuta;
		this.flagHeader = false;
	}

	@Override
	public void run() {
		if (display.isDisposed()) {
			return;
		}
		this.updateGUIWhenStart();
		totalCount = 0;
		getCountFile(myPath);
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
				SimpleDateFormat ft = new SimpleDateFormat("hh:mm:ss a");
				statuslog = "Hora Inicial: " + ft.format(myFechaIni);
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

	private void getFiles(String directorio) {
		final String extension = ".csv";
		File dir = new File("dir");
		FileFilter directoryFilter = new FileFilter() {
			public boolean accept(File file) {
				return file.isDirectory();
			}
		};

		File f = new File(directorio);
		File[] ar1 = f.listFiles((File pathname) -> pathname.getName().endsWith(extension));
		File[] ar2 = f.listFiles(directoryFilter);
		File[] ficheros = Stream.of(ar1, ar2).flatMap(Stream::of).toArray(File[]::new);
		progressC = totalCount + 2;

		int i = 0;
		if (!flagHeader) {
			arList.add(
					"par de torsi�n perno_exterior_izq,�ngulo perno_exterior_izq,par de torsi�n perno_interior_izq,�ngulo perno_interior_izq,par de torsi�n perno_interior_drch,�ngulo perno_interior_drch,par de torsi�n perno_exterior_drch,�ngulo perno_exterior_drch,ruta archivo");
			flagHeader = true;
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
				this.updateGUIInProgress(fName, count, progressC);
				count++;
				countSize = countSize + (int) (ficheros[x].length());
				String fNameComp = ficheros[x].getAbsolutePath();
				String thisLine;
				FileInputStream fis;
				try {
					System.out.println(fName);
					fis = new FileInputStream(fName);
					linea = new String[8];
					linea[0] = ",";
					linea[1] = ",";
					linea[2] = ",";
					linea[3] = ",";
					linea[4] = ",";
					linea[5] = ",";
					linea[6] = ",";
					linea[7] = ",";
					DataInputStream myInput = new DataInputStream(fis);
					while ((thisLine = myInput.readLine()) != null) {
						String strar[] = thisLine.split(";");
						if (strar.length > 1) {
							if (strar[0].equals("par de torsi�n perno_exterior_izq")
									|| strar[0].equals("torque dowel pin_outside_left")
									|| strar[0].equals("Drehmoment Stehbolzen_Aussen_Li")) {
								strar[1] = strar[1].replaceAll(",",".");
								linea[0] = strar[1] + ",";
							}
							if (strar[0].equals("�ngulo perno_exterior_izq")
									|| strar[0].equals("angle dowel pin_outside_left")
									|| strar[0].equals("Winkel Stehbolzen_Aussen_Li")) {
								strar[1] = strar[1].replaceAll(",",".");
								linea[1] = strar[1] + ",";
							}
							if (strar[0].equals("par de torsi�n perno_interior_izq")
									|| strar[0].equals("torque dowel pin_inside_left")
									|| strar[0].equals("Drehmoment Stehbolzen_Innen_Li")) {
								strar[1] = strar[1].replaceAll(",",".");
								linea[2] = strar[1] + ",";
							}
							if (strar[0].equals("�ngulo perno_interior_izq")
									|| strar[0].equals("angle dowel pin_inside_left")
									|| strar[0].equals("Winkel Stehbolzen_Innen_Li")) {
								strar[1] = strar[1].replaceAll(",",".");
								linea[3] = strar[1] + ",";
							}
							if (strar[0].equals("par de torsi�n perno_interior_drch")
									|| strar[0].equals("torque dowel pin_inside_right")
									|| strar[0].equals("Drehmoment Stehbolzen_Innen_Re")) {
								strar[1] = strar[1].replaceAll(",",".");
								linea[4] = strar[1] + ",";
							}
							if (strar[0].equals("�ngulo perno_interior_drch")
									|| strar[0].equals("angle dowel pin_inside_right")
									|| strar[0].equals("Winkel Stehbolzen_Innen_Re")) {
								strar[1] = strar[1].replaceAll(",",".");
								linea[5] = strar[1] + ",";
							}
							if (strar[0].equals("par de torsi�n perno_exterior_drch")
									|| strar[0].equals("torque dowel pin_outside_right")
									|| strar[0].equals("Drehmoment Stehbolzen_Aussen_Re")) {
								strar[1] = strar[1].replaceAll(",",".");
								linea[6] = strar[1] + ",";
							}
							if (strar[0].equals("�ngulo perno_exterior_drch")
									|| strar[0].equals("angle dowel pin_outside_right")
									|| strar[0].equals("Winkel Stehbolzen_Aussen_Re")) {
								strar[1] = strar[1].replaceAll(",",".");
								linea[7] = strar[1] + ",";
							}
							i++;
						}
					}
					myLine = myLine.concat(linea[0]).concat(linea[1]).concat(linea[2]).concat(linea[3]).concat(linea[4])
							.concat(linea[5]).concat(linea[6]).concat(linea[7]);
					System.out.println(myLine);
				} catch (IOException e) {
					e.printStackTrace();
				}
				myLine = myLine.concat(fNameComp).concat(",");
				arList.add(myLine);
				linea = null;
				myLine = "";
			}
		}
	}

	private void updateGUIWhenFinish() {
		display.asyncExec(new Runnable() {

			@Override
			public void run() {
				// Excel
				if (!cancel) {
					try {
						progressBar.setSelection(progressBar.getSelection() + 2);
						int hojas = 1;
						int rows = 0;
						if (arList.size() > 1) {

							SXSSFWorkbook hwb = new SXSSFWorkbook();
							SXSSFSheet sheet = hwb.createSheet("Sheet" + hojas);

							for (int k = 0; k < arList.size(); k++) {
								if (cancel) {
									break;
								}
								String myLine = arList.get(k).toString();
								String data[] = myLine.split(",");

								// crea una nueva hoja si llega al limite
								if (rows == 1048576) {
									hojas++;
									sheet = hwb.createSheet("Sheet " + hojas);
									rows = 0;

								}
								SXSSFRow row = sheet.createRow((short) 0 + rows);
								rows++;

								for (int p = 0; p < data.length; p++) {
									SXSSFCell cell = row.createCell((short) p);
									cell.setCellValue(data[p]);
								}
							}
							Date myFecha = new Date();
							SimpleDateFormat ft2 = new SimpleDateFormat("YYYY_MM_DD_HHmmss");
							SimpleDateFormat ft = new SimpleDateFormat("hh:mm:ss a");
							ft2.format(myFecha);

							java.util.GregorianCalendar jCal = new java.util.GregorianCalendar();
							java.util.GregorianCalendar jCal2 = new java.util.GregorianCalendar();

							jCal.setTime(myFechaIni);
							jCal2.setTime(myFecha);

							String tiempo;
							long diferencia = (jCal2.getTime().getTime() - jCal.getTime().getTime()) / 1000;
							long hora = 0;
							long minu = 0;
							long seg = 0;
							tiempo = "";
							if (diferencia > 3600) {
								hora = (diferencia / 3600);
								tiempo = tiempo + hora + " hrs";
							}

							if ((diferencia - (3600 * hora)) > 60) {
								minu = ((diferencia - (3600 * hora)) / 60);
								tiempo = tiempo + " " + minu + " min ";
							}

							seg = (diferencia - ((hora * 3600) + (minu * 60)));
							tiempo = tiempo + seg + " seg.";

							String fileN = "/Extracto_" + ft2.format(myFecha) + ".xlsx";
							FileOutputStream fileOut = new FileOutputStream(myPath + fileN);
							hwb.write(fileOut);
							fileOut.close();
							System.out.println("Your excel file has been generated");
							try {
								// definiendo la ruta en la propiedad file
								Desktop.getDesktop().open(new File(myPath + fileN));
							} catch (IOException e) {
								e.printStackTrace();
							}

							statuslog = statuslog + " |  Hora Final: " + ft.format(myFecha);
							statuslog = statuslog + "\nTiempo de Ejecuci�n: " + tiempo;
							String finalSize = size(countSize);
							statuslog = statuslog + "\n" + count + " Archivos Procesados | Tama�o: " + finalSize;
							statuslog = statuslog + "\nArchivo generado: " + myPath + fileN;
							myOutput.setText(statuslog);
						} else {
							Date myFecha = new Date();
							statuslog = statuslog + "\n - Ningun archivo procesado / sin informaci�n";
							SimpleDateFormat ft = new SimpleDateFormat("hh:mm:ss a");
							statuslog = statuslog + "\nHora Final: " + ft.format(myFecha);
							myOutput.setText(statuslog);
						}

						progressBar.setVisible(false);
						text1.setVisible(false);
						lblArchivoLeido.setVisible(false);
						btnSalir.setVisible(false);
						btnProcesar.setEnabled(true);
						btnCambiarRuta.setEnabled(true);

					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				if (cancel) {
					Date myFecha = new Date();
					statuslog = statuslog + "\n - Proceso Cancelado";
					SimpleDateFormat ft = new SimpleDateFormat("hh:mm:ss a");
					statuslog = statuslog + "\nHora Final: " + ft.format(myFecha);
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

	public static String size(int size) {
		String hrSize = "";
		int k = size;
		double m = size / 1024;
		double g = size / 1048576;
		double t = size / 1073741824;

		DecimalFormat dec = new DecimalFormat("0.00");

		if (k > 0) {
			hrSize = dec.format(k).concat(" byte");
		}
		if (m > 0) {
			hrSize = dec.format(m).concat(" KB");
		}
		if (g > 0) {
			hrSize = dec.format(g).concat(" MB");
		}
		if (t > 0) {
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
					extension = file.getName().substring(c + 1);
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