
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
 *         &lt;element name="SendEmailResult" type="{http://iamsweb.gmcc.net/WS/}ActionResult"/>
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
    "sendEmailResult"
})
@XmlRootElement(name = "SendEmailResponse")
public class SendEmailResponse {

    @XmlElement(name = "SendEmailResult", required = true)
    protected ActionResult sendEmailResult;

    /**
     * ��ȡsendEmailResult���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link ActionResult }
     *     
     */
    public ActionResult getSendEmailResult() {
        return sendEmailResult;
    }

    /**
     * ����sendEmailResult���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link ActionResult }
     *     
     */
    public void setSendEmailResult(ActionResult value) {
        this.sendEmailResult = value;
    }

}
