/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FlooringController;

import FlooringDto.Order;
import FlooringService.ConsoleClearingException;
import FlooringService.DataPersistenceException;
import FlooringService.DataValidationException;
import FlooringService.NoSuchOrderException;
import FlooringService.OrderAlreadyCancelledException;
import FlooringService.fmServiceLayer;
import UI.fmView;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 *
 * @author crjos
 */
public class fmController {
    
       // View and service layer passed to and used by this controller
    private final fmView view;
    private final fmServiceLayer service;
    
    private final String FORMAT = "MM/dd/yyyy";
    
    private final int MIN_AREA = 100;
    private final int MAX_AREA = 30000000;
    private final int MAX_SCALE = 2;
   
    
    public fmController(fmView view, fmServiceLayer service) {
        this.view = view;
        this.service = service;
    }
    
  
    public void run() throws DataValidationException {
        
        boolean keepGoing = true;
        int menuSelection;
        try {
            service.validateData();
            
            while (keepGoing) {
                
                menuSelection = getMenuSelection();

                switch (menuSelection) {
                    case 1:
                        displayOrders();
                        break;
                    case 2:
                        addOrder();
                        break;
                    case 3:
                        editOrder();
                        break;
                    case 4:
                        cancelOrder();
                        break;
                    case 5:
                        exportData();
                        break;
                    case 6:
                        keepGoing = false;
                        break;
                    default:
                        unknownCommand();
                }

            }
            exitMessage();
        } catch (DataPersistenceException | ConsoleClearingException  e) {
            // Ensure user is informed when error occurs
            view.displayErrorMessage(e.getMessage());
        }
    }
    
  
    private int getMenuSelection() {
        return view.printMenuAndGetSelection();
    }
    
    
    private void displayOrders() throws 
            DataPersistenceException, 
            ConsoleClearingException {
        LocalDate dateChoice = view.getDateChoice(FORMAT);
        List<Order> orderList = service.getOrdersAtDate(dateChoice);
        view.displayDisplayAllAtDateBanner(dateChoice.format(DateTimeFormatter.ofPattern(FORMAT)));
        view.displayOrderList(orderList);
    }
    
   
    private void addOrder() throws DataPersistenceException, ConsoleClearingException {
        // Make view display create banner to inform user
        view.displayCreateOrderBanner();
        boolean hasErrors;
        Order createdOrder;
        
        do {
            String newOrderDetails[] = view.getNewOrderInfo(FORMAT, MIN_AREA, MAX_AREA, MAX_SCALE);
            try {
                createdOrder = service.createOrder(newOrderDetails);
                if (view.displayOrderAndConfirm(createdOrder, "create")) {
                    view.displayCreateSuccessBanner();
                    service.addOrder(createdOrder, "created");
                    hasErrors = false;
                } else {
                    hasErrors = view.getTryAgainChoice();
                }
            } catch (DataValidationException e) {
                view.displayErrorMessage(e.getMessage());
                hasErrors = view.getTryAgainChoice();
            }
        } while (hasErrors);
    }
    
   
    private void editOrder() throws DataPersistenceException, ConsoleClearingException {
        boolean hasErrors;
        Order chosenOrder;
        Order orderToEdit;
        // Make view display edit banner to inform user
        view.displayEditOrderBanner();

        do {
            LocalDate date = view.getDateChoice(FORMAT);
            int orderNumber = view.getOrderNumber();
            try {
                chosenOrder = service.getOrder(orderNumber, date);
                String newOrderDetails[] = view.getEditOrderInfo(chosenOrder,FORMAT, MIN_AREA, MAX_AREA, MAX_SCALE);
                orderToEdit = service.editOrder(orderNumber, newOrderDetails);
                if (orderToEdit.equals(chosenOrder)) {
                    view.displayNoEditSuccessBanner();
                    hasErrors = false;
                } else if (view.displayOrderAndConfirm(orderToEdit, "edit")) {
                    view.displayEditSuccessBanner();
                    service.addOrder(orderToEdit, "edited");
                    hasErrors = false;
                } else {
                    hasErrors = view.getTryAgainChoice();
                }
            } catch (DataValidationException | NoSuchOrderException e) {
                view.displayErrorMessage(e.getMessage());
                hasErrors = view.getTryAgainChoice();
            }
        } while (hasErrors);
    }
    
    
    private void cancelOrder() throws DataPersistenceException, ConsoleClearingException {
        boolean hasErrors;
        Order orderToCancel;
        // Make view display create banner to inform user
        view.displayCancelOrderBanner();
        
        do {
            LocalDate date = view.getDateChoice(FORMAT);
            int orderNumber = view.getOrderNumber();
            try {
                orderToCancel = service.getOrder(orderNumber, date);
                if (view.displayOrderAndConfirm(orderToCancel, "cancel")) {
                    service.cancelOrder(orderToCancel);
                    view.displayCancelSuccessBanner();
                    hasErrors = false;
                } else {
                    hasErrors = view.getTryAgainChoice();
                }
            } catch (OrderAlreadyCancelledException | NoSuchOrderException e) {
                view.displayErrorMessage(e.getMessage());
                hasErrors = view.getTryAgainChoice();
            }
        } while (hasErrors);
    }
    
    
    private void exportData() throws DataPersistenceException, ConsoleClearingException {
        view.displayExportDataBanner();
        service.exportData();
        view.displayExportSuccessBanner();
    }
    
   
    private void unknownCommand() {
        view.displayUnknownCommandBanner();
    }
  
    private void exitMessage() {
        view.displayExitMessage();
    }
    
}
