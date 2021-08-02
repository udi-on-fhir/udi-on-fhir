package edu.mayo.informatics.fhir.device;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ProcessGUDIDIdentifiers {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String inputFile = "/Users/m005994/Documents/medical-device/AccessGUDID_Delimited_Full_Release_20201001/identifiers.txt";
		String outputFile = "/Users/m005994/Documents/medical-device/AccessGUDID_Delimited_Full_Release_20201001/deviceIdentifiers.txt";
		
		BufferedReader br = null;
        BufferedWriter bw = null;
		try {
			
			br = new BufferedReader(new FileReader(inputFile));
			//bw = new BufferedWriter(new FileWriter(outputFile));
			String line = br.readLine();
			//line = br.readLine(); //skip header
			int index1 = 1;
			int index2 = 1;
			int index3 = 1;
			int index4 = 1;
			int index5 = 1;
			int index6 = 1;
			
			while(line!= null) {
				String[] items = line.split("\\|");
				String primaryDI = items[0];
				String deviceId = items[1];
				String deviceIdType = items[2];
				//String deviceIdIssuingAgency = items[3];
				//String containsDINumber = items[4];
				//String pkgQuantity = items[5];
				
				if(deviceIdType.equals("Primary")) {
					
					index1 ++;
					
				}else if (deviceIdType.equals("Package")) {
				
					index2 ++;
				
				}else if (deviceIdType.equals("Unit of Use")) {
					
					index3 ++;
					
				}else if (deviceIdType.equals("Secondary")) {
					
					index4 ++;
					
				}else if (deviceIdType.equals("Direct Marking")) {
					
					index5 ++;
					
				}else if (deviceIdType.equals("Previous")) {
					
					index6 ++;
					
				}
				

				line = br.readLine();
				
				
				//if(index++ > 10) break;
				
			}
			
			System.out.println("Primary: " + "|" + index1);
			System.out.println("Package: " + "|" + index2);
			System.out.println("Unit of use: " + "|" + index3);
			System.out.println("Secondary: " + "|" + index4);
			System.out.println("Direct marking: " + "|" + index5);
			System.out.println("Previous: " + "|" + index6);
			
			br.close();
			//bw.close();
			
		}catch(IOException ie) {
			ie.printStackTrace();
		}
		

	}	
	
	
}
