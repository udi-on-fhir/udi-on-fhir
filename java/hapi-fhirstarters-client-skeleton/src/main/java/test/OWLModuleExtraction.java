package test;

import static org.semanticweb.owlapi.model.parameters.Imports.INCLUDED;
import static org.semanticweb.owlapi.search.Searcher.annotationObjects;
import static org.semanticweb.owlapi.search.Searcher.sup;
import static org.semanticweb.owlapi.vocab.OWLFacet.MAX_EXCLUSIVE;
import static org.semanticweb.owlapi.vocab.OWLFacet.MIN_INCLUSIVE;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import org.semanticweb.HermiT.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.ManchesterSyntaxDocumentFormat;
import org.semanticweb.owlapi.formats.OWLXMLDocumentFormat;
import org.semanticweb.owlapi.formats.TurtleDocumentFormat;
import org.semanticweb.owlapi.io.StreamDocumentTarget;
import org.semanticweb.owlapi.io.StringDocumentSource;
import org.semanticweb.owlapi.io.StringDocumentTarget;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.AddOntologyAnnotation;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataExactCardinality;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLDataUnionOf;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLDatatypeDefinitionAxiom;
import org.semanticweb.owlapi.model.OWLDatatypeRestriction;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLDocumentFormat;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLFacetRestriction;
import org.semanticweb.owlapi.model.OWLFunctionalDataPropertyAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyIRIMapper;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.model.SWRLAtom;
import org.semanticweb.owlapi.model.SWRLObjectPropertyAtom;
import org.semanticweb.owlapi.model.SWRLRule;
import org.semanticweb.owlapi.model.SWRLVariable;
import org.semanticweb.owlapi.model.SetOntologyID;
import org.semanticweb.owlapi.reasoner.BufferingMode;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasoner;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.search.Filters;
import org.semanticweb.owlapi.util.AutoIRIMapper;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.util.InferredAxiomGenerator;
import org.semanticweb.owlapi.util.InferredOntologyGenerator;
import org.semanticweb.owlapi.util.InferredSubClassAxiomGenerator;
import org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter;
import org.semanticweb.owlapi.util.OWLEntityRemover;
import org.semanticweb.owlapi.util.OWLOntologyMerger;
import org.semanticweb.owlapi.util.OWLOntologyWalker;
import org.semanticweb.owlapi.util.OWLOntologyWalkerVisitorEx;
import org.semanticweb.owlapi.util.SimpleIRIMapper;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.semanticweb.owlapi.vocab.OWLFacet;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import uk.ac.manchester.cs.owlapi.modularity.ModuleType;
import uk.ac.manchester.cs.owlapi.modularity.SyntacticLocalityModuleExtractor;

public class OWLModuleExtraction {
	
	private static String sctOnt = "/Users/m005994/snomed-owl-toolkit/ontology-2021-04-07_08-55-31.owl";
	private static String modOnt = "/Users/m005994/snomed-owl-toolkit/ontology-mod.owl";
	private static String modOntTop100 = "/Users/m005994/snomed-owl-toolkit/ontology-mod-top100.owl";
	private static String acuteDiseases = "http://snomed.info/id/2704003";
	private static String acuteRenalFailure = "http://snomed.info/id/14669001";
	
	private static String sigFile = "/Users/m005994/snomed-owl-toolkit/snomed_codes_ltn.txt";
	private static String sigFileTop100 = "/Users/m005994/snomed-owl-toolkit/snomed_codes_ltn_top100.txt";

	private static String output_direct = "/Users/m005994/snomed-owl-toolkit/subclassof_direct_ltn.txt";
	private static String output_all = "/Users/m005994/snomed-owl-toolkit/subclassof_all_ltn.txt";

	
	private static String output_direct_top100 = "/Users/m005994/snomed-owl-toolkit/subclassof_direct_ltn_top100.txt";
	private static String output_all_top100 = "/Users/m005994/snomed-owl-toolkit/subclassof_all_ltn_top100.txt";
	

	
	   /**
     * Visits existential restrictions and collects the properties which are restricted.
     */
    private static class RestrictionVisitor extends OWLClassExpressionVisitorAdapter {

        @Nonnull
        private final Set<OWLClass> processedClasses;
        private final Set<OWLOntology> onts;

        RestrictionVisitor(Set<OWLOntology> onts) {
            processedClasses = new HashSet<OWLClass>();
            this.onts = onts;
        }

