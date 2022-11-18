
import FlooringDto.Costs;
import FlooringDto.Order;
import FlooringDto.Product;
import FlooringDto.State;
import FlooringDto.Statuses;
import FlooringService.DataPersistenceException;
import FlooringService.DataValidationException;
import FlooringService.NoSuchOrderException;
import FlooringService.OrderAlreadyCancelledException;
import FlooringService.fmServiceLayer;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;



/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author crjos
 */
public class fmServiceLayerFileImplTest {
    
     
private final fmServiceLayer testService;
    
    /**
     * Loads ApplicationContext, prepares test service layer object 
     * with respective bean from said context.
     */
    public fmServiceLayerFileImplTest() {
        // Load context from resources
        ApplicationContext ctx =
                new ClassPathXmlApplicationContext("applicationContext.xml");
        // Get controller object from its bean, everything in memory is built
        testService = 
                ctx.getBean("serviceLayer", fmServiceLayer.class);
    }
    
    @BeforeAll
    public static void setUpClass() {
        fmServiceLayerFileImplTest obj = new fmServiceLayerFileImplTest();
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }
    
    @Test
    public void testValidateData() {
        try {
            testService.validateData();
        } catch (DataPersistenceException ex) {
            Logger.getLogger(fmServiceLayerFileImplTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DataValidationException ex) {
            fail("Data should be valid.");
        }
    }
    
  
    @Test
    public void testGetOrder() throws DataPersistenceException, NoSuchOrderException {
        String[] newOrderDetails = {"07/01/2022","Ada Lovelace","CA","Tile","249.00"};
        
        Order correctOrder = makeTestOrder(1, "06012022");
        
        LocalDate testOrderDate = LocalDate.parse(07/01/2022, DateTimeFormatter.ofPattern("MMddyyyy"));
        Order testOrder = testService.getOrder(1, testOrderDate);
        try {
            testOrder = testService.getOrder(1, testOrderDate);
        } catch (NoSuchOrderException e) {
            System.out.print ("Order retrieval should have been successful.");
        }
        
        assertEquals(correctOrder.getOrderNumber(), testOrder.getOrderNumber());
        newOrderDetails[2] = "XX";
        
        // Check that correct error is printed in case of attempt to get Order
        // with invalid date.
        try {
            testOrder = testService.getOrder(1, LocalDate.MAX);
        } catch (NoSuchOrderException e) {
            String[] errorArr = e.toString().split(": ");
            assertEquals("ERROR: An order with the entered number exists,"
                    + "but its order completion date does not match the entered date.", 
                    errorArr[1] + ": " + errorArr[2], "Checking that correct error message is thrown.");
        }
        
        // Check that correct error is printed in case of attempt to get Order
        // with non existant order number.
        try {
            testOrder = testService.getOrder(10, testOrderDate);
        } catch (NoSuchOrderException e) {
            String[] errorArr = e.toString().split(": ");
            assertEquals("ERROR: No order with entered details exists.", 
                    errorArr[1] + ": " + errorArr[2], "Checking that correct error message is thrown.");
        }
    }

    /**
     * Tests that getting all orders gets a list of size 1 - the size the DAO stub gets.
     * Also test that only correct date retrieves the item
     * 
     * @throws DataPersistenceException 
     */
    @Test
    public void testGetOrdersAtDate() throws DataPersistenceException {
        // Test orders to test against order in list
        Order testOrder1 = makeTestOrder(1, "07012022");
        Order testOrder2 = makeTestOrder(2, "06012022");
        
        // Get orders with date 06012013
        List<Order> orderList = testService.getOrdersAtDate(LocalDate.parse("06012022", DateTimeFormatter.ofPattern("MMddyyyy")));
        
        // Confirm correct size of order list
        assertEquals(1, orderList.size(), "Should only have 1 Order.");
        
        assertOrdersEqual(testOrder2, orderList.get(0));
        
        // Assert that only order with correct date is found in list
        assertTrue(orderList.contains(testOrder2), "List should contain test Order 2.");
        assertFalse(orderList.contains(testOrder1), "List should not contain test Order 1.");
    }
    
    
    @Test
    public void testCreateOrder() throws DataPersistenceException, DataValidationException {
        String[] newOrderDetails = {"06/01/2022","Ada Lovelace","CA","Tile","249.00"};
        Order testOrder = null;
        Order correctOrder = makeTestOrder(3, "06012022");
        try {
            testOrder = testService.createOrder(newOrderDetails);
        } catch (DataValidationException e) {
            fail("Creation should have been valid.");
        }
        
        assertOrdersEqual(correctOrder, testOrder);
        newOrderDetails[2] = "XX";
        
        // Check that correct error is printed in case of attempt to create Order
        // with invalid data.
        try {
            testOrder = testService.createOrder(newOrderDetails);
        } catch (DataValidationException e) {
            String[] errorArr = e.toString().split(": ");
            assertEquals("ERROR: Could not create Order. State XX is not in our tax records.", 
                    errorArr[1] + ": " + errorArr[2], "Checking that correct error message is thrown.");
        }
    }
    
    /**
     * Tests that order edit only works with correct data,
     * and that the correct order is resultant.
     * Effectively, tests data validation of order editing.
    */
    @Test
    public void testEditOrder() throws DataPersistenceException {
        String[] newOrderDetails = {"Ada Lovelace","CA","Tile","249.00"};
        Order testOrder = null;
        Order correctOrder = makeTestOrder(1, "07012022");
        try {
            testOrder = testService.editOrder(1, newOrderDetails);
        } catch (DataValidationException e) {
            fail("Edit should have been valid.");
        }
        
        assertOrdersEqual(correctOrder, testOrder);
        newOrderDetails[2] = "XX";
        
        // Check that correct error is printed in case of attempt to edit Order
        // with invalid data.
        try {
            testOrder = testService.editOrder(1, newOrderDetails);
        } catch (DataValidationException e) {
            String[] errorArr = e.toString().split(": ");
            assertEquals("ERROR: Could not edit Order. Product XX is not in our list of products.", 
                    errorArr[1] + ": " + errorArr[2], "Checking that correct error message is thrown.");
        }
    }
    
    /**
     * Tests that order cancelling actually cancels order,
     * and that the error as a result of trying to cancel a
     * cancelled order is correct
    */
    @Test
    public void testCancelOrder() throws DataPersistenceException, OrderAlreadyCancelledException, NoSuchOrderException {
        Order correctOrder = makeTestOrder(1, "06012022");
        
        try {
            testService.cancelOrder(correctOrder);
        } catch (OrderAlreadyCancelledException e) {
            fail("Cancellation should have been valid.");
        }
        
        Order testOrder = makeTestOrder(1, "06012022");
        testOrder.setStatus(Statuses.CANCELLED);
        
        assertOrdersEqual(correctOrder, testOrder);
        
        // Check that correct error is printed in case of attempt to edit Order
        // with invalid data.
        try {
            testService.cancelOrder(correctOrder);
        } catch (OrderAlreadyCancelledException e) {
            String[] errorArr = e.toString().split(": ");
            assertEquals("ERROR: The order was already cancelled.", 
                    errorArr[1] + ": " + errorArr[2], "Checking that correct error message is thrown.");
        }
    }
    
    /**
     * Creates and returns a generic test order with passed order number and order date.
    */
    private Order makeTestOrder(int orderNumber, String dateString) throws DataPersistenceException {
        LocalDateTime testCreationDate = LocalDateTime.now().withNano(0);
        LocalDate testOrderDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("MMddyyyy"));
        
        // Make test order with same fields as known order in file
        Order testOrder = new Order(orderNumber, testCreationDate, testOrderDate);
        
        State testState = new State("CA", "California", new BigDecimal("25.00"));
        Product testProduct = new Product("Tile", new BigDecimal("3.50"), new BigDecimal("4.15"));
        
        Costs testCosts = new Costs();
        testCosts.setMaterialCost(new BigDecimal("871.50"));
        testCosts.setLaborCost(new BigDecimal("1033.35"));
        testCosts.setTaxCost(new BigDecimal("476.21"));
        testCosts.setTotal(new BigDecimal("2381.06"));
        
        testOrder.setCustomerName("Ada Lovelace");
        testOrder.setStatus(Statuses.ACTIVE);
        testOrder.setArea(new BigDecimal("249.00"));
        testOrder.setProduct(testProduct);
        testOrder.setState(testState);
        testOrder.setCosts(testCosts);
        
        return testOrder;
    }
    
