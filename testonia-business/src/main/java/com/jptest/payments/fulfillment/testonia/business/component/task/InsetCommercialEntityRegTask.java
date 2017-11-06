package com.jptest.payments.fulfillment.testonia.business.component.task;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseTask;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.dao.cloc.WCommercialEntityRegDAO;
import com.jptest.qi.rest.domain.pojo.User;

/**
 * insert values into commercial_entity_reg for the given account number
 *
 * @param accountNumber
 * @return
 * 
 * @JP Inc.
 */

public class InsetCommercialEntityRegTask extends BaseTask<Void> {

	private static final Logger LOGGER = LoggerFactory.getLogger(InsetCommercialEntityRegTask.class);

	@Inject
	private WCommercialEntityRegDAO wCommercialEntityRegDAO;

	public Void process(Context context) {
		User user = (User) context.getData(ContextKeys.SELLER_VO_KEY.getName());

		int insertCount = wCommercialEntityRegDAO.insertCommercialEntity(user.getAccountNumber());

		LOGGER.info("Inserted Values into commercial_entity_reg :", insertCount);
		return null;
	}

}
