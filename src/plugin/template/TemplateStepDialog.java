package plugin.template;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.Props;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.job.entries.sql.JobEntrySQL;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.ui.core.gui.WindowProperty;
import org.pentaho.di.ui.core.widget.StyledTextComp;
import org.pentaho.di.ui.core.widget.TextVar;
import org.pentaho.di.ui.trans.step.BaseStepDialog;


/**
 * 对话框类 
 * @Author      : http://www.ahuoo.com, ydh
 * @Date        : Jan 7, 2015
 * @Email       : ydh110100@163.com
 * @Version     : V1.0
 */
public class TemplateStepDialog extends BaseStepDialog implements StepDialogInterface {

	private static Class<?> PKG = TemplateStepMeta.class; // for i18n purposes
	
	private TemplateStepMeta input;

	private static final String[] FILETYPES = new String[] { BaseMessages.getString(PKG, "JobSQL.Filetype.Sql"), BaseMessages.getString(PKG, "JobSQL.Filetype.Text"), BaseMessages.getString(PKG, "JobSQL.Filetype.All") };

	
    private Label wlName;

    private Text wName;

    private FormData fdlName, fdName;

    private CCombo wConnection;

    private Label wlUseSubs;

    private Button wUseSubs;
    
    private Button wSQLFromFile;
    
    private Label wlSQLFromFile;

    private FormData fdlUseSubs, fdUseSubs;
    
    private FormData fdlSQLFromFile, fdSQLFromFile;

    private Label wlSQL;

    private StyledTextComp wSQL;

    private FormData fdlSQL, fdSQL;

    private Label wlPosition;

    private FormData fdlPosition;

    private Button wOK, wCancel;

    private Listener lsOK, lsCancel;

    private JobEntrySQL jobEntry;

    private Shell shell;

    private SelectionAdapter lsDef;

    private boolean changed;
    
    private Label wlUseOneStatement;

    private Button wSendOneStatement;
    
    private FormData fdlUseOneStatement, fdUseOneStatement;
    

	// File
	private Label wlFilename, wlOutFilename;
	private Button wbFilename, wbOutFilename;
	private TextVar wFilename, wOutFilename;
	private FormData fdlFilename, fdbFilename, fdFilename, 
	fdlOutFilename, fdbOutFilename, fdOutFilename;
	
	public TemplateStepDialog(Shell parent, Object in, TransMeta transMeta, String sname) {
		super(parent, (BaseStepMeta) in, transMeta, sname);
		input = (TemplateStepMeta) in;
	}