        @Override
        public void visit(OWLClass ce) {
            if (!processedClasses.contains(ce)) {
                // If we are processing inherited restrictions then we
                // recursively visit named supers. Note that we need to keep
                // track of the classes that we have processed so that we don't
                // get caught out by cycles in the taxonomy
                processedClasses.add(ce);
                for (OWLOntology ont : onts) {
                    for (OWLSubClassOfAxiom ax : ont.getSubClassAxiomsForSubClass(ce)) {
                        ax.getSuperClass().accept(this);
                    }
                }
            }
        }
    }
    
    
    /**
     * This example shows how to examine the restrictions on a class.
     * 
     * @throws Exception exception
     */
    public static void shouldLookAtRestrictions() throws Exception {
        // Create our manager
        OWLOntologyManager man = OWLManager.createOWLOntologyManager();
        // Load the Koala ontology
		File ontFile = new File(modOnt);

		OWLDataFactory dataFactory = OWLManager.getOWLDataFactory();
		// Load the SCT ontology
		OWLOntology ont = man.loadOntologyFromOntologyDocument(ontFile);
		
		Set<OWLOntology> onts = new HashSet<OWLOntology>();
		onts.add(ont);
		
        // We want to examine the restrictions on Quokka. To do this,
        // we need to obtain a reference to the Quokka class. In this
        // case, we know the IRI (it happens to be the ontology IRI + #Quokka
        // This isn't always the case. A class may have a IRI that bears no
        // resemblance to the ontology IRI which contains axioms about the
        // class.
        IRI arfIRI = IRI.create(acuteRenalFailure);
        OWLClass arf = man.getOWLDataFactory().getOWLClass(arfIRI);
        // Now we want to collect the properties which are used in existential
        // restrictions on the class. To do this, we will create a utility class
        // - RestrictionVisitor, which acts as a filter for existential
        // restrictions. This uses the Visitor Pattern (google Visitor Design
        // Pattern for more information on this design pattern, or see
        // http://en.wikipedia.org/wiki/Visitor_pattern)
        RestrictionVisitor restrictionVisitor = new RestrictionVisitor(onts);
        // In this case, restrictions are used as (anonymous) superclasses, so
        // to get the restrictions on quokka we need to obtain the
        // subclass axioms for quokka.
        for (OWLSubClassOfAxiom ax : ont.getSubClassAxiomsForSubClass(arf)) {
            ax.getSuperClass().accept(restrictionVisitor);
            System.out.println(ax.toString());
        }

        
       
    }
    
    
	private static void getRDFSLabelForClasses() throws Exception {
		
		Set<String> rdfslabels = new HashSet<String>();

		// Create our manager
		OWLOntologyManager man = OWLManager.createOWLOntologyManager();

		File ontFile = new File(modOntTop100);

		OWLDataFactory dataFactory = OWLManager.getOWLDataFactory();
		// Load the SCT ontology
		OWLOntology ont = man.loadOntologyFromOntologyDocument(ontFile);
		// We want to extract a module for all toppings. We therefore have to
		// generate a seed signature that contains "Quokka" and its
		// subclasses. We start by creating a signature that consists of
		// "Quokka".

		//IRI iri = IRI.create(acuteRenalFailure);

		//OWLClass toppingCls = dataFactory.getOWLClass(iri);
		
		
		//System.out.println(ont.getClassesInSignature().size());
		
		
		
		
		Set<OWLEntity> sig = getSignaturesFromFile(sigFile);
		
		
		for (OWLEntity ent : sig) {
	
			for(OWLAnnotationAssertionAxiom a : ont.getAnnotationAssertionAxioms(((OWLClass)ent).getIRI())) {
			    if(a.getProperty().isLabel()) {
			        if(a.getValue() instanceof OWLLiteral) {
			            OWLLiteral val = (OWLLiteral) a.getValue();
			            System.out.println(((OWLClass)ent).getIRI() + "|" + val.getLiteral());
			        }
			    }
			}
			
		}
			

	}  
    

