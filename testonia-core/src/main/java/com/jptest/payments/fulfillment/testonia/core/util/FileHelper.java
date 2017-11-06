package com.jptest.payments.fulfillment.testonia.core.util;

import com.jptest.payments.fulfillment.testonia.model.util.ResourceUtil;
import org.apache.commons.configuration.ConfigurationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileSystemUtils;
import org.testng.TestException;

import javax.inject.Singleton;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Exposes file related utility methods
 */
@Singleton
public class FileHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileHelper.class);

    /**
     * Read file content from JAR file
     *
     * @param //file File
     * @return String
     * @throws IOException
     */
    public String readContent(String fileLocation) throws IOException {
        URL fileURL = ConfigurationUtils.locate(fileLocation);
        LOGGER.info("Loading file {} from URL {}", fileLocation, fileURL);
        return readContent(fileURL.openStream());
    }

    /**
     * Read file content from src/test/resources directory
     * <p>
     * Using ConfigurationUtils to read golden files brings ClassLoader into the picture which sometimes results in
     * stale reads
     * <p>
     * To fix that, now directly reading golden files from src/test/resources directory
     * 
     * @param fileLocation
     * @return
     * @throws IOException
     */
    public String readContentFromTestResource(String fileLocation) throws IOException {
        String file = ResourceUtil.getTestResourceDirectory() + fileLocation;
        String fileContent = null;
        try (FileInputStream fis = new FileInputStream(new File(file))) {
            fileContent = readContent(fis);
        }
        return fileContent;
    }

    public String readContent(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader isr = new InputStreamReader(inputStream, "UTF-8");
        BufferedReader br = new BufferedReader(isr, 4096);
        String line = null;
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (br != null) {
                br.close();
            }
        }
        return sb.toString();
    }

    public void writeToFile(String fileLocation, String os) {
        try {
            File file = new File(fileLocation);
            // if file doesnt exists, then create it
            if (!file.exists()) {
                // if parent directory does not exist create it
                if (!file.getParentFile().exists()) {
                    boolean dirCreated = file.getParentFile().mkdirs();
                    if (!dirCreated) {
                        LOGGER.warn("Unable to create new directory");
                    }
                }
                boolean fileCreated = file.createNewFile();
                if (!fileCreated) {
                    LOGGER.warn("Unable to create new file");
                }
            }
            Writer writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            try (BufferedWriter bw = new BufferedWriter(writer)) {
                bw.write(os);
            }
            LOGGER.info("Stored content on file at {}", fileLocation);
        } catch (IOException e) {
            LOGGER.error("Error writing a file:{}", fileLocation, e);
        }

    }

    public File getFileObject(String filePath) {
        try {
            return new File(this.getClass().getClassLoader().getResource(filePath).toURI());
        } catch (URISyntaxException e) {
            throw new TestException("Unable to load the file: " + filePath);
        }
    }

    public boolean deleteDirectory(File dir) {
        return FileSystemUtils.deleteRecursively(dir);
    }

    public boolean createDirectory(String path) {
        boolean flag = new File(path).mkdirs();
        return flag;
    }
}