	public String open()
    {
        Shell parent = getParent();
        Display display = parent.getDisplay();

        shell = new Shell(parent);
        props.setLook(shell);
        setShellImage(shell, input);

        ModifyListener lsMod = new ModifyListener()
        {
            public void modifyText(ModifyEvent e)
            {
            	input.setChanged();
            }
        };
        changed = input.hasChanged();

        FormLayout formLayout = new FormLayout();
        formLayout.marginWidth = Const.FORM_MARGIN;
        formLayout.marginHeight = Const.FORM_MARGIN;

        shell.setLayout(formLayout);
        shell.setText(BaseMessages.getString(PKG, "JobSQL.Title"));

        int middle = props.getMiddlePct();
        int margin = Const.MARGIN;

        wOK = new Button(shell, SWT.PUSH);
        wOK.setText(BaseMessages.getString(PKG, "System.Button.OK"));
        wCancel = new Button(shell, SWT.PUSH);
        wCancel.setText(BaseMessages.getString(PKG, "System.Button.Cancel"));

        BaseStepDialog.positionBottomButtons(shell, new Button[] { wOK, wCancel }, margin, null);

        // Filename line
        wlName = new Label(shell, SWT.RIGHT);
        wlName.setText(BaseMessages.getString(PKG, "JobSQL.Name.Label"));
        props.setLook(wlName);
        fdlName = new FormData();
        fdlName.left = new FormAttachment(0, 0);
        fdlName.right = new FormAttachment(middle, 0);
        fdlName.top = new FormAttachment(0, margin);
        wlName.setLayoutData(fdlName);
        wName = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
        wName.setText(stepname);
        props.setLook(wName);
        wName.addModifyListener(lsMod);
        fdName = new FormData();
        fdName.left = new FormAttachment(middle, 0);
        fdName.top = new FormAttachment(0,  margin);
        fdName.right = new FormAttachment(100, 0);
        wName.setLayoutData(fdName);
        
        // SQL from file?
        wlSQLFromFile = new Label(shell, SWT.RIGHT);
        wlSQLFromFile.setText(BaseMessages.getString(PKG, "JobSQL.SQLFromFile.Label"));
        props.setLook(wlSQLFromFile);
        fdlSQLFromFile = new FormData();
        fdlSQLFromFile.left = new FormAttachment(0, 0);
        fdlSQLFromFile.top = new FormAttachment(wlName,2*margin);
        fdlSQLFromFile.right = new FormAttachment(middle, -margin);
        wlSQLFromFile.setLayoutData(fdlSQLFromFile);
        wSQLFromFile = new Button(shell, SWT.CHECK);
        props.setLook(wSQLFromFile);
        wSQLFromFile.setToolTipText(BaseMessages.getString(PKG, "JobSQL.SQLFromFile.Tooltip"));
        fdSQLFromFile = new FormData();
        fdSQLFromFile.left = new FormAttachment(middle, 0);
        fdSQLFromFile.top = new FormAttachment(wlName, 2*margin);
        fdSQLFromFile.right = new FormAttachment(100, 0);
        wSQLFromFile.setLayoutData(fdSQLFromFile);
        wSQLFromFile.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
            	activeSQLFromFile();	
            	input.setChanged();
            }
        });


		// Filename line
		wlFilename = new Label(shell, SWT.RIGHT);
		wlFilename.setText(BaseMessages.getString(PKG, "JobSQL.Filename.Label"));
		props.setLook(wlFilename);
		fdlFilename = new FormData();
		fdlFilename.left = new FormAttachment(0, 0);
		fdlFilename.top = new FormAttachment(wSQLFromFile,  margin);
		fdlFilename.right = new FormAttachment(middle, -margin);
		wlFilename.setLayoutData(fdlFilename);

		wbFilename = new Button(shell, SWT.PUSH | SWT.CENTER);
		props.setLook(wbFilename);
		wbFilename.setText(BaseMessages.getString(PKG, "System.Button.Browse"));
		fdbFilename = new FormData();
		fdbFilename.right = new FormAttachment(100, 0);
		fdbFilename.top = new FormAttachment(wSQLFromFile, margin);
		wbFilename.setLayoutData(fdbFilename);

		wFilename = new TextVar(transMeta, shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		props.setLook(wFilename);
		wFilename.setToolTipText(BaseMessages.getString(PKG, "JobSQL.Filename.Tooltip"));
		wFilename.addModifyListener(lsMod);
		fdFilename = new FormData();
		fdFilename.left = new FormAttachment(middle, 0);
		fdFilename.top = new FormAttachment(wSQLFromFile,  margin);
		fdFilename.right = new FormAttachment(wbFilename, -margin);
		wFilename.setLayoutData(fdFilename);

		// Whenever something changes, set the tooltip to the expanded version:
		wFilename.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				wFilename.setToolTipText(transMeta.environmentSubstitute(wFilename.getText()));
			}
		});

		wbFilename.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
				FileDialog dialog = new FileDialog(shell, SWT.OPEN);
				dialog.setFilterExtensions(new String[] { "*" });
				if (wFilename.getText() != null)
				{
					dialog.setFileName(transMeta.environmentSubstitute(wFilename.getText()));
				}
				dialog.setFilterNames(FILETYPES);
				if (dialog.open() != null)
				{
					wFilename.setText(dialog.getFilterPath() + Const.FILE_SEPARATOR
						+ dialog.getFileName());
				}
			}
		});
		
		// Filename line out
		wlOutFilename = new Label(shell, SWT.RIGHT);
		wlOutFilename.setText("文件输出");
		props.setLook(wlOutFilename);
		fdlOutFilename = new FormData();
		fdlOutFilename.left = new FormAttachment(0, 0);
		fdlOutFilename.top = new FormAttachment(wFilename,  margin);
		fdlOutFilename.right = new FormAttachment(middle, -margin);
		wlOutFilename.setLayoutData(fdlOutFilename);

		wbOutFilename = new Button(shell, SWT.PUSH | SWT.CENTER);
		props.setLook(wbOutFilename);
		wbOutFilename.setText(BaseMessages.getString(PKG, "System.Button.Browse"));
		fdbOutFilename = new FormData();
		fdbOutFilename.right = new FormAttachment(100, 0);
		fdbOutFilename.top = new FormAttachment(wFilename, margin);
		wbOutFilename.setLayoutData(fdbOutFilename);

		wOutFilename = new TextVar(transMeta, shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		props.setLook(wOutFilename);
		wOutFilename.setToolTipText("哈哈");
		wOutFilename.addModifyListener(lsMod);
		fdFilename = new FormData();
		fdFilename.left = new FormAttachment(middle, 0);
		fdFilename.top = new FormAttachment(wFilename,  margin);
		fdFilename.right = new FormAttachment(wbOutFilename, -margin);
		wOutFilename.setLayoutData(fdFilename);

		// Whenever something changes, set the tooltip to the expanded version:
		wOutFilename.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				wOutFilename.setToolTipText(transMeta.environmentSubstitute(wOutFilename.getText()));
			}
		});

		wbOutFilename.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
				FileDialog dialog = new FileDialog(shell, SWT.OPEN);
				dialog.setFilterExtensions(new String[] { "*" });
				if (wOutFilename.getText() != null)
				{
					dialog.setFileName(transMeta.environmentSubstitute(wOutFilename.getText()));
				}
				dialog.setFilterNames(FILETYPES);
				if (dialog.open() != null)
				{
					wOutFilename.setText(dialog.getFilterPath() + Const.FILE_SEPARATOR
						+ dialog.getFileName());
				}
			}
		});
		
        // Use variable substitution?
        wlUseSubs = new Label(shell, SWT.RIGHT);
        wlUseSubs.setText(BaseMessages.getString(PKG, "JobSQL.UseVariableSubst.Label"));
        props.setLook(wlUseSubs);
        fdlUseSubs = new FormData();
        fdlUseSubs.left = new FormAttachment(0, 0);
        fdlUseSubs.top = new FormAttachment(wbOutFilename, margin);
        fdlUseSubs.right = new FormAttachment(middle, -margin);
        wlUseSubs.setLayoutData(fdlUseSubs);
        wUseSubs = new Button(shell, SWT.CHECK);
        props.setLook(wUseSubs);
        wUseSubs.setToolTipText(BaseMessages.getString(PKG, "JobSQL.UseVariableSubst.Tooltip"));
        fdUseSubs = new FormData();
        fdUseSubs.left = new FormAttachment(middle, 0);
        fdUseSubs.top = new FormAttachment(wbOutFilename, margin);
        fdUseSubs.right = new FormAttachment(100, 0);
        wUseSubs.setLayoutData(fdUseSubs);
        wUseSubs.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
