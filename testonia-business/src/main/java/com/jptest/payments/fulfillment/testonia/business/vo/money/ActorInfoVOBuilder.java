package com.jptest.payments.fulfillment.testonia.business.vo.money;

import java.math.BigInteger;
import com.jptest.common.ActorInfoVO;
import com.jptest.payments.fulfillment.testonia.business.vo.ListWrappedVOBuilder;

public class ActorInfoVOBuilder extends ListWrappedVOBuilder<ActorInfoVO> {

    
    private Byte actorType = Byte.valueOf("10");
    private Byte actorAuthType = Byte.parseByte("0");
    private Byte actorAuthCredential =Byte.parseByte("0");
    private BigInteger actorAccountNumber;
    private BigInteger actorId;
    private String actorIpAddress = "10.10.10.1";
    
    private ActorInfoVOBuilder(BigInteger accountNumber) {
        actorAccountNumber = accountNumber;
        
    }
    
    public static ActorInfoVOBuilder newBuilder(BigInteger actorAccountNumber) {
        return new ActorInfoVOBuilder(actorAccountNumber);
    }
    
    public ActorInfoVOBuilder actorType(Byte actorType) {
        this.actorType = actorType;
        return this;
    }
    
    public ActorInfoVOBuilder actorAuthType(Byte actorAuthType) {
        this.actorAuthType = actorAuthType;
        return this;
    }
    
    public ActorInfoVOBuilder actorAuthCredential(Byte actorAuthCredential) {
        this.actorAuthCredential = actorAuthCredential;
        return this;
    }
    
    public ActorInfoVOBuilder actorId(BigInteger actorId) {
        this.actorId = actorId;
        return this;
    }
    
    
    @Override
    public ActorInfoVO build() {
        ActorInfoVO actorInfoVO = new ActorInfoVO();
        actorInfoVO.setActorType(actorType);
        actorInfoVO.setActorAuthType(actorAuthType);
        actorInfoVO.setActorAuthCredential(actorAuthCredential);
        actorInfoVO.setActorAccountNumber(actorAccountNumber);
        actorInfoVO.setActorId(actorId);
        actorInfoVO.setActorIpAddr(actorIpAddress);
        return actorInfoVO;
    }

}
