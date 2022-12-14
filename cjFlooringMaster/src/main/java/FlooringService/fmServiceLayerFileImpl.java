/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FlooringService;

import FlooringDao.fmAuditDao;
import FlooringDao.fmOrderDao;
import FlooringDao.fmProductDao;
import FlooringDao.fmStateDao;
import FlooringDto.Costs;
import FlooringDto.Order;
import FlooringDto.Product;
import FlooringDto.State;
import FlooringDto.Statuses;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author crjos
 */
public class fmServiceLayerFileImpl implements fmServiceLayer {
  
    private final fmOrderDao dao;
    private final fmProductDao productDao;
    private final fmStateDao stateDao;

    private final fmAuditDao auditDao;
    
 
    public fmServiceLayerFileImpl(fmOrderDao dao, fmProductDao productDao, fmStateDao stateDao, fmAuditDao auditDao) {
        this.dao = dao;
        this.productDao = productDao;
        this.stateDao = stateDao;
        this.auditDao = auditDao;
    }
    
    @Override
    public void validateData() throws 
            DataPersistenceException,
            DataValidationException {
        List<Order> orderList = dao.getOrders();
        for (Order order : orderList) {
            // Get State object in order and State object in file for comparison
            State orderState = order.getState();
            State retrievedState = stateDao.getState(orderState.getStateAbbreviation());
            // Order filename to inform user
            String orderFile = "Order_" + order.getDate().format(DateTimeFormatter.ofPattern("MMddyyyy")) + ".txt";
            // If State in order does not exist, or if its data is invalid, inform the user
            if (retrievedState != null) {
                if (retrievedState.getStateTaxRate().equals(orderState.getStateTaxRate())) {
                    // Set the order state to the state retrieved from the taxes file.
                    // The current order state has no name, as its details are retrieved
                    // from the order file.
                    order.setState(retrievedState);
                } else {
                    throw new DataValidationException(
                            "ERROR: State tax rate of Order " + order.getOrderNumber() + " in Order file " + orderFile + " is invalid according to Taxes file.");
                }
            } else {
                    throw new DataValidationException(
                            "ERROR: State " + orderState.getStateAbbreviation() + " of Order " 
                                    + order.getOrderNumber() + " in Order file " 
                                    + orderFile + " does not exist in Taxes file.");
            }
            
            // Get Product object in order and Product object in file for comparison
            Product orderProduct = order.getProduct();
            Product retrievedProduct = productDao.getProduct(orderProduct.getProductType());
            // If Product in order does not exist, or if its data is invalid, inform the user
            if (retrievedProduct != null) {
                if (!retrievedProduct.equals(orderProduct)) {
                    throw new DataValidationException(
                            "ERROR: Product details of Order " + order.getOrderNumber() 
                                    + " in Order file " + orderFile 
                                    + " are invalid according to Products file.");
                }
            } else {
                    throw new DataValidationException(
                            "ERROR: Product " + orderProduct.getProductType()+ " of Order " 
                                    + order.getOrderNumber() + " in Order file " 
                                    + orderFile + " does not exist in Products file.");
            }
        }
    }
    
    @Override
    public Order getOrder(int orderNumber, LocalDate date) throws 
            DataPersistenceException,
            NoSuchOrderException {
        Order retrievedOrder = dao.getOrder(orderNumber);
        // Throw error if order is null, or if date does not match
        if (retrievedOrder == null) {
            throw new NoSuchOrderException("ERROR: No order with entered details exists.");
        }
        if (!retrievedOrder.getDate().equals(date)) {
            throw new NoSuchOrderException("ERROR: An order with the entered number exists,"
                    + "but its order completion date does not match the entered date.");
        }
        
        return retrievedOrder;
    }

