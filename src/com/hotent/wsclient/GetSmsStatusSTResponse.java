
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
 *         &lt;element name="GetSmsStatusSTResult" type="{http://iamsweb.gmcc.net/WS/}ActionResult"/>
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
    "getSmsStatusSTResult"
})
@XmlRootElement(name = "GetSmsStatusSTResponse")
public class GetSmsStatusSTResponse {

    @XmlElement(name = "GetSmsStatusSTResult", required = true)
    protected ActionResult getSmsStatusSTResult;

    /**
     * 获取getSmsStatusSTResult属性的值。
     * 
     * @return
     *     possible object is
     *     {@link ActionResult }
     *     
     */
    public ActionResult getGetSmsStatusSTResult() {
        return getSmsStatusSTResult;
    }

    /**
     * 设置getSmsStatusSTResult属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link ActionResult }
     *     
     */
    public void setGetSmsStatusSTResult(ActionResult value) {
        this.getSmsStatusSTResult = value;
    }

}
