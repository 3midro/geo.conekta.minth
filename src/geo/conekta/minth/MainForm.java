package geo.conekta.minth;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.awt.Desktop;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFileChooser;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;

public class MainForm {

	protected Shell shlExtraerTorqueY;
	private Text txtExtraerDe;
	private Display display = Display.getDefault();
	private JFileChooser chooser = new JFileChooser("C:\\csv2extract");
	public static Label myOutput;
	public static Text text1;
	public static ProgressBar progressBar1;
	
	String myPath=null;
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MainForm window = new MainForm();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		
		createContents();
		shlExtraerTorqueY.open();
		shlExtraerTorqueY.layout();
		while (!shlExtraerTorqueY.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlExtraerTorqueY = new Shell();
		shlExtraerTorqueY.setSize(586, 342);
		shlExtraerTorqueY.setText("Extraer Torque y Angulo");

		txtExtraerDe = new Text(shlExtraerTorqueY, SWT.BORDER);
		txtExtraerDe.setEditable(false);
		txtExtraerDe.setEnabled(false);
		txtExtraerDe.setText("C:/csv2Extract");
		txtExtraerDe.setBounds(75, 38, 336, 21);
		
		Label lblNewLabel = new Label(shlExtraerTorqueY, SWT.NONE);
		lblNewLabel.setBounds(34, 41, 55, 15);
		lblNewLabel.setText("Ruta");
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(shlExtraerTorqueY, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setToolTipText("Current action");
		scrolledComposite.setBounds(75, 76, 459, 132);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		Button btnProcesar = new Button(shlExtraerTorqueY, SWT.NONE);
		//crea el directorio csv2extract si no existe
		try {
			File folder = new File("c:\\csv2extract\\");
			folder.mkdirs(); 
		} catch(SecurityException e){
			//no tiene acceso a la unidad C: le pide al usuario que cambie o seleccione una ruta
			btnProcesar.setEnabled(false);
			System.out.println(e);
        }
		
		
		
//		btnProcesar.setEnabled(true);

		Button btnCambiarRuta = new Button(shlExtraerTorqueY, SWT.NONE);
		btnCambiarRuta.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,1,0));
		btnCambiarRuta.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setDialogTitle("Seleccione un directorio");
			    Integer returnVal = chooser.showOpenDialog(null);
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			    	myPath=chooser.getSelectedFile().getPath();
			    	txtExtraerDe.setText(myPath);
			    	txtExtraerDe.setEnabled(true);    	
			    	btnProcesar.setEnabled(true);
			    }
			}
		});
		btnCambiarRuta.setBounds(417, 36, 35, 25);
		btnCambiarRuta.setText("...");
		
        progressBar1 = new ProgressBar(shlExtraerTorqueY, SWT.NULL);
        progressBar1.setMinimum(0);
        progressBar1.setVisible(false);
        //progressBar1.setSelection(0);
        progressBar1.setBounds(337, 272, 115, 21);
        
		Label lblArchivoLeido = new Label(shlExtraerTorqueY, SWT.NONE);
		lblArchivoLeido.setBounds(14, 278, 55, 15);
		lblArchivoLeido.setText("Abriendo");
		lblArchivoLeido.setVisible(false);
		
		btnProcesar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//myPath=chooser.getSelectedFile().getPath();
				
				progressBar1.setVisible(true);
				text1.setVisible(true);
				lblArchivoLeido.setVisible(true);
				
				myPath = txtExtraerDe.getText();
				if (myPath == null)
				{
				    Shell shell = new Shell();
			        MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING | SWT.ABORT | SWT.RETRY | SWT.IGNORE);		        
			        messageBox.setText("Alerta");
			        messageBox.setMessage("Selecciona una carpeta para proceder");
				}
				else {					
					ReadCsv myProcess= new ReadCsv();
					try {			
						myOutput = new Label( scrolledComposite, SWT.NONE );
						myOutput.setBackground( display.getSystemColor( SWT.COLOR_WHITE ) );
						myOutput.setSize( 400, 400 );
						scrolledComposite.setContent(myOutput);
						myProcess.processCsv(myPath);
						progressBar1.setSelection(100);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		btnProcesar.setBounds(458, 36, 75, 25);
		btnProcesar.setText("Procesar");
		
		Label lblLog = new Label(shlExtraerTorqueY, SWT.NONE);
		lblLog.setBounds(34, 76, 35, 15);
		lblLog.setText("Log");
		

		

		
		text1 = new Text(shlExtraerTorqueY, SWT.BORDER);
		text1.setBounds(89, 272, 242, 21);
		text1.setVisible(false);
		
		Button btnSalir = new Button(shlExtraerTorqueY, SWT.NONE);
		btnSalir.setBounds(458, 272, 76, 25);
		btnSalir.setText("Cancelar");
		
		ToolBar toolBar = new ToolBar(shlExtraerTorqueY, SWT.FLAT | SWT.RIGHT);
		toolBar.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		toolBar.setBounds(0, 0, 570, 23);
		
		ToolItem tltmManual = new ToolItem(toolBar, SWT.NONE);
		tltmManual.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					Desktop.getDesktop().open(new File("C:\\Users\\PinXe\\Pictures\\minth\\MANUAL USUARIO MINTH.pdf"));
				}catch(Exception i) {
					System.out.println("No se pudo abrir el PDF");
				}
				
			}
		});
		tltmManual.setText("Manual");
		


	}
	
	public static void setLabelText(String txt)
	{
		myOutput.setText(txt);
	}
	
	public static void setLabelFile(String txt)
	{
		text1.setText(txt);
	}
	
	public static void setPBMax(int num)
	{
        progressBar1.setMaximum(num);
	}
	
	public static void setPBSel(int num)
	{
        progressBar1.setSelection(num);
	}
}
