
package com.hotent.wsclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>SendSMSResult complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="SendSMSResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="appAuth" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="returnValue" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="sendSMSID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SendSMSResult", propOrder = {
    "appAuth",
    "returnValue",
    "sendSMSID"
})
public class SendSMSResult {

    protected int appAuth;
    protected boolean returnValue;
    protected String sendSMSID;

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
     * ��ȡsendSMSID���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSendSMSID() {
        return sendSMSID;
    }

    /**
     * ����sendSMSID���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSendSMSID(String value) {
        this.sendSMSID = value;
    }

}
