/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FlooringDto;

import java.math.BigDecimal;
import java.util.Objects;

/**
 *
 * @author crjos
 */
public class Costs {

    private BigDecimal materialCost;
    private BigDecimal laborCost;
    private BigDecimal taxCost;
    private BigDecimal total;

    public BigDecimal getMaterialCost() {
        return materialCost;
    }

    public BigDecimal getLaborCost() {
        return laborCost;
    }

    public BigDecimal getTaxCost() {
        return taxCost;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setMaterialCost(BigDecimal materialCost) {
        this.materialCost = materialCost;
    }

    public void setLaborCost(BigDecimal labourCost) {
        this.laborCost = labourCost;
    }

    public void setTaxCost(BigDecimal taxCost) {
        this.taxCost = taxCost;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Costs{" + "materialCost=" + materialCost + ", labourCost=" + laborCost + ", taxCost=" + taxCost + ", total=" + total + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.materialCost);
        hash = 41 * hash + Objects.hashCode(this.laborCost);
        hash = 41 * hash + Objects.hashCode(this.taxCost);
        hash = 41 * hash + Objects.hashCode(this.total);
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
        final Costs other = (Costs) obj;
        if (!Objects.equals(this.materialCost, other.materialCost)) {
            return false;
        }
        if (!Objects.equals(this.laborCost, other.laborCost)) {
            return false;
        }
        if (!Objects.equals(this.taxCost, other.taxCost)) {
            return false;
        }
        if (!Objects.equals(this.total, other.total)) {
            return false;
        }
        return true;
    }

    /*public String getLaborCost() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }*/
    
}
