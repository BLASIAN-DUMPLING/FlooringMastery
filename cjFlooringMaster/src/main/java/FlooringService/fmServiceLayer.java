/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package FlooringService;

import FlooringDto.Order;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author crjos
 */
public interface fmServiceLayer {

    public void validateData() throws 
    DataPersistenceException,
    DataValidationException;


Order getOrder(int orderNumber, LocalDate date) throws 
    DataPersistenceException,
    NoSuchOrderException;


List<Order> getOrdersAtDate(LocalDate date) throws DataPersistenceException;


Order createOrder(String[] newOrderDetails) throws 
    DataPersistenceException,
    DataValidationException;


void addOrder(Order orderToAdd, String processType) throws DataPersistenceException;


Order editOrder(int orderNumber, String[] newDetails) throws 
    DataPersistenceException,
    DataValidationException;


void cancelOrder(Order order) throws 
    DataPersistenceException,
    OrderAlreadyCancelledException;

void exportData() throws DataPersistenceException;
    
    
}
