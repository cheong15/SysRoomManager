
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
 *         &lt;element name="systemid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sysAccount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sysPassword" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="strSmsID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="strSubType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "systemid",
    "sysAccount",
    "sysPassword",
    "strSmsID",
    "strSubType"
})
@XmlRootElement(name = "GetSmsStatus")
public class GetSmsStatus {

    protected String systemid;
    protected String sysAccount;
    protected String sysPassword;
    protected String strSmsID;
    protected String strSubType;

    /**
     * ��ȡsystemid���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSystemid() {
        return systemid;
    }

    /**
     * ����systemid���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSystemid(String value) {
        this.systemid = value;
    }

    /**
     * ��ȡsysAccount���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSysAccount() {
        return sysAccount;
    }

    /**
     * ����sysAccount���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSysAccount(String value) {
        this.sysAccount = value;
    }

    /**
     * ��ȡsysPassword���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSysPassword() {
        return sysPassword;
    }

    /**
     * ����sysPassword���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSysPassword(String value) {
        this.sysPassword = value;
    }

    /**
     * ��ȡstrSmsID���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrSmsID() {
        return strSmsID;
    }

    /**
     * ����strSmsID���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrSmsID(String value) {
        this.strSmsID = value;
    }

    /**
     * ��ȡstrSubType���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrSubType() {
        return strSubType;
    }

    /**
     * ����strSubType���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrSubType(String value) {
        this.strSubType = value;
    }

}
