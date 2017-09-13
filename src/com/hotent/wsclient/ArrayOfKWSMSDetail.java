
package com.hotent.wsclient;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>ArrayOfKWSMSDetail complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="ArrayOfKWSMSDetail">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="KWSMSDetail" type="{http://iamsweb.gmcc.net/WS/}KWSMSDetail" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfKWSMSDetail", propOrder = {
    "kwsmsDetail"
})
public class ArrayOfKWSMSDetail {

    @XmlElement(name = "KWSMSDetail")
    protected List<KWSMSDetail> kwsmsDetail;

    /**
     * Gets the value of the kwsmsDetail property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the kwsmsDetail property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getKWSMSDetail().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link KWSMSDetail }
     * 
     * 
     */
    public List<KWSMSDetail> getKWSMSDetail() {
        if (kwsmsDetail == null) {
            kwsmsDetail = new ArrayList<KWSMSDetail>();
        }
        return this.kwsmsDetail;
    }

}