//                input.setUseVariableSubstitution(!input.getUseVariableSubstitution());
                input.setChanged();
            }
        });

        wlPosition = new Label(shell, SWT.NONE);
        wlPosition.setText(BaseMessages.getString(PKG, "JobSQL.LineNr.Label", "0"));
        props.setLook(wlPosition);
        fdlPosition = new FormData();
        fdlPosition.left = new FormAttachment(0, 0);
        fdlPosition.right= new FormAttachment(100, 0);
        fdlPosition.bottom = new FormAttachment(wOK, -margin);
        wlPosition.setLayoutData(fdlPosition);

        // Script line
        wlSQL = new Label(shell, SWT.NONE);
        wlSQL.setText(BaseMessages.getString(PKG, "JobSQL.Script.Label"));
        props.setLook(wlSQL);
        fdlSQL = new FormData();
        fdlSQL.left = new FormAttachment(0, 0);
        fdlSQL.top = new FormAttachment(wUseSubs, margin);
        wlSQL.setLayoutData(fdlSQL);

        wSQL=new StyledTextComp(transMeta, shell, SWT.MULTI | SWT.LEFT | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL, "");
        props.setLook(wSQL, Props.WIDGET_STYLE_FIXED);
        wSQL.addModifyListener(lsMod);
        fdSQL = new FormData();
        fdSQL.left = new FormAttachment(0, 0);
        fdSQL.top = new FormAttachment(wlSQL, margin);
        fdSQL.right = new FormAttachment(100, -10);
        fdSQL.bottom = new FormAttachment(wlPosition, -margin);
        wSQL.setLayoutData(fdSQL);

        // Add listeners
        lsCancel = new Listener()
        {
            public void handleEvent(Event e)
            {
                cancel();
            }
        };
        lsOK = new Listener()
        {
            public void handleEvent(Event e)
            {
                ok();
            }
        };

        wCancel.addListener(SWT.Selection, lsCancel);
        wOK.addListener(SWT.Selection, lsOK);

        lsDef = new SelectionAdapter()
        {
            public void widgetDefaultSelected(SelectionEvent e)
            {
                ok();
            }
        };

        wName.addSelectionListener(lsDef);

        // Detect X or ALT-F4 or something that kills this window...
        shell.addShellListener(new ShellAdapter()
        {
            public void shellClosed(ShellEvent e)
            {
                cancel();
            }
        });

        wSQL.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent arg0)
            {
                setPosition();
            }

	        }
	    );
		
		wSQL.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e) { setPosition(); }
			public void keyReleased(KeyEvent e) { setPosition(); }
			} 
		);
		wSQL.addFocusListener(new FocusAdapter(){
			public void focusGained(FocusEvent e) { setPosition(); }
			public void focusLost(FocusEvent e) { setPosition(); }
			}
		);
		wSQL.addMouseListener(new MouseAdapter(){
			public void mouseDoubleClick(MouseEvent e) { setPosition(); }
			public void mouseDown(MouseEvent e) { setPosition(); }
			public void mouseUp(MouseEvent e) { setPosition(); }
			}
		);
		wSQL.addModifyListener(lsMod);
		
		
		// Text Higlighting
