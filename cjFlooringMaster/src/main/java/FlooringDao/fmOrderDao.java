/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package FlooringDao;

import FlooringDto.Order;
import FlooringService.DataPersistenceException;
import java.util.List;

/**
 *
 * @author crjos
 */
public interface fmOrderDao {
    
    
    Order getOrder(int orderNumber) throws DataPersistenceException;
    
    
    List<Order> getOrders() throws DataPersistenceException;
    
   
    Order addOrder(int orderNumber, Order order) throws DataPersistenceException;
    
     
    void exportData() throws DataPersistenceException;

   
    
}
