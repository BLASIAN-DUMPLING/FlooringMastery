/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FlooringDao;

import FlooringDto.Product;
import FlooringService.DataPersistenceException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Scanner;

/**
 *
 * @author crjos
 */
public class fmProductDaoFileImpl implements fmProductDao{
    
     HashMap<String, Product> products = new HashMap<>();
    
    // Directory to be used by this DAO, points to Products file
    public final String PRODUCT_DIR;
    // Delimiter used when marshalling/unmarshalling
    // Won't change
    public final String DELIMITER = ",";
    
    /**
     * Constructs a FlooringMasteryProductDaoFileImpl object with default directory for Products file.
     */
    public fmProductDaoFileImpl() {
        this.PRODUCT_DIR = "PRODUCT_DIR\\product.txt";
    }
    
    /**
     * Constructs a FlooringMasteryProductDaoFileImpl object with passed custom file name
     * for products directory.
     * 
     * @param PRODUCT_DIR 
     */
    public fmProductDaoFileImpl(String PRODUCT_DIR) {
        this.PRODUCT_DIR = PRODUCT_DIR;
    }
    
    @Override
    public Product getProduct(String productType) throws DataPersistenceException {
        loadProductFile();
        return products.get(productType);
    }
    
    /**
     * Load products in the products file to memory, converting strings
     * of product fields to Product objects.
     * 
     * @throws DataPersistenceException 
     */
    private void loadProductFile() throws DataPersistenceException {
        // Scanner from java.util.Scanner
        Scanner scanner;

        try {
            // Create Scanner for reading the file
            scanner = new Scanner(
                    // BuferredRead from java.io.BufferedReader
                    new BufferedReader(
                            // Filereader from java.io.FileReader
                            new FileReader(PRODUCT_DIR)));
        } catch (FileNotFoundException e) { 
            // Translate FileNotFoundException
            throw new DataPersistenceException(
                    "Could not load state data into memory.", e);
        }
        
        // currentLine holds the most recent line read from the file
        String currentLine;
        
        Product currentProduct;
        scanner.nextLine();
        while (scanner.hasNextLine()) {
            // get the next line in the file
            currentLine = scanner.nextLine();
            // unmarshall the line into a Product object
            currentProduct = unmarshallProduct(currentLine);
            // Put currentProduct into the map using state abbreviation as key
            products.put(currentProduct.getProductType(), currentProduct);
        }
        // close scanner
        scanner.close();
    }
    
    /**
     * Convert a line from the Products file to a Product object.
     * 
     * @param productAsText line of text from Products file
     * @return Product object created with fields from file line
     * @throws DataPersistenceException 
     */
    private Product unmarshallProduct(String productAsText) throws DataPersistenceException {
        // split returns string array split on DELIMETER
        String[] productFields = productAsText.split(DELIMITER);
        
        Product product;
        String productType;
        BigDecimal costPerSquareFoot;
        BigDecimal laborCostPerSquareFoot;
        
        try {
            
            // Product type should be in index 0.
            productType = productFields[0];
            
            // Cost per square foot of a product should be in index 1.
            costPerSquareFoot = new BigDecimal(productFields[1]);
            
            // Labor cost per square foot should be in index 2.
            laborCostPerSquareFoot = new BigDecimal(productFields[2]);
            
            // Make a new product object with input fields.
            product = new Product(productType, costPerSquareFoot, laborCostPerSquareFoot);
            
        } catch (IndexOutOfBoundsException e) {
            // Error translation, helpful message
            throw new DataPersistenceException(
                    "Error in format of a Product: try checking field count in prodct file.", e);        
        }
        catch ( NumberFormatException e) {
            throw new DataPersistenceException(
                    "Error in format of a Product: a field has an incorrect format.", e);
        }


        // Return Product created from file
        return product;
    }
    
}
