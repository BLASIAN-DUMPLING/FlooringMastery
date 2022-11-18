/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FlooringDao;

import FlooringService.DataPersistenceException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

/**
 *
 * @author crjos
 */
public class fmAuditDaoFileImpl implements fmAuditDao {

    public static String AUDIT_FILE = "audit.txt";
    
    /**
     * Sets file audit DAO will read from to default.
     */
    public fmAuditDaoFileImpl() {
        AUDIT_FILE = "audit.txt";
    }
    
    public fmAuditDaoFileImpl(String AUDIT_FILE) {
        fmAuditDaoFileImpl.AUDIT_FILE = AUDIT_FILE;
    }
   
    @Override
    public void writeAuditEntry(String entry) throws DataPersistenceException {
        PrintWriter out;
       
        try {
            out = new PrintWriter(new FileWriter(AUDIT_FILE, true));
        } catch (IOException e) {
            throw new DataPersistenceException("Could not persist audit information.", e);
        }
 
        LocalDateTime timestamp = LocalDateTime.now();
        out.println(timestamp.toString() + " : " + entry);
        out.flush();
    }  
    
}
