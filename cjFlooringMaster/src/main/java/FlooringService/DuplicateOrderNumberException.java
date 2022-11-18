/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FlooringService;

/**
 *
 * @author crjos
 */
public class DuplicateOrderNumberException extends Exception {

    public DuplicateOrderNumberException(String message) {
        super(message);
    }

    public DuplicateOrderNumberException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
