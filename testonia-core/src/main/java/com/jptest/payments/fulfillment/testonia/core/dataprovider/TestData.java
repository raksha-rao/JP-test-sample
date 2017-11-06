package com.jptest.payments.fulfillment.testonia.core.dataprovider;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * To be used in conjunction with {@link TestoniaDataProvider}
 * <p>
 * <li>filename and type are mandatory attributes</li>
 * <li>indices starts at 0</li>
 * <p>
 * Sample usage:
 * @TestData(filename="somedatafile.json", type = SomeClass.class, indices = {0,1,2})
 */
@Retention(RUNTIME)
@Target({ METHOD })
public @interface TestData {

    String filename();

    Class<?> type();

    int[] indices() default {};

}
