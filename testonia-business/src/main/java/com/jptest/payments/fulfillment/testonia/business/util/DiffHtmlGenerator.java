package com.jptest.payments.fulfillment.testonia.business.util;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Utility class that generates diff.html from db diff
 * 
 * @see //DiffHtmlGeneratorTest
 */
@Singleton
public class DiffHtmlGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiffHtmlGenerator.class);

    private static final String WHICH_PERL_COMMAND = "which perl";
    private static final String WHICH_BASH_COMMAND = "which bash";
    private static final String PERL = "perl";
    private static final String BASH = "bash";

    @Inject
    private Configuration config;

    public void generateHtml(String actualFileLocation, String goldenFileLocation, String diffHtmlLocation) {
        try {
            performWork(actualFileLocation, goldenFileLocation, diffHtmlLocation);
        } catch (Exception e) {
            LOGGER.warn("Error occurred generating diff.html file. Ignoring and moving forward... ", e);
        }
    }

    private void performWork(String actualFileLocation, String goldenFileLocation, String diffHtmlLocation)
            throws IOException {
        String perlBinary = executeCommand(WHICH_PERL_COMMAND, PERL);
        String bashBinary = executeCommand(WHICH_BASH_COMMAND, BASH);

        if (perlBinary == null || bashBinary == null) {
            LOGGER.info("Either perl or bash is not installed, returning");
            return;
        }
        
        LOGGER.info("Diff file is generated at {}", diffHtmlLocation);

        URL fileURL = ConfigurationUtils
                .locate(config.getString(BizConfigKeys.DIFF_HTML_COMPARE_SCRIPT_LOCATION.getName()));

        URL actualFileURL = ConfigurationUtils.locate(actualFileLocation);
        URL goldenFileURL = ConfigurationUtils.locate(goldenFileLocation);

        ProcessBuilder processBuilder = new ProcessBuilder(bashBinary, fileURL.getFile(), actualFileURL.getFile(),
                goldenFileURL.getFile(), diffHtmlLocation);
        processBuilder.redirectErrorStream(true);

        Process p = processBuilder.start();
        String processOutput = IOUtils.toString(p.getInputStream(), StandardCharsets.UTF_8);
        LOGGER.info("process output: \n*************\n{}\n*************", processOutput);
        try {
            p.waitFor();
        } catch (InterruptedException e) {
            LOGGER.warn("Failed while waiting for the process to finish {}", processBuilder.toString(), e);
        }
    }

    private String executeCommand(String command, String findString) {
        Process p = null;
        String executable = null;

        try {
            p = Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            LOGGER.warn("Failed while executing the comamnd: {}", command, e);
        }

        if (findString != null) {
            try (
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8));) {
                while ((executable = reader.readLine()) != null) {
                    if (executable.contains(findString)) {
                        break;
                    }
                }
            } catch (IOException e) {
                LOGGER.warn("Failed while reading the output of command execution of {}", command, e);
            }
        }
        return executable;
    }
}
