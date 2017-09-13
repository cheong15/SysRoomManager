
package com.hotent.wsclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>ReceiveMSGResult complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
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

    /**
     * ��ȡreceiveSmsResult���Ե�ֵ��
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
     * ����receiveSmsResult���Ե�ֵ��
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
     * ��ȡreceiveKWSmsResult���Ե�ֵ��
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
     * ����receiveKWSmsResult���Ե�ֵ��
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
     * ��ȡreceiveLcsResult���Ե�ֵ��
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
     * ����receiveLcsResult���Ե�ֵ��
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
