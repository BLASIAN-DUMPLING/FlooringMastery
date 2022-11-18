/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FlooringDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author crjos
 */
public class Order {

    private final int orderNumber;
    
    private final LocalDateTime creationDateTime;
      
    private final LocalDate orderDate;
      
    private String customerName;
        
    private Statuses status;
 
    private State state;
    
    
    private Product product;
    
    
    private Costs costs;
    
    private BigDecimal area;

   
    public Order(int orderNumber, LocalDateTime creationDateTime, LocalDate orderDate) {
        this.orderNumber = orderNumber;
        this.creationDateTime = creationDateTime;
        this.orderDate = orderDate;
    }

   
    public int getOrderNumber() {
        return orderNumber;
    }
    
  
    public LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }

  
    public LocalDate getDate() {
        return orderDate;
    }

    
    public String getCustomerName() {
        return customerName;
    }

    public Statuses getStatus() {
        return status;
    }

    public State getState() {
        return state;
    }

    public Product getProduct() {
        return product;
    }

    public Costs getCosts() {
        return costs;
    }

    public BigDecimal getArea() {
        return area;
    }
   
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setStatus(Statuses status) {
        this.status = status;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setCosts(Costs costs) {
        this.costs = costs;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + this.orderNumber;
        hash = 83 * hash + Objects.hashCode(this.creationDateTime);
        hash = 83 * hash + Objects.hashCode(this.orderDate);
        hash = 83 * hash + Objects.hashCode(this.customerName);
        hash = 83 * hash + Objects.hashCode(this.status);
        hash = 83 * hash + Objects.hashCode(this.state);
        hash = 83 * hash + Objects.hashCode(this.product);
        hash = 83 * hash + Objects.hashCode(this.costs);
        hash = 83 * hash + Objects.hashCode(this.area);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Order other = (Order) obj;
        if (this.orderNumber != other.orderNumber) {
            return false;
        }
        if (!Objects.equals(this.customerName, other.customerName)) {
            return false;
        }
        if (!Objects.equals(this.creationDateTime, other.creationDateTime)) {
            return false;
        }
        if (!Objects.equals(this.orderDate, other.orderDate)) {
            return false;
        }
        if (this.status != other.status) {
            return false;
        }
        if (!Objects.equals(this.state, other.state)) {
            return false;
        }
        if (!Objects.equals(this.product, other.product)) {
            return false;
        }
        if (!Objects.equals(this.costs, other.costs)) {
            return false;
        }
        if (!Objects.equals(this.area, other.area)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "Order{" + "orderNumber=" + orderNumber + ", creationDateTime=" + creationDateTime + ", orderDate=" + orderDate + ", customerName=" + customerName + ", status=" + status + ", state=" + state + ", product=" + product + ", costs=" + costs + ", area=" + area + '}';
    }
    
}
