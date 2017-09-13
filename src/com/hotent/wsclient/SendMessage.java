
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
 *         &lt;element name="smsparm" type="{http://iamsweb.gmcc.net/WS/}SmsParms" minOccurs="0"/>
 *         &lt;element name="lcsparm" type="{http://iamsweb.gmcc.net/WS/}LcsParms" minOccurs="0"/>
 *         &lt;element name="emailparm" type="{http://iamsweb.gmcc.net/WS/}EmailParms" minOccurs="0"/>
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
    "smsparm",
    "lcsparm",
    "emailparm"
})
@XmlRootElement(name = "SendMessage")
public class SendMessage {

    protected String systemid;
    protected String sysAccount;
    protected String sysPassword;
    protected SmsParms smsparm;
    protected LcsParms lcsparm;
    protected EmailParms emailparm;

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
     * ��ȡsmsparm���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link SmsParms }
     *     
     */
    public SmsParms getSmsparm() {
        return smsparm;
    }

    /**
     * ����smsparm���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link SmsParms }
     *     
     */
    public void setSmsparm(SmsParms value) {
        this.smsparm = value;
    }

    /**
     * ��ȡlcsparm���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link LcsParms }
     *     
     */
    public LcsParms getLcsparm() {
        return lcsparm;
    }

    /**
     * ����lcsparm���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link LcsParms }
     *     
     */
    public void setLcsparm(LcsParms value) {
        this.lcsparm = value;
    }

    /**
     * ��ȡemailparm���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link EmailParms }
     *     
     */
    public EmailParms getEmailparm() {
        return emailparm;
    }

    /**
     * ����emailparm���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link EmailParms }
     *     
     */
    public void setEmailparm(EmailParms value) {
        this.emailparm = value;
    }

}
