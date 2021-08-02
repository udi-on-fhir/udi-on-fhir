package edu.mayo.informatics.fhir.device;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.RandomUtils;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Bundle.BundleType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Device;
import org.hl7.fhir.r4.model.Device.DeviceDeviceNameComponent;
import org.hl7.fhir.r4.model.Device.DeviceNameType;
import org.hl7.fhir.r4.model.Device.DeviceUdiCarrierComponent;
import org.hl7.fhir.r4.model.DeviceDefinition;
import org.hl7.fhir.r4.model.DeviceUseStatement;
import org.hl7.fhir.r4.model.DeviceUseStatement.DeviceUseStatementStatus;
import org.hl7.fhir.r4.model.Reference;

import ca.uhn.fhir.context.FhirContext;

public class ProcessFHIRDevice {
	
	final static String inputFileImplantable = "/Users/m005994/Documents/medical-device/implantable_list_snomed/Implant_device_counts.csv";
	final static String outputFilePathDevice = "/Users/m005994/Documents/medical-device/implantable_list_snomed/Device/";
	final static String outputFilePathDeviceDefinition = "/Users/m005994/Documents/medical-device/implantable_list_snomed/DeviceDefinition/";
	final static String outputFilePathDeviceUseStatement = "/Users/m005994/Documents/medical-device/implantable_list_snomed/DeviceUseStatement/";
	final static String outputFilePathBundle = "/Users/m005994/Documents/medical-device/implantable_list_snomed/Bundle/";

    static FhirContext ctx = FhirContext.forR4();

	//final static String uuid = UUID.randomUUID().toString().replace("-", "");
    
	
	public static void processFHIRDeviceInstance() {

		
		
		BufferedReader br = null;
        BufferedWriter bw = null;
		try {
			
			br = new BufferedReader(new FileReader(inputFileImplantable));
			//bw = new BufferedWriter(new FileWriter(outputFile));
			String line = br.readLine();
			line = br.readLine(); //skip header

			
			int index = 1;

			
			while(line!= null) {
				String[] items = line.split(",");
				String item_Type = items[1].replaceAll("\"", "");
				String item_Name = items[2].replaceAll("\"", "");
				String lot_Number = items[3].replaceAll("\"", "");
				String serial_Number = items[4].replaceAll("\"", "");
				String lawson_ID = items[5].replaceAll("\"", "");
				String man_Catalog_Num = items[6].replaceAll("\"", "");
				String man_Name = items[7].replaceAll("\"", "");
				String count = items[8];
				String gtin_ID = items[9].replaceAll("\"", "");
				
				//System.out.println(deviceId);
				
               if (!lot_Number.equals("")) {
            	   
            	   
            	   Bundle bundle = new Bundle();
            	   
  					String uuid1 = UUID.randomUUID().toString().replace("-", "");

            	   
            	   bundle.setId("http://hl7.org/fhir/Bundle/" +lawson_ID + "-" + uuid1);
            	   
            	   bundle.setType(BundleType.COLLECTION);
            	   
            	   BundleEntryComponent bundleEntry1 = new BundleEntryComponent();
               	   BundleEntryComponent bundleEntry2 = new BundleEntryComponent();
           	   
            	   
            	   Device device = new Device();
            	   
            	   
            	   device.setId("http://hl7.org/fhir/Device/" +lawson_ID);
            	   
            	   Reference defReference = new Reference();
            	   defReference.setReference("http://hl7.org/fhir/DeviceDefinition/" + gtin_ID);
            	   defReference.setDisplay(item_Name);
            	   
            	   device.setDefinition(defReference);
            	   
            	   
            	   DeviceUdiCarrierComponent udiCarrier = new DeviceUdiCarrierComponent();
            	   udiCarrier.setDeviceIdentifier(gtin_ID);
            	   
             	   
            	   device.addUdiCarrier(udiCarrier);
            	   
            	   DeviceDeviceNameComponent deviceName = new DeviceDeviceNameComponent();
            	   deviceName.setName(item_Name);
            	   deviceName.setType(DeviceNameType.MANUFACTURERNAME);
            	   
            	   device.addDeviceName(deviceName);
            	   
            	   device.setModelNumber(man_Catalog_Num);
            	   
            	   device.setLotNumber(lot_Number);
            	   
            	   device.setSerialNumber(serial_Number);
            	   
					CodeableConcept concept = new CodeableConcept();

					List<Coding> codings = new ArrayList<Coding>();
					
					
					Coding coding2 = new Coding();
					coding2.setSystem("http://snomed.info/sct");
					coding2.setCode("40388003");
					coding2.setDisplay("Implant, device (physical object) ");
					codings.add(coding2);
					
					concept.setCoding(codings);
					
					device.setType(concept);
					
					
					printoutDevice(lawson_ID, device);
					
					bundleEntry1.setResource(device); //add device
            	   
            	   
           			DeviceDefinition ddef = ProcessFHIRDeviceDefinition.renderGUDIDDeviceInformation(gtin_ID);
           			printoutDeviceDefinition(gtin_ID, ddef);
           			
           			
           			bundleEntry2.setResource(ddef);
           			
           			bundle.addEntry(bundleEntry1);
           			bundle.addEntry(bundleEntry2);
           			
           			
           			if(Integer.parseInt(count) > 0) {
           				
           				for(int i=0; i < Integer.parseInt(count); i++) {
           					
                        	   BundleEntryComponent bundleEntry3 = new BundleEntryComponent();

           					
           					String uuid = UUID.randomUUID().toString().replace("-", "");
           					
           					DeviceUseStatement duse = new DeviceUseStatement();
           					
           					duse.setId("http://hl7.org/fhir/DeviceUseStatement/" + uuid + "-" + lawson_ID);
           					
           					duse.setStatus(DeviceUseStatementStatus.ACTIVE);
           					
           					Reference refPatient = new Reference();
           					
           					refPatient.setReference("http://hl7.org/fhir/Patient/" + uuid);
           					
           					duse.setSubject(refPatient);
           					
           					
           					Random random = new Random();
           					int minDay = (int) LocalDate.of(2014, 1, 1).toEpochDay();
           					int maxDay = (int) LocalDate.of(2020, 1, 1).toEpochDay();
           					long randomDay = minDay + random.nextInt(maxDay - minDay);

           					LocalDate randomDate = LocalDate.ofEpochDay(randomDay);
           					
           					Date date = java.sql.Date.valueOf(randomDate);
           					
           					duse.setRecordedOn(date);
           					
           					//System.out.println(randomDate);
           					
           					
           					Reference refDevice = new Reference();
           					refDevice.setReference("http://hl7.org/fhir/Device/" + lawson_ID);
           					refDevice.setDisplay(item_Name);
           					duse.setDevice(refDevice);
           					
           					printoutDeviceUseStatement(uuid + "-" + lawson_ID, duse);
           					
           					bundleEntry3.setResource(duse);
           					
           					bundle.addEntry(bundleEntry3);
           					
           				}
           				
           				
           				
           				
           			}
           			
           			printoutDeviceBundle(lawson_ID + "-" + uuid1, bundle);
           			
           			if(index++ > 10) {
           				break;
           			}
           			
            	

            	   
               }
				

				line = br.readLine();
				
				
				//if(index++ > 10) break;
				
			}
						
			br.close();
			//bw.close();
			
		}catch(IOException ie) {
			ie.printStackTrace();
		}		

		
	}	
	
	
	public static void printoutDevice(String itemId, Device device) {
		//DeviceDefinition ddef = ProcessFHIRDeviceDefinition.renderGUDIDDeviceInformation(di);
		
		//String string = ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(device);
		String string = ctx.newRDFParser().setPrettyPrint(true).encodeResourceToString(device);
	    
		System.out.println(string);
		
		BufferedWriter bw = null;
		
		try {
			
			bw = new BufferedWriter(new FileWriter(outputFilePathDevice + "Device-" + itemId + ".json"));
			bw.write(string + "\n");
			
			bw.close();
			
		}catch(IOException ie) {
			ie.addSuppressed(ie);
		}
		
		
		
		
	}	
	
