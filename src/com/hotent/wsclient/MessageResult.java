
package com.hotent.wsclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>MessageResult complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="MessageResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Success" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="Message" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MessageInfo" type="{http://tempuri.org/}ArrayOfMessageDetail" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MessageResult", namespace = "http://tempuri.org/", propOrder = {
    "success",
    "message",
    "messageInfo"
})
public class MessageResult {

    @XmlElement(name = "Success")
    protected boolean success;
    @XmlElement(name = "Message")
    protected String message;
    @XmlElement(name = "MessageInfo")
    protected ArrayOfMessageDetail messageInfo;

    /**
     * 获取success属性的值。
     * 
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * 设置success属性的值。
     * 
     */
    public void setSuccess(boolean value) {
        this.success = value;
    }

    /**
     * 获取message属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置message属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessage(String value) {
        this.message = value;
    }

    /**
     * 获取messageInfo属性的值。
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfMessageDetail }
     *     
     */
    public ArrayOfMessageDetail getMessageInfo() {
        return messageInfo;
    }

    /**
     * 设置messageInfo属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfMessageDetail }
     *     
     */
    public void setMessageInfo(ArrayOfMessageDetail value) {
        this.messageInfo = value;
    }

}
