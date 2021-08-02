package edu.mayo.informatics.fhir.device;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProcessGUDIDImplantable {

	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String inputFileImplantable = "/Users/m005994/Documents/medical-device/implantable_list_snomed/implantable_devices_snomed.csv";
		String inputFileDeviceClass3 = "/Users/m005994/Documents/medical-device/implantable_list_snomed/deviceClass-3.txt";
		String outputFile = "/Users/m005994/Documents/medical-device/implantable_list_snomed/deviceClass3Implantable.txt";
		
		List<String> deviceClass3Ids = new ArrayList<String>();
		
		BufferedReader br1 = null;
		BufferedReader br2 = null;
        BufferedWriter bw = null;
		try {
			
			
			br1 = new BufferedReader(new FileReader(inputFileImplantable));
			br2 = new BufferedReader(new FileReader(inputFileDeviceClass3));
			
			
			bw = new BufferedWriter(new FileWriter(outputFile));
			
			
			int index = 1;
			
			
			String line2 = br2.readLine();
			line2 = br2.readLine(); //skip header

			while(line2 != null) {
				String[] items = line2.split("\\|");
				String deviceId = items[0];
				//System.out.println(deviceId);
                deviceClass3Ids.add(deviceId);
				

				line2 = br2.readLine();
				
				
				//if(index++ > 10) break;
				
			}
			

			br2.close();
			
			
			String line1 = br1.readLine();
			
			bw.write(line1 + "\n");
			
			line1 = br1.readLine();
			
			while(line1 != null) {
				
				String[] tokens = line1.split("\\|");
				String deviceId1 = tokens[0];
				
				if(deviceClass3Ids.contains(deviceId1)) {
					
					bw.write(line1 + "\n");
				}
				
				line1 = br1.readLine();
				
				
				
			}
			
			br1.close();
			bw.close();
			
		}catch(IOException ie) {
			ie.printStackTrace();
		}
		

	}	
	

}
