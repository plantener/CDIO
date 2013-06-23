package Calibrate;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyManager {

		private static final String PROPERTIES_FILE = "cdio.properties";
		private static final Properties PROPERTIES = new Properties();

		
		FileOutputStream fos; 
		ClassLoader classLoader;
		InputStream propertiesFile;
		FileInputStream fis;
		
		public PropertyManager() {
//			classLoader = Thread.currentThread().getContextClassLoader();
//			propertiesFile = classLoader.getResourceAsStream(PROPERTIES_FILE);
			try {
				fis = new FileInputStream(PROPERTIES_FILE);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		
			try {
				PROPERTIES.load(fis);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void save() {
			try {
				fos = new FileOutputStream(PROPERTIES_FILE, false);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				PROPERTIES.store(fos, "test");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void setProperty(String key, int value) {
			PROPERTIES.setProperty(key, String.valueOf(value));
			
		}


		public String getProperty(String key) {
			String property = PROPERTIES.getProperty(key);

			if (property == null || property.trim().length() == 0) {
				property = null;
			}

			return property;
		}

	}
