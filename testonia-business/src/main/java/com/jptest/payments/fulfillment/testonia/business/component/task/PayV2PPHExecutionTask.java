package com.jptest.payments.fulfillment.testonia.business.component.task;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import com.jptest.finsys.CardPresentDataEnum;
import com.jptest.finsys.CardPresentDataInfoVO;
import com.jptest.finsys.CardPresentPCISensitiveVO;
import com.jptest.finsys.CardPresentStorableVO;
import com.jptest.finsys.DateTimeVO;
import com.jptest.finsys.DeviceCapabilityEnum;
import com.jptest.money.DisbursementPolicyType;
import com.jptest.money.ParticipantExtensionsVO;
import com.jptest.money.PaymentFlagsVO;


/**
 * Class to perform Pay_V2 operation task for PPH flow.
 * <p>
 * This task has similar logic as PayV2ExecutionTask which performs pay_v2 operation for DCC flow. Only difference from
 * PayV2ExecutionTask is in building the request for PPH flow. PassThroughData in payV2Request is different for PPH from
 * DCC's payV2Request. Flags are set to mimic flags from bluefin PPH tests.
 * </p>
 * 
 * @JP Inc.
 */
public class PayV2PPHExecutionTask extends PayV2ExecutionTask {

    private DisbursementPolicyType disbursementPolicyType;

    public PayV2PPHExecutionTask(TransactionAmountInput amount, DisbursementPolicyType disbursementPolicyType) {
        super(amount);
        this.disbursementPolicyType = disbursementPolicyType;
    }

    @Override
    protected PaymentFlagsVO getPaymentFlags() {
        PaymentFlagsVO paymentFlagsVO = new PaymentFlagsVO();
        paymentFlagsVO.setFlags(new BigInteger(Integer.toString(4194304), 10));
        paymentFlagsVO.setFlags2(new BigInteger(Integer.toString(0), 10));
        paymentFlagsVO.setFlags3(new BigInteger(Integer.toString(65536), 10));
        paymentFlagsVO.setFlags4(new BigInteger(Integer.toString(32), 10));
        paymentFlagsVO.setFlags5(new BigInteger(Integer.toString(134217984), 10));
        return paymentFlagsVO;
    }

    @Override
    protected List<ParticipantExtensionsVO> getPassThroughData() {
        List<ParticipantExtensionsVO> particiantExtensionData = super.getPassThroughData();

        CardPresentDataInfoVO dataVO = new CardPresentDataInfoVO();
        dataVO.setCardPresent(CardPresentDataEnum.MAGNETIC_STRIPE_TRACK_1_AND_2);
        dataVO.setPinPresent(false);
        dataVO.setSignaturePresent(false);
        CardPresentPCISensitiveVO pciVO = new CardPresentPCISensitiveVO();
        pciVO.setCardPresentDataInfoPci(dataVO);
        ParticipantExtensionsVO pciExtn1 = new ParticipantExtensionsVO();
        pciExtn1.setData(pciVO);
        pciExtn1.setName("FinSys");
        particiantExtensionData.add(pciExtn1);

        CardPresentStorableVO data1VO = new CardPresentStorableVO();
        data1VO.setCardPresentDataInfo(dataVO);
        ArrayList<DeviceCapabilityEnum> values = new ArrayList<DeviceCapabilityEnum>();
        values.add(DeviceCapabilityEnum.SWIPE);
        data1VO.setDeviceCapabilityAsEnum(values);
        DateTimeVO dateTime = new DateTimeVO();
        dateTime.setYear((long) 2014);
        dateTime.setMonth((short) 7);
        dateTime.setDay((short) 8);
        dateTime.setHour((short) 5);
        dateTime.setMinute((short) 45);
        dateTime.setSecond((short) 23);
        data1VO.setLocalDateTime(dateTime);

        ParticipantExtensionsVO pciExtn2 = new ParticipantExtensionsVO();
        pciExtn2.setData(data1VO);
        pciExtn2.setName("FinSys");
        particiantExtensionData.add(pciExtn2);

        return particiantExtensionData;
    }

    @Override
    protected DisbursementPolicyType getDisbursementPolicyType() {
        return this.disbursementPolicyType;
    }
}
