
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
 *         &lt;element name="CloseReceivedKeywordSMSResult" type="{http://iamsweb.gmcc.net/WS/}ActionResult"/>
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
    "closeReceivedKeywordSMSResult"
})
@XmlRootElement(name = "CloseReceivedKeywordSMSResponse")
public class CloseReceivedKeywordSMSResponse {

    @XmlElement(name = "CloseReceivedKeywordSMSResult", required = true)
    protected ActionResult closeReceivedKeywordSMSResult;

    /**
     * ��ȡcloseReceivedKeywordSMSResult���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link ActionResult }
     *     
     */
    public ActionResult getCloseReceivedKeywordSMSResult() {
        return closeReceivedKeywordSMSResult;
    }

    /**
     * ����closeReceivedKeywordSMSResult���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link ActionResult }
     *     
     */
    public void setCloseReceivedKeywordSMSResult(ActionResult value) {
        this.closeReceivedKeywordSMSResult = value;
    }

}
