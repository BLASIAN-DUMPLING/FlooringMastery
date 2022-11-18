/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FlooringDao;

import FlooringDto.Costs;
import FlooringDto.Order;
import FlooringDto.Product;
import FlooringDto.State;
import FlooringDto.Statuses;
import FlooringService.DataPersistenceException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author crjos
 */
public class fmOrderDaoFileImpl implements fmOrderDao {

    
    HashMap<Integer, Order> orders = new HashMap<>();
    
    
    public final String ORDER_DIR;
    public final String BACKUP_DIR;
    
    public final String DELIMITER = ",";

   
    public fmOrderDaoFileImpl() {
        // Full directory required when
        this.ORDER_DIR = "Orders/";
        this.BACKUP_DIR = "Backup/";
    }
    
   
    public fmOrderDaoFileImpl(String ORDER_DIR, String BACKUP_DIR) {
        this.ORDER_DIR = ORDER_DIR;
        this.BACKUP_DIR = BACKUP_DIR;
    }
    
    @Override
    public Order getOrder(int orderNumber) throws DataPersistenceException {
        loadData();
        return orders.get(orderNumber);
    }

    /**
     *
     * @return
     * @throws DataPersistenceException
     */
    /*@Override
    public List<Order> getOrders() throws DataPersistenceException {
        loadData();
        return new ArrayList(orders.values());
    }*/

    @Override
    public Order addOrder(int orderNumber, Order order) throws DataPersistenceException {
        loadData();
        Order createdOrder = orders.put(orderNumber, order);
        writeData();
        return createdOrder;
    }
    
    @Override
    public void exportData() throws DataPersistenceException {
        // Printwriter from java.io.PrintWriter;
        PrintWriter out;
        
        File file = new File(BACKUP_DIR);
        file.mkdir();
        String directory = BACKUP_DIR + "DataExport.txt";

        try {
            new FileOutputStream(directory, true).close();
        } catch (FileNotFoundException e) {
            throw new DataPersistenceException("ERROR: Backup folder does not exist in current directory.");
        } catch (IOException e) {
            throw new DataPersistenceException(
                    "Could not save Order data.", e);
        }
        
        try {
            // Filewriter from java.io.FileWriter
            out = new PrintWriter(new FileWriter(directory));
        } catch (IOException e) {
            throw new DataPersistenceException(
                    "Could not save Order data.", e);
        }

        // Print header for order file
        out.println("OrderNumber,CustomerName,CreationDate,Status,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total,OrderDate");
        // Force PrintWriter to write line to the file
        out.flush();
        
        Collection<Order> orderObjects = orders.values();
        // Write out the Order objects to the respective orders file
        String orderAsText;
        for (Order currentOrder : orderObjects) {
            // turn a Order into a String
            orderAsText = marshallOrder(currentOrder) + currentOrder.getDate();
            // write the Order object to the file
            out.println(orderAsText);
            // force PrintWriter to write line to the file
            out.flush();
        }
        // Clean up
        out.close();
    }
    
    
    private List<String> getOrderFiles() throws DataPersistenceException {
        List<String> orderFiles = null;
        try (Stream<Path> walk = Files.walk(Paths.get(ORDER_DIR))) {

                orderFiles = walk.filter(Files::isRegularFile)
                                .map(x -> x.toString()).collect(Collectors.toList());
                
        } catch (IOException e) {
            // Translate IOException
            throw new DataPersistenceException(
                    "Could not load order data into memory with ORDER_DIR: " + ORDER_DIR + "," + e);
        }
        
        return orderFiles;
    }
    
  
    private void loadData() throws DataPersistenceException {
        LocalDate dateLocalDate;
        
        List<String> orderFiles = getOrderFiles();
        for (int i = 0; i < orderFiles.size(); i++) {
            // File name has format Orders_date.txt
            // First split file at '_' and get date.txt from index 1 of result
            // Then split date.txt at '.' and get date string from index 0
            String dateString = orderFiles.get(i).split("_")[1].split("\\.")[0];
            
            // Convert String to Date, Date to LocalDate
            try {
                Date dateDate = new SimpleDateFormat("MMddyyyy").parse(dateString);
                dateLocalDate = dateDate.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
            } catch (ParseException ex) {
                throw new DataPersistenceException(
                        "Error in format of Order file.", ex);
            }
            loadOrderFile(orderFiles.get(i), dateLocalDate);
        }
        
    }
    
   
    private void writeData() throws DataPersistenceException {
        // Group orders by date
        ArrayList<Order> orderList = new ArrayList<>(orders.values());
        Stream<Order> orderStream = orderList.stream();
        Map<String, List<Order>> groupedMap = orderStream.collect(Collectors.groupingBy((o) -> o.getDate().format(DateTimeFormatter.ofPattern("MMddyyyy"))));
        // Get each date
        Set<String> dateSet = groupedMap.keySet();
        
        // For each date
        for (String date: dateSet) {
            // File name needs format Orders_date.txt
            String fileName = "Orders_" + date + ".txt";
            writeOrderFile(ORDER_DIR + fileName, groupedMap.get(date));
        }        
    }
  
