package com.jptest.payments.fulfillment.testonia.model.dynamicvalue;

import com.jptest.payments.fulfillment.testonia.model.GoldenFileComparisonTaskInput;
import org.w3c.dom.Document;

import java.nio.file.Path;
//import com.jptest.qi.rest.domain.pojo.User;

/**
 * DTO that carries placeholder generation information for an sender/recipient
 * golden file
 */
public class GoldenFilePlaceHolderDTO {

	private static final String FUNDING_SOURCE_HOLDING = "HOLDING";
	private static final String FUNDING_SOURCE_BALANCE = "BALANCE";

    private String testName;
	private Path jsonFilePath;
	private String goldenFileLocation;
	private Document goldenFileDocument;

	//private User sender;
	//private User recipient;
	private String transactionCurrency;
	private String fundingSource;

	private boolean changed;

    private String chargeBackGrade;

	private GoldenFilePlaceHolderGenerationStats stats;

	public GoldenFilePlaceHolderDTO(Path jsonFilePath, String goldenFileLocation,
			GoldenFilePlaceHolderGenerationStats stats) {
		this.jsonFilePath = jsonFilePath;
		this.goldenFileLocation = goldenFileLocation;
		this.stats = stats;
	}

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getTestName() {
        return testName;
    }

	public String getGoldenFileLocation() {
		return goldenFileLocation;
	}

    /**
     * Returns source location of golden file
     * 
     * @return
     */
    public String getGoldenFileLocationSource() {
        return GoldenFileComparisonTaskInput.getGoldenFileLocationSource(goldenFileLocation);
    }

	public Document getGoldenFileDocument() {
		return goldenFileDocument;
	}

	public void setGoldenFileDocument(Document goldenFileDocument) {
		this.goldenFileDocument = goldenFileDocument;
	}

/*	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public User getRecipient() {
		return recipient;
	}

	public void setRecipient(User recipient) {
		this.recipient = recipient;
	}*/

	public String getTransactionCurrency() {
		return transactionCurrency;
	}

	public void setTransactionCurrency(String transactionCurrency) {
		this.transactionCurrency = transactionCurrency;
	}

	public String getFundingSource() {
		return fundingSource;
	}

	public String getFundingSourceForFee() {

		return FUNDING_SOURCE_HOLDING.equals(fundingSource) ? FUNDING_SOURCE_BALANCE : fundingSource;
	}

	public void setFundingSource(String fundingSource) {
		this.fundingSource = fundingSource;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	public boolean isChanged() {
		return changed;
	}

    public void setChargeBackGrade(String chargeBackGrade) {
        this.chargeBackGrade = chargeBackGrade;
    }

    public String getChargeBackGrade() {
        return chargeBackGrade;
    }

	public String getPath() {
        return goldenFileLocation + " " + testName + " " + jsonFilePath;
	}

	public void setStats(GoldenFilePlaceHolderGenerationStats stats) {
		this.stats = stats;
	}

	public GoldenFilePlaceHolderGenerationStats getStats() {
		return stats;
	}

}
