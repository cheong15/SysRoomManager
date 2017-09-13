
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
 *         &lt;element name="toUserID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="toMobile" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fromUserID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fromMobile" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="autoForward" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="validdt" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="allowLock" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="allowAlert" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="sourceNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="content" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "toUserID",
    "toMobile",
    "fromUserID",
    "fromMobile",
    "autoForward",
    "validdt",
    "allowLock",
    "allowAlert",
    "sourceNo",
    "content"
})
@XmlRootElement(name = "SendSMSExt")
public class SendSMSExt {

    protected String systemid;
    protected String sysAccount;
    protected String sysPassword;
    protected String toUserID;
    protected String toMobile;
    protected String fromUserID;
    protected String fromMobile;
    protected boolean autoForward;
    protected String validdt;
    protected boolean allowLock;
    protected boolean allowAlert;
    protected String sourceNo;
    protected String content;

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
     * ��ȡtoUserID���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getToUserID() {
        return toUserID;
    }

    /**
     * ����toUserID���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setToUserID(String value) {
        this.toUserID = value;
    }

    /**
     * ��ȡtoMobile���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getToMobile() {
        return toMobile;
    }

    /**
     * ����toMobile���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setToMobile(String value) {
        this.toMobile = value;
    }

    /**
     * ��ȡfromUserID���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFromUserID() {
        return fromUserID;
    }

    /**
     * ����fromUserID���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFromUserID(String value) {
        this.fromUserID = value;
    }

    /**
     * ��ȡfromMobile���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFromMobile() {
        return fromMobile;
    }

    /**
     * ����fromMobile���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFromMobile(String value) {
        this.fromMobile = value;
    }

    /**
     * ��ȡautoForward���Ե�ֵ��
     * 
     */
    public boolean isAutoForward() {
        return autoForward;
    }

    /**
     * ����autoForward���Ե�ֵ��
     * 
     */
    public void setAutoForward(boolean value) {
        this.autoForward = value;
    }

    /**
     * ��ȡvaliddt���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValiddt() {
        return validdt;
    }

    /**
     * ����validdt���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValiddt(String value) {
        this.validdt = value;
    }

    /**
     * ��ȡallowLock���Ե�ֵ��
     * 
     */
    public boolean isAllowLock() {
        return allowLock;
    }

    /**
     * ����allowLock���Ե�ֵ��
     * 
     */
    public void setAllowLock(boolean value) {
        this.allowLock = value;
    }

    /**
     * ��ȡallowAlert���Ե�ֵ��
     * 
     */
    public boolean isAllowAlert() {
        return allowAlert;
    }

    /**
     * ����allowAlert���Ե�ֵ��
     * 
     */
    public void setAllowAlert(boolean value) {
        this.allowAlert = value;
    }

    /**
     * ��ȡsourceNo���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSourceNo() {
        return sourceNo;
    }

    /**
     * ����sourceNo���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSourceNo(String value) {
        this.sourceNo = value;
    }

    /**
     * ��ȡcontent���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContent() {
        return content;
    }

    /**
     * ����content���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContent(String value) {
        this.content = value;
    }

}
