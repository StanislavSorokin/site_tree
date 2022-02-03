import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.RecursiveTask;

public class SiteTree extends RecursiveTask<String>{

    private String url;
    private static Set<String> links = new HashSet<>();

    public SiteTree (String url){
        this.url = url;
    }

    private void getChildrenLinks(Set<SiteTree> childTask)

    {
        Document document;
        Elements elements;

        try {
            Thread.sleep(150);
            document = Jsoup.connect(url)
                    .userAgent("Chrome/4.0.249.0 Safari/532.5")
                    .referrer("http://www.google.com")
                    .timeout(3000)
                    .get();
            elements = document.select("a");

            for (Element element : elements)
            {
                String link = element.attr("abs:href");
                if (!link.isEmpty() && link.startsWith(url) && !link.contains("#") && !links.contains(link))
                {
                    SiteTree siteTree = new SiteTree(link);
                    siteTree.fork();
                    childTask.add(siteTree);
                    links.add(link);
                }
            }
        } catch (InterruptedException | IOException ignored) {
        }


    }

    @Override
    protected String compute()  {
        StringBuffer stringBuffer = new StringBuffer(url + "\n");
        Set<SiteTree> childTask = new HashSet<>();
        getChildrenLinks(childTask);
        for (SiteTree siteTree : childTask) {
            stringBuffer.append(siteTree.join());
        }
        return stringBuffer.toString();
    }
}
