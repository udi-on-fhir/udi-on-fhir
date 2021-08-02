package edu.mayo.informatics.fhir.device;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ProcessGUDIDCatalogNumbers {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String inputFile = "/Users/m005994/Documents/medical-device/AccessGUDID_Delimited_Full_Release_20201001/device.txt";
		String outputFile = "/Users/m005994/Documents/medical-device/AccessGUDID_Delimited_Full_Release_20201001/deviceCatalog.txt";
		
		BufferedReader br = null;
        BufferedWriter bw = null;
		try {
			
			br = new BufferedReader(new FileReader(inputFile));
			bw = new BufferedWriter(new FileWriter(outputFile));
			String line = br.readLine();
			//line = br.readLine(); //skip header
			int index = 1;
			while(line!= null) {
				String[] items = line.split("\\|");
				String primaryDI = items[0];
				String brandName = items[9];
				String versionModelNumber = items[10];
				String catalogNumber = items[11];
				System.out.println(primaryDI + "|" + catalogNumber);
				bw.write(primaryDI + "|" + versionModelNumber + "|" + catalogNumber + "\n");
				line = br.readLine();
				
				
				//if(index++ > 10) break;
				
			}
			
			br.close();
			bw.close();
			
		}catch(IOException ie) {
			ie.printStackTrace();
		}
		

	}

}