	public static void printoutDeviceDefinition(String di, DeviceDefinition ddef) {
		
		//String string = ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(ddef);
		String string = ctx.newRDFParser().setPrettyPrint(true).encodeResourceToString(ddef);
	    
		System.out.println(string);
		
		BufferedWriter bw = null;
		
		try {
			
			bw = new BufferedWriter(new FileWriter(outputFilePathDeviceDefinition + "GUDID-" + di + ".json"));
			bw.write(string + "\n");
			
			bw.close();
			
		}catch(IOException ie) {
			ie.addSuppressed(ie);
		}
		
		
		
		
	}	
	
	public static void printoutDeviceUseStatement(String duseId, DeviceUseStatement duse) {
		
		//String string = ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(duse);
		String string = ctx.newRDFParser().setPrettyPrint(true).encodeResourceToString(duse);
	    
		System.out.println(string);
		
		BufferedWriter bw = null;
		
		try {
			
			bw = new BufferedWriter(new FileWriter(outputFilePathDeviceUseStatement + "DeviceUseStatement-" + duseId + ".json"));
			bw.write(string + "\n");
			
			bw.close();
			
		}catch(IOException ie) {
			ie.addSuppressed(ie);
		}
		
		
		
		
	}	
	
	
	public static void printoutDeviceBundle(String itemId, Bundle bundle) {
		//DeviceDefinition ddef = ProcessFHIRDeviceDefinition.renderGUDIDDeviceInformation(di);
		
		//String string = ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle);
		String string = ctx.newRDFParser().setPrettyPrint(true).encodeResourceToString(bundle);
	    
		System.out.println(string);
		
		BufferedWriter bw = null;
		
		try {
			
			bw = new BufferedWriter(new FileWriter(outputFilePathBundle + "Bundle-" + itemId + ".json"));
			bw.write(string + "\n");
			
			bw.close();
			
		}catch(IOException ie) {
			ie.addSuppressed(ie);
		}
		
		
		
		
	}		
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		processFHIRDeviceInstance();
	
	
	}
	
	

}
