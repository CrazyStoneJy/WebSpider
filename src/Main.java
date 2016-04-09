import javax.xml.ws.soap.MTOM;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {

//        fetchInfoByUrls();

    }


    private static void fetchInfoBySearchList(String url) {
        WebInfoFetch spider = new WebInfoFetch();
        spider.fetchDetailUrl(url, Method.GET);
    }


    /***
     * 通过urls来截取房源信息
     */
    private static void fetchInfoByUrls() {
        //将要摘文字的url加入list中
        List<String> urls = new ArrayList<String>();
        urls.add("http://esf.fang.com/chushou/10_289400079.htm");
        urls.add("http://esf.fang.com/chushou/10_289121046.htm");
        urls.add("http://esf.fang.com/chushou/3_288396930.htm");
        urls.add("http://esf.fang.com/chushou/3_289027691.htm");
        urls.add("http://esf.fang.com/chushou/3_290450996.htm");
        urls.add("http://esf.fang.com/chushou/3_291073794.htm");
        urls.add("http://esf.fang.com/chushou/3_289394507.htm");
        spiderInfo(urls);
    }

    /**
     * 通过urls来截取房源信息
     *
     * @param urls
     */
    private static void spiderInfo(List<String> urls) {
        ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

        for (final String url : urls) {
            service.execute(new Runnable() {
                @Override
                public void run() {
                    WebInfoFetch spider = new WebInfoFetch();
                    String str = spider.fectch(url, Method.GET);
                    System.out.println(str);
                }
            });
        }
        service.shutdown();
    }

}
