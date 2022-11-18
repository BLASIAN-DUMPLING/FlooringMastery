
import FlooringDao.fmAuditDao;
import FlooringDao.fmAuditDaoFileImpl;
import FlooringService.DataPersistenceException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author crjos
 */
public class fmAuditDaoFileImplTest {
    
     fmAuditDao testAuditDao;
    
    public fmAuditDaoFileImplTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    /**
     * Create or clear an output file and give our test audit DAO access.
     * 
     * @throws IOException 
     */
    @BeforeEach
    public void setUp() throws IOException {
        String testFile = "testlog.txt";
        // Use the FileWriter to quickly blank the file
        new FileWriter(testFile);
        testAuditDao = new fmAuditDaoFileImpl(testFile);
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test that audit DAO is writing logs to file correctly.
     * 
     * @throws DataPersistenceException 
     */
    @Test
    public void testWriteAuditEntry() throws DataPersistenceException {
        String testString = "log 1";
        testAuditDao.writeAuditEntry(testString);
        
        // Scanner from java.util.Scanner
        Scanner s;

        try {
            // Create Scanner for reading the file
            s = new Scanner(
                    // BuferredRead from java.io.BufferedReader
                    new BufferedReader(
                            // Filereader from java.io.FileReader
                            new FileReader("testlog.txt")));
        } catch (FileNotFoundException e) { // Catch and translate FileNotFoundException
            throw new DataPersistenceException(
                    "Could not load item data into memory.", e);
        }
        // line read from the file
        String line = s.nextLine();
        // Close scanner
        s.close();
        
        assertTrue(line.contains(testString));
    }
    
}