	private static void ExtractSuperClasses() throws Exception {
		
		Set<String> subclassofs = new HashSet<String>();

		// Create our manager
		OWLOntologyManager man = OWLManager.createOWLOntologyManager();

		File ontFile = new File(sctOnt);

		OWLDataFactory dataFactory = OWLManager.getOWLDataFactory();
		// Load the SCT ontology
		OWLOntology ont = man.loadOntologyFromOntologyDocument(ontFile);
		// We want to extract a module for all toppings. We therefore have to
		// generate a seed signature that contains "Quokka" and its
		// subclasses. We start by creating a signature that consists of
		// "Quokka".

		//IRI iri = IRI.create(acuteRenalFailure);

		//OWLClass toppingCls = dataFactory.getOWLClass(iri);
		
		
		//System.out.println(ont.getClassesInSignature().size());
		
		
		Set<OWLEntity> sig = getSignaturesFromFile(sigFile);
		//sig.addAll(ont.getClassesInSignature());
		//sig.add(toppingCls);
		// We now add all subclasses (direct and indirect) of the chosen
		// classes. Ideally, it should be done using a DL reasoner, in order to
		// take inferred subclass relations into account. We are using the
		// structural reasoner of the OWL API for simplicity.
		//Set<OWLEntity> seedSig = new HashSet<OWLEntity>();
		OWLReasoner reasoner = new StructuralReasoner(ont, new SimpleConfiguration(), BufferingMode.NON_BUFFERING);
		
		OWLReasonerFactory rf = new ReasonerFactory();
		OWLReasoner r = rf.createReasoner(ont);
		r.precomputeInferences(InferenceType.CLASS_HIERARCHY);		


		
		for (OWLEntity ent : sig) {
			//seedSig.add(ent);
			if (OWLClass.class.isAssignableFrom(ent.getClass())) {
				//NodeSet<OWLClass> subClasses = r.getSubClasses((OWLClass) ent, false);
				//seedSig.addAll(subClasses.getFlattened());
				NodeSet<OWLClass> superClasses = r.getSuperClasses((OWLClass) ent, false); // true indicates direct
				
				Set<OWLClass> fsuperClasses = superClasses.getFlattened();
				
				for(OWLClass superCls : fsuperClasses) {
					
					subclassofs.add("SubClassOf(" + ent.getIRI() + ", " + superCls.getIRI() + ")");
				
					System.out.println("all-SubClassOf(" + ent.getIRI() + "," + superCls.getIRI() + ")");
				
				}
				
				
			}
		}
			
		saveSetAsFile(subclassofs, output_all_top100);		

	}
	
	
	
	private static void ExtractSuperClassesForOntology() throws Exception {
		
		Set<String> subclassofs = new HashSet<String>();

		// Create our manager
		OWLOntologyManager man = OWLManager.createOWLOntologyManager();

		File ontFile = new File(modOntTop100);

		OWLDataFactory dataFactory = OWLManager.getOWLDataFactory();
		// Load the SCT ontology
		OWLOntology ont = man.loadOntologyFromOntologyDocument(ontFile);
		// We want to extract a module for all toppings. We therefore have to
		// generate a seed signature that contains "Quokka" and its
		// subclasses. We start by creating a signature that consists of
		// "Quokka".

		//IRI iri = IRI.create(acuteRenalFailure);

		//OWLClass toppingCls = dataFactory.getOWLClass(iri);
		
		
		//System.out.println(ont.getClassesInSignature().size());
		
		//Set<OWLClass> all_classes = ont.getClassesInSignature();
		
		
		
		Set<OWLEntity> sig = new HashSet<OWLEntity>();
		sig.addAll(ont.getClassesInSignature());
		//sig.add(toppingCls);
		// We now add all subclasses (direct and indirect) of the chosen
		// classes. Ideally, it should be done using a DL reasoner, in order to
		// take inferred subclass relations into account. We are using the
		// structural reasoner of the OWL API for simplicity.
		//Set<OWLEntity> seedSig = new HashSet<OWLEntity>();
		OWLReasoner reasoner = new StructuralReasoner(ont, new SimpleConfiguration(), BufferingMode.NON_BUFFERING);
		
		OWLReasonerFactory rf = new ReasonerFactory();
		OWLReasoner r = rf.createReasoner(ont);
		r.precomputeInferences(InferenceType.CLASS_HIERARCHY);		


		
		for (OWLEntity ent : sig) {
			//seedSig.add(ent);
			if (OWLClass.class.isAssignableFrom(ent.getClass())) {
				//NodeSet<OWLClass> subClasses = r.getSubClasses((OWLClass) ent, false);
				//seedSig.addAll(subClasses.getFlattened());
				NodeSet<OWLClass> superClasses = r.getSuperClasses((OWLClass) ent, false); // true indicates direct
				
				Set<OWLClass> fsuperClasses = superClasses.getFlattened();
				
				for(OWLClass superCls : fsuperClasses) {
					
					subclassofs.add("SubClassOf(" + ent.getIRI() + ", " + superCls.getIRI() + ")");
				
					System.out.println("all-SubClassOf(" + ent.getIRI() + "," + superCls.getIRI() + ")");
				
				}
				
				
			}
		}
			
		saveSetAsFile(subclassofs, output_all_top100);		

	}	
	
	
	private static void saveSetAsFile(Set<String> set, String outputFile) throws Exception {
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
		
		for(String ent : set) {
			
			bw.write(ent);
			bw.write("\n");
			
		}
		
		bw.close();
		
	}
	
	private static Set<OWLEntity> getSignaturesFromFile(String fileName) throws Exception {
		
	    Set<OWLEntity> signatures = new HashSet<OWLEntity>();
	    OWLDataFactory dataFactory = OWLManager.getOWLDataFactory();
		
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		
		String line = br.readLine();
		while(line != null) {
			
			
			
			
			IRI iri = IRI.create("http://snomed.info/id/" + line.trim());
			OWLClass signature = dataFactory.getOWLClass(iri);
			

			
			System.out.println();
			
			signatures.add(signature);
			
			line = br.readLine();
			
		}
		
		br.close();
		
		
		return signatures;
		
	}
	
