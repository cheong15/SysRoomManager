
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
     * ��ȡsendSMSExtResult���Ե�ֵ��
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
     * ����sendSMSExtResult���Ե�ֵ��
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
