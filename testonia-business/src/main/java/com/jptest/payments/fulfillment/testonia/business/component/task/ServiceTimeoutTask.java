package com.jptest.payments.fulfillment.testonia.business.component.task;

import javax.inject.Inject;

import com.jpinc.kernel.cal.api.sync.CalTransactionHelper;
import com.jptest.payments.fulfillment.testonia.bridge.MsMonitorBridge;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseTask;

/**
 * This task is to simulate component timeout in ms stage enviroment Currently
 * works for user stages. MS team is working to make it work on msmaster
 * 
 * @JP Inc.
 *
 */
public class ServiceTimeoutTask extends BaseTask<Void> {

	private ServiceTimeoutTaskInput input;

	@Inject
	private MsMonitorBridge msMonitorBridge;

	public ServiceTimeoutTask(ServiceTimeoutTaskInput faultData) {
		super();
		this.input = faultData;
	}

	@Override
	public Void process(Context context) {
		String correlationId = CalTransactionHelper.getTopTransaction().getCorrelationId();
		msMonitorBridge.serviceTimeout(correlationId, input.getPort(), input.getDuration());
		return null;
	}

	public static class ServiceTimeoutTaskInput {
		private String port;
		private int duration;

		public ServiceTimeoutTaskInput(String port, int duration) {
			super();
			this.port = port;
			this.duration = duration;
		}

		public int getDuration() {
			return duration;
		}

		public void setDuration(int duration) {
			this.duration = duration;
		}

		public String getPort() {
			return port;
		}

	}
}
