
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
     * 获取receiveSMSBySJDResult属性的值。
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
     * 设置receiveSMSBySJDResult属性的值。
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
