package com.jptest.payments.fulfillment.testonia.model.money;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PaymentRecipientSubBalanceDTO {

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "wuser_holding_subbalance")
    private List<WUserHoldingSubBalanceDTO> userHoldingSubBalances;

    public List<WUserHoldingSubBalanceDTO> getUserHoldingSubBalances() {
        return userHoldingSubBalances;
    }

    public void setUserHoldingSubBalances(List<WUserHoldingSubBalanceDTO> userHoldingSubBalances) {
        this.userHoldingSubBalances = userHoldingSubBalances;
    }

}
