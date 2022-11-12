package project.vinyl.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import project.vinyl.dto.SearchDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class SearchService {

    public List<SearchDto> getResult(String keyword) {
        List<SearchDto> list = new ArrayList<>();

        List<SearchDto> result1 = gimbab(keyword);
        List<SearchDto> result2 = dope(keyword);
        list.addAll(result1);
        list.addAll(result2);

        if (!list.isEmpty()) {
            Collections.sort(list, (r1, r2) -> r1.getPrice() - r2.getPrice());
        }
        return list;
    }

    private int priceToInt(String strPrice) {
        strPrice = strPrice.replace("원", "");
        strPrice = strPrice.replace(",", "");
        return Integer.parseInt(strPrice);
    }

    public List<SearchDto> gimbab(String keyword) {
        List<SearchDto> list = new ArrayList<>();

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
                        Elements divOfPrices = doc.select("div.box");
                        Elements parentOfAvails = doc.select("p.name");

                        for (int i = 0; i < links.size(); i++) {
                            String link = links.get(i).attr("abs:href");
                            boolean vinyl = link.contains("cate_no=25") || link.contains("cate_no=32") || link.contains("cate_no=33")
                                    || link.contains("cate_no=52")|| link.contains("cate_no=53")|| link.contains("cate_no=54")
                                    || link.contains("cate_no=55")|| link.contains("cate_no=56")|| link.contains("cate_no=57")
                                    || link.contains("cate_no=58")|| link.contains("cate_no=59")|| link.contains("cate_no=60")
                                    || link.contains("cate_no=61")|| link.contains("cate_no=62")|| link.contains("cate_no=63")
                                    || link.contains("cate_no=93")|| link.contains("cate_no=42");

                            if (vinyl) {
                                boolean available = true;
                                if (parentOfAvails.get(i).childrenSize() > 1) {
                                    available = !parentOfAvails.get(i).child(1).attr("alt").equals("품절");
                                }
                                String name = names.get(i).text();
                                if (name.contains("gimbabrecords2.com")) { available = false; }
                                if (available) {
                                    String img = "https:" + imgs.get(i).attr("src");
                                    int price = priceToInt(divOfPrices.get(i).child(2).child(0).child(1).text());

                                    SearchDto result = SearchDto.builder()
                                            .itemLink(link)
                                            .imgLink(img)
                                            .name(name)
                                            .price(price)
                                            .build();

                                    list.add(result);

                                    log.info("url = {}", link);
                                    log.info("img = {}", img);
                                    log.info("name = {}", name);
                                    log.info("price = {}", price);
                                    log.info("avail = {}", available);
                                    log.info("vinyl = {}", vinyl);
                                }
                            }
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
        return list;
    }

    public List<SearchDto> dope(String keyword) {
        List<SearchDto> list = new ArrayList<>();
        if (keyword != null) {
            keyword = keyword.replace(" ", "+");

            String dope = "https://doperecord.com/product/search.html?keyword="+keyword;
            Connection conn = Jsoup.connect(dope);

            try {
                Document doc = conn.get();

                // 검색 결과가 있을 때
                Boolean data = doc.select("div.noData").get(0).hasClass("displaynone");
                log.info("data = {}", data);

                if (data) {
                    while (true) {
                        Elements links = doc.select("div.prdImg > a");
                        Elements imgs = doc.select("div.prdImg > a > img");
                        Elements names = doc.select("div.description > strong > a");
                        Elements ulOfPrices = doc.select("ul.xans-search-listitem");
                        Elements parentOfAvails = doc.select("div.promotion");

                        for (int i = 0; i < links.size(); i++) {
                            String link = links.get(i).attr("abs:href");
                            boolean vinyl = !link.contains("category/47") && !link.contains("category/57") && !link.contains("category/60");
                            if (vinyl) {
                                boolean available = true;
                                if (parentOfAvails.get(i).childrenSize() > 0) {
                                    available = !parentOfAvails.get(i).child(0).attr("alt").equals("품절");
                                }
                                String strPrice = ulOfPrices.get(i).child(0).child(1).text();
                                if (strPrice.equals("품절")) { available = false; }
                                if (available) {
                                    String img = "https:" + imgs.get(i).attr("src");
                                    String name = names.get(i).child(1).text();
                                    int price = priceToInt(strPrice);
                                    SearchDto result = SearchDto.builder()
                                            .itemLink(link)
                                            .imgLink(img)
                                            .name(name)
                                            .price(price)
                                            .build();
                                    list.add(result);

                                    log.info("url = {}", link);
                                    log.info("img = {}", img);
                                    log.info("name = {}", name);
                                    log.info("price = {}", price);
                                    log.info("avail = {}", available);
                                    log.info("vinyl = {}", vinyl);
                                }
                            }

                        }

                        // 다음 페이지 접근
                        String next = doc.getElementsByClass("xans-search-paging").get(0).child(3).attr("href");
                        if (next.equals("#none")) {
                            log.info("다음페이지 없음");
                            break;
                        } else {
                            String nextPage = "https://doperecord.com/product/search.html" + next;
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
        return list;
    }

    /*public  List<ComparePriceDto> yes24Vinyl(String keyword){
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
    }*/

}
