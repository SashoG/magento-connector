package org.mule.module.magento.automation.testcases;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.api.MuleEvent;
import org.mule.api.processor.MessageProcessor;

import com.magento.api.CatalogProductCreateEntity;

public class UpdateStockItemTestCases extends MagentoTestParent {

	@Before
	public void setUp() {
		try {
			testObjects = (Map<String, Object>) context.getBean("updateStockItem");
			
			String type = (String) testObjects.get("type");
			String sku = (String) testObjects.get("sku");
			int set = Integer.parseInt(testObjects.get("set").toString());
			
			CatalogProductCreateEntity productAttributes = (CatalogProductCreateEntity) testObjects.get("attributesRef");
			int productId = createProduct(type, set, sku, productAttributes);
			
			testObjects.put("productId", productId);
		}
		catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Category({RegressionTests.class})
	@Test
	public void testUpdateStockItem() {
		try {
			int productId = (Integer) testObjects.get("productId");
			testObjects.put("productIdOrSku", productId);
			
			MessageProcessor flow = lookupFlowConstruct("update-stock-item");
			MuleEvent response = flow.process(getTestEvent(testObjects));
			
			Integer result = (Integer) response.getMessage().getPayload();
			assertTrue(result == 1);
		}
		catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@After
	public void tearDown() {
		try {
			int productId = (Integer) testObjects.get("productId");
			deleteProductById(productId);
		}
		catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
}
