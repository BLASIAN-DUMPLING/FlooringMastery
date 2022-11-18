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
public class State {

   private final String stateAbbreviation;
    private final String stateName;
    private final BigDecimal stateTaxRate;

   
    public State(String stateAbbreviation, String stateName, BigDecimal stateTaxRate) {
        this.stateAbbreviation = stateAbbreviation;
        this.stateName = stateName;
        this.stateTaxRate = stateTaxRate;
    }

    public String getStateAbbreviation() {
        return stateAbbreviation;
    }

    public String getStateName() {
        return stateName;
    }
    
    public BigDecimal getStateTaxRate() {
        return stateTaxRate;
    }

    @Override
    public String toString() {
        return "State{" + "stateAbbreviation=" + stateAbbreviation + ", stateName=" + stateName + ", stateTaxRate=" + stateTaxRate + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 43 * hash + Objects.hashCode(this.stateAbbreviation);
        hash = 43 * hash + Objects.hashCode(this.stateName);
        hash = 43 * hash + Objects.hashCode(this.stateTaxRate);
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
        final State other = (State) obj;
        if (!Objects.equals(this.stateAbbreviation, other.stateAbbreviation)) {
            return false;
        }
        if (!Objects.equals(this.stateName, other.stateName)) {
            return false;
        }
        return Objects.equals(this.stateTaxRate, other.stateTaxRate);
    }
    
}
