package com.jptest.payments.fulfillment.testonia.business.vo.money;

import java.math.BigInteger;
import java.util.Collections;
import com.jptest.money.PaymentRecipientVO;
import com.jptest.money.PaymentSenderVO;
import com.jptest.money.SessionVO;
import com.jptest.money.UserIdentifierVO;
import com.jptest.payments.fulfillment.testonia.business.vo.ListWrappedVOBuilder;


/**
 * Builds PaymentSenderVO, PaymentRecipientVO and Facilitator
 * 
 * @JP Inc.
 */
public class PartyVOsBuilder {

    public static PaymentSenderVOBuilder sender(BigInteger accountNumber) {
        return new PaymentSenderVOBuilder(accountNumber);
    }

    public static PaymentRecipientVOBuilder recipient() {
        return new PaymentRecipientVOBuilder();
    }

    public static PaymentRecipientVOBuilder recipient(BigInteger accountNumber, String email) {
        return recipient().accountNumber(accountNumber).email(email);
    }

    public static class PaymentSenderVOBuilder extends ListWrappedVOBuilder<PaymentSenderVO> {

        private final BigInteger accountNumber;
        private String emailAddress;

        private PaymentSenderVOBuilder(BigInteger accountNumber) {
            this.accountNumber = accountNumber;
        }

        public static PaymentSenderVOBuilder newBuilder(BigInteger accountNumber) {
            return new PaymentSenderVOBuilder(accountNumber);
        }
        
        public static PaymentSenderVOBuilder newBuilder(BigInteger accountNumber, String emailAddress) {
            return new PaymentSenderVOBuilder(accountNumber);
        }
        
        public PaymentSenderVOBuilder email(String emailAddress) {
            this.emailAddress = emailAddress;
            return this;
        }

        public PaymentSenderVO build() {
            PaymentSenderVO vo = new PaymentSenderVO();
            UserIdentifierVO id = new UserIdentifierVO();
            id.setAccountNumber(accountNumber);
            if (emailAddress != null) {
                id.setEmailAddress(emailAddress);
            }
            
//            id.setUserDetails();

            vo.setSenderId(id);
            vo.setIsPresent(true);
            vo.setProfileUpdates(Collections.emptyList());
            vo.setConstraints(Collections.emptyList());

            SessionVO sessVO = new SessionVO();
            sessVO.setSessionId("1234567890");
            vo.setSessionData(Collections.singletonList(sessVO));
            return vo;
        }

    }

    public static class PaymentRecipientVOBuilder extends ListWrappedVOBuilder<PaymentRecipientVO> {

        private BigInteger accountNumber = null;
        private String email = null;

        public PaymentRecipientVOBuilder accountNumber(BigInteger accountNumber) {
            this.accountNumber = accountNumber;
            return this;
        }

        public PaymentRecipientVOBuilder email(String email) {
            this.email = email;
            return this;
        }

        public PaymentRecipientVO build() {
            PaymentRecipientVO vo = new PaymentRecipientVO();
            UserIdentifierVO id = new UserIdentifierVO();
            id.setAccountNumber(accountNumber);
            vo.setRecipientId(id);
            vo.setPaymentEmailAddress(email);
            return vo;
        }
    }
}
