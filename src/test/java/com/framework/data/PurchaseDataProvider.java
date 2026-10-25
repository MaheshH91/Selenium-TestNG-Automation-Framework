package com.framework.data;

import java.util.HashMap;
import java.util.List;
import org.testng.annotations.DataProvider;

import com.framework.utils.JsonUtils;

public final class PurchaseDataProvider {

    private PurchaseDataProvider() {}

    @DataProvider(name = "purchaseOrders", parallel = true)
    public static Object[][] getPurchaseOrders() {
        List<HashMap<String, String>> records = JsonUtils.parseJsonToListOfMaps("testdata/PurchaseOrder.json");
        Object[][] data = new Object[records.size()][1];
        for (int i = 0; i < records.size(); i++) {
            data[i][0] = records.get(i);
        }
        return data;
    }
}