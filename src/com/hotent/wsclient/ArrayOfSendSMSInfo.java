
package com.hotent.wsclient;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>ArrayOfSendSMSInfo complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="ArrayOfSendSMSInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SendSMSInfo" type="{http://iamsweb.gmcc.net/WS/}SendSMSInfo" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfSendSMSInfo", propOrder = {
    "sendSMSInfo"
})
public class ArrayOfSendSMSInfo {

    @XmlElement(name = "SendSMSInfo")
    protected List<SendSMSInfo> sendSMSInfo;

    /**
     * Gets the value of the sendSMSInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sendSMSInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSendSMSInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SendSMSInfo }
     * 
     * 
     */
    public List<SendSMSInfo> getSendSMSInfo() {
        if (sendSMSInfo == null) {
            sendSMSInfo = new ArrayList<SendSMSInfo>();
        }
        return this.sendSMSInfo;
    }

}
