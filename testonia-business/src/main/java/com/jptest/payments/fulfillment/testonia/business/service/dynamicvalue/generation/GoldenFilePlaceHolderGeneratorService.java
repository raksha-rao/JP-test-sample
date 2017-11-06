package com.jptest.payments.fulfillment.testonia.business.service.dynamicvalue.generation;

import com.jptest.payments.fulfillment.testonia.model.PostOperationValidationsInput;
import com.jptest.payments.fulfillment.testonia.model.PostPaymentRequest;
import com.jptest.payments.fulfillment.testonia.model.TestCaseInputData;
import com.jptest.payments.fulfillment.testonia.model.dynamicvalue.GoldenFilePlaceHolderDTO;
import com.jptest.payments.fulfillment.testonia.model.dynamicvalue.GoldenFilePlaceHolderGenerationStats;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.transform.TransformerException;
import java.nio.file.Path;


/**
 * Service that helps generating placeholders in golden files
 * 
 * @see GoldenFilePlaceHolderPreGeneratorListener
 */
@Singleton
public class GoldenFilePlaceHolderGeneratorService {

    @Inject
    private SenderGoldenFilePlaceHolderGenerator senderGoldenFileGenerator;

    @Inject
    private RecipientGoldenFilePlaceHolderGenerator recipientGoldenFileGenerator;

    public void generatePlaceHolders(String testName, Path jsonFilePath, TestCaseInputData data,
            GoldenFilePlaceHolderGenerationStats stats) throws TransformerException {
        // 1. Placeholders for golden file in PaymentValidationsInput
        generatePlaceHolders(testName, jsonFilePath, data, stats, data.getPaymentValidationsInput());
        // 2. Placeholders for golden file in PostOperationValidationsInput
        generatePlaceHolders(testName, jsonFilePath, data, stats, data.getPostOperationValidationsInput());
        // 3. Placeholders for golden file in request
        if (data.getPostPaymentRequests() != null) {
            for (PostPaymentRequest request : data.getPostPaymentRequests()) {
                generatePlaceHolders(testName, jsonFilePath, data, stats, request.getPostOperationValidationsInput());
            }
        }
    }

    private void generatePlaceHolders(String testName, Path jsonFilePath, TestCaseInputData data,
            GoldenFilePlaceHolderGenerationStats stats, PostOperationValidationsInput validations)
            throws TransformerException {
        if (validations != null) {
            if (validations.getPaymentSenderValidation() != null && StringUtils.isNotBlank(
                    validations.getPaymentSenderValidation().getGoldenFileLocation())) {
                senderGoldenFileGenerator.generateAndReplacePlaceHolder(buildDTO(testName, jsonFilePath,
                        validations.getPaymentSenderValidation().getGoldenFileLocation(), stats,
                        data));
            }
            if (validations.getPaymentRecipientValidation() != null
                    && StringUtils.isNotBlank(validations.getPaymentRecipientValidation()
                            .getGoldenFileLocation())) {
                recipientGoldenFileGenerator.generateAndReplacePlaceHolder(buildDTO(testName, jsonFilePath,
                        validations.getPaymentRecipientValidation().getGoldenFileLocation(),
                        stats, data));
            }
        }

    }

    private static GoldenFilePlaceHolderDTO buildDTO(String testName, Path jsonFilePath, String goldenFileLocation,
            GoldenFilePlaceHolderGenerationStats stats, TestCaseInputData data) {
        GoldenFilePlaceHolderDTO dto = new GoldenFilePlaceHolderDTO(jsonFilePath, goldenFileLocation, stats);
        dto.setTestName(testName);
       // dto.setSender(data.getBuyerData().getUser());
        //dto.setRecipient(data.getSellerData().getUser());
        dto.setTransactionCurrency(data.getFulfillPaymentPlanOptions().getTxnAmount().getCurrencyCode());
        dto.setFundingSource(data.getFulfillPaymentPlanOptions().getFundingSource());
       /* if (CollectionUtils.isNotEmpty(data.getPostPaymentRequests())) {
            data.getPostPaymentRequests().stream()
                    .filter(p -> p.getRequest() instanceof RecoverDisputedFundsRequest)
                    .forEach(p -> setChargeBackGrade(dto, p));
        }*/

        return dto;
    }

    private static void setChargeBackGrade(GoldenFilePlaceHolderDTO dto, PostPaymentRequest p) {
        /*RecoverDisputedFundsRequest request = (RecoverDisputedFundsRequest) p.getRequest();
        if (request.getApplyChargebackFee()) {
            dto.setChargeBackGrade(request.getChargebackGrade());
        }*/
    }
}
