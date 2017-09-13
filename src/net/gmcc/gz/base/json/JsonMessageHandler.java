package net.gmcc.gz.base.json;

import com.hotent.core.page.PageBean;
import net.gmcc.gz.base.json.JsonRequest.JsonRequest;
import net.gmcc.gz.base.json.JsonRequest.Requestbody;
import net.gmcc.gz.base.json.JsonResponse.JsonResponse;
import net.gmcc.gz.base.json.JsonResponse.ResponseHeader;
import net.gmcc.gz.base.json.JsonResponse.Responsebody;
import net.gmcc.gz.base.json.PageInfo;
import net.gmcc.gz.base.json.RequestBodyExt;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * <pre>
 * 对象功能:通用JsonRequest、JsonResponse handler
 * 开发公司:亚信科技（中国）有限公司
 * 开发人员:Liangyy
 * 创建时间:2017-09-10 01:31:55
 * </pre>
 */
@Aspect
@Component
public class JsonMessageHandler {

    @Pointcut("execution (* net.gmcc.gz..*(..))")
    public void jsonMessageHadlerPointcut() {}

    @Around("jsonMessageHadlerPointcut()")
    public Object jsonMessageHadler(ProceedingJoinPoint pjp) throws Throwable {
        Object retVal = null;  //连接点方法返回值
        //获取将要执行的方法名称
        String methodName = pjp.getSignature().getName();
        //获取执行方法的参数
        Object[] args = pjp.getArgs();

        //获取调用的目录类
        Class<?> classTarget = pjp.getTarget().getClass();
        //获取调用方法参数列表
        Class<?>[] parameterTypes = ((MethodSignature) pjp.getSignature()).getParameterTypes();
        //获取调用的方法
        Method method = classTarget.getMethod(methodName, parameterTypes);

        //获取方法参数标注的注解
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        boolean isEnd = false;
        Class<?> requestBodyParam = null;
        /**
         * 查询标注RequestBodyExt注解的方法参数
         */
        for (int i = 0; i < parameterAnnotations.length && !isEnd; i++) {
            for (int j = 0; j < parameterAnnotations[i].length; j++) {
                if(RequestBodyExt.class == parameterAnnotations[i][j].annotationType()) {
                    RequestBodyExt requestBodyExt = (RequestBodyExt) parameterAnnotations[i][j];
                    requestBodyParam = requestBodyExt.value();
                    isEnd = true;
                    break;
                }
            }
        }

        /**
         * 获取request、response
         */
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        ServletWebRequest servletWebRequest = new ServletWebRequest(request);
        HttpServletResponse response = servletWebRequest.getResponse();

        /**
         * 是否JsonRequest请求
         */
        boolean isJsonRequest = false;

        ObjectMapper mapper = new ObjectMapper();
        JsonResponse jsonResponse = null;

        try {
            int index = 0;
            //从参数列表中获取参数对象
            for(Object obj : args) {

                if (obj.getClass() == requestBodyParam) {

                    //获取最后一个泛型，支持Map的情况
                    //List<Class> classList = GenericsUtils.getMethodGenericParameterTypes(method, index);

                    StringBuilder stringBuilder = new StringBuilder();
                    BufferedReader bufferedReader = null;
                    try {
                        InputStream inputStream = request.getInputStream();
                        if (inputStream != null) {
                            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                            char[] charBuffer = new char[128];
                            int bytesRead = -1;
                            while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                                stringBuilder.append(charBuffer, 0, bytesRead);
                            }
                        } else {
                            stringBuilder.append("");
                        }
                    } catch (IOException ex) {
                        throw ex;
                    } finally {
                        if (bufferedReader != null) {
                            try {
                                bufferedReader.close();
                            } catch (IOException ex) {
                                throw ex;
                            }
                        }
                    }

                    String requestJson = stringBuilder.toString();

                    if (requestJson != null && requestJson.indexOf("requestHeader") != -1) {
                        isJsonRequest = true;
                    }

                    /**
                     * 如果JSON串包含requestHeader， 则对其进行解析构建JsonRequest对象信息
                     */
                    if (isJsonRequest) {
                        JsonRequest jsonRequest = mapper.readValue(requestJson, JsonRequest.class);
                        Requestbody requestbody = jsonRequest.getRequestbody();
                        if (requestbody != null) {
                            //设置分页信息
                            PageInfo pageInfo = requestbody.getPageInfo();
                            if (pageInfo != null) {
                                request.setAttribute("pageInfo", pageInfo);
                            }

                            Object requestContent = requestbody.getRequestContent();
                            if (requestContent != null) {
                                //泛型类型的参数默认用Map接收参数，需要转换为JSON字符串，再通过获取请求参数上面的泛型，将JSON串转为泛型类型
                                requestContent = mapper.readValue(mapper.writeValueAsString(requestContent), requestBodyParam);
                                jsonRequest.getRequestbody().setRequestContent(requestContent);
                            }
                        }

                        if (jsonRequest.getRequestbody() != null)
                            args[index] = jsonRequest.getRequestbody().getRequestContent();

                    } else {
                        args[index] = mapper.readValue(requestJson, requestBodyParam);
                    }


                    /*
                    //支持多个请求体
                    //JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, Requestbody.class, classList.get(0));
                    //先转换外层的requestBody
                    List<Requestbody> requestbody = mapper.readValue(mapper.writeValueAsString(jsonRequest.getRequestbody()), new TypeReference<List<Requestbody>>() {});
                    for (Requestbody body : requestbody) {
                        Object requesetCotnent = body.getRequestContent();
                        //泛型类型的参数默认用Map接收参数，需要转换为JSON字符串，再通过获取请求参数上面的泛型，将JSON串转为泛型类型
                        requesetCotnent = mapper.readValue(mapper.writeValueAsString(requesetCotnent), classList.get(0));
                        body.setRequestContent(requesetCotnent);

                    }
                    jsonRequest.setRequestbody(requestbody);
                    */
                }
                index++;
            }
        } catch (IOException e) {
            e.printStackTrace();
            jsonResponse = new JsonResponse();
            //设置错误响应头
            jsonResponse.setResponseHeader(new ResponseHeader("1", "请求失败！"));
            retVal = jsonResponse;
            return retVal;
        }

