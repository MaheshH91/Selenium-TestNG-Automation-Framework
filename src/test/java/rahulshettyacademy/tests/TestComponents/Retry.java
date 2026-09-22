package rahulshettyacademy.tests.TestComponents;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;


public class Retry implements IRetryAnalyzer {

    private int count = 0;
    private final int maxTry = 1; // Maximum number of retries

    @Override
    public boolean retry(ITestResult result) {
        if (count < maxTry) {
            count++;
            return true;
        }
        return false;
    }
}