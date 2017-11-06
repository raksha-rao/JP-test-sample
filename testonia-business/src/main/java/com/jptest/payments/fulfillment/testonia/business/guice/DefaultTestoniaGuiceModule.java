package com.jptest.payments.fulfillment.testonia.business.guice;

/**
 * Default implementation of TestoniaBaseModule.
 * <p>
 * If your project does not have any project specific REST/ASF dependencies, 
 * then use this class for "Guice" annotation in your test class.
 * <p>
 * If your project does have project specific REST/ASF dependencies, 
 * then create a new class by extending TestoniaBaseModule and use that for
 * "Guice" annotation in your test class.
 */
public class DefaultTestoniaGuiceModule extends TestoniaBaseModule {

    @Override
    protected void configureProjectDependencies() {
        // No extra dependencies required
        // If you have any project specific dependencies then 
        // create your own Module class by overriding TestoniaBaseModule and
        // use that for your project.
    }
}