        try {
            //调用目标方法，并获取返回值
            retVal = pjp.proceed(args);

            //如果是JsonRequest接口请求
            if (isJsonRequest) {
                /**
                 * 以下步骤构建响应内容
                 */
                jsonResponse = new JsonResponse();

                //设置响应头
                ResponseHeader responseHeader = (ResponseHeader) request.getAttribute("responseHeader");
                //如果controller没有设置响应头，默认返回以下信息
                if (responseHeader == null) {
                    responseHeader = new ResponseHeader("0", "请求成功！");
                }
                jsonResponse.setResponseHeader(responseHeader);

                if (retVal != null) {
                    //设置响应体
                    Responsebody responseBody = new Responsebody();
                    PageBean pageBean = (PageBean) request.getAttribute("pageBean");
                    //设置分页信息
                    if (pageBean != null) {
                        PageInfo pageInfo = new PageInfo(pageBean.getCurrentPage(), pageBean.getPageSize(), pageBean.getTotalCount(), pageBean.getTotalPage());
                        responseBody.setPageInfo(pageInfo);
                    }
                    //设置响应内容
                    responseBody.setResponseContent(retVal);
                    jsonResponse.setResponsebody(responseBody);
                }

                //改变返回值类型
                retVal = jsonResponse;
            }

        } catch (Throwable throwable) {
            jsonResponse = new JsonResponse();
            //设置错误响应头
            jsonResponse.setResponseHeader(new ResponseHeader("1", "请求失败！"));
            retVal = jsonResponse;
        }

        return retVal;
    }

}
