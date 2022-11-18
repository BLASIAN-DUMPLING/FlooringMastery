public class fmOrderDaoStubImpl implements fmOrderDao {

    private Order testOrder1;
    private Order testOrder2;
    private State testState;
    private Product testProduct;

    public fmOrderDaoStubImpl() {
        LocalDateTime testCreationDate = LocalDateTime.now().withNano(0);
        LocalDate testOrderDate = LocalDate.parse("07012022", DateTimeFormatter.ofPattern("MMddyyyy"));
        
        // Make test order for list
        testOrder1 = new Order(1, testCreationDate, testOrderDate);
        
        testState = new State("CA", "California", new BigDecimal("25.00"));
        testProduct = new Product("Tile", new BigDecimal("3.50"), new BigDecimal("4.15"));
        
        Costs testCosts = new Costs();
        testCosts.setMaterialCost(new BigDecimal("871.50"));
        testCosts.setLabourCost(new BigDecimal("1033.35"));
        testCosts.setTaxCost(new BigDecimal("476.21"));
        testCosts.setTotal(new BigDecimal("2381.06"));
        
        testOrder1.setCustomerName("Ada Lovelace");
        testOrder1.setStatus(Statuses.ACTIVE);
        testOrder1.setArea(new BigDecimal("249.00"));
        testOrder1.setProduct(testProduct);
        testOrder1.setState(testState);
        testOrder1.setCosts(testCosts);
        
        testOrderDate = LocalDate.parse("06012022", DateTimeFormatter.ofPattern("MMddyyyy"));
        
        // Make test order for list
        testOrder2 = new Order(2, testCreationDate, testOrderDate);
        
        testOrder2.setCustomerName("Ada Lovelace");
        testOrder2.setStatus(Statuses.ACTIVE);
        testOrder2.setArea(new BigDecimal("249.00"));
        testOrder2.setProduct(testProduct);
        testOrder2.setState(testState);
        testOrder2.setCosts(testCosts);
    }
    
    public fmOrderDaoStubImpl(Order testOrder1, Order testOrder2) {
        this.testOrder1 = testOrder1;
        this.testOrder2 = testOrder2;
    }
    
    @Override
    public Order getOrder(int orderNumber) throws DataPersistenceException {
        if (orderNumber == testOrder1.getOrderNumber()) {
            return testOrder1;
        } else if (orderNumber == testOrder2.getOrderNumber()) {
            return testOrder2;
        } else {
            return null;
        }
    }

    @Override
    public List<Order> getOrders() throws DataPersistenceException {
        List<Order> orderList = new ArrayList<>();
        orderList.add(testOrder1);
        orderList.add(testOrder2);
        
        return orderList;
    }

    @Override
    public Order addOrder(int orderNumber, Order order) throws DataPersistenceException {
        if (orderNumber == testOrder1.getOrderNumber()) {
            return testOrder1 = order;
        } else {
            return null;
        }
    }

    @Override
    public void exportData() throws DataPersistenceException {
        // Do nothing
    }
    
}
