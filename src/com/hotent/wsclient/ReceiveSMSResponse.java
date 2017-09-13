
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
     * 获取receiveSMSResult属性的值。
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
     * 设置receiveSMSResult属性的值。
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
