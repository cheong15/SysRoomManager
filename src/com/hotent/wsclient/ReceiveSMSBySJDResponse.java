
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
 *         &lt;element name="ReceiveSMSBySJDResult" type="{http://iamsweb.gmcc.net/WS/}SmsResponse" minOccurs="0"/>
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
    "receiveSMSBySJDResult"
})
@XmlRootElement(name = "ReceiveSMSBySJDResponse")
public class ReceiveSMSBySJDResponse {

    @XmlElement(name = "ReceiveSMSBySJDResult")
    protected SmsResponse receiveSMSBySJDResult;

    /**
     * ��ȡreceiveSMSBySJDResult���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link SmsResponse }
     *     
     */
    public SmsResponse getReceiveSMSBySJDResult() {
        return receiveSMSBySJDResult;
    }

    /**
     * ����receiveSMSBySJDResult���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link SmsResponse }
     *     
     */
    public void setReceiveSMSBySJDResult(SmsResponse value) {
        this.receiveSMSBySJDResult = value;
    }

}
