
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
 *         &lt;element name="SendEmail20Result" type="{http://iamsweb.gmcc.net/WS/}ActionResult"/>
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
    "sendEmail20Result"
})
@XmlRootElement(name = "SendEmail20Response")
public class SendEmail20Response {

    @XmlElement(name = "SendEmail20Result", required = true)
    protected ActionResult sendEmail20Result;

    /**
     * ��ȡsendEmail20Result���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link ActionResult }
     *     
     */
    public ActionResult getSendEmail20Result() {
        return sendEmail20Result;
    }

    /**
     * ����sendEmail20Result���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link ActionResult }
     *     
     */
    public void setSendEmail20Result(ActionResult value) {
        this.sendEmail20Result = value;
    }

}