	private static void ExtractModuleUsingSignatureFile() throws Exception {

		// Create our manager
		OWLOntologyManager man = OWLManager.createOWLOntologyManager();

		File ontFile = new File(sctOnt);

		OWLDataFactory dataFactory = OWLManager.getOWLDataFactory();
		// Load the SCT ontology
		OWLOntology ont = man.loadOntologyFromOntologyDocument(ontFile);
		// We want to extract a module for all toppings. We therefore have to
		// generate a seed signature that contains "Quokka" and its
		// subclasses. We start by creating a signature that consists of
		// "Quokka".


		Set<OWLEntity> sig = getSignaturesFromFile(sigFileTop100);


		// We now extract a locality-based module. For most reuse purposes, the
		// module type should be STAR -- this yields the smallest possible
		// locality-based module. These modules guarantee that all entailments
		// of the original ontology that can be formulated using only terms from
		// the seed signature or the module will also be entailments of the
		// module. In easier words, the module preserves all knowledge of the
		// ontology about the terms in the seed signature or the module.
		SyntacticLocalityModuleExtractor sme = new SyntacticLocalityModuleExtractor(man, ont, ModuleType.STAR);
		IRI moduleIRI = IRI.create("http://snomed.info/sct/900000000000207008/version/20210410");
		OWLOntology mod = sme.extractAsOntology(sig, moduleIRI);
		// The module can now be saved as usual

		OWLXMLDocumentFormat owlxmlFormat = new OWLXMLDocumentFormat();
		File modFile = new File(modOntTop100);
		man.saveOntology(mod, owlxmlFormat, IRI.create(modFile.toURI()));

	}

    
	private static void ExtractModule() throws Exception {

		// Create our manager
		OWLOntologyManager man = OWLManager.createOWLOntologyManager();

		File ontFile = new File(sctOnt);

		OWLDataFactory dataFactory = OWLManager.getOWLDataFactory();
		// Load the SCT ontology
		OWLOntology ont = man.loadOntologyFromOntologyDocument(ontFile);
		// We want to extract a module for all toppings. We therefore have to
		// generate a seed signature that contains "Quokka" and its
		// subclasses. We start by creating a signature that consists of
		// "Quokka".

		IRI iri = IRI.create(acuteRenalFailure);

		OWLClass toppingCls = dataFactory.getOWLClass(iri);

		Set<OWLEntity> sig = new HashSet<OWLEntity>();
		sig.add(toppingCls);
		// We now add all subclasses (direct and indirect) of the chosen
		// classes. Ideally, it should be done using a DL reasoner, in order to
		// take inferred subclass relations into account. We are using the
		// structural reasoner of the OWL API for simplicity.
		Set<OWLEntity> seedSig = new HashSet<OWLEntity>();
		OWLReasoner reasoner = new StructuralReasoner(ont, new SimpleConfiguration(), BufferingMode.NON_BUFFERING);
		for (OWLEntity ent : sig) {
			seedSig.add(ent);
			if (OWLClass.class.isAssignableFrom(ent.getClass())) {
				NodeSet<OWLClass> subClasses = reasoner.getSubClasses((OWLClass) ent, false);
				seedSig.addAll(subClasses.getFlattened());
			}
		}
		// We now extract a locality-based module. For most reuse purposes, the
		// module type should be STAR -- this yields the smallest possible
		// locality-based module. These modules guarantee that all entailments
		// of the original ontology that can be formulated using only terms from
		// the seed signature or the module will also be entailments of the
		// module. In easier words, the module preserves all knowledge of the
		// ontology about the terms in the seed signature or the module.
		SyntacticLocalityModuleExtractor sme = new SyntacticLocalityModuleExtractor(man, ont, ModuleType.STAR);
		IRI moduleIRI = IRI.create("http://snomed.info/sct/900000000000207008/version/20210408");
		OWLOntology mod = sme.extractAsOntology(seedSig, moduleIRI);
		// The module can now be saved as usual

		OWLXMLDocumentFormat owlxmlFormat = new OWLXMLDocumentFormat();
		File modFile = new File(modOnt);
		man.saveOntology(mod, owlxmlFormat, IRI.create(modFile.toURI()));

	}





	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try {
			
			
			//ExtractModuleUsingSignatureFile();
			
			//ExtractSuperClasses();
			
			//ExtractSuperClassesForOntology();
			
			//getRDFSLabelForClasses();
			
		} catch(Exception e) {
			e.printStackTrace();

		}
	}

}
