
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
     * 获取strSysID属性的值。
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
     * 设置strSysID属性的值。
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
     * 获取strSysAccount属性的值。
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
     * 设置strSysAccount属性的值。
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
     * 获取strSysPassword属性的值。
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
     * 设置strSysPassword属性的值。
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
     * 获取smsparm属性的值。
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
     * 设置smsparm属性的值。
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
     * 获取lcsparm属性的值。
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
     * 设置lcsparm属性的值。
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
