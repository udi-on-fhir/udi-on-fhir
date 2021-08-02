package test;


import static org.semanticweb.owlapi.model.parameters.Imports.INCLUDED;
import static org.semanticweb.owlapi.search.Searcher.annotationObjects;
import static org.semanticweb.owlapi.search.Searcher.sup;
import static org.semanticweb.owlapi.vocab.OWLFacet.MAX_EXCLUSIVE;
import static org.semanticweb.owlapi.vocab.OWLFacet.MIN_INCLUSIVE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;


import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.ManchesterSyntaxDocumentFormat;
import org.semanticweb.owlapi.formats.OWLXMLDocumentFormat;
import org.semanticweb.owlapi.formats.TurtleDocumentFormat;
import org.semanticweb.owlapi.io.StreamDocumentTarget;
import org.semanticweb.owlapi.io.StringDocumentSource;
import org.semanticweb.owlapi.io.StringDocumentTarget;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.AddOntologyAnnotation;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLAxiomVisitor;
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
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLFacetRestriction;
import org.semanticweb.owlapi.model.OWLFunctionalDataPropertyAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
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
import org.semanticweb.owlapi.util.OWLEntityRenamer;
import org.semanticweb.owlapi.util.OWLOntologyMerger;
import org.semanticweb.owlapi.util.OWLOntologyWalker;
import org.semanticweb.owlapi.util.OWLOntologyWalkerVisitorEx;
import org.semanticweb.owlapi.util.SimpleIRIMapper;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.semanticweb.owlapi.vocab.OWLFacet;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import uk.ac.manchester.cs.owlapi.modularity.ModuleType;
import uk.ac.manchester.cs.owlapi.modularity.SyntacticLocalityModuleExtractor;



public class HandleFHIRRDFLite {


	private static String fhirModelOnt = "/Users/m005994/Documents/fhircat-2021/fhir-rdf-lite/fhir.ttl";
	private static String w5Ont = "/Users/m005994/Documents/fhircat-2021/fhir-rdf-lite/w5.ttl";
	

	
    /**
     * The examples here show how to load ontologies.
     * 
     * @throws Exception exception
     */
    public static void shouldLoad() throws Exception {
		// Create our manager
		OWLOntologyManager man = OWLManager.createOWLOntologyManager();

		File fhirOntFile = new File(fhirModelOnt);

		OWLDataFactory dataFactory = OWLManager.getOWLDataFactory();
		// Load the SCT ontology
		OWLOntology fhirOnt = man.loadOntologyFromOntologyDocument(fhirOntFile);
		
		File w5OntFile = new File(w5Ont);
		OWLOntology w5Ontology = man.loadOntologyFromOntologyDocument(w5OntFile);
		
		
		
		
		
		IRI fhirOntologyIRI = IRI.create("http://hl7.org/fhir/");
		
		IRI w5OntologyIRI = IRI.create("http://hl7.org/fhir/w5#");
		
		
		man.getIRIMappers().add(new SimpleIRIMapper(fhirOntologyIRI,w5OntologyIRI));
		
		
		PrefixManager pmfhir = new DefaultPrefixManager(null, null, fhirOntologyIRI.toString());
		PrefixManager pmw5 = new DefaultPrefixManager(null, null, w5OntologyIRI.toString());
		
		OWLObjectProperty why = dataFactory.getOWLObjectProperty(":why", pmw5);
		
		
		
		OWLObjectProperty conceptMapPurpose = dataFactory.getOWLObjectProperty(":ConceptMap.purpose", pmfhir);
		
		System.out.println(why.getIRI());
		
		OWLSubObjectPropertyOfAxiom subAxiom = dataFactory.getOWLSubObjectPropertyOfAxiom(conceptMapPurpose, why);
		
		System.out.println(subAxiom.toString());
		
		

		
    }
    
    
    public static Set<OWLObjectPropertyExpression> shouldReasoningSubproperties(OWLObjectProperty prop) throws Exception {
    	
		OWLOntologyManager man = OWLManager.createOWLOntologyManager();

		File fhirOntFile = new File(fhirModelOnt);

		OWLDataFactory dataFactory = OWLManager.getOWLDataFactory();
		// Load the SCT ontology
		OWLOntology fhirOnt = man.loadOntologyFromOntologyDocument(fhirOntFile);

    	
    	OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();

    	OWLReasoner reasoner = reasonerFactory.createReasoner(fhirOnt);
        // Ask the reasoner to do all the necessary work now
        reasoner.precomputeInferences();
        // We can determine if the ontology is actually consistent (in this
        // case, it should be).
        boolean consistent = reasoner.isConsistent();
		
        //OWLObjectProperty why = dataFactory.getOWLObjectProperty(IRI.create("http://hl7.org/fhir/w5#" + "why"));
        
        NodeSet<OWLObjectPropertyExpression> subproperties =  reasoner.getSubObjectProperties(prop, true);

        Set<OWLObjectPropertyExpression> osubprops = subproperties.getFlattened();
        //for(OWLObjectPropertyExpression osubprop : osubprops) {
        	
        //	System.out.println("osubprop: " + osubprop);
        	
        //}
        
        return osubprops;

    	
    }
    
