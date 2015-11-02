package org.time2java.jpingservice;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author time2java
 */
public class PropertiesHolder {

    static Properties properties = initProperties();

    private static Properties initProperties() {
        FileInputStream fis;
        Properties property = new Properties();

        try {
            fis = new FileInputStream("src/main/resources/config.properties");
            property.load(fis);
        } catch (IOException e) {
            System.err.println("ОШИБКА: Файл свойств отсуствует!");
        }
        return property;
    }

    ;
    
    static public Properties getProperties() {
        return properties;
    }

    static public int getIntProperties(String key) {
        int result = 0;

        try {
            result = Integer.valueOf(properties.getProperty(key));
        } catch (NumberFormatException ex) {
            result = 0;
            System.out.println("key: " + key + "\tvalue: " + properties.getProperty(key) + "\t number format exception");
        }
        return result;
    }
    
    static public int getIntProperties(String key, int defVal) {
        int result = 0;

        try {
            result = Integer.valueOf(properties.getProperty(key));
        } catch (NumberFormatException ex) {
            result = defVal;
            System.out.println("key: " + key + "\tvalue: " + properties.getProperty(key) + "\t number format exception");
        }
        return result;
    }

    static public long getLongProperties(String key) {
        long result = 0;

        try {
            result = Long.valueOf(properties.getProperty(key));
        } catch (NumberFormatException ex) {
            result = 0;
            System.out.println("key: " + key + "\tvalue: " + properties.getProperty(key) + "\t number format exception");
        }
        return result;
    }
    
     static public long getLongProperties(String key, Long defVal) {
        long result = 0;

        try {
            result = Long.valueOf(properties.getProperty(key));
        } catch (NumberFormatException ex) {
            result = defVal;
            System.out.println("key: " + key + "\tvalue: " + properties.getProperty(key) + "\t number format exception");
        }
        return result;
    }
}
