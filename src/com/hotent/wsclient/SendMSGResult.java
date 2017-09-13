
package com.hotent.wsclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>SendMSGResult complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�����ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="SendMSGResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="appAuth" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="returnValue" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="returnType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SmsResult" type="{http://iamsweb.gmcc.net/WS/}SendSMSResult"/>
 *         &lt;element name="LcsResult" type="{http://tempuri.org/}SendResult" minOccurs="0"/>
 *         &lt;element name="EmailResult" type="{http://iamsweb.gmcc.net/WS/}ActionResult"/>
 *         &lt;element name="returnMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SendMSGResult", propOrder = {
    "appAuth",
    "returnValue",
    "returnType",
    "smsResult",
    "lcsResult",
    "emailResult",
    "returnMessage"
})
public class SendMSGResult {

    protected int appAuth;
    protected boolean returnValue;
    protected String returnType;
    @XmlElement(name = "SmsResult", required = true)
    protected SendSMSResult smsResult;
    @XmlElement(name = "LcsResult")
    protected SendResult lcsResult;
    @XmlElement(name = "EmailResult", required = true)
    protected ActionResult emailResult;
    protected String returnMessage;

    /**
     * ��ȡappAuth���Ե�ֵ��
     * 
     */
    public int getAppAuth() {
        return appAuth;
    }

    /**
     * ����appAuth���Ե�ֵ��
     * 
     */
    public void setAppAuth(int value) {
        this.appAuth = value;
    }

    /**
     * ��ȡreturnValue���Ե�ֵ��
     * 
     */
    public boolean isReturnValue() {
        return returnValue;
    }

    /**
     * ����returnValue���Ե�ֵ��
     * 
     */
    public void setReturnValue(boolean value) {
        this.returnValue = value;
    }

    /**
     * ��ȡreturnType���Ե�ֵ��
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
     * ����returnType���Ե�ֵ��
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
     * ��ȡsmsResult���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link SendSMSResult }
     *     
     */
    public SendSMSResult getSmsResult() {
        return smsResult;
    }

    /**
     * ����smsResult���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link SendSMSResult }
     *     
     */
    public void setSmsResult(SendSMSResult value) {
        this.smsResult = value;
    }

    /**
     * ��ȡlcsResult���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link SendResult }
     *     
     */
    public SendResult getLcsResult() {
        return lcsResult;
    }

    /**
     * ����lcsResult���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link SendResult }
     *     
     */
    public void setLcsResult(SendResult value) {
        this.lcsResult = value;
    }

    /**
     * ��ȡemailResult���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link ActionResult }
     *     
     */
    public ActionResult getEmailResult() {
        return emailResult;
    }

    /**
     * ����emailResult���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link ActionResult }
     *     
     */
    public void setEmailResult(ActionResult value) {
        this.emailResult = value;
    }

    /**
     * ��ȡreturnMessage���Ե�ֵ��
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
     * ����returnMessage���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReturnMessage(String value) {
        this.returnMessage = value;
    }

}
