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
        return property ;
    };
    
    public Properties getProperties(){
        return properties ;
    }
}
