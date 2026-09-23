package rahulshettyacademy.data;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.DataProvider;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import rahulshettyacademy.utils.JsonUtils;

public class TestDataProvider {

    // Set parallel = true for multi-threaded test execution per data row
    @DataProvider(name = "purchaseOrderData", parallel = true)
    public static Object[][] getPurchaseOrderData() throws IOException {

        List<HashMap<String, String>> data = JsonUtils.getJsonDataToMapList("testdata/PurchaseOrder.json");

        Object[][] testData = new Object[data.size()][1];
        for (int i = 0; i < data.size(); i++) {
            testData[i][0] = data.get(i);
        }
        return testData;
    }
}