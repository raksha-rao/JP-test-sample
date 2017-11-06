package com.jptest.payments.fulfillment.testonia.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * This class represents the input data which the test cases depend on for
 * MSP(Multi sellers payments) fulfillment use cases. Typically this class is
 * instantiated by deserializing the JSON data from src/test/resources/testdata/
 * <testclass>.json files
 */
@JsonInclude(Include.NON_NULL)
public class MSPTestCaseInputData extends TestCaseInputData {
	private List<UserCreationTaskInput> sellerDataList;

	public List<UserCreationTaskInput> getSellerDataList() {
		return sellerDataList;
	}

	public void setSellerDataList(List<UserCreationTaskInput> sellerDataList) {
		this.sellerDataList = sellerDataList;
	}
}