    @Override
    public List<Order> getOrdersAtDate(LocalDate date) throws DataPersistenceException {
        // Placed date checking here as logic
        return dao.getOrders().stream().filter((o) -> (o.getDate().compareTo(date) == 0)).collect(Collectors.toList());
    }

  
    @Override
    public Order createOrder(String[] newOrderDetails) throws 
            DataPersistenceException,
            DataValidationException {
        LocalDate date = LocalDate.parse(newOrderDetails[0], DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        String customerName =  newOrderDetails[1];
        String stateAbbreviation =  newOrderDetails[2];
        String productType =  newOrderDetails[3];
        BigDecimal area = new BigDecimal(newOrderDetails[4]);
        
        if (date.isBefore(LocalDate.now())) {
            throw new DataValidationException(
                    "ERROR: Could not create Order. Date "
                    + date
                    + " is in the past.");
        }
        
        if (date.isAfter(LocalDate.now().plusYears(5))) {
            throw new DataValidationException(
                    "ERROR: Could not create Order. Date "
                    + date
                    + " is more than five years from now."
                    + " Orders cannot be completed past five"
                    + " years in the future.");
        }
        
        if (area.compareTo(new BigDecimal(100)) < 0) {
            throw new DataValidationException(
                    "ERROR: Could not create Order. area "
                    + area
                    + " is less than 100.");
        }
        
        Product product;
        product = productDao.getProduct(productType);
        if (product == null) {
            throw new DataValidationException(
                    "ERROR: Could not create Order. Product "
                    + productType
                    + " is not in our list of products.");
        }
        
        State state;
        state = stateDao.getState(stateAbbreviation);
        if (state == null) {
            throw new DataValidationException(
                    "ERROR: Could not create Order. State "
                    + stateAbbreviation
                    + " is not in our tax records.");
        }
        
        Costs costs = getCosts(product, state, area);
        
        List<Order> orders = dao.getOrders();
        int maxOrderNumber = orders.stream().max(Comparator.comparing((o) -> o.getOrderNumber())).get().getOrderNumber();
        
        Order orderToCreate = new Order(maxOrderNumber + 1, LocalDateTime.now().withNano(0), date);
        orderToCreate.setCustomerName(customerName);
        orderToCreate.setStatus(Statuses.ACTIVE);
        orderToCreate.setProduct(product);
        orderToCreate.setState(state);
        orderToCreate.setCosts(costs);
        orderToCreate.setArea(area);
               
        return orderToCreate;
    }
    
    @Override
    public void addOrder(Order orderToAdd, String processType) throws DataPersistenceException {
        dao.addOrder(orderToAdd.getOrderNumber(), orderToAdd);
        auditDao.writeAuditEntry("Order " + orderToAdd.getOrderNumber() + " " + processType + ".");
    }
    
    @Override
    public Order editOrder(int orderNumber, String[] newDetails) throws 
            DataPersistenceException, 
            DataValidationException {
        Order orderToEdit = dao.getOrder(orderNumber);
        
        String newCustomerName = newDetails[0];
        String newStateAbbreviation = newDetails[1];
        String newProductType = newDetails[2];
        String newAreaString = newDetails[3];
        
        if (!newCustomerName.equals("")) {
            orderToEdit.setCustomerName(newCustomerName);
        }
        
        if (!newStateAbbreviation.equals("")) {
            State newState = stateDao.getState(newStateAbbreviation);
            if (newState == null) {
                throw new DataValidationException(
                        "ERROR: Could not edit Order. State "
                        + newStateAbbreviation
                        + " is not in our tax records.");
            }
            
            orderToEdit.setState(newState);
        }
        
        if (!newProductType.equals("")) {
            Product newProduct = productDao.getProduct(newProductType);
            if (newProduct == null) {
                throw new DataValidationException(
                        "ERROR: Could not edit Order. Product "
                        + newProductType
                        + " is not in our list of products.");
            }
            
            orderToEdit.setProduct(newProduct);
        }
        
        if (!newAreaString.equals("")) {
            BigDecimal newArea = new BigDecimal(newAreaString);
            if (newArea.compareTo(new BigDecimal(100)) < 0) {
                throw new DataValidationException(
                        "ERROR: Could not create Order. area "
                        + newArea
                        + " is less than 100.");
            }
            
            orderToEdit.setArea(newArea);
        }
        
        return orderToEdit;
    }
    
    @Override
    public void cancelOrder(Order order) throws 
            DataPersistenceException, 
            OrderAlreadyCancelledException {
        if (order.getStatus() == Statuses.CANCELLED) {
            throw new OrderAlreadyCancelledException(""
                    + "ERROR: The order was already cancelled.");
        }
        order.setStatus(Statuses.CANCELLED);
        dao.addOrder(order.getOrderNumber(), order);
    }

    @Override
    public void exportData() throws DataPersistenceException {
        dao.exportData();
    }
    
  
    private Costs getCosts(Product product, State state, BigDecimal area) {
        // MaterialCost = (Area * CostPerSquareFoot)
        BigDecimal materialCost = area.multiply(product.getCostPerSquareFoot());
        // LaborCost = (Area * LaborCostPerSquareFoot)
        BigDecimal laborCost = area.multiply(product.getLaborCostPerSquareFoot());
        
        // Tax = (MaterialCost + LaborCost) * (TaxRate/100)
        // Use scale of 4 when dividing for additional precision - will be rounded to standard 2 decimal points below
        BigDecimal taxCost = materialCost.add(laborCost).multiply(state.getStateTaxRate().divide(new BigDecimal(100), 10, RoundingMode.HALF_UP));
        // Total = (MaterialCost + LaborCost + Tax)
        BigDecimal totalCost = materialCost.add(laborCost).add(taxCost);
        
        // Create costs object and store each cost in it.
        Costs costs = new Costs();
        costs.setMaterialCost(materialCost.setScale(2, RoundingMode.HALF_UP));
        costs.setLaborCost(laborCost.setScale(2, RoundingMode.HALF_UP));
        costs.setTaxCost(taxCost.setScale(2, RoundingMode.HALF_UP));
        costs.setTotal(totalCost.setScale(2, RoundingMode.HALF_UP));
        
        return costs;
    }
    
}
