package com.jptest.payments.fulfillment.testonia.core.guice;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestRunner;
import org.testng.collections.ListMultiMap;

import com.google.inject.Injector;
import com.google.inject.Module;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;

/**
 * For objects that has broader lifecycle than a testclass (e.g. TestListener, SLF4J Appender), 
 * guice module dependencies injected in test class are not available for these objects.
 * The framework needs to provide a way to inject guice modules in such objects (through partial injection). 
 * This class provides such work around.
 * <p>
 * To inject guice module dependencies, such objects should implement GuiceInjector interface and call its method inject(context, object)
 * <p>
 * For usage, checkout <code>TestResultSummaryReporter</code> and <code>LogstashAppender</code>
 */
public interface GuiceInjector {

    /**
     * Method that injects all guice module dependencies in input object.
     * 
     * The modules are retrieved from testng context.
     * 
     * @param context
     * @param object
     */
    default void inject(ITestContext context, Object object) {
        // integrate with guice
        Injector injector = context.getInjector(getGuiceModules(context));
        if (injector != null)
            injector.injectMembers(object);
    }

    default void initializeDependencies() {
        ITestResult result = Reporter.getCurrentTestResult();
        ITestContext testContext = result.getTestContext();
        inject(testContext, this);
    }

    /**
     * It gets the ItestContext from the current Reporter and uses it to inject the 
     * guice dependent members.
     * 
     * @param object
     */
    default void inject(Object object) {
        ITestResult result = Reporter.getCurrentTestResult();
        ITestContext testContext = result.getTestContext();
        inject(testContext, object);
    }

    /**
     * Cheating through reflection to get list of guice modules from testng context
     *  
     * @param context
     * @return
     */
    default List<Module> getGuiceModules(ITestContext context) {
        List<Module> returnList = new ArrayList<>();
        try {
            Field guiceModuleField = ((TestRunner) context).getClass().getDeclaredField("m_guiceModules");
            guiceModuleField.setAccessible(true);
            ListMultiMap<Class<? extends Module>, Module> guiceModules = (ListMultiMap<Class<? extends Module>, Module>) guiceModuleField
                    .get(context);
            Collection<List<Module>> values = guiceModules.getValues();
            for (List<Module> value : values) {
                returnList.addAll(value);
            }
        } catch (Exception e) {
            throw new TestExecutionException("Guice module retrieval failed with exception", e);
        }

        return returnList;
    }
}