    private void loadOrderFile(String directory, LocalDate orderDate) throws DataPersistenceException {
        // Scanner from java.util.Scanner
        Scanner scanner;

        try {
            // Create Scanner for reading the file
            scanner = new Scanner(
                    // BuferredRead from java.io.BufferedReader
                    new BufferedReader(
                            // Filereader from java.io.FileReader
                            new FileReader(directory)));
        } catch (FileNotFoundException e) { 
            // Translate FileNotFoundException
            throw new DataPersistenceException(
                    "Could not load order data into memory with directory: " + directory + ".", e);
        }
        
        // currentLine holds the most recent line read from the file
        String currentLine;
        
        Order currentOrder;
        scanner.nextLine();
        while (scanner.hasNextLine()) {
            // get the next line in the file
            currentLine = scanner.nextLine();
            // unmarshall the line into an Order
            currentOrder = unmarshallOrder(currentLine, orderDate);
            // Put currentOrder into the map using order number as key
            orders.put(currentOrder.getOrderNumber(), currentOrder);
        }
        // close scanner
        scanner.close();
    }
    
    
    private void writeOrderFile(String directory, List<Order> ordersToWrite) throws DataPersistenceException {
        // Printwriter from java.io.PrintWriter;
        PrintWriter out;

        try {
            new FileOutputStream(directory, true).close();
        } catch (FileNotFoundException e) {
            throw new DataPersistenceException("ERROR: Orders folder does not exist in current directory.");
        } catch (IOException e) {
            throw new DataPersistenceException(
                    "Could not save Order data.", e);
        }
        
        try {
            // Filewriter from java.io.FileWriter
            out = new PrintWriter(new FileWriter(directory));
        } catch (IOException e) {
            throw new DataPersistenceException(
                    "Could not save Order data.", e);
        }

        // Print header for order file
        out.println("OrderNumber,CustomerName,CreationDate,Status,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total");
        // Force PrintWriter to write line to the file
        out.flush();
        
        // Write out the Order objects to the respective orders file
        String orderAsText;
        for (Order currentOrder : ordersToWrite) {
            // turn a Order into a String
            orderAsText = marshallOrder(currentOrder);
            // write the Order object to the file
            out.println(orderAsText);
            // force PrintWriter to write line to the file
            out.flush();
        }
        // Clean up
        out.close();
    }
   
