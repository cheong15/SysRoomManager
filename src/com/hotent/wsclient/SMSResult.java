
package com.hotent.wsclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>SMSResult complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="SMSResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="appAuth" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="smsInfo" type="{http://iamsweb.gmcc.net/WS/}ArrayOfSmsDetail" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SMSResult", propOrder = {
    "appAuth",
    "smsInfo"
})
public class SMSResult {

    protected int appAuth;
    protected ArrayOfSmsDetail smsInfo;

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
     * ��ȡsmsInfo���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfSmsDetail }
     *     
     */
    public ArrayOfSmsDetail getSmsInfo() {
        return smsInfo;
    }

    /**
     * ����smsInfo���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfSmsDetail }
     *     
     */
    public void setSmsInfo(ArrayOfSmsDetail value) {
        this.smsInfo = value;
    }

}
