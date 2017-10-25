package eu.larkc.csparql.cep.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;

import static java.lang.Double.min;


/**
 * Created by nosrepus on 2017-06-18.
 */

public class WatdivTestGenerator extends RdfStream implements Runnable {

    /** The logger. */
    protected final Logger logger = LoggerFactory
            .getLogger(TestGenerator.class);

    private int c = 1;
    private boolean keepRunning = false;

    public WatdivTestGenerator(final String iri) {
        super(iri);
    }

    public void pleaseStop() {
        keepRunning = false;
    }

    private long getTime(String ss){
        return Long.parseLong(ss.split("\t")[3]);
    }

    public void run() {
        FileInputStream file_like = null;
        String prefix = "/u2/l36gao/workspace/watdiv/UWaterloo-WatDiv/bin/Release/";
        try {
            file_like = new FileInputStream(prefix+"10-stream.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader in_like = new BufferedReader(new InputStreamReader(file_like));
        try {
            String curr_like = in_like.readLine();

            long time_like = 0;

            ArrayList<String> subjects = new ArrayList<String>();
            ArrayList<String> predicates = new ArrayList();
            ArrayList<String> objects = new ArrayList();

            while (curr_like.length() != 0) {

                //long t1 = System.nanoTime();
                //System.out.printf("starttime:"+'\t'+System.nanoTime()+'\n');

                String[] items = curr_like.split("\t");
                subjects.add(items[0]);
                predicates.add(items[1]);
                objects.add(items[2]);

                while (curr_like.length() != 0 && time_like == getTime(curr_like)) {
                    items = curr_like.split("\t");
                    subjects.add(items[0]);
                    predicates.add(items[1]);
                    objects.add(items[2]);

                    curr_like = in_like.readLine();
                }

                //System.out.printf("endtime:"+'\t'+System.nanoTime()+'\n'+'\n');
                //long t2 = System.nanoTime();
                //System.out.printf("interval: "+ (t2-t1) +'\n');

                try {
                    Thread.sleep(getTime(curr_like) - time_like);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                long now = System.currentTimeMillis();
                for(int i = 0; i<predicates.size();i++) {
                    final RdfQuadruple q = new RdfQuadruple(subjects.get(i), predicates.get(i), objects.get(i), now);
                    //System.out.printf(q.toString()+'\n');
                    this.put(q);
                }

                subjects.clear();
                predicates.clear();
                objects.clear();
                time_like = getTime(curr_like);
                curr_like = in_like.readLine();
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}