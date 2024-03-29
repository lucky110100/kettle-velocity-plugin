package plugin.template;

import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Shell;
import org.pentaho.di.core.CheckResult;
import org.pentaho.di.core.CheckResultInterface;
import org.pentaho.di.core.Counter;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleDatabaseException;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.w3c.dom.Node;

/**
 * 对话框类 
 * @Author      : http://www.ahuoo.com, ydh
 * @Date        : Jan 7, 2015
 * @Email       : ydh110100@163.com
 * @Version     : V1.0
 */
public class TemplateStepMeta extends BaseStepMeta implements StepMetaInterface {

	private static Class<?> PKG = TemplateStepMeta.class; // for i18n purposes
	
	private String sql;
	private boolean useVariableSubstitution = false;
	private boolean sqlfromfile=false;
	private String sqlfilename;
	private String outfilename;

	public TemplateStepMeta() {
		super(); 
	}
	
	public Object clone() {
		Object retval = super.clone();
		return retval;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public boolean isUseVariableSubstitution() {
		return useVariableSubstitution;
	}

	public void setUseVariableSubstitution(boolean useVariableSubstitution) {
		this.useVariableSubstitution = useVariableSubstitution;
	}

	public boolean isSqlfromfile() {
		return sqlfromfile;
	}

	public void setSqlfromfile(boolean sqlfromfile) {
		this.sqlfromfile = sqlfromfile;
	}

	public String getSqlfilename() {
		return sqlfilename;
	}

	public void setSqlfilename(String sqlfilename) {
		this.sqlfilename = sqlfilename;
	}

	public String getOutfilename() {
		return outfilename;
	}

	public void setOutfilename(String outfilename) {
		this.outfilename = outfilename;
	}

	@Override
	public void setDefault() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getXML() throws KettleException {
		
		StringBuffer retval = new StringBuffer(200);
		retval.append("      ").append(XMLHandler.addTagValue("sql",      sql));
		retval.append("      ").append(XMLHandler.addTagValue("useVariableSubstitution", useVariableSubstitution ? "T" : "F"));
		retval.append("      ").append(XMLHandler.addTagValue("sqlfromfile", sqlfromfile ? "T" : "F"));
		retval.append("      ").append(XMLHandler.addTagValue("sqlfilename",      sqlfilename));
		retval.append("      ").append(XMLHandler.addTagValue("outfilename",      outfilename));
		return retval.toString();
		
	}
	
	@Override
	public void loadXML(Node stepnode, List<DatabaseMeta> databases,
			Map<String, Counter> counters) throws KettleXMLException {
		
		sql           = XMLHandler.getTagValue(stepnode, "sql");
		String sSubs  = XMLHandler.getTagValue(stepnode, "useVariableSubstitution");

		if (sSubs != null && sSubs.equalsIgnoreCase("T"))
			useVariableSubstitution = true;
		
		String ssql  = XMLHandler.getTagValue(stepnode, "sqlfromfile");
		if (ssql != null && ssql.equalsIgnoreCase("T"))
			sqlfromfile = true;
		
		sqlfilename    = XMLHandler.getTagValue(stepnode, "sqlfilename");
		outfilename    = XMLHandler.getTagValue(stepnode, "outfilename");
		
	}

	@Override
	public void saveRep(Repository rep, ObjectId id_transformation,
			ObjectId id_step) throws KettleException {
		try
		{
			rep.saveJobEntryAttribute(id_transformation, getObjectId(), "sql", sql);
			rep.saveJobEntryAttribute(id_transformation, getObjectId(), "useVariableSubstitution", useVariableSubstitution ? "T" : "F" );
			rep.saveJobEntryAttribute(id_transformation, getObjectId(), "sqlfromfile", sqlfromfile ? "T" : "F" );
			rep.saveJobEntryAttribute(id_transformation, getObjectId(), "sqlfilename", sqlfilename);
			rep.saveJobEntryAttribute(id_transformation, getObjectId(), "outfilename", outfilename);
		}
		catch(KettleDatabaseException dbe)
		{
			throw new KettleException("Unable to save job entry of type 'sql' to the repository for id_job="+id_transformation, dbe);
		}
	}

	@Override
	public void readRep(Repository rep, ObjectId id_step,
			List<DatabaseMeta> databases, Map<String, Counter> counters)
			throws KettleException {
		try
		{
			sql = rep.getJobEntryAttributeString(id_step, "sql");
			String sSubs = rep.getJobEntryAttributeString(id_step, "useVariableSubstitution");
			if (sSubs != null && sSubs.equalsIgnoreCase("T"))
				useVariableSubstitution = true;
			
			String ssql = rep.getJobEntryAttributeString(id_step, "sqlfromfile");
			if (ssql != null && ssql.equalsIgnoreCase("T"))
				sqlfromfile = true;
			
			sqlfilename = rep.getJobEntryAttributeString(id_step, "sqlfilename");
			outfilename = rep.getJobEntryAttributeString(id_step, "outfilename");
			
		}
		catch(KettleDatabaseException dbe)
		{
			throw new KettleException("Unable to load job entry of type 'sql' from the repository with id_jobentry="+id_step, dbe);
		}
		
	}

	@Override
	public void check(List<CheckResultInterface> remarks, TransMeta transMeta,
			StepMeta stepMeta, RowMetaInterface prev, String[] input,
			String[] output, RowMetaInterface info) {
		
		CheckResult cr;
		// See if we have input streams leading to this step!
		if (input.length > 0) {
			cr = new CheckResult(CheckResult.TYPE_RESULT_OK, "Step is receiving info from other steps.", stepMeta);
			remarks.add(cr);
		} else {
			cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, "No input received from other steps!", stepMeta);
			remarks.add(cr);
		}	
		
	}

	@Override
	public StepInterface getStep(StepMeta stepMeta,
			StepDataInterface stepDataInterface, int copyNr,
			TransMeta transMeta, Trans trans) {
		return new TemplateStep(stepMeta, stepDataInterface, copyNr, transMeta, trans);
	}

	@Override
	public StepDataInterface getStepData() {
		return new TemplateStepData();
	}
	
	public StepDialogInterface getDialog(Shell shell, StepMetaInterface meta, TransMeta transMeta, String name) {
		return new TemplateStepDialog(shell, meta, transMeta, name);
	}

}
