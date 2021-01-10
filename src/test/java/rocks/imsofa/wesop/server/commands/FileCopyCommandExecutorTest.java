/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.imsofa.wesop.server.commands;

import java.io.File;
import org.apache.commons.io.FileUtils;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author lendle
 */
public class FileCopyCommandExecutorTest {
    private Command command=null;
    public FileCopyCommandExecutorTest() {
    }

    @org.junit.jupiter.api.BeforeAll
    public static void setUpClass() throws Exception {
        
    }

    @org.junit.jupiter.api.AfterAll
    public static void tearDownClass() throws Exception {
    }

    @org.junit.jupiter.api.BeforeEach
    public void setUp() throws Exception {
        
    }

    @org.junit.jupiter.api.AfterEach
    public void tearDown() throws Exception {
    }

    /**
     * Test of canHandle method, of class FileCopyCommandExecutor.
     */
    @org.junit.jupiter.api.Test
    public void testCanHandle() {
        command = new Command();
        command.setGroupName("com.example.lendle.esopserver.commands");
        command.setName("copyFile");
        FileCopyCommandExecutor instance = new FileCopyCommandExecutor();
        boolean expResult = true;
        boolean result = instance.canHandle(command);
        assertEquals(expResult, result);
    }

    /**
     * Test of _execute method, of class FileCopyCommandExecutor.
     */
    @org.junit.jupiter.api.Test
    public void test_execute() throws Exception {
        command = new Command();
        command.setGroupName("com.example.lendle.esopserver.commands");
        command.setName("copyFile");
        String file="eCx5LHoNCjEsMiwwDQoyLDMsMA0KMyw0LDANCjQsNSwwDQo1LDYsMA0KNiw3LDANCjcsOCwxDQo4LDksMQ0KOSwxMCwxDQoxMCwxMSwxDQoxMSwxMiwxDQoxMiwxMywwDQoxMywxNCwxDQoxNCwxNSwxDQoxNSwxNiwwDQoxNiwxNywwDQoxNywxOCwxDQoxOCwxOSwxDQoxOSwyMCwxDQoyMCwyMSwwDQoyMSwyMiwxDQoyMiwyMywxDQoyMywyNCwxDQoyNCwyNSwxDQoyNSwyNiwxDQoyNiwyNywxDQoyNywyOCwxDQoyOCwyOSwwDQoyOSwzMCwwDQozMCwzMSwxDQozMSwzMiwxDQozMiwzMywxDQozMywzNCwxDQozNCwzNSwxDQozNSwzNiwxDQozNiwzNywxDQo=";
        command.getParams().put("file", file);
        command.getParams().put("fileName", "Book1.csv");
        command.getParams().put("fileDirectory", FileUtils.getTempDirectory().getAbsolutePath());
        FileCopyCommandExecutor instance = new FileCopyCommandExecutor();
        Object result = instance._execute(command);
        File targetFile=new File(FileUtils.getTempDirectory(), "Book1.csv");
        assertEquals(targetFile.exists(), true);
    }
    
}
