package rahulshettyacademy.data;

import java.util.HashMap;
import java.util.List;

import org.testng.annotations.DataProvider;

import rahulshettyacademy.utils.JsonUtils;

public class TestDataProvider {

    @DataProvider(name = "purchaseOrderData")
    public static Object[][] getPurchaseData() {
        // Reads from src/test/resources/testData/PurchaseOrder.json via ClassLoader
        List<HashMap<String, String>> data = JsonUtils.getJsonDataToMapList("testData/PurchaseOrder.json");

        Object[][] testData = new Object[data.size()][1];
        for (int i = 0; i < data.size(); i++) {
            testData[i][0] = data.get(i);
        }
        return testData;
    }
}