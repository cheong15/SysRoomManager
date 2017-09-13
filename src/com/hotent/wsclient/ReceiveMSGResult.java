
package com.hotent.wsclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>ReceiveMSGResult complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="ReceiveMSGResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="appAuth" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="returnValue" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="returnType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="returnMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ReceiveSmsResult" type="{http://iamsweb.gmcc.net/WS/}SMSResult"/>
 *         &lt;element name="ReceiveKWSmsResult" type="{http://iamsweb.gmcc.net/WS/}KWSMSResult"/>
 *         &lt;element name="ReceiveLcsResult" type="{http://tempuri.org/}MessageResult" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReceiveMSGResult", propOrder = {
    "appAuth",
    "returnValue",
    "returnType",
    "returnMessage",
    "receiveSmsResult",
    "receiveKWSmsResult",
    "receiveLcsResult"
})
public class ReceiveMSGResult {

    protected int appAuth;
    protected boolean returnValue;
    protected String returnType;
    protected String returnMessage;
    @XmlElement(name = "ReceiveSmsResult", required = true)
    protected SMSResult receiveSmsResult;
    @XmlElement(name = "ReceiveKWSmsResult", required = true)
    protected KWSMSResult receiveKWSmsResult;
    @XmlElement(name = "ReceiveLcsResult")
    protected MessageResult receiveLcsResult;

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
     * 获取returnType属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReturnType() {
        return returnType;
    }

    /**
     * 设置returnType属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReturnType(String value) {
        this.returnType = value;
    }

    /**
     * 获取returnMessage属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReturnMessage() {
        return returnMessage;
    }

    /**
     * 设置returnMessage属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReturnMessage(String value) {
        this.returnMessage = value;
    }

    /**
     * 获取receiveSmsResult属性的值。
     * 
     * @return
     *     possible object is
     *     {@link SMSResult }
     *     
     */
    public SMSResult getReceiveSmsResult() {
        return receiveSmsResult;
    }

    /**
     * 设置receiveSmsResult属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link SMSResult }
     *     
     */
    public void setReceiveSmsResult(SMSResult value) {
        this.receiveSmsResult = value;
    }

    /**
     * 获取receiveKWSmsResult属性的值。
     * 
     * @return
     *     possible object is
     *     {@link KWSMSResult }
     *     
     */
    public KWSMSResult getReceiveKWSmsResult() {
        return receiveKWSmsResult;
    }

    /**
     * 设置receiveKWSmsResult属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link KWSMSResult }
     *     
     */
    public void setReceiveKWSmsResult(KWSMSResult value) {
        this.receiveKWSmsResult = value;
    }

    /**
     * 获取receiveLcsResult属性的值。
     * 
     * @return
     *     possible object is
     *     {@link MessageResult }
     *     
     */
    public MessageResult getReceiveLcsResult() {
        return receiveLcsResult;
    }

    /**
     * 设置receiveLcsResult属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link MessageResult }
     *     
     */
    public void setReceiveLcsResult(MessageResult value) {
        this.receiveLcsResult = value;
    }

}