//		wSQL.addLineStyleListener(new SQLValuesHighlight());
		

        getData();
        activeSQLFromFile();

        BaseStepDialog.setSize(shell);

        shell.open();
        props.setDialogSize(shell, "JobSQLDialogSize");
        while (!shell.isDisposed())
        {
            if (!display.readAndDispatch())
                display.sleep();
        }
        return stepname;
    }

	public void setPosition(){
		
		String scr = wSQL.getText();
		int linenr = wSQL.getLineAtOffset(wSQL.getCaretOffset())+1;
		int posnr  = wSQL.getCaretOffset();
				
		// Go back from position to last CR: how many positions?
		int colnr=0;
		while (posnr>0 && scr.charAt(posnr-1)!='\n' && scr.charAt(posnr-1)!='\r')
		{
			posnr--;
			colnr++;
		}
		wlPosition.setText(BaseMessages.getString(PKG, "JobSQL.Position.Label",""+linenr,""+colnr));

	}
    public void dispose()
    {
        WindowProperty winprop = new WindowProperty(shell);
        props.setScreen(winprop);
        shell.dispose();
    }

    /**
     * Copy information from the meta-data input to the dialog fields.
     */
    public void getData()
    {
        if (stepname != null)
            wName.setText(stepname);
        if (input.getSql() != null)
            wSQL.setText(input.getSql());

        wUseSubs.setSelection(input.isUseVariableSubstitution());
        wSQLFromFile.setSelection(input.isSqlfromfile());
        
        if (input.getSqlfilename() != null)
        	wFilename.setText(input.getSqlfilename());
        
        if (input.getOutfilename() != null)
        	wOutFilename.setText(input.getOutfilename());
        
        
        
        wName.selectAll();
    }
    private void activeSQLFromFile()
    {
    	wlFilename.setEnabled(wSQLFromFile.getSelection());
    	wFilename.setEnabled(wSQLFromFile.getSelection());
    	wbFilename.setEnabled(wSQLFromFile.getSelection());
    	wSQL.setEnabled(!wSQLFromFile.getSelection());
    	wlSQL.setEnabled(!wSQLFromFile.getSelection());
    	wlPosition.setEnabled(!wSQLFromFile.getSelection());
    }

    private void cancel()
    {
        input.setChanged(changed);
        input = null;
        dispose();
    }

    private void ok()
    {
 	   if(Const.isEmpty(wName.getText())) 
      {
			MessageBox mb = new MessageBox(shell, SWT.OK | SWT.ICON_ERROR );
			mb.setText(BaseMessages.getString(PKG, "System.StepJobEntryNameMissing.Title"));
			mb.setMessage(BaseMessages.getString(PKG, "System.JobEntryNameMissing.Msg"));
			mb.open(); 
			return;
      }
 	   	stepname = wName.getText();
        input.setSql(wSQL.getText());
        input.setUseVariableSubstitution(wUseSubs.getSelection());
        input.setSqlfromfile(wSQLFromFile.getSelection());
        input.setSqlfilename(wFilename.getText());
        input.setOutfilename(wOutFilename.getText());
        dispose();
    }
}
