
package com.hotent.wsclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.hotent.wsclient.SendMSGResult;


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
 *         &lt;element name="SendMessageWithReplyStatusResult" type="{http://iamsweb.gmcc.net/WS/}SendMSGResult"/>
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
    "sendMessageWithReplyStatusResult"
})
@XmlRootElement(name = "SendMessageWithReplyStatusResponse")
public class SendMessageWithReplyStatusResponse {

    @XmlElement(name = "SendMessageWithReplyStatusResult", required = true)
    protected SendMSGResult sendMessageWithReplyStatusResult;

    /**
     * 获取sendMessageWithReplyStatusResult属性的值。
     * 
     * @return
     *     possible object is
     *     {@link SendMSGResult }
     *     
     */
    public SendMSGResult getSendMessageWithReplyStatusResult() {
        return sendMessageWithReplyStatusResult;
    }

    /**
     * 设置sendMessageWithReplyStatusResult属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link SendMSGResult }
     *     
     */
    public void setSendMessageWithReplyStatusResult(SendMSGResult value) {
        this.sendMessageWithReplyStatusResult = value;
    }

}
