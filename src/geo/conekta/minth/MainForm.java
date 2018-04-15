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
	private Text text;
	public String myPath=null;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Display display = Display.getDefault();
	private JFileChooser chooser = new JFileChooser();

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
		shlExtraerTorqueY.setSize(609, 342);
		shlExtraerTorqueY.setText("Extraer Torque y Angulo");
		
		
		
		txtExtraerDe = new Text(shlExtraerTorqueY, SWT.BORDER);
		txtExtraerDe.setText("C:\\Users\\stike\\Desktop\\PARSEADOR LALO\\Archivos");
		txtExtraerDe.setBounds(114, 38, 188, 21);
		
		Label lblNewLabel = new Label(shlExtraerTorqueY, SWT.NONE);
		lblNewLabel.setBounds(34, 41, 55, 15);
		lblNewLabel.setText("Extraer de:");
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(shlExtraerTorqueY, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setToolTipText("Current action");
		scrolledComposite.setBounds(95, 99, 418, 120);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
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
			    }
			}
		});
		btnCambiarRuta.setBounds(351, 34, 92, 25);
		btnCambiarRuta.setText("Cambiar Ruta");
		
		Button btnProcesar = new Button(shlExtraerTorqueY, SWT.NONE);
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
						
						Label oputout = new Label( scrolledComposite, SWT.NONE );
						label.setBackground( display.getSystemColor( SWT.COLOR_WHITE ) );
						label.setSize( 400, 400 );
						Date myFecha = new Date();
						SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
						label.setText("Proceso Iniciado: "+ft.format(myFecha));
						scrolledComposite.setContent( label );
						myProcess.processCsv(myPath);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
						
				}
			}
		});
		btnProcesar.setBounds(462, 34, 75, 25);
		btnProcesar.setText("Procesar");
		
		Label lblLog = new Label(shlExtraerTorqueY, SWT.NONE);
		lblLog.setBounds(34, 88, 55, 15);
		lblLog.setText("Log:");
		

		
		Label lblArchivoLeido = new Label(shlExtraerTorqueY, SWT.NONE);
		lblArchivoLeido.setBounds(10, 237, 79, 15);
		lblArchivoLeido.setText("Archivo Leido: ");
		
		text = new Text(shlExtraerTorqueY, SWT.BORDER);
		text.setBounds(114, 237, 241, 21);
		
		Button btnSalir = new Button(shlExtraerTorqueY, SWT.NONE);
		btnSalir.setBounds(494, 237, 75, 25);
		btnSalir.setText("Salir");
		
	  /*  Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("SWT ProgressBar (o7planning.org)");
        shell.setSize(450, 200);
 
        ProgressBar progressBar1 = new ProgressBar(shell, SWT.NULL);
        ProgressBar progressBar2 = new ProgressBar(shell, SWT.SMOOTH);
        ProgressBar progressBar3 = new ProgressBar(shell, SWT.INDETERMINATE);
 
        progressBar1.setMinimum(30);
        progressBar1.setMaximum(250);
        progressBar1.setSelection(160);
         
        progressBar2.setMinimum(30);
        progressBar2.setMaximum(250);
        progressBar2.setSelection(200);        
 
        progressBar1.setBounds(140, 10, 200, 20);
        progressBar2.setBounds(140, 40, 200, 20);
        progressBar3.setBounds(140, 70, 200, 20);
 
        Label label1 = new Label(shell, SWT.NULL);
        label1.setText("(default)");
        Label label2 = new Label(shell, SWT.NULL);
        label2.setText("SWT.SMOOTH");
        Label label3 = new Label(shell, SWT.NULL);
        label3.setText("SWT.INDETERMINATE");
 
        label1.setAlignment(SWT.RIGHT);
        label2.setAlignment(SWT.RIGHT);
        label3.setAlignment(SWT.RIGHT);
 
        label1.setBounds(10, 10, 120, 20);
        label2.setBounds(10, 40, 120, 20);
        label3.setBounds(10, 70, 120, 20);
  
        shell.open();
 
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
 
        display.dispose();*/

	}
}
