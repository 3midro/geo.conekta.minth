package geo.conekta.minth;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import java.awt.Desktop;
import java.io.File;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.wb.swt.SWTResourceManager;

public class MainForm  {

	protected Shell shlExtraerTorqueY;
	private Text txtExtraerDe;
	public static Display display = Display.getDefault();

	String selected = "c:\\csv2extract\\";
	
	public static Label myOutput;
	public static Text text1;
	public static ProgressBar progressBar1;

    private ReadCsv myProcess = null;

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
		shlExtraerTorqueY.setImage(SWTResourceManager.getImage(MainForm.class, "/geo/conekta/minth/ico48.ico"));
		shlExtraerTorqueY.setSize(586, 321);
		shlExtraerTorqueY.setText("Extraer Torque y \u00C1ngulo"); 

		txtExtraerDe = new Text(shlExtraerTorqueY, SWT.BORDER);
		txtExtraerDe.setEditable(false);
		txtExtraerDe.setEnabled(false);
		txtExtraerDe.setText("C:/csv2Extract");
		txtExtraerDe.setBounds(75, 38, 336, 21);
		
		Label lblNewLabel = new Label(shlExtraerTorqueY, SWT.NONE);
		lblNewLabel.setBounds(34, 41, 35, 15);
		lblNewLabel.setText("Ruta");
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(shlExtraerTorqueY, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setToolTipText("Current action");
		scrolledComposite.setBounds(75, 76, 459, 132);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		myOutput = new Label( scrolledComposite, SWT.NONE );
		myOutput.setBackground( display.getSystemColor( SWT.COLOR_WHITE ) );
		myOutput.setSize( 400, 400 );
		scrolledComposite.setContent(myOutput);

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

		DirectoryDialog fd = new DirectoryDialog(shlExtraerTorqueY, SWT.OPEN);
		Button btnCambiarRuta = new Button(shlExtraerTorqueY, SWT.NONE);
		btnCambiarRuta.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,1,0));
		btnCambiarRuta.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				fd.setText("Seleccionar");
				fd.setFilterPath("C:\\csv2extract");
				selected = fd.open();
				txtExtraerDe.setText(selected);
				System.out.println(selected);
			}
		});
		btnCambiarRuta.setBounds(417, 36, 35, 25);
		btnCambiarRuta.setText("...");
		
        progressBar1 = new ProgressBar(shlExtraerTorqueY, SWT.HORIZONTAL);
        progressBar1.setMinimum(0);
        progressBar1.setVisible(false);
        progressBar1.setBounds(337, 236, 115, 21); 
        
		Label lblArchivoLeido = new Label(shlExtraerTorqueY, SWT.NONE);
		lblArchivoLeido.setBounds(28, 240, 55, 15);
		lblArchivoLeido.setText("Abriendo");
		lblArchivoLeido.setVisible(false);
		
		Button btnSalir = new Button(shlExtraerTorqueY, SWT.NONE);
		btnSalir.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.exit(0);
			}
		});
		btnSalir.setBounds(458, 234, 76, 25);
		btnSalir.setText("Cancelar");
		btnSalir.setVisible(false);
		
		btnProcesar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				progressBar1.setVisible(true);
				text1.setVisible(true);
				lblArchivoLeido.setVisible(true);	
				btnSalir.setVisible(true);
				myPath = txtExtraerDe.getText();				
					myProcess = new ReadCsv(display,progressBar1, myPath, myOutput,text1,btnSalir,lblArchivoLeido);
					myProcess.start();
			}
		});
		btnProcesar.setBounds(458, 36, 75, 25);
		btnProcesar.setText("Procesar");
		
		Label lblLog = new Label(shlExtraerTorqueY, SWT.NONE);
		lblLog.setBounds(34, 76, 35, 15);
		lblLog.setText("Log");
		
		text1 = new Text(shlExtraerTorqueY, SWT.BORDER);
		text1.setEditable(false);
		text1.setBounds(85, 236, 242, 21);
		text1.setVisible(false);
		

		
		ToolBar toolBar = new ToolBar(shlExtraerTorqueY, SWT.FLAT | SWT.RIGHT);
		toolBar.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		toolBar.setBounds(0, 0, 570, 23);
		
		ToolItem tltmManual = new ToolItem(toolBar, SWT.NONE);
		tltmManual.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					Desktop.getDesktop().open(new File("C:\\Program Files (x86)\\Minth\\MANUAL-USUARIO-MINTH.pdf"));
				}catch(Exception i) {
			        MessageBox messageBox = new MessageBox(shlExtraerTorqueY, SWT.ICON_INFORMATION | SWT.OK );		        
			        messageBox.setText("Alerta");
			        messageBox.setMessage("Hay un problema con el Manual de usuario, favor de ponerse en contacto con su proveedor.");
			        messageBox.open();			
			        System.out.println(i.getStackTrace().toString());
				}
			}
		});
		tltmManual.setText("Manual");
	}
}