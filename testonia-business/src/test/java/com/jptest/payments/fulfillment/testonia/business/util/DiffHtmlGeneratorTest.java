package com.jptest.payments.fulfillment.testonia.business.util;

import org.testng.annotations.Test;

import javax.inject.Inject;


/**
 * Test for {@link DiffHtmlGenerator}
 */
//@Guice(modules = DefaultTestoniaGuiceModule.class)
public class DiffHtmlGeneratorTest {

    @Inject
    private DiffHtmlGenerator generator;

    @Test
    public void testGenerateHtml() {
        String actualFileLocation = "htmlgenerator/actual.xml";
        String goldenFileLocation = "htmlgenerator/golden.xml";
        String diffLocation = "test-output/diff.html";
        generator.generateHtml(actualFileLocation, goldenFileLocation, diffLocation);
    }
}
