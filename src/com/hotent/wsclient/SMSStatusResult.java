
package com.hotent.wsclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>SMSStatusResult complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="SMSStatusResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="appAuth" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="SendStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ReceiveStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SMSStatusResult", propOrder = {
    "appAuth",
    "sendStatus",
    "receiveStatus"
})
public class SMSStatusResult {

    protected int appAuth;
    @XmlElement(name = "SendStatus")
    protected String sendStatus;
    @XmlElement(name = "ReceiveStatus")
    protected String receiveStatus;

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
     * ��ȡsendStatus���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSendStatus() {
        return sendStatus;
    }

    /**
     * ����sendStatus���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSendStatus(String value) {
        this.sendStatus = value;
    }

    /**
     * ��ȡreceiveStatus���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReceiveStatus() {
        return receiveStatus;
    }

    /**
     * ����receiveStatus���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReceiveStatus(String value) {
        this.receiveStatus = value;
    }

}
