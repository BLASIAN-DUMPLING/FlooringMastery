public class fmProductDaoStubImpl implements fmProductDao {
    
    private final Product testProduct;
    
    public fmProductDaoStubImpl() {
        testProduct = new Product("Tile", new BigDecimal("3.50"), new BigDecimal("4.15"));
    }
    
    @Override
    public Product getProduct(String productType) throws DataPersistenceException {
        if (productType.equals(testProduct.getProductType())) {
            return testProduct;
        } else {
            return null;
        }
    }
    
}
