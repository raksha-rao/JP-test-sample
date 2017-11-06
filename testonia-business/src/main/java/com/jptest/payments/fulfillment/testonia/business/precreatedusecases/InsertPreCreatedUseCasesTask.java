package com.jptest.payments.fulfillment.testonia.business.precreatedusecases;

import java.math.BigInteger;
import java.util.List;
import javax.inject.Inject;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseTask;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.dao.pymt.PreCreatedUseCasesDao;
import com.jptest.payments.fulfillment.testonia.dao.txn.WTransactionP20DaoImpl;
import com.jptest.payments.fulfillment.testonia.model.pymt.PreCreatedUseCasesDTO;
import com.jptest.payments.fulfillment.testonia.model.util.WTransactionConstants;
import com.jptest.qi.rest.domain.pojo.User;


/**
 * This task Constructs the PreCreatedUseCasesDTO and using this inserts records into precreated_usecases table
 *
 * @JP Inc.
 */
public class InsertPreCreatedUseCasesTask extends BaseTask<PreCreatedUseCasesDTO> {

    @Inject
    private PreCreatedUseCasesDao preCreatedUseCasesDao;
    @Inject
    private WTransactionP20DaoImpl wTxnDAO;

    PreCreatedUseCasesDTO preCreatedUseCasesDTO = null;

    public InsertPreCreatedUseCasesTask(PreCreatedUseCasesDTO preCreatedUseCasesDTO) {
        this.preCreatedUseCasesDTO = preCreatedUseCasesDTO;
    }

    @Override
    public PreCreatedUseCasesDTO process(Context context) {

        final User seller = (User) context.getData(ContextKeys.SELLER_VO_KEY.getName());
        final User buyer = (User) context.getData(ContextKeys.BUYER_VO_KEY.getName());
        final User funder = (User) context.getData(ContextKeys.FUNDER_VO_KEY.getName());
        final List<BigInteger> txnId = this.wTxnDAO.getTransactionIdByAccountNumberAndType(
                new BigInteger(buyer.getAccountNumber()),
                String.valueOf(WTransactionConstants.Type.USERUSER.getValue()));

        this.preCreatedUseCasesDTO.setBuyerAccountNumber(new BigInteger(buyer.getAccountNumber()));
        this.preCreatedUseCasesDTO.setSellerAccountNumber(new BigInteger(seller.getAccountNumber()));
        this.preCreatedUseCasesDTO.setTransactionId(txnId.get(0));
        if(funder != null)
            this.preCreatedUseCasesDTO.setFunderAccountNumber(new BigInteger(funder.getAccountNumber()));
        this.preCreatedUseCasesDao.insertrecords(this.preCreatedUseCasesDTO);
        return this.preCreatedUseCasesDTO;
    }

}