    private Order unmarshallOrder(String orderAsText, LocalDate orderDate) throws DataPersistenceException {
        // split returns string array split on DELIMETER
        String[] orderFields = orderAsText.split(DELIMITER);
        
        Order order;
        int orderNumber;
        String customerName;
        LocalDateTime creationDate;
        Statuses status;
        BigDecimal area;
        
        Product product;
        String productType;
        BigDecimal costPerSquareFoot;
        BigDecimal labourCostPerSquareFoot;
        
        State state;
        String stateAbbreviation;
        BigDecimal stateTaxRate;
        
        Costs costs;
        BigDecimal materialCost;
        BigDecimal labourCost;
        BigDecimal taxCost;
        BigDecimal totalCost;
        
        try {
            // Order number should be in index 0 of the array.
            orderNumber = Integer.parseInt(orderFields[0]);
            
            // Order customer name should be in index 1 of the array.
            customerName = orderFields[1];
            
            // Creation date of order should be in index 2.
            creationDate = LocalDateTime.parse(orderFields[2]);
            
            // Make new object with order number and creation date from file, and passed order date.
            order = new Order(orderNumber, creationDate, orderDate);
            
            order.setCustomerName(customerName);
            
            // Status of the order should be in index 3.
            status = Statuses.valueOf(orderFields[3].toUpperCase());
            
            order.setStatus(status);
            
            // State abbreviation should be in index 4.
            stateAbbreviation = orderFields[4];
            
            // State tax rate should be in index 5.
            stateTaxRate = new BigDecimal(orderFields[5]);
            
          
            state = new State(stateAbbreviation, "", stateTaxRate);
            
            // Product type should be in index 6.
            productType = orderFields[6];
            
            // Area of flooring order should be in index 7.
            area = new BigDecimal(orderFields[7]);
            
            // If a read Object has invalid flooring area, thow an error
            if (area.compareTo(new BigDecimal(100)) < 0) {
                throw new DataPersistenceException("Area of flooring for a read object is too small.");
            }
            
            // Set the area of the order.
            order.setArea(area);
            
            // Cost per square foot of a product should be in index 8.
            costPerSquareFoot = new BigDecimal(orderFields[8]);
            
            // Labour cost per square foot should be in index 9.
            labourCostPerSquareFoot = new BigDecimal(orderFields[9]);
            
            // Make a new product object with input fields.
            product = new Product(productType, costPerSquareFoot, labourCostPerSquareFoot);
            
            // Material cost should be in index 10.
            materialCost = new BigDecimal(orderFields[10]);
                
            // Labour cost should be in index 11.
            labourCost = new BigDecimal(orderFields[11]);
            
            // Tax cost should be in index 12.
            taxCost = new BigDecimal(orderFields[12]);
            
            // Total cost should be in index 13.
            totalCost = new BigDecimal(orderFields[13]);
            
            // Create the new cost object and set all fields.
            costs = new Costs();
            costs.setMaterialCost(materialCost);
            costs.setLaborCost(labourCost);
            costs.setTaxCost(taxCost);
            costs.setTotal(totalCost);

            // Set necessary order fields to the respective created objects.
            order.setProduct(product);
            order.setState(state);
            order.setCosts(costs);
            
        } catch (IndexOutOfBoundsException e) {
            // Error translation, helpful message
            throw new DataPersistenceException(
                    "Error in format of an Order: try checking field count in order file.", e);        
        } catch ( NumberFormatException e) {
            throw new DataPersistenceException(
                    "Error in format of an Order: a field has an incorrect format.", e);
        } catch ( DateTimeParseException e) {
            throw new DataPersistenceException(
                    "Error in format of an Order: a creation date field has an incorrect format.", e);
        }

        // Return Order created from file
        return order;
    }
    
    
    
    private String marshallOrder(Order order) {
        
        // Get Product, State, Costs objects associated with passed order
        Product product = order.getProduct();
        State state = order.getState();
        Costs costs = order.getCosts();
        
        // Make String of each field to be printed to Order file, in order
        String orderAsText = order.getOrderNumber() + DELIMITER
                + order.getCustomerName() + DELIMITER
                + order.getCreationDateTime() + DELIMITER
                + order.getStatus() + DELIMITER
                + state.getStateAbbreviation() + DELIMITER
                + state.getStateTaxRate() + DELIMITER
                + product.getProductType() + DELIMITER
                + order.getArea() + DELIMITER
                + product.getCostPerSquareFoot() + DELIMITER
                + product.getLaborCostPerSquareFoot() + DELIMITER
                + costs.getMaterialCost() + DELIMITER
                + costs.getLaborCost() + DELIMITER
                + costs.getTaxCost() + DELIMITER
                + costs.getTotal();
        
        return orderAsText;
    }

    public List<Order> getOrders() throws DataPersistenceException {
      loadData();
        return new ArrayList(orders.values());
    }

    

   
}