    public static void shouldReasoningW5Subproperties(OWLObjectProperty prop) throws Exception {
    	
		OWLOntologyManager man = OWLManager.createOWLOntologyManager();
		
		File fhirOntFile = new File(fhirModelOnt);


		OWLDataFactory dataFactory = OWLManager.getOWLDataFactory();
		// Load the SCT ontology
	
		OWLOntology fhirOnt = man.loadOntologyFromOntologyDocument(fhirOntFile);

		
		//class
    	OWLObjectProperty w5class = dataFactory.getOWLObjectProperty(IRI.create("http://hl7.org/fhir/w5#" + "class"));
    	
    	//context
    	OWLObjectProperty w5context = dataFactory.getOWLObjectProperty(IRI.create("http://hl7.org/fhir/w5#" + "context"));
    	
    	//grade
    	OWLObjectProperty w5grade = dataFactory.getOWLObjectProperty(IRI.create("http://hl7.org/fhir/w5#" + "grade"));   	
		
		//id
    	OWLObjectProperty w5id = dataFactory.getOWLObjectProperty(IRI.create("http://hl7.org/fhir/w5#" + "id"));
    	
    	//status
    	OWLObjectProperty w5status = dataFactory.getOWLObjectProperty(IRI.create("http://hl7.org/fhir/w5#" + "status"));

    	//what
    	OWLObjectProperty w5what = dataFactory.getOWLObjectProperty(IRI.create("http://hl7.org/fhir/w5#" + "what"));

    	
    	//when
    	OWLObjectProperty w5when = dataFactory.getOWLObjectProperty(IRI.create("http://hl7.org/fhir/w5#" + "when"));
    	OWLObjectProperty w5whendone = dataFactory.getOWLObjectProperty(IRI.create("http://hl7.org/fhir/w5#" + "when.done"));
    	OWLObjectProperty w5wheninit = dataFactory.getOWLObjectProperty(IRI.create("http://hl7.org/fhir/w5#" + "when.init"));
    	OWLObjectProperty w5whenplanned = dataFactory.getOWLObjectProperty(IRI.create("http://hl7.org/fhir/w5#" + "when.planned"));
    	OWLObjectProperty w5whenrecorded = dataFactory.getOWLObjectProperty(IRI.create("http://hl7.org/fhir/w5#" + "when.recorded"));

    	
    	//where
    	OWLObjectProperty w5where = dataFactory.getOWLObjectProperty(IRI.create("http://hl7.org/fhir/w5#" + "where"));

    	
    	
		//who
    	OWLObjectProperty w5who = dataFactory.getOWLObjectProperty(IRI.create("http://hl7.org/fhir/w5#" + "who"));
    	OWLObjectProperty w5whoactor = dataFactory.getOWLObjectProperty(IRI.create("http://hl7.org/fhir/w5#" + "who.actor"));
       	OWLObjectProperty w5whoauthor = dataFactory.getOWLObjectProperty(IRI.create("http://hl7.org/fhir/w5#" + "who.author"));
    	OWLObjectProperty w5whocause = dataFactory.getOWLObjectProperty(IRI.create("http://hl7.org/fhir/w5#" + "who.cause"));
       	OWLObjectProperty w5whofocus = dataFactory.getOWLObjectProperty(IRI.create("http://hl7.org/fhir/w5#" + "who.focus"));
    	OWLObjectProperty w5whosource = dataFactory.getOWLObjectProperty(IRI.create("http://hl7.org/fhir/w5#" + "who.source"));
       	OWLObjectProperty w5whowitness = dataFactory.getOWLObjectProperty(IRI.create("http://hl7.org/fhir/w5#" + "who.witness"));
       	
       	
       	//why
    	OWLObjectProperty w5why = dataFactory.getOWLObjectProperty(IRI.create("http://hl7.org/fhir/w5#" + "why"));
    	
    	Set<OWLObjectProperty> w5properties = new HashSet<OWLObjectProperty>();
    	w5properties.add(w5class);
    	w5properties.add(w5context);
    	w5properties.add(w5grade);
    	w5properties.add(w5id);
    	w5properties.add(w5what);
    	//w5properties.add(w5when);
    	w5properties.add(w5whendone);
    	w5properties.add(w5wheninit);
    	w5properties.add(w5whenplanned);
    	w5properties.add(w5whenrecorded);
    	w5properties.add(w5where);
    	//w5properties.add(w5who);
    	w5properties.add(w5whoactor);
    	w5properties.add(w5whoauthor);
    	w5properties.add(w5whocause);
    	w5properties.add(w5whofocus);
    	w5properties.add(w5whosource);
    	w5properties.add(w5whowitness);
    	w5properties.add(w5why);

       	
    	
    	//processing all w5 properties except for who and when
		for (OWLObjectProperty w5property : w5properties) {
			Set<OWLObjectPropertyExpression> subproperties = shouldReasoningSubproperties(w5property);

			for (OWLObjectPropertyExpression subprop : subproperties) {

				String fragment = ((OWLEntity) subprop).getIRI().getFragment();
				
				//if(!fragment.startsWith("who.") || !fragment.startsWith("when.")) {
				
				int index = fragment.lastIndexOf(".");
				if (index >= 0) {
					fragment = fragment.substring(index + 1);
				}
				//}

				OWLAnnotation labelAnno = dataFactory.getOWLAnnotation(dataFactory.getRDFSLabel(),
						dataFactory.getOWLLiteral(fragment));
				OWLAxiom ax = dataFactory.getOWLAnnotationAssertionAxiom(IRI.create("http://hl7.org/fhir/" + fragment),
						labelAnno);

				AddAxiom addLabelAxiom = new AddAxiom(fhirOnt, ax);
				man.applyChange(addLabelAxiom);

				OWLObjectProperty fragmentprop = dataFactory
						.getOWLObjectProperty(IRI.create("http://hl7.org/fhir/" + fragment));

				OWLSubObjectPropertyOfAxiom subAxiom = dataFactory.getOWLSubObjectPropertyOfAxiom(fragmentprop,
						w5property);

				OWLSubObjectPropertyOfAxiom subAxiomLite = dataFactory.getOWLSubObjectPropertyOfAxiom(subprop,fragmentprop);

				OWLSubObjectPropertyOfAxiom subAxiomOrig = dataFactory.getOWLSubObjectPropertyOfAxiom(subprop,
						w5property);

				AddAxiom addAxiom = new AddAxiom(fhirOnt, subAxiom);

				man.applyChange(addAxiom);

				AddAxiom addAxiomLite = new AddAxiom(fhirOnt, subAxiomLite);

				man.applyChange(addAxiomLite);

				Set<OWLAxiom> axiomsToRemove = new HashSet<OWLAxiom>();

				axiomsToRemove.add(subAxiomOrig);

				man.removeAxioms(fhirOnt, axiomsToRemove);

			}
		}
		
		
		//processing who
		Set<OWLObjectPropertyExpression> subproperties = shouldReasoningSubproperties(w5who);

		for (OWLObjectPropertyExpression subprop : subproperties) {
			

			String fragment = ((OWLEntity) subprop).getIRI().getFragment();
			
			if(!fragment.startsWith("who")) {
			
			//if(!fragment.startsWith("who.") || !fragment.startsWith("when.")) {
			
			int index = fragment.lastIndexOf(".");
			if (index >= 0) {
				fragment = fragment.substring(index + 1);
			}
			//}

			OWLAnnotation labelAnno = dataFactory.getOWLAnnotation(dataFactory.getRDFSLabel(),
					dataFactory.getOWLLiteral(fragment));
			OWLAxiom ax = dataFactory.getOWLAnnotationAssertionAxiom(IRI.create("http://hl7.org/fhir/" + fragment),
					labelAnno);

			AddAxiom addLabelAxiom = new AddAxiom(fhirOnt, ax);
			man.applyChange(addLabelAxiom);

			OWLObjectProperty fragmentprop = dataFactory
					.getOWLObjectProperty(IRI.create("http://hl7.org/fhir/" + fragment));

			OWLSubObjectPropertyOfAxiom subAxiom = dataFactory.getOWLSubObjectPropertyOfAxiom(fragmentprop,
					w5who);

			OWLSubObjectPropertyOfAxiom subAxiomLite = dataFactory.getOWLSubObjectPropertyOfAxiom(subprop,fragmentprop);

			OWLSubObjectPropertyOfAxiom subAxiomOrig = dataFactory.getOWLSubObjectPropertyOfAxiom(subprop,
					w5who);

			AddAxiom addAxiom = new AddAxiom(fhirOnt, subAxiom);

			man.applyChange(addAxiom);

			AddAxiom addAxiomLite = new AddAxiom(fhirOnt, subAxiomLite);

			man.applyChange(addAxiomLite);

			Set<OWLAxiom> axiomsToRemove = new HashSet<OWLAxiom>();

			axiomsToRemove.add(subAxiomOrig);

			man.removeAxioms(fhirOnt, axiomsToRemove);
			}

		}
		

		man.saveOntology(fhirOnt);
	}
    
