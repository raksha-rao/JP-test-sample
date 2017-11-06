package com.jptest.payments.fulfillment.testonia.business.vo.money;

import java.math.BigInteger;
import com.jptest.money.BusinessContextVO;
import com.jptest.payments.fulfillment.testonia.business.vo.VOBuilder;


public class BusinessContextVOBuilder implements VOBuilder<BusinessContextVO> {

    private Long actorType;
    private Byte actorAuthType;
    private BigInteger actorAccountNumber;
    private BigInteger actorId;
    private String actorIpAddr;
    private Boolean isStandin;
    private BigInteger visitorId;
    private BigInteger guid;
    private BigInteger slaTimeout;
    private String actorSessionId;
    private BigInteger actorPartyId;

    public static BusinessContextVOBuilder newBuilder() {
        return new BusinessContextVOBuilder();
    }

    public BusinessContextVOBuilder actorType(Long actorType) {
        this.actorType = actorType;
        return this;

    }

    public BusinessContextVOBuilder actorAuthType(Byte actorAuthType) {
        this.actorAuthType = actorAuthType;
        return this;

    }

    public BusinessContextVOBuilder actorAccountNumber(BigInteger actorAccountNumber) {
        this.actorAccountNumber = actorAccountNumber;
        return this;

    }

    public BusinessContextVOBuilder actorId(BigInteger actorId) {
        this.actorId = actorId;
        return this;

    }

    public BusinessContextVOBuilder actorIpAddr(String actorIpAddr) {
        this.actorIpAddr = actorIpAddr;
        return this;

    }

    public BusinessContextVOBuilder isStandin(Boolean isStandin) {
        this.isStandin = isStandin;
        return this;

    }

    public BusinessContextVOBuilder visitorId(BigInteger visitorId) {
        this.visitorId = visitorId;
        return this;

    }

    public BusinessContextVOBuilder guid(BigInteger guid) {
        this.guid = guid;
        return this;

    }

    public BusinessContextVOBuilder slaTimeout(BigInteger slaTimeout) {
        this.slaTimeout = slaTimeout;
        return this;

    }

    public BusinessContextVOBuilder actorSessionId(String actorSessionId) {
        this.actorSessionId = actorSessionId;
        return this;

    }

    public BusinessContextVOBuilder actorPartyId(BigInteger actorPartyId) {
        this.actorPartyId = actorPartyId;
        return this;

    }

    @Override
    public BusinessContextVO build() {
        BusinessContextVO vo = new BusinessContextVO();
        vo.setActorType(actorType);
        vo.setActorAuthType(actorAuthType);
        vo.setActorAccountNumber(actorAccountNumber);
        vo.setActorId(actorId);
        vo.setActorIpAddr(actorIpAddr);
        vo.setIsStandin(isStandin);
        vo.setVisitorId(visitorId);
        vo.setGuid(guid);
        vo.setSlaTimeout(slaTimeout);
        vo.setActorSessionId(actorSessionId);
        vo.setActorPartyId(actorPartyId);
        return vo;
    }
}
