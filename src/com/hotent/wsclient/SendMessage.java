
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
     * 获取smsparm属性的值。
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
     * 设置smsparm属性的值。
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
     * 获取lcsparm属性的值。
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
     * 设置lcsparm属性的值。
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
     * 获取emailparm属性的值。
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
     * 设置emailparm属性的值。
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
