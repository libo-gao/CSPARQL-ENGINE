/**
 * Copyright 2011-2015 DEIB - Politecnico di Milano
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Acknowledgements:
 * We would like to thank Davide Barbieri, Emanuele Della Valle,
 * Marco Balduini, Soheila Dehghanzadeh, Shen Gao, and
 * Daniele Dell'Aglio for the effort in the development of the software.
 *
 * This work is partially supported by
 * - the European LarKC project (FP7-215535) of DEIB, Politecnico di
 * Milano
 * - the ERC grant “Search Computing” awarded to prof. Stefano Ceri
 * - the European ModaClouds project (FP7-ICT-2011-8-318484) of DEIB,
 * Politecnico di Milano
 * - the IBM Faculty Award 2013 grated to prof. Emanuele Della Valle;
 * - the City Data Fusion for Event Management 2013 project funded
 * by EIT Digital of DEIB, Politecnico di Milano
 * - the Dynamic and Distributed Information Systems Group of the
 * University of Zurich;
 * - INSIGHT NUIG and Science Foundation Ireland (SFI) under grant
 * No. SFI/12/RC/2289
 */
package eu.larkc.csparql.ui;

import java.text.ParseException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import eu.larkc.csparql.cep.api.RDFStreamAggregationTestGenerator;
import eu.larkc.csparql.cep.api.RdfStream;
import eu.larkc.csparql.cep.api.TestGenerator;
import eu.larkc.csparql.cep.api.WatdivTestGenerator;
import eu.larkc.csparql.core.engine.ConsoleFormatter;
import eu.larkc.csparql.core.engine.CsparqlEngine;
import eu.larkc.csparql.core.engine.CsparqlEngineImpl;
import eu.larkc.csparql.core.engine.CsparqlQueryResultProxy;
import eu.larkc.csparql.core.streams.formats.CSparqlQuery;

public final class Application {

   /**
    * @param args
    */

   public static void main(final String[] args) {

       final String queryGetAll = "REGISTER QUERY PIPPO AS "
               +"SELECT * FROM STREAM <http://myexample.org/stream> [RANGE 20s STEP 1s] FROM <http://github.com/nosrepus/UWaterloo-WatDiv/raw/master/tiny.ttl> "
               +"WHERE { ?s <http://purl.org/stuff/rev#reviewer> ?O }";

       final String q1 = "REGISTER QUERY q1 AS " +
               "SELECT * " +
               "FROM STREAM <http://myexample.org/stream> [RANGE 60s STEP 1s] " +
               "FROM <http://github.com/nosrepus/UWaterloo-WatDiv/raw/master/tiny.ttl> " +
               "WHERE {  " +
               "?v2 <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?v4 .  " +
               "?v1 <http://purl.org/stuff/rev#reviewer> ?v2 . }";

       final String s7 = "REGISTER QUERY S7 AS " +
               "SELECT * " +
               "FROM STREAM <http://myexample.org/stream> [RANGE 60s STEP 1s] " +
               "FROM <http://github.com/nosrepus/UWaterloo-WatDiv/raw/master/tiny.ttl> " +
               "WHERE { " +
               "?V0 <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?v1 . " +
               "?V0 <http://schema.org/text> ?v2 . " +
               "?V3 <http://db.uwaterloo.ca/~galuc/wsdbm/likes> ?V0 . }";

        final String c3 = "REGISTER QUERY C3 AS " +
                "SELECT * " +
                "FROM STREAM <http://myexample.org/stream> [RANGE 60s STEP 1s] " +
                "FROM <http://github.com/nosrepus/UWaterloo-WatDiv/raw/master/tiny.ttl> " +
                "WHERE {" +
                "?V0 <http://db.uwaterloo.ca/~galuc/wsdbm/likes> ?V1 . " +
                "?V0 <http://purl.org/dc/terms/Location> ?V3 . "  +
                "?V0 <http://xmlns.com/foaf/age> ?V4 . " +
                "?V0 <http://db.uwaterloo.ca/~galuc/wsdbm/gender> ?V5 . " +
                "?V0 <http://xmlns.com/foaf/givenName> ?V6 . " +
                "}";

      final CsparqlEngine engine = new CsparqlEngineImpl();
      engine.initialize();

      engine.putStaticNamedModel("http://github.com/nosrepus/UWaterloo-WatDiv/raw/master/tiny.ttl", "/Users/nosrepus/workspace/UWaterloo-WatDiv/tiny.ttl");
//      final RDFStreamAggregationTestGenerator tg = new RDFStreamAggregationTestGenerator("http://www.larkc.eu/defaultRDFInputStream");
//      final GlueStreamGenerator tg = new GlueStreamGenerator();
      WatdivTestGenerator tg = new WatdivTestGenerator("http://myexample.org/stream");
      
      RdfStream rs = engine.registerStream(tg);
      engine.unregisterStream(rs.getIRI());
      engine.registerStream(tg);
      //engine.registerStream(tg2);
      final Thread t = new Thread(tg);
      t.start();
      
      CsparqlQueryResultProxy c1 = null;
      final CsparqlQueryResultProxy c2 = null;

      try {
         c1 = engine.registerQuery(c3, false);
      } catch (final ParseException ex) {
         System.out.println("errore di parsing: " + ex.getMessage());
      }

       Collection<CSparqlQuery> items = engine.getAllQueries();
       for(Iterator it = items.iterator(); it.hasNext();){
           CSparqlQuery q = (CSparqlQuery) it.next();
           System.out.println("esper: "+q.getCepQuery().getQueryCommand());
           System.out.println("sparql: "+q.getSparqlQuery().getQueryCommand());
       }

      if (c1 != null) {
         c1.addObserver(new ConsoleFormatter());
         //c1.addObserver(new TextualFormatter());
      }
   }

   private Application() {
      // hidden constructor
   }

}