
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
     * 获取receiveKeywordSMSResult属性的值。
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
     * 设置receiveKeywordSMSResult属性的值。
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