    /**
     * Test whether a pair of orders are equal.
       */
    private void assertOrdersEqual(Order testOrder, Order retrievedOrder) {
        assertEquals(testOrder.getOrderNumber(),
                    retrievedOrder.getOrderNumber(),
                    "Checking order order number.");
        assertEquals(testOrder.getCustomerName(),
                    retrievedOrder.getCustomerName(),
                    "Checking order customer.");
        assertEquals(testOrder.getArea(),
                    retrievedOrder.getArea(),
                    "Checking order area.");
        assertEquals(testOrder.getState(), 
                    retrievedOrder.getState(),
                    "Checking order State object.");
        assertEquals(testOrder.getProduct(), 
                    retrievedOrder.getProduct(),
                    "Checking order Product object.");
        assertEquals(testOrder.getCosts(), 
                    retrievedOrder.getCosts(),
                    "Checking order Costs object.");
        assertEquals(testOrder.getDate(), 
                    retrievedOrder.getDate(),
                    "Checking order dates.");
        // nano seconds must be set to zero as objects made at slightly different times
        assertEquals(testOrder.getCreationDateTime().withNano(0), 
                    retrievedOrder.getCreationDateTime().withNano(0),
                    "Checking order creation times.");
        assertEquals(testOrder.getStatus(), 
                    retrievedOrder.getStatus(),
                    "Checking order statuses.");
    }
}
