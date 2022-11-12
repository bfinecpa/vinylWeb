package project.vinyl.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import project.vinyl.dto.ComparePriceDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class SearchService {

    public void gimbab(String keyword) {
        if (keyword != null) {
            keyword = keyword.replace(" ", "+");

            String gimbab = "https://gimbabrecords.com/product/search.html?keyword="+keyword;
            Connection conn = Jsoup.connect(gimbab);

            try {
                Document doc = conn.get();

                // 검색 결과가 있을 때
                Boolean data = doc.select("p.noData").get(0).hasClass("displaynone");
                log.info("data = {}", data);

                if (data) {
                    while (true) {
                        Elements links = doc.select("div.box > a");
                        Elements imgs = doc.select("div.box > a > img");
                        Elements names = doc.select("p.name > a > span");
                        Elements ulOfPrices = doc.select("ul.xans-search-listitem");
                        Elements siblingOfAvails = doc.select("p.name > a");

                        for (int i = 0; i < links.size(); i++) {
                            String link = links.get(i).attr("abs:href");
                            String img = "https:" + imgs.get(i).attr("src");
                            String name = names.get(i).text();
                            String price = ulOfPrices.get(i).child(0).child(1).text();
                            Boolean available = siblingOfAvails.get(i).nextElementSibling().attr("alt").equals("품절");

                            log.info("url = {}", link);
                            log.info("img = {}", img);
                            log.info("name = {}", name);
                            log.info("price = {}", price);
                            log.info("avail = {}", available);
                        }

                        // 다음 페이지 접근
                        String next = doc.getElementById("paging").child(3).child(0).attr("href");
                        if (next.equals("#none")) {
                            log.info("다음페이지 없음");
                            break;
                        } else {
                            String nextPage = "https://gimbabrecords.com/product/search.html" + next;
                            log.info("nextPage = {}", nextPage);
                            Connection connect = Jsoup.connect(nextPage);
                            doc = connect.get();
                        }
                    }
                } else {
                    log.info("검색 결과가 없습니다.");
                }

            } catch (IOException e) {
            }

        }
    }

    public  List<ComparePriceDto> yes24Vinyl(String keyword){
        List<ComparePriceDto> comparePriceDtoList = new ArrayList<>();
        if ( keyword != null){
            keyword = keyword + " lp";
            int page=1;
            String seoul = "http://www.yes24.com/Product/Search?domain=MUSIC&query="+keyword;
            Connection conn = Jsoup.connect(seoul);
            try{
                Document document = conn.get();

                Elements select = document.select(".sGoodsSecArea > div.noData");
                if(select.size()==0){
                    while(true){
                        Elements elements = document.select("#yesSchList > li > div.itemUnit");
                        if(elements.size()==0 || comparePriceDtoList.size()>10)break;
                        for (Element element : elements) {
                            try {
                                if(comparePriceDtoList.size()>10)break;
                                String link = "http://www.yes24.com"+element.select("a").get(0).attr("href");
                                String img = element.select("img").get(0).attr("data-original");
                                String name = element.select("a.gd_name").get(0).text();
                                String price = element.select("em.yes_b").get(0).text();
                                boolean available = element.select("span.soldOut").size() == 0 ? true : false;
                                ComparePriceDto comparePriceDto = new ComparePriceDto(img, link, name, price, available);
                                comparePriceDtoList.add(comparePriceDto);
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
                    return comparePriceDtoList;
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        return comparePriceDtoList;
    }

    public List<ComparePriceDto> comparePrice(String keyword){
        List<ComparePriceDto> comparePriceDtos = yes24Vinyl(keyword);
        Collections.sort(comparePriceDtos);
        return comparePriceDtos;
    }
}
