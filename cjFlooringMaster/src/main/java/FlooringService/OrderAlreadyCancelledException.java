/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FlooringService;

/**
 *
 * @author crjos
 */
public class OrderAlreadyCancelledException extends Exception {

    public OrderAlreadyCancelledException(String message) {
        super(message);
    }

    public OrderAlreadyCancelledException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
