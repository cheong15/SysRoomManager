
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
 *         &lt;element name="CloseReceivedSMSResult" type="{http://iamsweb.gmcc.net/WS/}ActionResult"/>
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
    "closeReceivedSMSResult"
})
@XmlRootElement(name = "CloseReceivedSMSResponse")
public class CloseReceivedSMSResponse {

    @XmlElement(name = "CloseReceivedSMSResult", required = true)
    protected ActionResult closeReceivedSMSResult;

    /**
     * ��ȡcloseReceivedSMSResult���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link ActionResult }
     *     
     */
    public ActionResult getCloseReceivedSMSResult() {
        return closeReceivedSMSResult;
    }

    /**
     * ����closeReceivedSMSResult���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link ActionResult }
     *     
     */
    public void setCloseReceivedSMSResult(ActionResult value) {
        this.closeReceivedSMSResult = value;
    }

}
