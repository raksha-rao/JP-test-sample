package com.jptest.payments.fulfillment.testonia.model.amq;

import com.jptest.payments.fulfillment.testonia.model.RawString;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Represents a record in (any) queue table 
 * e.g.
 * AMQ_MONEYINTERNAL_Q table in Money Database
 * AMQ_FULFILLMENT_JRNL_Q table in ENG Database
 */
public class QueueDTO {

    private String rowid;
    private Long rownum;
    private RawString payloadB;
    private byte[] payloadB4;
    private byte[] payloadB3;
    private byte[] payloadB2;
    private byte[] payloadB1;
    private String payloadC;
    private String payload1;
    private String version;
    private long enqTime;
    private String deleteFlag;
    private String encode;
    private long plsize;
    private String messageSource;
    private long groupId;
    private long partitionId;
    private long messageId;
    private long dqTime;
    private long subqueueId;
    private String messageType;
    private String dbName;
    private String tableName;
    private int retry;

    public RawString getPayloadB() {
        return payloadB;
    }

    public void setPayloadB(RawString payloadB) {
        this.payloadB = payloadB;
    }

    @SuppressFBWarnings("EI_EXPOSE_REP")
    public byte[] getPayloadB4() {
        return payloadB4;
    }

    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public void setPayloadB4(byte[] payloadB4) {
        this.payloadB4 = payloadB4;
    }

    @SuppressFBWarnings("EI_EXPOSE_REP")
    public byte[] getPayloadB3() {
        return payloadB3;
    }

    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public void setPayloadB3(byte[] payloadB3) {
        this.payloadB3 = payloadB3;
    }

    @SuppressFBWarnings("EI_EXPOSE_REP")
    public byte[] getPayloadB2() {
        return payloadB2;
    }

    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public void setPayloadB2(byte[] payloadB2) {
        this.payloadB2 = payloadB2;
    }

    @SuppressFBWarnings("EI_EXPOSE_REP")
    public byte[] getPayloadB1() {
        return payloadB1;
    }

    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public void setPayloadB1(byte[] payloadB1) {
        this.payloadB1 = payloadB1;
    }

    public String getPayloadC() {
        return payloadC;
    }

    public void setPayloadC(String payloadC) {
        this.payloadC = payloadC;
    }

    public String getPayload1() {
        return payload1;
    }

    public void setPayload1(String payload1) {
        this.payload1 = payload1;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public long getEnqTime() {
        return enqTime;
    }

    public void setEnqTime(long enqTime) {
        this.enqTime = enqTime;
    }

    public char getDeleteFlag() {
        if (deleteFlag == null)
            return 0;
        if (deleteFlag.length() == 0)
            return 0;
        return deleteFlag.charAt(0);
    }

    public void setDeleteFlag(char deleteFlag) {
        this.deleteFlag = deleteFlag + "";
    }

    public char getEncode() {
        if (encode == null)
            return 0;
        if (encode.length() == 0)
            return 0;
        return encode.charAt(0);
    }

    public void setEncode(char encode) {
        this.encode = encode + "";
    }

    public long getPlsize() {
        return plsize;
    }

    public void setPlsize(long plsize) {
        this.plsize = plsize;
    }

    public String getMessageSource() {
        return messageSource;
    }

    public void setMessageSource(String messageSource) {
        this.messageSource = messageSource;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public long getPartitionId() {
        return partitionId;
    }

    public void setPartitionId(long partitionId) {
        this.partitionId = partitionId;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public long getDqTime() {
        return dqTime;
    }

    public void setDqTime(long dqTime) {
        this.dqTime = dqTime;
    }

    public long getSubqueueId() {
        return subqueueId;
    }

    public void setSubqueueId(long subqueueId) {
        this.subqueueId = subqueueId;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getRowid() {
        return rowid;
    }

    public void setRowid(String rowid) {
        this.rowid = rowid;
    }

    public void setRownum(Long rownum) {
        this.rownum = rownum;
    }

    public Long getRownum() {
        return rownum;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public int getRetry() {
        return retry;
    }

    public void setRetry(int retry) {
        this.retry = retry;
    }
    
}
