package edu.mayo.informatics.fhir.device;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.DeviceDefinition;
import org.hl7.fhir.r4.model.DeviceDefinition.DeviceDefinitionDeviceNameComponent;
import org.hl7.fhir.r4.model.DeviceDefinition.DeviceDefinitionUdiDeviceIdentifierComponent;
import org.hl7.fhir.r4.model.DeviceDefinition.DeviceNameType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.StringType;

import ca.uhn.fhir.context.FhirContext;

public class ProcessFHIRDeviceDefinition {
	
	final static String inputFileIdentifiers = "/Users/m005994/Documents/medical-device/AccessGUDID_Delimited_Full_Release_20201001/identifiers.txt";
	final static String inputFileDevice = "/Users/m005994/Documents/medical-device/AccessGUDID_Delimited_Full_Release_20201001/device.txt";
	final static String inputFileGmdnTerms = "/Users/m005994/Documents/medical-device/AccessGUDID_Delimited_Full_Release_20201001/gmdnTerms.txt";
	private final static String ohdsi_sct_concept_file = "/Users/m005994/Documents/medical-device/ohdsi-snomedct/CONCEPT.csv";
	final static String outputFile = "/Users/m005994/Documents/medical-device/AccessGUDID_Delimited_Full_Release_20201001/deviceIdentifiers.txt";
	final static String outputFilePath = "/Users/m005994/Documents/medical-device/AccessGUDID_Delimited_Full_Release_20201001/DeviceDefinition/";
    // Create a context
    static FhirContext ctx = FhirContext.forR4();

    
	public static List<String> getAllDIsByPrimaryDI(String pdi) {
		List<String> dis = new ArrayList<String>();

		
		
		BufferedReader br = null;
        BufferedWriter bw = null;
		try {
			
			br = new BufferedReader(new FileReader(inputFileIdentifiers));
			//bw = new BufferedWriter(new FileWriter(outputFile));
			String line = br.readLine();
			//line = br.readLine(); //skip header
			int index = 1;

			
			while(line!= null) {
				String[] items = line.split("\\|");
				String primaryDIItem = items[0];
				String deviceId = items[1];
				String deviceIdType = items[2];
				String deviceIdIssuingAgency = items[3];
				//String containsDINumber = items[4];
				//String pkgQuantity = items[5];
				
				//System.out.println(deviceId);
				
				if(primaryDIItem.equals(pdi)) {
					
					dis.add(deviceId);
					
										
				}

				

				line = br.readLine();
				
				
				//if(index++ > 10) break;
				
			}
						
			br.close();
			//bw.close();
			
		}catch(IOException ie) {
			ie.printStackTrace();
		}		
		
		
		return dis;
		
	}
   
    
	public static String getPrimaryDI(String di) {
		String primaryDI = null;

		
		
		BufferedReader br = null;
        BufferedWriter bw = null;
		try {
			
			br = new BufferedReader(new FileReader(inputFileIdentifiers));
			//bw = new BufferedWriter(new FileWriter(outputFile));
			String line = br.readLine();
			//line = br.readLine(); //skip header
			int index = 1;

			
			while(line!= null) {
				String[] items = line.split("\\|");
				String primaryDIItem = items[0];
				String deviceId = items[1];
				String deviceIdType = items[2];
				String deviceIdIssuingAgency = items[3];
				//String containsDINumber = items[4];
				//String pkgQuantity = items[5];
				
				//System.out.println(deviceId);
				
				if(deviceId.equals(di)) {
					
					primaryDI = primaryDIItem;
					break;
					
										
				}

				

				line = br.readLine();
				
				
				//if(index++ > 10) break;
				
			}
						
			br.close();
			//bw.close();
			
		}catch(IOException ie) {
			ie.printStackTrace();
		}		
		
		
		return primaryDI;
		
	}
	
	
	public static String getParentDI(String di) {
		String parentDI = null;

		
		
		BufferedReader br = null;
        BufferedWriter bw = null;
		try {
			
			br = new BufferedReader(new FileReader(inputFileIdentifiers));
			//bw = new BufferedWriter(new FileWriter(outputFile));
			String line = br.readLine();
			//line = br.readLine(); //skip header
			int index = 1;

			
			while(line!= null) {
				String[] items = line.split("\\|");
				String primaryDIItem = items[0];
				String deviceId = items[1];
				String deviceIdType = items[2];
				String deviceIdIssuingAgency = items[3];
				
				if( items.length > 4 && items[4] != null) {
					
					//System.out.println(di);
					if(items[4].equals(di)){
						parentDI = deviceId;
						
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
		
		
		return parentDI;
		
	}
	
	public static DeviceDefinition renderGUDIDIdentifierInDeviceDefinition(String di) {
		DeviceDefinition deviceDef = new DeviceDefinition();

		
		
		BufferedReader br = null;
        BufferedWriter bw = null;
		try {
			
			br = new BufferedReader(new FileReader(inputFileIdentifiers));
			//bw = new BufferedWriter(new FileWriter(outputFile));
			String line = br.readLine();
			//line = br.readLine(); //skip header
			int index = 1;

			
			while(line!= null) {
				String[] items = line.split("\\|");
				String primaryDI = items[0];
				String deviceId = items[1];
				String deviceIdType = items[2];
				String deviceIdIssuingAgency = items[3];
				//String containsDINumber = items[4];
				//String pkgQuantity = items[5];
				
				
				if(deviceId.equals(di)) {
					
					deviceDef.setId("http://hl7.org/fhir/DeviceDefinition/" + deviceId);
					
					
					Identifier identifier1 = new Identifier();
					identifier1.setId("DeviceIdentifier");
					identifier1.setSystem("http://hl7.org/fhir/DeviceDefinition");
					identifier1.setValue(di);
					
					Identifier identifier2 = new Identifier();
					identifier2.setId("PrimaryDI");
					identifier2.setSystem("http://hl7.org/fhir/DeviceDefinition");
					identifier2.setValue(primaryDI);
					
					
					deviceDef.addIdentifier(identifier1);
					deviceDef.addIdentifier(identifier2);

					
					
					DeviceDefinitionUdiDeviceIdentifierComponent udiDeviceIdentifier1 = new DeviceDefinitionUdiDeviceIdentifierComponent();
					udiDeviceIdentifier1.setDeviceIdentifier(deviceId);
					udiDeviceIdentifier1.setIssuer(deviceIdIssuingAgency);
					udiDeviceIdentifier1.setJurisdiction(deviceIdType);
					
					deviceDef.addUdiDeviceIdentifier(udiDeviceIdentifier1);

					//if(!deviceIdType.equals("Primary")) {
					
					//	DeviceDefinitionUdiDeviceIdentifierComponent udiDeviceIdentifier2 = new DeviceDefinitionUdiDeviceIdentifierComponent();
					//	udiDeviceIdentifier2.setDeviceIdentifier(primaryDI);
					//	udiDeviceIdentifier2.setIssuer(deviceIdIssuingAgency);
					//	udiDeviceIdentifier2.setJurisdiction("Primary");
					//
					//	deviceDef.addUdiDeviceIdentifier(udiDeviceIdentifier2);
					
					//}
					
					
					String parentDI = getParentDI(di);
					
					if(parentDI != null) {
						
						Reference reference = new Reference();
						reference.setReference("http://hl7.org/fhir/DeviceDefinition/" + parentDI);
						
						deviceDef.setParentDevice(reference);
						
						

						
					}
					
					
					if(items.length > 5 && items[5] != null ) {
						
						String pkgQuantity = items[5];
					
						Quantity quantity = new Quantity();
						quantity.setValue(Integer.parseInt(pkgQuantity));
						deviceDef.setQuantity(quantity);
					}
					
					
					
					break;
					
					
					
				}

				

				line = br.readLine();
				
				
				//if(index++ > 10) break;
				
			}
						
			br.close();
			//bw.close();
			
		}catch(IOException ie) {
			ie.printStackTrace();
		}		
		
		
		return deviceDef;
		
	}

	public static DeviceDefinition renderGUDIDGmdnTermInDeviceDefinition(String pdi, DeviceDefinition dd) {
		DeviceDefinition deviceDef = dd;

		
		
		BufferedReader br = null;
        BufferedWriter bw = null;
		try {
			
			br = new BufferedReader(new FileReader(inputFileGmdnTerms));
			//bw = new BufferedWriter(new FileWriter(outputFile));
			String line = br.readLine();
			line = br.readLine(); //skip header
			int index = 1;

			
			while(line!= null) {
				String[] items = line.split("\\|");
				String primaryDI = items[0];
				String gmdnPTName = items[1];
				String gmdnPTDefinition = items[2];

				if(primaryDI.equals(pdi)) {
					
					CodeableConcept concept = new CodeableConcept();

					List<Coding> codings = new ArrayList<Coding>();
					
					Coding coding1 = new Coding();
					coding1.setSystem("https://www.gmdnagency.org/gmdn");
					coding1.setCode(gmdnPTName);
					coding1.setDisplay(gmdnPTDefinition);
					
					codings.add(coding1);
					
					String response = GUDIDDeviceSNOMEDAPI.getDeviceSNOMEDCodeName(pdi);
					
					//System.out.println(response);
					
					if(!response.equals("261665006")) {
					
						String code = GUDIDDeviceSNOMEDAPI.parseDeviceSNOMEDIdentifier(response);
						String name = GUDIDDeviceSNOMEDAPI.parseDeviceSNOMEDCTName(response);
					
						Coding coding2 = new Coding();
						coding2.setSystem("http://snomed.info/sct");
						coding2.setCode(code);
						coding2.setDisplay(name);
						codings.add(coding2);

						concept.setCoding(codings);
						concept.setText(name);
						deviceDef.setType(concept);
					
				
					}else {
						
						concept.setCoding(codings);
						concept.setText(gmdnPTName);
						deviceDef.setType(concept);
						
						
					}
					
					
					break;
				}
				

				line = br.readLine();
				
				
				//if(index++ > 10) break;
				
			}
						
			br.close();
			//bw.close();
			
		}catch(IOException ie) {
			ie.printStackTrace();
		}		
		
		
		return deviceDef;
		
	}	
	
	public static DeviceDefinition renderGUDIDDeviceInformation(String di) {
		DeviceDefinition deviceDef = renderGUDIDIdentifierInDeviceDefinition(di);
		String pdi = getPrimaryDI(di);
		deviceDef = renderGUDIDGmdnTermInDeviceDefinition(pdi, deviceDef);
		
		BufferedReader br = null;
        BufferedWriter bw = null;
		try {
			
			br = new BufferedReader(new FileReader(inputFileDevice));
			//bw = new BufferedWriter(new FileWriter(outputFile));
			String line = br.readLine();
			//line = br.readLine(); //skip header
			int index = 1;
			while(line!= null) {
				String[] items = line.split("\\|");
				String primaryDI = items[0];
				String brandName = items[9];
				String versionModelNumber = items[10];
				String catalogNumber = items[11];
				String companyName = items[13];
				String deviceCount = items[14];
				String deviceDescription = items[15];
				//System.out.println(primaryDI + "|" + catalogNumber);
				//bw.write(primaryDI + "|" + versionModelNumber + "|" + catalogNumber + "\n");
				
				
				if(primaryDI.equals(pdi)) {
					
				
					List<DeviceDefinitionDeviceNameComponent> deviceNames = new ArrayList<DeviceDefinitionDeviceNameComponent>();
					
					
					DeviceDefinitionDeviceNameComponent deviceName1 = new DeviceDefinitionDeviceNameComponent();
				
					deviceName1.setName(brandName + "(" + primaryDI + ")");
					deviceName1.setType(DeviceNameType.UDILABELNAME);
					
					
					DeviceDefinitionDeviceNameComponent deviceName2 = new DeviceDefinitionDeviceNameComponent();
					
					deviceName2.setName(brandName);
					deviceName2.setType(DeviceNameType.MANUFACTURERNAME);
					
					deviceNames.add(deviceName1);
					deviceNames.add(deviceName2);


					
					
					if(!deviceDescription.equals("")) {
					
						DeviceDefinitionDeviceNameComponent deviceName3 = new DeviceDefinitionDeviceNameComponent();
					
						deviceName3.setName(deviceDescription);
						deviceName3.setType(DeviceNameType.USERFRIENDLYNAME);
						
						deviceNames.add(deviceName3);
					
					}
					
					
					if(!catalogNumber.equals("")) {
						DeviceDefinitionDeviceNameComponent deviceName4 = new DeviceDefinitionDeviceNameComponent();
					
						deviceName4.setName(catalogNumber);
						deviceName4.setType(DeviceNameType.MODELNAME);
						
						deviceNames.add(deviceName4);
					
					}
					
					
					

					
					deviceDef.setDeviceName(deviceNames);
					
					
					deviceDef.setManufacturer(new StringType(companyName));
					
					deviceDef.setModelNumber(versionModelNumber);
					
					deviceDef.setOnlineInformation("https://accessgudid.nlm.nih.gov/devices/" + pdi);
					
					
					if(primaryDI.equals(di)) {
						Quantity quantity = new Quantity();
						quantity.setValue(Integer.parseInt(deviceCount));
						deviceDef.setQuantity(quantity);
						
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
		
		
		
		
		
		return deviceDef;
		
		
	}
	
	public static String getSCTConceptNameByCode(String fileName, String sct){
    	BufferedReader br = null;
    	String sct_name = null;
    	
    	try{
    		br = new BufferedReader(new FileReader(fileName));
    		String line = br.readLine();
    		line = br.readLine(); //skip the first line
    		while(line != null){
    			String[] items = line.split("\\t");
                String concept_id = items[0];
                String concept_name = items[1];
                String concept_code = items[6];
                //System.out.println(concept_code + "|" + concept_id);
                //sctConceptCodeIdMap.put(concept_code, concept_id);
                
                if(concept_code.equals(sct)) {
                	
                	sct_name = concept_name;
                	break;
                	
                }
    			    			
                line = br.readLine();
    		}
    		
    		br.close();

    	}catch(IOException ie){
    		ie.printStackTrace();
    	}	
    	
    	return sct_name;
	}	
	

	public static List<String> getListOfDeviceIdentifiersFromFle(String fileName){
    	BufferedReader br = null;
    	List<String> pdis = new ArrayList<String>();
    	
    	try{
    		br = new BufferedReader(new FileReader(fileName));
    		String line = br.readLine();
    		//line = br.readLine(); //skip the first line
    		while(line != null){
    			
    			pdis.add(line);
    			    			
                line = br.readLine();
    		}
    		
    		br.close();

    	}catch(IOException ie){
    		ie.printStackTrace();
    	}	
    	
    	return pdis;
	}	
	
	public static void printoutDeviceDefinition(String di) {
		DeviceDefinition ddef = renderGUDIDDeviceInformation(di);
		
		String string = ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(ddef);
	    
		System.out.println(string);
		
		BufferedWriter bw = null;
		
		try {
			
			bw = new BufferedWriter(new FileWriter(outputFilePath + "GUDID-" + di + ".json"));
			bw.write(string + "\n");
			
			bw.close();
			
		}catch(IOException ie) {
			ie.addSuppressed(ie);
		}
		
		
		
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String top50Identifiers = "/Users/m005994/Documents/medical-device/CERSIB3UDI/top50primarydis.txt";

		
		//ProcessFHIRDeviceDefinition model = new ProcessFHIRDeviceDefinition();
		//String primaryID = ProcessFHIRDeviceDefinition.getPrimaryDI("10605388059540");
		//String parentID = ProcessFHIRDeviceDefinition.getParentDI("10605388059540");
		
		//Collection<String> alldis = ProcessFHIRDeviceDefinition.getAllDIsByPrimaryDI("00605388564474");
		
		//for (String di : alldis) {
		//	System.out.println(di);
			
		//}
		
		//System.out.println(primaryID + "|" + parentID);
		

		//printoutDeviceDefinition("10605388059540");
		//printoutDeviceDefinition("10846835009347");
		
		//String code = GUDIDDeviceSNOMEDAPI.getDeviceSNOMEDCode("10846835009347");
		//String name = getSCTConceptNameByCode(ohdsi_sct_concept_file, code);
		//System.out.println(code + "|" + name);
		
		List<String> pdis = getListOfDeviceIdentifiersFromFle(top50Identifiers);
		for(String pdi : pdis) {
			
			Collection<String> alldis = ProcessFHIRDeviceDefinition.getAllDIsByPrimaryDI(pdi);
			
			for (String di : alldis) {
				//System.out.println(di);
				printoutDeviceDefinition(di);
				break;
				
			}
			
			
		}

	}	
}
