
package com.hotent.wsclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>SendSMSNewResult complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="SendSMSNewResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="appAuth" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="returnValue" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="returnMsg" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sendSMSInfo" type="{http://iamsweb.gmcc.net/WS/}ArrayOfSendSMSInfo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SendSMSNewResult", propOrder = {
    "appAuth",
    "returnValue",
    "returnMsg",
    "sendSMSInfo"
})
public class SendSMSNewResult {

    protected int appAuth;
    protected boolean returnValue;
    protected String returnMsg;
    protected ArrayOfSendSMSInfo sendSMSInfo;

    /**
     * 获取appAuth属性的值。
     * 
     */
    public int getAppAuth() {
        return appAuth;
    }

    /**
     * 设置appAuth属性的值。
     * 
     */
    public void setAppAuth(int value) {
        this.appAuth = value;
    }

    /**
     * 获取returnValue属性的值。
     * 
     */
    public boolean isReturnValue() {
        return returnValue;
    }

    /**
     * 设置returnValue属性的值。
     * 
     */
    public void setReturnValue(boolean value) {
        this.returnValue = value;
    }

    /**
     * 获取returnMsg属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReturnMsg() {
        return returnMsg;
    }

    /**
     * 设置returnMsg属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReturnMsg(String value) {
        this.returnMsg = value;
    }

    /**
     * 获取sendSMSInfo属性的值。
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfSendSMSInfo }
     *     
     */
    public ArrayOfSendSMSInfo getSendSMSInfo() {
        return sendSMSInfo;
    }

    /**
     * 设置sendSMSInfo属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfSendSMSInfo }
     *     
     */
    public void setSendSMSInfo(ArrayOfSendSMSInfo value) {
        this.sendSMSInfo = value;
    }

}
