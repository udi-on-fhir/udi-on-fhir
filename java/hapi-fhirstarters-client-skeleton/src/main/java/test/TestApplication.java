package test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;

import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Patient;

public class TestApplication {

   /**
    * This is the Java main method, which gets executed
    */
   public static void main(String[] args) {

      // Create a context
      FhirContext ctx = FhirContext.forR4();

      // Create a client
      IGenericClient client = ctx.newRestfulGenericClient("https://hapi.fhir.org/baseR4");

      // Read a patient with the given ID
      //Patient patient = client.read().resource(Patient.class).withId("1405199").execute();
      
      //String string = ctx.newRDFParser().setPrettyPrint(true).encodeResourceToString(patient);
      //System.out.println(string);
      
      
      Condition condition = client.read().resource(Condition.class).withId("4faa5764-1642-41fc-aadb-81c168834d7a").execute();
      
      
   // Create an extension
      Extension ext = new Extension();
      ext.setUrl("teo:before");
      ext.setValue(new DateTimeType("2011-01-02T11:13:15"));
      
      Extension extNLP = new Extension();
      extNLP.setUrl("nlp:span_start");
      extNLP.setValue(new IntegerType("266"));

      
      condition.addExtension(ext);
      
      CodeableConcept code = condition.getCode();
      
      code.addExtension(extNLP);

      // Print the output
      String string = ctx.newRDFParser().setPrettyPrint(true).encodeResourceToString(condition);
      System.out.println(string);

   }

}