    public static void testLookupRestrictions() throws OWLException {
		OWLOntologyManager man = OWLManager.createOWLOntologyManager();

		File fhirOntFile = new File(fhirModelOnt);

		OWLDataFactory dataFactory = OWLManager.getOWLDataFactory();
		// Load the SCT ontology
		OWLOntology fhirOnt = man.loadOntologyFromOntologyDocument(fhirOntFile);
		
		File w5OntFile = new File(w5Ont);
		OWLOntology w5Ontology = man.loadOntologyFromOntologyDocument(w5OntFile);
		
		
		
		
		
		IRI fhirOntologyIRI = IRI.create("http://hl7.org/fhir/");
		
		IRI w5OntologyIRI = IRI.create("http://hl7.org/fhir/w5#");
		
		
		man.getIRIMappers().add(new SimpleIRIMapper(fhirOntologyIRI,w5OntologyIRI));
		
		PrefixManager pmfhir = new DefaultPrefixManager(null, null, fhirOntologyIRI.toString());
		PrefixManager pmw5 = new DefaultPrefixManager(null, null, w5OntologyIRI.toString());

		 OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
	       
		
		OWLReasoner reasoner = reasonerFactory.createReasoner(fhirOnt);
        // Ask the reasoner to do all the necessary work now
        reasoner.precomputeInferences();
        // We can determine if the ontology is actually consistent (in this
        // case, it should be).
        boolean consistent = reasoner.isConsistent();
		
		
		OWLClass domainResourceCls = dataFactory.getOWLClass(":DomainResource", pmfhir);
		
		NodeSet<OWLClass> subClses = reasoner.getSubClasses(domainResourceCls, true);
		
		Set<OWLClass> clses = subClses.getFlattened();
		
		
        Map<OWLEntity, IRI> entity2IRIMap = new HashMap<>();             
        OWLEntityRenamer renamer = new OWLEntityRenamer(man, Collections.singleton(fhirOnt));        

	
        // We want to examine the restrictions on all classes.
        for (OWLClass c : clses) {
        	
        	
        	System.out.println("cls:" + c.getIRI());
            //assert c != null;
            // collect the properties which are used in existential restrictions
            RestrictionVisitor visitor = new RestrictionVisitor(Collections.singleton(fhirOnt));
            for (OWLSubClassOfAxiom ax : fhirOnt.getSubClassAxiomsForSubClass(c)) {
                // Ask our superclass to accept a visit from the
                // RestrictionVisitor
                ax.getSuperClass().accept(visitor);
            }
            // Our RestrictionVisitor has now collected all of the properties
            // that have been restricted in existential
            // restrictions - print them out.
            Set<OWLObjectPropertyExpression> restrictedProperties = visitor.getRestrictedProperties();
            // System.out.println("Restricted properties for " + labelFor(c, o)
            // + ": " + restrictedProperties.size());

             
            
            for (OWLObjectPropertyExpression prop : restrictedProperties) {
                //assertNotNull(prop);
                System.out.println("prop:" + prop);
                
                String fragment = ((OWLEntity)prop).getIRI().getFragment();
                int index = fragment.lastIndexOf(".");
                if(index >= 0) {
                fragment = fragment.substring(index + 1);
                }
                
                //System.out.println("fragment:" + fragment);
                         
        
                entity2IRIMap.put((OWLEntity)prop, IRI.create("http://hl7.org/fhir/" + fragment));  
                
                System.out.println((OWLEntity)prop + "|" + IRI.create("http://hl7.org/fhir/" + fragment));
                
                OWLObjectProperty fragmentprop = dataFactory.getOWLObjectProperty(IRI.create("http://hl7.org/fhir/" + fragment));
                
                
                
                
                OWLObjectProperty subpropertyof = dataFactory.getOWLObjectProperty(IRI.create("rdfs:subPropertyOf"));
                
                
                
                OWLAnnotation labelAnno = dataFactory.getOWLAnnotation(dataFactory.getRDFSLabel(), dataFactory.getOWLLiteral(fragment));
                OWLAxiom ax = dataFactory.getOWLAnnotationAssertionAxiom(IRI.create("http://hl7.org/fhir/" + fragment), labelAnno);
                
                AddAxiom addLabelAxiom = new AddAxiom(fhirOnt, ax);
                man.applyChange(addLabelAxiom);
                
                
        		OWLSubObjectPropertyOfAxiom subAxiom = dataFactory.getOWLSubObjectPropertyOfAxiom(prop, fragmentprop);
        		
        		AddAxiom addAxiom = new AddAxiom(fhirOnt, subAxiom);
        		man.applyChange(addAxiom);
            
            }
            
 
        }
        
        man.applyChanges(renamer.changeIRI(entity2IRIMap)); 
        
        
        man.saveOntology(fhirOnt);     
        
    }

