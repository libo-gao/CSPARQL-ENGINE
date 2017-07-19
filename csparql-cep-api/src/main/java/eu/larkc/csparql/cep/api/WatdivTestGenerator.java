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

    private double getTime(String ss){
        return Double.parseDouble(ss.split("\t")[3]);
    }

    private double getMin(double a, double b, double c){
        return min(a, min(b, c));
    }

    public void run() {
        FileInputStream file_like = null;
        FileInputStream file_purchase = null;
        FileInputStream file_review = null;
        FileInputStream file_offer = null;
        try {
            file_like = new FileInputStream("data/scale50/like_stream.txt");
            file_purchase = new FileInputStream("data/scale50/purchase.txt");
            file_review = new FileInputStream("data/scale50/review.txt");
            file_offer = new FileInputStream("data/scale50/offer.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader in_like = new BufferedReader(new InputStreamReader(file_like));
        BufferedReader in_purchase = new BufferedReader(new InputStreamReader(file_purchase));
        BufferedReader in_review = new BufferedReader(new InputStreamReader(file_review));
        BufferedReader in_offer = new BufferedReader(new InputStreamReader(file_offer));
        try {
            String curr_like = in_like.readLine();
            String curr_purchase = in_purchase.readLine();
            String curr_review = in_review.readLine();
            String curr_offer = in_offer.readLine();

            double time_like = getTime(curr_like);
            double time_purchase = getTime(curr_purchase);
            double time_review = getTime(curr_review);
            double time_offer = getTime(curr_offer);

            ArrayList<String> subjects = new ArrayList<String>();
            ArrayList<String> predicates = new ArrayList();
            ArrayList<String> objects = new ArrayList();

            while (curr_like.length() != 0 || curr_purchase.length() != 0 || curr_review.length() != 0|| curr_offer.length() != 0) {

                //the time used to prepare the data is around 0.3 millisecond

                //long t1 = System.nanoTime();
                //System.out.printf("starttime:"+'\t'+System.nanoTime()+'\n');

                if (time_like <= getMin(time_purchase, time_review, time_offer)) {
                    //System.out.printf(curr_like + '\n');

                    String[] items = curr_like.split("\t");
                    subjects.add(items[0]);
                    predicates.add(items[1]);
                    objects.add(items[2]);

                    curr_like = in_like.readLine();

                    while (curr_like.length() != 0 && time_like == getTime(curr_like)) {
                        items = curr_like.split("\t");
                        subjects.add(items[0]);
                        predicates.add(items[1]);
                        objects.add(items[2]);

                        curr_like = in_like.readLine();
                    }

                    if (curr_like.length() == 0) {
                        time_like = 2.0;
                    } else {
                        time_like = getTime(curr_like);
                    }
                } else if (time_purchase <= getMin(time_like, time_review, time_offer)) {
                    //System.out.printf(curr_purchase + '\n');

                    String[] items = curr_purchase.split("\t");
                    subjects.add(items[0]);
                    predicates.add(items[1]);
                    objects.add(items[2]);

                    curr_purchase = in_purchase.readLine();

                    while (curr_purchase.length() != 0 && time_purchase == getTime(curr_purchase)) {
                        //System.out.printf(curr_purchase + '\n');
                        items = curr_purchase.split("\t");
                        subjects.add(items[0]);
                        predicates.add(items[1]);
                        objects.add(items[2]);

                        curr_purchase = in_purchase.readLine();
                    }

                    if (curr_purchase.length() == 0) time_purchase = 2.0;
                    else time_purchase = getTime(curr_purchase);
                } else if (time_review <= getMin(time_like, time_purchase, time_offer)) {
                    //System.out.printf(curr_review + '\n');

                    String[] items = curr_review.split("\t");
                    subjects.add(items[0]);
                    predicates.add(items[1]);
                    objects.add(items[2]);

                    curr_review = in_review.readLine();

                    while (curr_review.length() != 0 && time_review == getTime(curr_review)) {
//                        System.out.printf(curr_review + '\n');
                        items = curr_review.split("\t");
                        subjects.add(items[0]);
                        predicates.add(items[1]);
                        objects.add(items[2]);

                        curr_review = in_review.readLine();
                    }

                    if (curr_review.length() == 0) time_review = 2.0;
                    else time_review = getTime(curr_review);
                } else if (time_offer <= getMin(time_like, time_purchase, time_review)){
                    String[] items = curr_offer.split("\t");
                    subjects.add(items[0]);
                    predicates.add(items[1]);
                    objects.add(items[2]);

                    curr_offer = in_offer.readLine();

                    while (curr_offer.length() != 0 && time_offer == getTime(curr_offer)) {
                        items = curr_offer.split("\t");
                        subjects.add(items[0]);
                        predicates.add(items[1]);
                        objects.add(items[2]);

                        curr_offer = in_offer.readLine();
                    }
                    if (curr_offer.length() ==0) time_offer = 2.0;
                    else time_offer = getTime(curr_offer);
                }
                //System.out.printf("endtime:"+'\t'+System.nanoTime()+'\n'+'\n');
                //long t2 = System.nanoTime();
                //System.out.printf("interval: "+ (t2-t1) +'\n');

                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                long now = System.currentTimeMillis();
                for(int i = 0; i<predicates.size();i++) {
                    final RdfQuadruple q = new RdfQuadruple(subjects.get(i), predicates.get(i), objects.get(i), now);
                    this.put(q);
                }

                subjects.clear();
                predicates.clear();
                objects.clear();

            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}