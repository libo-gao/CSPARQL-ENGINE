/*******************************************************************************
 * Copyright 2014 DEIB -Politecnico di Milano
 *   
 *  Marco Balduini (marco.balduini@polimi.it)
 *  Emanuele Della Valle (emanuele.dellavalle@polimi.it)
 *  Davide Barbieri
 *   
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *   
 *  	http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *   
 *  Acknowledgements:
 *  
 *  This work was partially supported by the European project LarKC (FP7-215535)
 ******************************************************************************/
package eu.larkc.csparql.core.general_test;

import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.RDF;

public class Inference {

	public static void main(String[] args) {
		
		Model schema = FileManager.get().loadModel("file:/Users/baldo/Documents/Work/inference_test/rdfsDemoSchema.rdf");
		Model data = FileManager.get().loadModel("file:/Users/baldo/Documents/Work/inference_test/rdfsDemoData.rdf");
		InfModel infmodel = ModelFactory.createRDFSModel(schema, data);
		
		Resource colin = infmodel.getResource("http://streamreasoning.org#t");
		System.out.println("t has types:");
		printStatements(infmodel, colin, RDF.type, null);


		Resource Person = infmodel.getResource("http://streamreasoning.org#Person");
		System.out.println("\nPerson has types:");
		printStatements(infmodel, Person, RDF.type, null);
		
	}
	
	public static void printStatements(Model m, Resource s, Property p, Resource o) {
        for (StmtIterator i = m.listStatements(s,p,(RDFNode)o); i.hasNext(); ) {
            Statement stmt = i.nextStatement();
            System.out.println(" - " + stmt.toString());
        }
    }

}
