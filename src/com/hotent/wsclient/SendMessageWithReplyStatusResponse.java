
package com.hotent.wsclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.hotent.wsclient.SendMSGResult;


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
     * ��ȡsendMessageWithReplyStatusResult���Ե�ֵ��
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
     * ����sendMessageWithReplyStatusResult���Ե�ֵ��
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
