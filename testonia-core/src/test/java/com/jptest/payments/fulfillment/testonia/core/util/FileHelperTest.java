package com.jptest.payments.fulfillment.testonia.core.util;

import java.io.File;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FileHelperTest {

    @Test
    public void readContent() {
        FileHelper fileHelper = new FileHelper();
        try {
            String fileContent = fileHelper.readContent("testonia-core.properties");
            Assert.assertNotNull(fileContent, "File content can not be null");
        } catch (Exception e) {
            Assert.fail("Reading testonia-core.properties from classpath failed", e);
        }
    }

    @Test
    public void readContentFromTestResource() {
        FileHelper fileHelper = new FileHelper();
        try {
            String fileContent = fileHelper.readContentFromTestResource("temp.xml");
            Assert.assertNotNull(fileContent, "File content can not be null");
        }
        catch (Exception e) {
            Assert.fail("Reading temp.xml from src/test/resource directory failed", e);
        }
    }

    @Test
    public void getFileObject() {
        FileHelper fileHelper = new FileHelper();
        File file = fileHelper.getFileObject("testonia-core.properties");
        Assert.assertNotNull(file, "File object can not be null");
        Assert.assertTrue(file.exists(), "testonia-core.properties should be present in classpath");
    }
    
    @Test
    public void createAndDeleteDirectory() {
        try {
            String dirPath = "src/test/resources/tempdir1";
            FileHelper fileHelper = new FileHelper();
            fileHelper.createDirectory(dirPath);
            File file = new File(dirPath);
            fileHelper.deleteDirectory(file);
        }
        catch(Exception e){
            Assert.fail("createAndDeleteDirectory error:"+e.getMessage());
        }
    }
    
    @Test
    public void writeToFile() {
        try {
            String dirPath = "src/test/resources/tempdir2/";
            String filePath = dirPath + "tempfile.txt";
            FileHelper fileHelper = new FileHelper();
            fileHelper.writeToFile(filePath, "test");
            File file = new File(dirPath);
            fileHelper.deleteDirectory(file);
        }
        catch(Exception e){
            Assert.fail("writeToFile error:"+e.getMessage());
        }
    }
}
