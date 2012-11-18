package kniemkiewicz.jqblocks;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This class is going to handle configuration such as window size and internal game settings.
 * Some of those should be configurable by user, others will be coming from internal config
 * files. For now it has only single immutable Properties object, loaded from file.
 * It also parses args, if provided, looking for ones of following format property_name=property_value,
 * using them in similar way as the ones from file, overriding them.
 * User: knie
 * Date: 7/21/12
 */
public class Configuration {
  Properties properties;

  static private String CONFIG_FILE = "config.properties";
  static private String USER_CONFIG_FILE = "user_config.properties";

  @PostConstruct
  void init() throws IOException {
    // TODO: use string utils for this.
    ClassLoader loader = this.getClass().getClassLoader();
    properties = new Properties();
    properties.load(loader.getResourceAsStream(CONFIG_FILE));
    InputStream userConfigStream = loader.getResourceAsStream(USER_CONFIG_FILE);
    if (userConfigStream != null) {
      properties.load(userConfigStream);
    }
  }

  public int getInt(String name, int defaultValue) {
    String value = properties.getProperty(name);
    if (value == null) return defaultValue;
    return Integer.valueOf(value);
  }

  public float getFloat(String name, float defaultValue) {
    String value = properties.getProperty(name);
    if (value == null) return defaultValue;
    return Float.valueOf(value);
  }

  public String getString(String name, String defaultValue) {
    String value = properties.getProperty(name);
    if (value == null) return defaultValue;
    return value;
  }

  public boolean getBoolean(String name, boolean defaultValue) {
    String value = properties.getProperty(name);
    if (value == null) return defaultValue;
    return Boolean.valueOf(value);
  }

  public void initArgs(String[] args) {
    for (String arg : args) {
      if (arg.contains("=")) {
        String[] tokens = arg.split("=");
        assert tokens.length > 1;
        if (tokens.length > 2) {
          StringBuilder b = new StringBuilder();
          for (int i = 1;i < tokens.length;i++) {
            b.append(tokens[i]).append("=");
          }
          String s = b.toString();
          properties.setProperty(tokens[0], s.substring(0, s.length() - 1));
        } else {
          properties.setProperty(tokens[0], tokens[1]);
        }
      }
    }
  }
}
