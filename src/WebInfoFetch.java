import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sun.org.mozilla.javascript.internal.Synchronizer;
import sun.org.mozilla.javascript.internal.json.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * web爬虫
 * Created by crazystone on 2016/4/9.
 */
public class WebInfoFetch {

    private static final int CONNECT_TIME = 5 * 1000;
    private List<String> mStrList = new ArrayList<String>();

    /***
     * 获取房源详情页的数据
     *
     * @param url
     * @param method
     * @return
     */
    public synchronized String fectch(String url, Method method) {
        if (!validateUrl(url)) return "";

        StringBuffer sb = new StringBuffer();
        try {
            Document document = null;
            if (Method.GET.equals(method)) {
                document = Jsoup.connect(url).cookie("auth", "token").timeout(CONNECT_TIME).get();
            } else if (Method.POST.equals(method)) {
                document = Jsoup.connect(url).cookie("auth", "token").timeout(CONNECT_TIME).post();
            } else {
                throw new RuntimeException("NOT THIS METHOD");
            }
            Element titleElement = document.getElementsByTag("title").first();
            sb.append("url:").append(url).append("\n");
            sb.append("title:").append(titleElement.text()).append("\n");
            Element infoElement = document.select("#hsPro-pos").first();//房源信息的div id
            if (infoElement != null) {
//                Elements allTags = infoElement.getAllElements();
               String str= infoElement.text();
                sb.append("info:").append(str);
//                for (Element e : allTags) {
//                    Element tag = e.getElementsByTag("SPAN").first();//现在只是判断了SPAN标签，如果是其他标签可以添加其他标签的代码
//                    if (tag != null) {
//                        String text = tag.text();
//                        if (!mStrList.contains(text)) {
//                            mStrList.add(text);
//                            sb.append("info:").append(text).append("\n");
//                        }
//                    }
//                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


    /***
     * 抓取房源信息列表的urls
     *
     * @param url
     * @param method
     */
    //todo  还未完成
    @Deprecated
    public void fetchDetailUrl(String url, Method method) {
        Document document = null;
        String host = "http://esf.fang.com";
        StringBuffer sb = new StringBuffer();
        try {
            document = Jsoup.connect(url).cookie("auth", "token").timeout(CONNECT_TIME).get();
            if (Method.GET.equals(method)) {
            } else if (Method.POST.equals(method)) {
                document = Jsoup.connect(url).cookie("auth", "token").timeout(CONNECT_TIME).post();
            } else {
                throw new RuntimeException("NOT THIS METHOD");
            }

            if (document != null) {
                Element searchList = document.select(".searchlist").first();
                if (searchList != null) {
                    Elements urlElements = searchList.getAllElements();
                    for (Element e : urlElements) {
                        String href = e.attr("href");
                        if (sb != null && sb.length() > 0) sb.delete(0, sb.length());
                        sb.append(host).append(href);
                        startRunnable(url);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void startRunnable(final String url) {
        ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
        service.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println(fectch(url, Method.GET));
            }
        });
    }

    @Deprecated
    private boolean selectKeyInfo(String key, String destText) {
        return destText.toLowerCase().contains(key.toLowerCase());
    }


    /**
     * 验证URL是否是http，或https打头
     *
     * @param url
     * @return
     */
    private boolean validateUrl(String url) {
        if (url.startsWith("http") || url.startsWith("https")) return true;
        return false;
    }

}
