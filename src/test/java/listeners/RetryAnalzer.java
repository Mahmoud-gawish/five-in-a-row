package listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalzer implements IRetryAnalyzer {

    private int retryCount = 0;
    private  static final int maxRetryCount = 1;

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < maxRetryCount){
            retryCount++;
            System.out.println("=--------------------------------------------------------=");
            System.out.println("Retrying the execution of the test case (" + maxRetryCount + " time(s))...............!!! ");
            System.out.println("=--------------------------------------------------------=");
            return true;
        }
        return false;
    }

}
