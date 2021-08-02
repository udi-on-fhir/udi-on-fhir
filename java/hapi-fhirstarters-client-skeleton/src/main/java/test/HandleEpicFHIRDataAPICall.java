package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.hl7.fhir.r4.model.Bundle;

import ca.uhn.fhir.context.FhirContext;

public class HandleEpicFHIRDataAPICall {

    // http://localhost:8080/RESTfulExample/json/product/get
    public static void main(String[] args) {
    	
        // Create a context
        FhirContext ctx = FhirContext.forR4();


      try {

        //URL url = new URL("https://icbg-np.et0958.epichosted.com/Interconnect-WebSvc-POC/api/FHIR/R4/Patient?family=Smith");
        //URL url = new URL("https://icbg-np.et0958.epichosted.com/Interconnect-WebSvc-POC/api/FHIR/R4/Patient?identifier=MC|3982547");
        //URL url = new URL("https://icbg-np.et0958.epichosted.com/Interconnect-WebSvc-POC/api/FHIR/R4/Patient?identifier=MC|12138154");
    	//URL url = new URL("https://icbg-np.et0958.epichosted.com/Interconnect-WebSvc-POC/api/FHIR/R4/Procedure?patient=ejS.eJlAVrT4EkUHQreD6OA3");
    	
    	//URL url = new URL ("https://icbg-np.et0958.epichosted.com/Interconnect-WebSvc-TSTECP/api/FHIR/R4/Patient?identifier=MC|11200226"); 

    	URL url = new URL ("https://icbg-np.et0958.epichosted.com/Interconnect-WebSvc-TSTECP/api/FHIR/R4/Device?patient=eMs1GYI51zhAqgqSNMdP.rQ3"); 
    	
    	
    	
    	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Epic-Client-ID", "370a2b31-c7f7-46d3-90bf-261b15d695cb");
        conn.setRequestProperty("Epic-User-ID", "12804");
        conn.setRequestProperty("Epic-User-IDType", "External");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(
            (conn.getInputStream())));

        String output;
        StringBuffer sb = new StringBuffer();
        System.out.println("Output from Server .... \n");
        while ((output = br.readLine()) != null) {
            System.out.println(output);
            
            sb.append(output);
        }
        
     // Print the output
        Bundle bundle = ctx.newJsonParser().setPrettyPrint(true).parseResource(Bundle.class, sb.toString());
        
        System.out.println(ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));
        

        conn.disconnect();

      } catch (MalformedURLException e) {

        e.printStackTrace();

      } catch (IOException e) {

        e.printStackTrace();

      }

    }

}