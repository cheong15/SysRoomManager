
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
 *         &lt;element name="ReceiveKeywordSMSResult" type="{http://iamsweb.gmcc.net/WS/}KWSMSResult"/>
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
    "receiveKeywordSMSResult"
})
@XmlRootElement(name = "ReceiveKeywordSMSResponse")
public class ReceiveKeywordSMSResponse {

    @XmlElement(name = "ReceiveKeywordSMSResult", required = true)
    protected KWSMSResult receiveKeywordSMSResult;

    /**
     * ��ȡreceiveKeywordSMSResult���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link KWSMSResult }
     *     
     */
    public KWSMSResult getReceiveKeywordSMSResult() {
        return receiveKeywordSMSResult;
    }

    /**
     * ����receiveKeywordSMSResult���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link KWSMSResult }
     *     
     */
    public void setReceiveKeywordSMSResult(KWSMSResult value) {
        this.receiveKeywordSMSResult = value;
    }

}
