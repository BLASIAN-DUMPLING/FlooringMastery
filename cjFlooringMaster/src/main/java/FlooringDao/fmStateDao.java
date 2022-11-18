/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package FlooringDao;

import FlooringDto.State;
import FlooringService.DataPersistenceException;

/**
 *
 * @author crjos
 */
public interface fmStateDao {
    
    State getState(String stateAbbreviation) throws DataPersistenceException;
    
}
