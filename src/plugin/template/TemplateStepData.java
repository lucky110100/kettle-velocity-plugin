package plugin.template;

/**
 * 数据类
 * @Author      : http://www.ahuoo.com, ydh
 * @Date        : Jan 7, 2015
 * @Email       : ydh110100@163.com
 * @Version     : V1.0
 */
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.step.BaseStepData;
import org.pentaho.di.trans.step.StepDataInterface;

public class TemplateStepData extends BaseStepData implements StepDataInterface {

	public RowMetaInterface outputRowMeta;
	
    public TemplateStepData()
	{
		super();
	}
}
	