    /**
     * Visits existential restrictions and collects the properties which are
     * restricted
     */
    private static class RestrictionVisitor extends OWLClassExpressionVisitorAdapter {

        @Nonnull
        private final Set<OWLClass> processedClasses;
        @Nonnull
        private final Set<OWLObjectPropertyExpression> restrictedProperties;
        private final Set<OWLOntology> onts;

        RestrictionVisitor(Set<OWLOntology> onts) {
            restrictedProperties = new HashSet<>();
            processedClasses = new HashSet<>();
            this.onts = onts;
        }

        @Nonnull
        public Set<OWLObjectPropertyExpression> getRestrictedProperties() {
            return restrictedProperties;
        }

        @Override
        public void visit(OWLClass ce) {
            // avoid cycles
            if (!processedClasses.contains(ce)) {
                // If we are processing inherited restrictions then
                // we recursively visit named supers.
                processedClasses.add(ce);
                for (OWLOntology ont : onts) {
                    for (OWLSubClassOfAxiom ax : ont.getSubClassAxiomsForSubClass(ce)) {
                        ax.getSuperClass().accept(this);
                    }
                }
            }
        }

        @Override
        public void visit(@Nonnull OWLObjectSomeValuesFrom ce) {
            // This method gets called when a class expression is an
            // existential (someValuesFrom) restriction and it asks us to visit
            // it
            restrictedProperties.add(ce.getProperty());
        }
        
        @Override
        public void visit(@Nonnull OWLObjectAllValuesFrom ce) {
            // This method gets called when a class expression is an
            // existential (someValuesFrom) restriction and it asks us to visit
            // it
            restrictedProperties.add(ce.getProperty());
        }
        
        @Override
        public void visit(@Nonnull OWLObjectUnionOf ce) {
            // This method gets called when a class expression is an
            // existential (someValuesFrom) restriction and it asks us to visit
            // it
        	
        	for (OWLObject child : ((OWLObjectUnionOf)ce).getOperands()) {
        		if (child instanceof OWLObjectAllValuesFrom) {
        			restrictedProperties.add(((OWLObjectAllValuesFrom) child).getProperty());
        		} else if (child instanceof OWLObjectSomeValuesFrom) {
        			restrictedProperties.add(((OWLObjectSomeValuesFrom) child).getProperty());
        		} 
        	}
            
        }
    
    }

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
		    //shouldLoad();
		    //testLookupRestrictions();
		    
		    shouldReasoningW5Subproperties(null);
		    
		    //testLookupRestrictions();
		    
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
