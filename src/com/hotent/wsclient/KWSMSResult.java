
package com.hotent.wsclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>KWSMSResult complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
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
     * ��ȡappAuth���Ե�ֵ��
     * 
     */
    public int getAppAuth() {
        return appAuth;
    }

    /**
     * ����appAuth���Ե�ֵ��
     * 
     */
    public void setAppAuth(int value) {
        this.appAuth = value;
    }

    /**
     * ��ȡkwSMSInfo���Ե�ֵ��
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
     * ����kwSMSInfo���Ե�ֵ��
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
