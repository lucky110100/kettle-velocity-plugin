package plugin.template;

import java.io.File;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.tools.generic.DateTool;
import org.apache.velocity.tools.generic.MathTool;

/**
 * Velocity工具类
 * @Author      : ydh
 * @Date        : Jan 7, 2015
 * @Email       : ydh110100@163.com
 * @Version     : V1.0
 */
public class VelocityUtils {

	/**
	 * 合并模版,返回合并后的字符串
	 * @param vmRoot 指定模版根目录
	 * @param vmPath 模版路径,如果指定了根目录则此处填相对路径,否则填绝对路径
	 * @param map	填充的内容
	 * @return
	 * @throws Exception
	 */
	public static String mergeToString(String vmRoot, String vmPath, Map map) throws Exception{
		 String result = "";
		 VelocityEngine engine = new VelocityEngine();
		 Properties properties = new Properties();
		 properties.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
		 properties.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
		 properties.setProperty(Velocity.PARSE_DIRECTIVE_MAXDEPTH, "8");
		 properties.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, vmRoot);
		 engine.init(properties);
		 
		 // 封装几个默认值
		 map.put("date", new Date());
		 
		 VelocityContext context = new VelocityContext(map);
		 File directory = new File("");//
		 String path = directory.getAbsolutePath();
//		 String configFile = path + File.separator + "config" + File.separator + "mssql2java.properties";
//		 context.put("stringTool", new StringUtilsExt());
		 context.put("dateTool", new DateTool());
		 context.put("mathTool", new MathTool());
		 
		 Template template = engine.getTemplate(vmPath,"UTF-8");
		 StringWriter writer = new StringWriter();
		 template.merge(context, writer);
		 result = writer.toString();
		 
		 return result;
	}
	public static String mergeToString(String vm, Map map) throws Exception{
		 String result = "";
		 VelocityEngine engine = new VelocityEngine();
		 Properties properties = new Properties();
		 properties.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
		 properties.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
		 properties.setProperty(Velocity.PARSE_DIRECTIVE_MAXDEPTH, "8");
//		 properties.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, vmRoot);
		 engine.init(properties);
		 
		 // 封装几个默认值
		 map.put("date", new Date());
		 
		 VelocityContext context = new VelocityContext(map);
//		 context.put("stringTool", new StringUtilsExt());
		 context.put("dateTool", new DateTool());
		 context.put("mathTool", new MathTool());
		 
		 StringWriter writer = new StringWriter();
		 engine.evaluate(context, writer, "", vm);
		 result = writer.toString();
		 
		 return result;
	}
	
	/**
	 * 合并模版,并直接输出成文件
	 * @param vmPath 模版路径
	 * @param context 填充的内容
	 * @param filePath 生成文件路径
	 * @throws Exception
	 */
	public static void mergeToFile(String vmPath, Map context, String filePath, boolean append) throws Exception{
		String result = mergeToString("", vmPath, context);
       File outFile = new File(filePath);
       FileUtils.writeStringToFile ( outFile, result, "UTF-8" , append );
       System.out.println(filePath + "\t---->\tOK!\n");
	}
	
	public static void mergeStrToFile(String vm, Map context, String filePath, boolean append) throws Exception{
		String result = mergeToString(vm, context);
       File outFile = new File(filePath);
       FileUtils.writeStringToFile ( outFile, result, "UTF-8" , append );
       System.out.println(filePath + "\t---->\tOK!\n");
	}
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		// TODO Auto-generated method stub
		Map map = new HashMap();
		map.put("say", "Hello,world!");
		VelocityUtils va = new VelocityUtils();
		va.mergeToFile("D:\\test\\haha.vm", map, "D:\\hi.txt", false);
	}

}
