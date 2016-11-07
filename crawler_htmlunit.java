/**
 * Created by sujee on 2016. 10. 29..
 */
import java.util.List;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.ScriptResult;


public class crawler_htmlunit {
    public static void main(String[] args) throws Exception{
        WebClient webClient = new WebClient();

        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getCookieManager().setCookiesEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        //java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
        //java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);


        HtmlPage page = (HtmlPage) webClient.getPage("http://shopping.naver.com/detail/detail.nhn?nv_mid=9535985799&cat_id=50000246&frm=NVSCTAB&query=%EC%82%BC%EC%84%B1&section=review");

        String jscode = "shop.detail.ReviewHandler.page(2, ''); return false;";
        ScriptResult r= page.executeJavaScript(jscode);
        HtmlPage page2=(HtmlPage) r.getNewPage();
        synchronized (page2) {
            try {
                page2.wait(50000);
            } catch (InterruptedException e) {
                System.out.println("failed to sleep");
            }
        }
        System.out.println(page2.getWebResponse().getContentAsString());

/*
        List<DomElement> divlist = page.getElementsByTagName("div");
        System.out.println("here I am");
        for(DomElement e : divlist)
        {
            if(e.getAttribute("class").equals("co_paginate")) {
                System.out.println("=next=");
                DomElement link = e.getElementsByTagName("a").get(0);
                page = link.click();
                break;
                //System.out.println(e.getElementsByTagName("a").get(0).asText());

            }
        }

        List<DomElement> lilist = page.getElementsByTagName("li");
        //List<DomElement> lidivlist;
        for(DomElement li : lilist){
            if(li.getAttribute("id").equals("_reviewItem_")){
                System.out.println(li.getElementsByTagName("div").get(2).asText());

            }
        }
        //System.out.println(page.asText());
    */
    }

}
