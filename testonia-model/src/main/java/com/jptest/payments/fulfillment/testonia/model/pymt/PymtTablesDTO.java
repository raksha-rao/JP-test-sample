package com.jptest.payments.fulfillment.testonia.model.pymt;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class PymtTablesDTO {

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "payment_side")
    private List<PaymentSideDTO> paymentSideDTO = new ArrayList<>();

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "money_movement")
    private List<MoneyMovementDTO> moneyMovementDTO = new ArrayList<>();

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "fee_composition")
    private List<FeeCompositionDTO> feeCompositionDTO = new ArrayList<>();

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "withheld_balance_change")
    private List<WithHeldBalanceChangeDTO> withHeldBalChangeDTO = new ArrayList<>();

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "currency_exchange")
    private List<CurrencyExchangeDTO> currencyExchangeDTO = new ArrayList<>();

    public List<PaymentSideDTO> getPaymentSideDTO() {
        return paymentSideDTO;
    }

    public void addPaymentSideDTO(List<PaymentSideDTO> paymentSideDTO) {
        this.paymentSideDTO.addAll(paymentSideDTO);
    }

    public List<MoneyMovementDTO> getMoneyMovementDTO() {
        return moneyMovementDTO;
    }

    public void addMoneyMovementDTO(List<MoneyMovementDTO> moneyMovementDTO) {
        this.moneyMovementDTO.addAll(moneyMovementDTO);
    }

    public List<CurrencyExchangeDTO> getCurrencyExchangeDTO() {
        return currencyExchangeDTO;
    }

    public void addCurrencyExchangeDTO(List<CurrencyExchangeDTO> currencyExchangeDTO) {
        this.currencyExchangeDTO.addAll(currencyExchangeDTO);
    }

    public List<WithHeldBalanceChangeDTO> getWithHeldBalChangeDTO() {
        return withHeldBalChangeDTO;
    }

    public void addWithHeldBalChangeDTO(List<WithHeldBalanceChangeDTO> withHeldBalChangeDTO) {
        this.withHeldBalChangeDTO.addAll(withHeldBalChangeDTO);
    }

    public List<FeeCompositionDTO> getFeeCompositionDTO() {
        return feeCompositionDTO;
    }

    public void addfeeCompositionDTO(List<FeeCompositionDTO> feeCompositionDTO) {
        this.feeCompositionDTO.addAll(feeCompositionDTO);
    }

}
