package edu.mayo.informatics.fhir.device;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.ws.Response;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class GUDIDDeviceSNOMEDAPI {

    private static String ticketUri = "https://utslogin.nlm.nih.gov/cas/v1/tickets";
    private static String deviceSNOMEDUri = "https://accessgudid.nlm.nih.gov/api/v2/devices/snomed";

    public static String getTicketToken() {
        String ticketToken = "";
        String uri = ticketUri;
        URL url;
        HashMap<String, String> postDataParams = new HashMap();
        String response = "";
        try {
            url = new URL(uri);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
           
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            postDataParams.put("username", "gqjiang");
            postDataParams.put("password", "Dongjie##0619");

            String uriParams = "username=gqjiang&password=Dongjie##0619";

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(uriParams);

            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();
           

            if (responseCode == HttpsURLConnection.HTTP_CREATED) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                    
                    //System.out.println(line);
                }
            } else {
                response = "";
                //System.out.println(conn.getURL() + ":" + responseCode);

            }

            //System.out.println(parseResponseString(response));
            ticketToken = parseResponseString(response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ticketToken;
    }
    
    
    public static String getServiceTicket() {

	        String ticket = getTicketToken();

	        String uri = ticket;

	        URL url;
	        HashMap<String, String> postDataParams = new HashMap();
	        String response = "";
	        try {
	            url = new URL(uri);
	            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
	            conn.setReadTimeout(10000);
	            conn.setConnectTimeout(15000);
	            conn.setRequestMethod("POST");
	            conn.setDoInput(true);
	            conn.setDoOutput(true);

	            postDataParams.put("username", "gqjiang");
	            postDataParams.put("password", "Dongjie##0619");

	            String uriParams = "service=http://umlsks.nlm.nih.gov";

	            OutputStream os = conn.getOutputStream();
	            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
	            writer.write(uriParams);

	            writer.flush();
	            writer.close();
	            os.close();
	            int responseCode = conn.getResponseCode();

	            if (responseCode == HttpsURLConnection.HTTP_OK) {
	                String line;
	                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	                while ((line = br.readLine()) != null) {
	                    response += line;
	                }
	            } else {
	                response = "";
	                //System.out.println(conn.getURL() + ":" + responseCode);

	            }

	            System.out.println(response);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return response;
	    }
    
    public static String parseResponseString(String response){
    	
        int index_action = response.indexOf("action=");
        int index_method = response.indexOf("method=");
        String ticketUri = response.substring(index_action+8, index_method-2);
        
    	
    	return ticketUri;
    	
    }
    
    public static String getDeviceSNOMEDCodeName(String deviceId) {
        StringBuffer sb = new StringBuffer();
        String uri = deviceSNOMEDUri + ".xml";
        URL url;
        HttpURLConnection connection = null;
        String serviceTicket = getServiceTicket();

        String uriParams =  "ticket=" + serviceTicket + "&di=" + deviceId;

        try {
            url = new URL(uri + "?" + uriParams);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            //connection.setInstanceFollowRedirects(false);
            //connection.setRequestProperty("Acceptcharset", "UTF-8");
            // connection.setRequestProperty("Accept-Language",
            // "en-US,en;q=0.5");
            // connection.setRequestProperty("charset", "EN-US");
            //connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            connection.connect();
            // connection.setRequestProperty("Accept", "text/xml");
            if (connection.getResponseCode() != 200) {
                //throw new RuntimeException(
                //        "Failed : The HTTP error code is : " + connection.getResponseCode());
                
            	return "261665006";

            }
            BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
            String output;

            while ((output = br.readLine()) != null) {
                //System.out.println(output);
                sb.append(output + "\n");
            }

           //System.out.println(parseDeviceSNOMEDCTName(sb.toString()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return sb.toString();
    }
    
    
    public static String parseDeviceSNOMEDIdentifier(String response){
    	
    	System.out.println(response);
    	
        int index_start = response.indexOf("<snomedIdentifier type=\"integer\">");
        int index_end = response.indexOf("</snomedIdentifier>");
        String sctCode = response.substring(index_start+33, index_end);
        
    	
    	return sctCode;
    	
    }

    
    public static String parseDeviceSNOMEDCTName(String response){
    	
        int index_start = response.indexOf("<snomedCTName>");
        int index_end = response.indexOf("</snomedCTName>");
        String sctName = response.substring(index_start+33, index_end);
        
    	
    	return sctName;
    	
    }   
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//getServiceTicket();
		
		getDeviceSNOMEDCodeName("00801902041982");
	}

}
