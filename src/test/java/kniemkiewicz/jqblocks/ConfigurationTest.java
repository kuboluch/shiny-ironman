package kniemkiewicz.jqblocks;

import junit.framework.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * User: krzysiek
 * Date: 18.11.12
 */
public class ConfigurationTest {

  Configuration getConfiguration() throws IOException {
    Configuration configuration = new Configuration();
    configuration.init();
    return configuration;
  }

  @Test
  public void testInitArgs() throws IOException {
    Configuration c = getConfiguration();
    c.initArgs(new String[]{"Test1=TestValue","Test2=TestValue2","Test3=TestValue2=1","true=false","false=true",
                            "1=0","0=1."});
    Assert.assertEquals(c.getString("Test1",""), "TestValue");
    Assert.assertEquals(c.getString("Test2",""), "TestValue2");
    Assert.assertEquals(c.getString("Test3",""), "TestValue2=1");
    Assert.assertEquals(c.getString("Test4","4"), "4");
    Assert.assertEquals(c.getInt("Test4",4), 4);
    Assert.assertEquals(c.getBoolean("true",true), false);
    Assert.assertEquals(c.getBoolean("false",false), true);
    Assert.assertEquals(c.getInt("1",1), 0);
    Assert.assertEquals(c.getFloat("0", 0), 1.f);
  }
}
