
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
 *         &lt;element name="ReceiveSMSResult" type="{http://iamsweb.gmcc.net/WS/}SMSResult"/>
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
    "receiveSMSResult"
})
@XmlRootElement(name = "ReceiveSMSResponse")
public class ReceiveSMSResponse {

    @XmlElement(name = "ReceiveSMSResult", required = true)
    protected SMSResult receiveSMSResult;

    /**
     * ��ȡreceiveSMSResult���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link SMSResult }
     *     
     */
    public SMSResult getReceiveSMSResult() {
        return receiveSMSResult;
    }

    /**
     * ����receiveSMSResult���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link SMSResult }
     *     
     */
    public void setReceiveSMSResult(SMSResult value) {
        this.receiveSMSResult = value;
    }

}
