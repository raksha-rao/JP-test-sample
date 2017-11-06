package com.jptest.payments.fulfillment.testonia.business.component.task;

import javax.inject.Inject;

import com.jpinc.kernel.cal.api.sync.CalTransactionHelper;
import com.jptest.payments.fulfillment.testonia.bridge.MsMonitorBridge;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseTask;
import org.apache.commons.configuration.Configuration;

/**
 * This task is to simulate component markdown in ms stage enviroment
 * Currently works for user stages. MS team is working to make it work on msmaster
 * 
 * @JP Inc.
 *
 */
public class ServiceMarkdownTask extends BaseTask<Void>{
	
	private final static String PORT_SUFFIX_KEY = ".port";
	@Inject
	private MsMonitorBridge msMonitorBridge;
	@Inject
	private Configuration config;
	
	private String port;
	
	public ServiceMarkdownTask(String serviceName) {
		port = config.getString(serviceName + PORT_SUFFIX_KEY);
    }

	@Override
	public Void process(Context context) {
		String correlationId = CalTransactionHelper.getTopTransaction().getCorrelationId();
		msMonitorBridge.serviceMarkDown(correlationId, port);
		return null;
	}
}
