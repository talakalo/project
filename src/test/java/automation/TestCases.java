package automation;


import com.automation.systemtest.testbase.SystemTestBase;
import org.slf4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.LoggerFactory;


public class TestCases extends SystemTestBase {
    private static final Logger log = LoggerFactory.getLogger(TestCases.class);
    
    @Test
    public void test1() {
       SkinnyTies skinny=new SkinnyTies(testDriver);
        skinny.navigateToUrl();
        skinny.
        Assert.assertTrue("is true?",1==1);
    }
    
}