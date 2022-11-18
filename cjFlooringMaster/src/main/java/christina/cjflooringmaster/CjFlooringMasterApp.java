/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package christina.cjflooringmaster;

import FlooringController.fmController;
import FlooringService.DataValidationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author crjos
 */
public class CjFlooringMasterApp {

    public static void main(String[] args) throws DataValidationException {
        
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        fmController controller = ctx.getBean("controller", fmController.class);
        controller.run();

       
    }
}
