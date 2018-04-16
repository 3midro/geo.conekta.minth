package geo.conekta.minth;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

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

public class MainForm {

	protected Shell shlExtraerTorqueY;
	private Text txtExtraerDe;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Display display = Display.getDefault();
	private JFileChooser chooser = new JFileChooser();
	public static Label myOutput;
	public static Text text;
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
		txtExtraerDe.setText("");
		txtExtraerDe.setBounds(136, 38, 316, 21);
		
		Label lblNewLabel = new Label(shlExtraerTorqueY, SWT.NONE);
		lblNewLabel.setBounds(34, 41, 55, 15);
		lblNewLabel.setText("Extraer de :");
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(shlExtraerTorqueY, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setToolTipText("Current action");
		scrolledComposite.setBounds(75, 76, 459, 132);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		Button btnProcesar = new Button(shlExtraerTorqueY, SWT.NONE);
		btnProcesar.setEnabled(false);

		Button btnCambiarRuta = new Button(shlExtraerTorqueY, SWT.NONE);
		btnCambiarRuta.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,1,0));
		btnCambiarRuta.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			    Integer returnVal = chooser.showSaveDialog(null);
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			    	myPath=chooser.getSelectedFile().getPath();
			    	txtExtraerDe.setText(myPath);
			    	txtExtraerDe.setEnabled(true);    	
			    	btnProcesar.setEnabled(true);
			    }
			}
		});
		btnCambiarRuta.setBounds(95, 36, 35, 25);
		btnCambiarRuta.setText("...");
		
        progressBar1 = new ProgressBar(shlExtraerTorqueY, SWT.NULL);
        progressBar1.setMinimum(0);
        progressBar1.setMaximum(100);
        progressBar1.setSelection(0);
        progressBar1.setBounds(365, 240, 107, 21);
		
		btnProcesar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				myPath=chooser.getSelectedFile().getPath();
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
		lblLog.setText("Log:");
		

		
		Label lblArchivoLeido = new Label(shlExtraerTorqueY, SWT.NONE);
		lblArchivoLeido.setBounds(33, 242, 79, 15);
		lblArchivoLeido.setText("Archivo Leido: ");
		
		text = new Text(shlExtraerTorqueY, SWT.BORDER);
		text.setBounds(118, 240, 241, 21);
		
		Button btnSalir = new Button(shlExtraerTorqueY, SWT.NONE);
		btnSalir.setBounds(478, 238, 55, 25);
		btnSalir.setText("Salir");
		


	}
	
	public static void setLabelText(String txt)
	{
		myOutput.setText(txt);
	}
	
	public static void setLabelFile(String txt)
	{
		text.setText(txt);
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
