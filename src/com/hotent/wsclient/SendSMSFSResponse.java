
package com.hotent.wsclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element name="SendSMS_FSResult" type="{http://iamsweb.gmcc.net/WS/}SendSMSResult"/>
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
    "sendSMSFSResult"
})
@XmlRootElement(name = "SendSMS_FSResponse")
public class SendSMSFSResponse {

    @XmlElement(name = "SendSMS_FSResult", required = true)
    protected SendSMSResult sendSMSFSResult;

    /**
     * ��ȡsendSMSFSResult���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link SendSMSResult }
     *     
     */
    public SendSMSResult getSendSMSFSResult() {
        return sendSMSFSResult;
    }

    /**
     * ����sendSMSFSResult���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link SendSMSResult }
     *     
     */
    public void setSendSMSFSResult(SendSMSResult value) {
        this.sendSMSFSResult = value;
    }

}
