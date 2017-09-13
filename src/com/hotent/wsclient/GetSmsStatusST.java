
package com.hotent.wsclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element name="Sysid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SysAccout" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Syspwd" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="smsSendId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "sysid",
    "sysAccout",
    "syspwd",
    "smsSendId"
})
@XmlRootElement(name = "GetSmsStatusST")
public class GetSmsStatusST {

    @XmlElement(name = "Sysid")
    protected String sysid;
    @XmlElement(name = "SysAccout")
    protected String sysAccout;
    @XmlElement(name = "Syspwd")
    protected String syspwd;
    protected String smsSendId;

    /**
     * 获取sysid属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSysid() {
        return sysid;
    }

    /**
     * 设置sysid属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSysid(String value) {
        this.sysid = value;
    }

    /**
     * 获取sysAccout属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSysAccout() {
        return sysAccout;
    }

    /**
     * 设置sysAccout属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSysAccout(String value) {
        this.sysAccout = value;
    }

    /**
     * 获取syspwd属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSyspwd() {
        return syspwd;
    }

    /**
     * 设置syspwd属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSyspwd(String value) {
        this.syspwd = value;
    }

    /**
     * 获取smsSendId属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSmsSendId() {
        return smsSendId;
    }

    /**
     * 设置smsSendId属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSmsSendId(String value) {
        this.smsSendId = value;
    }

}
