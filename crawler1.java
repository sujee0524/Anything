/**
 * Created by sujee on 2016. 9. 19..
 */


import java.io.FileOutputStream;
import java.io.InputStream;

//import org.apache.http.client.methods.G


//import net.htmlparser.jericho.*;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

import java.util.*;
import java.io.*;
import java.net.*;

public class crawler1 {
/*
    public static InputStream rendering_pages(String reviewUrl) throws Exception {
        // Rendering javascript pages with HTMLUnit
        GetMehod

    }
*/


    //
    public static List<String> reviewLinkList(String sourceUrlString) throws IOException {
        //input sourceUrlString : Naver's item list page url
        //output reviewLinkList : extract a review page link from each item in the source
        //TODO : page number = 1, ....
        Source source = new Source(new URL(sourceUrlString));
        source.fullSequentialParse();

        Element itemlistdiv = source.getElementById("_search_list");
        List<Element> itemlist = itemlistdiv.getAllElementsByClass("_model_list");
        Element li;

        List linklist = new ArrayList();

        for(int i=0; i<itemlist.size(); i++){
            //System.out.println(i);
            li = itemlist.get(i);
            Element info = li.getFirstElementByClass("info");
            Element etc = info.getFirstElementByClass("etc");
            Element review = etc.getFirstElement(HTMLElementName.A);
            String reviewLink = review.getAttributeValue("href");
            //System.out.println(reviewLink);
            linklist.add(reviewLink);

        }
        return linklist;

    }

    public static List<String> reviewListperItem(String reviewUrlString) throws IOException {
        //input reviewUrlString : review list page for one item
        //output reviewListperItem : extract review text and other characteristics for the item
        Source source = new Source(new URL(reviewUrlString));
        source.fullSequentialParse();

        // Item Name
        Element iteminfo = source.getElementById("summary_info");
        String itemname = iteminfo.getFirstElementByClass("h_area").getFirstElement(HTMLElementName.H2).getContent().toString();

        System.out.println(itemname);

        List reviewlines = new ArrayList();

        try{
            Element listdiv = source.getElementById("user_review_list_area");
            Element reviewlist = listdiv.getFirstElementByClass("lst_review"); // UL
            List<Element> reviewItems = reviewlist.getAllElements(HTMLElementName.LI);

            String review;
            String starrate;
            String date;
            String line;


            for(int i=0; i < reviewItems.size(); i++){
                Element li = reviewItems.get(i);
                //review text
                Element atc = li.getFirstElementByClass("atc");
                review = atc.getContent().toString();
                review = review.replace("|",".");// for separator
                //rank star-based-rate
                Element star = li.getFirstElementByClass("curr_avg");
                starrate = star.getFirstElement(HTMLElementName.STRONG).getContent().toString();
                //date
                date = li.getFirstElementByClass("regdate").getContent().toString();

                line = itemname+ "|" + review.trim() + "|" + starrate + "|" + date + "\n";
                reviewlines.add(line);

                //System.out.println(line);
                //TODO : all pages for review items. Javascript Rendering

            }


        }catch (NullPointerException e){
            System.err.println("NullPointerException in "+itemname);

        }

        return reviewlines;
    }


    public static void main(String[] args) throws IOException {

        String csvFile = "samsung.csv";
        FileWriter writer = new FileWriter(csvFile);

        String query = "%EC%82%BC%EC%84%B1";
        String start = "http://shopping.naver.com";


        for(int pagenum=1; pagenum<20; pagenum++){

            String page = String.format("&pagingIndex=%d", pagenum);
            String sourceUrlString = start+"/search/all.nhn?where=all&frm=NVSCTAB&query="+query+page;
            if(args.length==0)
                System.err.println("Using default argument of \""+sourceUrlString+'"');
            else
                sourceUrlString = args[0];

            // get list of review links
            List<String> linklist = reviewLinkList(sourceUrlString);

            // test : select a link and get reviews

            for(Iterator<String> iterItem = linklist.iterator(); iterItem.hasNext(); ){
                String eachlink = iterItem.next();
                List<String> reviewlist = reviewListperItem(start+eachlink);

                for(Iterator<String> iterReview = reviewlist.iterator(); iterReview.hasNext(); ){
                    String line = iterReview.next();
                    writer.append(line);


                }

            }
            writer.flush();
        }

        writer.close();

        //String onelink = linklist.get(1);
        //List<String> reviewlist = reviewListperItem(start+onelink);




        /*
        Source source = new Source(new URL(start+onelink));
        source.fullSequentialParse();

        Element listdiv = source.getElementById("user_review_list_area");
        Element reviewlist = listdiv.getFirstElementByClass("lst_review");
        List<Element> reviewItems = reviewlist.getAllElements(HTMLElementName.LI);


        for(int i=0; i < reviewItems.size(); i++){
            Element li = reviewItems.get(i);
            Element atc = li.getFirstElementByClass("atc");
            String review = atc.getContent().toString();
            System.out.println(review);
            //TODO : all pages for review items

        }
        */








    }
}
