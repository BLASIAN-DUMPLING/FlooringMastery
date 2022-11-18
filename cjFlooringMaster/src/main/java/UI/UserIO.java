/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package UI;

import FlooringService.ConsoleClearingException;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *
 * @author crjos
 */
public interface UserIO {
    
     void print(String msg);
    

  
    String readString(String prompt);
    
 
    String readYesNo(String prompt);
    
   
    public LocalDate readLocalDate(String prompt, String format);
    
  
    public BigDecimal readBigDecimal(String prompt, String invalidInputResponse, int min, int max, int maxScale);
 
    public BigDecimal readBigDecimalOrEmpty(String prompt, String invalidInputResponse, int min, int max, int maxScale);

  
    double readDouble(String prompt);

  
    double readDouble(String prompt, double min, double max);

   
    float readFloat(String prompt);

   
    float readFloat(String prompt, float min, float max);

   
    int readInt(String prompt);

   
    int readInt(String prompt, int min, int max);
    
   
    int readIntWithMessages(String prompt, int min, int max, String tooSmall, String tooLarge);

 
    long readLong(String prompt);

   
    long readLong(String prompt, long min, long max);
    
    
    void clearConsole() throws ConsoleClearingException;
    
    
}
