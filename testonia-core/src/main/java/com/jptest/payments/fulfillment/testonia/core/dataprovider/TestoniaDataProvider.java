package com.jptest.payments.fulfillment.testonia.core.dataprovider;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.guice.ConfigurationHelper;
import com.jptest.payments.fulfillment.testonia.core.util.FileHelper;

import net.sourceforge.cobertura.CoverageIgnore;

/**
 * Use in conjunction with {@link TestData} annotation
 * <p>
 * It reads the file specified by <code>TestData.name</code> 
 * and returns list of input objects of type <code>TestData.type</code>
 * <p>
 * @org.testng.annotations.Test(dataProviderClass=TestoniaDataProvider.class, dataProvider = "input")
 * @TestData(filename="somedatafile.json", type = SomeClass.class, indices = {0,1,2})
 * <p>
 * Refer to {@link com.jptest.payments.fulfillment.testonia.sample.tests.TestoniaSampleTest} for sample.
 */
@CoverageIgnore
public class TestoniaDataProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestoniaDataProvider.class);

    private static final String TEST_DATA_FILE_LOCATION = "test.data.file.location";

    private static FileHelper fileHelper = new FileHelper();

    private static ObjectMapper mapper = new ObjectMapper();

    @DataProvider(name = "input")
    public static Object[][] input(Method testCase) {
        return buildInput(testCase);
    }

    @DataProvider(name = "parallelInput", parallel = true)
    public static Object[][] parallelInput(Method testCase) {
        return buildInput(testCase);
    }

    private static Object[][] buildInput(Method testCase) {
        // validate annotation
        TestData testData = testCase.getAnnotation(TestData.class);
        if (testData == null) {
            throw new TestExecutionException("TestData annotation missing for test case: " + testCase.getName());
        }

        List inputList = readJson(testData, testCase.getName());

        Object[][] input = new Object[inputList.size()][1];
        for (int i = 0; i < inputList.size(); i++) {
            input[i][0] = inputList.get(i);
        }

        return filterIndices(testData.indices(), input);
    }

    private static List readJson(TestData testData, String testcaseName) {
        try {
        	resolveSetterConflict();
            return mapper.readValue(getFileForInputData(testData.filename()),
                    mapper.getTypeFactory().constructCollectionType(List.class, testData.type()));
        } catch (IOException e) {
            String message = "couldn't load the test input data for test case: " + testcaseName;
            LOGGER.error(message, e);
            throw new TestExecutionException(message, e);
        }
    }

    private static Object[][] filterIndices(int[] indices, Object[][] input) {
        Object[][] filteredInput = input;
        if (indices.length > 0) {
            filteredInput = new Object[indices.length][];
            for (int j = 0; j < indices.length; j++) {
                filteredInput[j] = input[indices[j]];
            }
        }
        return filteredInput;
    }

    private static File getFileForInputData(String fileName) {
        String baseDir = ConfigurationHelper.getTestConfiguration().getString(TEST_DATA_FILE_LOCATION);
        return fileHelper.getFileObject(FilenameUtils.concat(baseDir, fileName));
    }

    private static void resolveSetterConflict() {
		// Resolving Setter Conflicts in PostPaymentRequests
		@SuppressWarnings("serial")
		final JacksonAnnotationIntrospector resolveConflictstoSetters = new JacksonAnnotationIntrospector() {
			@Override
			public AnnotatedMethod resolveSetterConflict(final MapperConfig<?> config, final AnnotatedMethod setter1,
					final AnnotatedMethod setter2) {

				final Class<?> cls1 = setter1.getRawParameterType(0);
				final Class<?> cls2 = setter2.getRawParameterType(0);
				if (cls1.isPrimitive()) {
					if (!cls2.isPrimitive()) {
						return setter1;
					}
				} else if (cls2.isPrimitive()) {
					return setter2;
				}

				if (cls1 == String.class) {
					if (cls2 != String.class) {
						return setter1;
					}
				} else if (cls2 == String.class) {
					return setter2;
				}
				return setter2;
			}

		};

		mapper.setAnnotationIntrospector(resolveConflictstoSetters);

	}

}
