package project.vinyl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SearchTest {

    @Test
    void seoulVinyl(){
        String keyword = "ab lp";
        int page=1;
        if ( keyword != null){
            String seoul = "http://www.yes24.com/Product/Search?domain=MUSIC&query="+keyword;
            Connection conn = Jsoup.connect(seoul);
            try{
                Document document = conn.get();

                Elements select = document.select(".sGoodsSecArea > div.noData");
                if(select.size()==0){
                    while(true){
                        Elements elements = document.select("#yesSchList > li > div.itemUnit");
                        if(elements.size()==0)break;
                        for (Element element : elements) {
                            try {
                                String link = "http://www.yes24.com"+element.select("a").get(0).attr("href");
                                String img = element.select("img").get(0).attr("data-original");
                                String name = element.select("a.gd_name").get(0).text();
                                String price = element.select("em.yes_b").get(0).text();
                                boolean available = element.select("span.soldOut").size() == 0 ? true : false;
                                log.info("name={}", name);
                                log.info("----------------");
                            }catch (IndexOutOfBoundsException e){
                                continue;
                            }
                        }
                        page+=1;
                        String nextPage = "http://www.yes24.com/Product/Search?domain=MUSIC&query=" + keyword +
                                "&page="+Integer.toString(page);
                        Connection nextCon = Jsoup.connect(nextPage);
                        document=nextCon.get();
                    }
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Test
    void stringTest(){

        List<Integer> a1 = new ArrayList<>(List.of(1,3,5,7));
        List<Integer> a2 = new ArrayList<>(List.of(2,4,6,8));
        a1.addAll(a2);
        log.info("a1={}", a1);
    }

}
