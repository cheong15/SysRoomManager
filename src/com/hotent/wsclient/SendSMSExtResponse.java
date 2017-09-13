
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
 *         &lt;element name="SendSMSExtResult" type="{http://iamsweb.gmcc.net/WS/}SendSMSNewResult"/>
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
    "sendSMSExtResult"
})
@XmlRootElement(name = "SendSMSExtResponse")
public class SendSMSExtResponse {

    @XmlElement(name = "SendSMSExtResult", required = true)
    protected SendSMSNewResult sendSMSExtResult;

    /**
     * 获取sendSMSExtResult属性的值。
     * 
     * @return
     *     possible object is
     *     {@link SendSMSNewResult }
     *     
     */
    public SendSMSNewResult getSendSMSExtResult() {
        return sendSMSExtResult;
    }

    /**
     * 设置sendSMSExtResult属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link SendSMSNewResult }
     *     
     */
    public void setSendSMSExtResult(SendSMSNewResult value) {
        this.sendSMSExtResult = value;
    }

}
