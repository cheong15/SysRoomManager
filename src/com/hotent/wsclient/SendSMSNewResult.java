
package com.hotent.wsclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>SendSMSNewResult complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
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
     * ��ȡreturnMsg���Ե�ֵ��
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
     * ����returnMsg���Ե�ֵ��
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
     * ��ȡsendSMSInfo���Ե�ֵ��
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
     * ����sendSMSInfo���Ե�ֵ��
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
