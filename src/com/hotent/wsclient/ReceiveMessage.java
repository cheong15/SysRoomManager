
package com.hotent.wsclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="strSysID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="strSysAccount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="strSysPassword" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="smsparm" type="{http://iamsweb.gmcc.net/WS/}ReceiveSmsParms" minOccurs="0"/>
 *         &lt;element name="lcsparm" type="{http://iamsweb.gmcc.net/WS/}ReceiveLcsParms" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "strSysID",
    "strSysAccount",
    "strSysPassword",
    "smsparm",
    "lcsparm"
})
@XmlRootElement(name = "ReceiveMessage")
public class ReceiveMessage {

    protected String strSysID;
    protected String strSysAccount;
    protected String strSysPassword;
    protected ReceiveSmsParms smsparm;
    protected ReceiveLcsParms lcsparm;

    /**
     * ��ȡstrSysID���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrSysID() {
        return strSysID;
    }

    /**
     * ����strSysID���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrSysID(String value) {
        this.strSysID = value;
    }

    /**
     * ��ȡstrSysAccount���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrSysAccount() {
        return strSysAccount;
    }

    /**
     * ����strSysAccount���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrSysAccount(String value) {
        this.strSysAccount = value;
    }

    /**
     * ��ȡstrSysPassword���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrSysPassword() {
        return strSysPassword;
    }

    /**
     * ����strSysPassword���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrSysPassword(String value) {
        this.strSysPassword = value;
    }

    /**
     * ��ȡsmsparm���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link ReceiveSmsParms }
     *     
     */
    public ReceiveSmsParms getSmsparm() {
        return smsparm;
    }

    /**
     * ����smsparm���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link ReceiveSmsParms }
     *     
     */
    public void setSmsparm(ReceiveSmsParms value) {
        this.smsparm = value;
    }

    /**
     * ��ȡlcsparm���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link ReceiveLcsParms }
     *     
     */
    public ReceiveLcsParms getLcsparm() {
        return lcsparm;
    }

    /**
     * ����lcsparm���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link ReceiveLcsParms }
     *     
     */
    public void setLcsparm(ReceiveLcsParms value) {
        this.lcsparm = value;
    }

}
