package gui;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.Scanner;

import main.Application;

public class PropertyManager {

		private static final String PROPERTIES_FILE = "cdio.properties";

		
		FileOutputStream fos; 
		OutputStreamWriter out;
		InputStreamReader in;
		FileInputStream fis;
		StringReader reader;
		ClassLoader classLoader;
		InputStream propertiesFile;
		
		public PropertyManager() {
			load();
		}
		
		public void load() {
			try {
				fis = new FileInputStream(PROPERTIES_FILE);
				Scanner sc = new Scanner(fis);
				
				for (int i = 0; i < Application.THRESHOLDS.length; i++) {
					for (int j = 0; j < Application.THRESHOLDS[i].length; j++) {
						Application.THRESHOLDS[i][j] = Integer.parseInt(sc.nextLine());
					}
				}
				
				sc.close();
				fis.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		public void save() {
			try {
				fos = new FileOutputStream(PROPERTIES_FILE);
				out = new OutputStreamWriter(fos);
			
				for (int i = 0; i < Application.THRESHOLDS.length; i++) {
					for (int j = 0; j < Application.THRESHOLDS[i].length; j++) {
						out.write(Application.THRESHOLDS[i][j] + "\n");
					}
				}
				out.flush();
				out.close();
				fos.close();
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}

	}
