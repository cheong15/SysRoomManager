package net.gmcc.gz.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;


/**
 * <pre>
 * 对象功能:TODO
 * 开发公司:亚信科技（中国）有限公司
 * 开发人员:Long
 * 创建时间:2017-9-6 下午5:46:58
 * </pre>
 */
public class CustomDateSerializer extends JsonSerializer<Date> {  
    @Override  
    public void serialize(Date value, JsonGenerator jsonGenerator,  
            SerializerProvider provider) throws IOException,  
            JsonProcessingException {  
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        jsonGenerator.writeString(sdf.format(value));  
    }  

}
