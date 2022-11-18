/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package FlooringDao;

import FlooringDto.Product;
import FlooringService.DataPersistenceException;

/**
 *
 * @author crjos
 */
public interface fmProductDao {
    
      Product getProduct(String productType) throws DataPersistenceException;
    
}
