
package com.hotent.wsclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
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
     * 获取systemid属性的值。
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
     * 设置systemid属性的值。
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
     * 获取sysAccount属性的值。
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
     * 设置sysAccount属性的值。
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
     * 获取sysPassword属性的值。
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
     * 设置sysPassword属性的值。
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
     * 获取toUserID属性的值。
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
     * 设置toUserID属性的值。
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
     * 获取toMobile属性的值。
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
     * 设置toMobile属性的值。
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
     * 获取fromUserID属性的值。
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
     * 设置fromUserID属性的值。
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
     * 获取fromMobile属性的值。
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
     * 设置fromMobile属性的值。
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
     * 获取autoForward属性的值。
     * 
     */
    public boolean isAutoForward() {
        return autoForward;
    }

    /**
     * 设置autoForward属性的值。
     * 
     */
    public void setAutoForward(boolean value) {
        this.autoForward = value;
    }

    /**
     * 获取validdt属性的值。
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
     * 设置validdt属性的值。
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
     * 获取allowLock属性的值。
     * 
     */
    public boolean isAllowLock() {
        return allowLock;
    }

    /**
     * 设置allowLock属性的值。
     * 
     */
    public void setAllowLock(boolean value) {
        this.allowLock = value;
    }

    /**
     * 获取allowAlert属性的值。
     * 
     */
    public boolean isAllowAlert() {
        return allowAlert;
    }

    /**
     * 设置allowAlert属性的值。
     * 
     */
    public void setAllowAlert(boolean value) {
        this.allowAlert = value;
    }

    /**
     * 获取sourceNo属性的值。
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
     * 设置sourceNo属性的值。
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
     * 获取content属性的值。
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
     * 设置content属性的值。
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
