package plugin.template;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.vfs.FileObject;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowDataUtil;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.vfs.KettleVFS;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStep;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;

/**
 * 步骤类
 * @Author      : http://www.ahuoo.com, ydh
 * @Date        : Jan 7, 2015
 * @Email       : ydh110100@163.com
 * @Version     : V1.0
 */
public class TemplateStep extends BaseStep implements StepInterface {

	private TemplateStepData data;
	private TemplateStepMeta meta;
	private ArrayList<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
	
	public TemplateStep(StepMeta s, StepDataInterface stepDataInterface, int c, TransMeta t, Trans dis) {
		super(s, stepDataInterface, c, t, dis);
	}

	public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws KettleException {
		meta = (TemplateStepMeta) smi;
		data = (TemplateStepData) sdi;

		Object[] r = getRow(); // get row, blocks when needed!
		if (r == null) // no more input to be expected...
		{
			setOutputDone();
			return false;
		}

		if (first) {
			first = false;

			data.outputRowMeta = (RowMetaInterface) getInputRowMeta().clone();
			meta.getFields(data.outputRowMeta, getStepname(), null, null, this);

			logBasic("template step initialized successfully");

		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		for (int c = 0; c < data.outputRowMeta.size(); c++) {
		      ValueMetaInterface v = data.outputRowMeta.getValueMeta(c);
		      String show;
		      show = v.getString(r[c]);
		      map.put(v.getName(), show);
		}
		rows.add(map);
		
		Object[] outputRow = RowDataUtil.addValueData(r, data.outputRowMeta.size() - 1, "dummy value");

		putRow(data.outputRowMeta, outputRow); // copy row to possible alternate rowset(s)

		if (checkFeedback(getLinesRead())) {
			logBasic("Linenr " + getLinesRead()); // Some basic logging
		}

		return true;
	}

	public boolean init(StepMetaInterface smi, StepDataInterface sdi) {
		meta = (TemplateStepMeta) smi;
		data = (TemplateStepData) sdi;

		return super.init(smi, sdi);
	}

	public void dispose(StepMetaInterface smi, StepDataInterface sdi) {
		meta = (TemplateStepMeta) smi;
		data = (TemplateStepData) sdi;
		
		try {
			Map<String, Object> context = new HashMap<String, Object>();
			context.put("rows",rows);
			if(meta.isUseVariableSubstitution()){
				//将kettle的变量加入模板变量中
				context.putAll(getVariables());
			}
			//替换环境变量
			String filename=environmentSubstitute(meta.getOutfilename());
			FileObject file = KettleVFS.getFileObject(filename, getTransMeta());
			
			if(!meta.isSqlfromfile()){
				VelocityUtils.mergeStrToFile(meta.getSql(), context, file.getURL().getFile(), false);
			}else{
				VelocityUtils.mergeToFile(meta.getSqlfilename(), context, file.getURL().getFile(), false);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.dispose(smi, sdi);
	}


}
