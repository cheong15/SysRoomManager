
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
 *         &lt;element name="GetSmsStatusResult" type="{http://iamsweb.gmcc.net/WS/}SMSStatusResult"/>
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
    "getSmsStatusResult"
})
@XmlRootElement(name = "GetSmsStatusResponse")
public class GetSmsStatusResponse {

    @XmlElement(name = "GetSmsStatusResult", required = true)
    protected SMSStatusResult getSmsStatusResult;

    /**
     * ��ȡgetSmsStatusResult���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link SMSStatusResult }
     *     
     */
    public SMSStatusResult getGetSmsStatusResult() {
        return getSmsStatusResult;
    }

    /**
     * ����getSmsStatusResult���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link SMSStatusResult }
     *     
     */
    public void setGetSmsStatusResult(SMSStatusResult value) {
        this.getSmsStatusResult = value;
    }

}
