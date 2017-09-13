
package com.hotent.wsclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>KWSMSResult complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="KWSMSResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="appAuth" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="kwSMSInfo" type="{http://iamsweb.gmcc.net/WS/}ArrayOfKWSMSDetail" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "KWSMSResult", propOrder = {
    "appAuth",
    "kwSMSInfo"
})
public class KWSMSResult {

    protected int appAuth;
    protected ArrayOfKWSMSDetail kwSMSInfo;

    /**
     * 获取appAuth属性的值。
     * 
     */
    public int getAppAuth() {
        return appAuth;
    }

    /**
     * 设置appAuth属性的值。
     * 
     */
    public void setAppAuth(int value) {
        this.appAuth = value;
    }

    /**
     * 获取kwSMSInfo属性的值。
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfKWSMSDetail }
     *     
     */
    public ArrayOfKWSMSDetail getKwSMSInfo() {
        return kwSMSInfo;
    }

    /**
     * 设置kwSMSInfo属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfKWSMSDetail }
     *     
     */
    public void setKwSMSInfo(ArrayOfKWSMSDetail value) {
        this.kwSMSInfo = value;
    }

}
