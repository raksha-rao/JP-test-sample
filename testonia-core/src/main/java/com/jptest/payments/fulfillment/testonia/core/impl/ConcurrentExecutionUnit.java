package com.jptest.payments.fulfillment.testonia.core.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;

import com.jpinc.kernel.executor.TaskExecutor;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.Unit;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.guice.GuiceInjector;
import com.jptest.payments.fulfillment.testonia.core.impl.ExecutionResult.ComponentExecutionStatus;
import org.apache.commons.configuration.Configuration;

/**
 * Implementation of {@link Unit} where the unit is an aggregation of other
 * instances of {@link Unit} In this implementation it is assumed that all the
 * Unit instances are independent of each other and can be triggered at
 * the same type in parallel threads. For more details
 * @see Unit
 */
public class ConcurrentExecutionUnit implements Unit, GuiceInjector {

    private String name;

    private List<Unit> units = new ArrayList<Unit>();

    @Inject
    private TaskExecutor executor;

    @Inject
    private Configuration configuration;

    public ConcurrentExecutionUnit(String id, Unit... Units) {
        this(id, Arrays.asList(Units));
    }

    public ConcurrentExecutionUnit(String id, List<Unit> Units) {
        super();
        name = id;
        units = Units;
        initializeDependencies();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getName()).append(" UNITS: ");
        if (!CollectionUtils.isEmpty(units)) {
            for (Unit unit : units) {
                sb.append(unit.toString()).append(" | ");
            }
            sb.replace(sb.length() - 2, sb.length(), "");
        } else {
            sb.append("NO CONCURRENT UNITS DEFINED");
        }
        return sb.toString();

    }

    @Override
    public ExecutionResult execute(Context context) throws TestExecutionException {

        boolean ignoreTaskTimeouts = configuration.getBoolean(CoreConfigKeys.IGNORE_TASK_TIMEOUTS.getName(), false);
        long defaultTimeoutMs = configuration.getLong(CoreConfigKeys.DEFAULT_TASK_TIMEOUT_IN_MS.getName(), 500);
        ComponentExecutor<ExecutionResult> exec = new ComponentExecutor<ExecutionResult>(executor, ignoreTaskTimeouts, defaultTimeoutMs);
        List<ExecutionResult> results = exec.executeAll(units, context);

        for (ExecutionResult result : results) {
            if (result.getStatus().equals(ComponentExecutionStatus.FAILED)) {
                return result;
                //				throw new TestExecutionException("Unit failed with error: ", result.getErrorCause());
            }
        }
        return new ExecutionResult(ComponentExecutionStatus.SUCCESS);

    }

}
