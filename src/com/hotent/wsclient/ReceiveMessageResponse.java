
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
 *         &lt;element name="ReceiveMessageResult" type="{http://iamsweb.gmcc.net/WS/}ReceiveMSGResult"/>
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
    "receiveMessageResult"
})
@XmlRootElement(name = "ReceiveMessageResponse")
public class ReceiveMessageResponse {

    @XmlElement(name = "ReceiveMessageResult", required = true)
    protected ReceiveMSGResult receiveMessageResult;

    /**
     * ��ȡreceiveMessageResult���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link ReceiveMSGResult }
     *     
     */
    public ReceiveMSGResult getReceiveMessageResult() {
        return receiveMessageResult;
    }

    /**
     * ����receiveMessageResult���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link ReceiveMSGResult }
     *     
     */
    public void setReceiveMessageResult(ReceiveMSGResult value) {
        this.receiveMessageResult = value;
    }

}
