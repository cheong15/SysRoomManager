
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
 *         &lt;element name="SendEmail20Result" type="{http://iamsweb.gmcc.net/WS/}ActionResult"/>
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
    "sendEmail20Result"
})
@XmlRootElement(name = "SendEmail20Response")
public class SendEmail20Response {

    @XmlElement(name = "SendEmail20Result", required = true)
    protected ActionResult sendEmail20Result;

    /**
     * 获取sendEmail20Result属性的值。
     * 
     * @return
     *     possible object is
     *     {@link ActionResult }
     *     
     */
    public ActionResult getSendEmail20Result() {
        return sendEmail20Result;
    }

    /**
     * 设置sendEmail20Result属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link ActionResult }
     *     
     */
    public void setSendEmail20Result(ActionResult value) {
        this.sendEmail20Result = value;
    }

}
