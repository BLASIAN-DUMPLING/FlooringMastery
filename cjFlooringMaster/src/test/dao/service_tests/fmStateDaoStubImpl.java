public class fmStateDaoStubImpl implements fmStateDao {
    
    private final State testState;
    
    public fmStateDaoStubImpl() {
        testState = new State("CA", "California", new BigDecimal("25.00"));
    }
    
    public State getState(String stateAbbreviation) {
        if (stateAbbreviation.equals(testState.getStateAbbreviation())) {
            return testState;
        }
        
        return null;
    }
    
}
