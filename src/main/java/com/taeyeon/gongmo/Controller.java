package com.taeyeon.gongmo;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeFilter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class Controller {

    private final GongmoRepository gongmoRepository;
    
    @GetMapping(value = "/info")
    public String info() throws IOException {

        final String BASE_URL = "http://www.38.co.kr";
        Document document = Jsoup.connect("http://www.38.co.kr/html/fund/index.htm?o=k").get();

        Elements trElements = document.select("table[summary='공모주 청약일정'] tr");

        LocalDate curLocalDate = LocalDate.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MMdd");
        String curDate = curLocalDate.format(format);
        int cur = Integer.parseInt(curDate);
        List<GongmoData> gongmoDataList = new ArrayList<>();
        for (Element trElement : trElements) {
            Elements tdElements = trElement.select("td");
            // 헤더 제외
            if (tdElements.size() != 7) continue;

            int max_ = Integer.parseInt(tdElements.get(1).text().split("~")[1].replace(".", ""));
            if (max_ <= cur) continue;

            GongmoData data = GongmoData.builder()
                    .name(tdElements.get(0).text())
                    .date(tdElements.get(1).text())
                    .finalPrice(tdElements.get(2).text())
                    .hopePrice(tdElements.get(3).text())
                    .competitionRate(tdElements.get(4).text())
                    .securites(tdElements.get(5).text())
                    .build();

            String detailURL = BASE_URL + tdElements.get(6).select("a").attr("href");
            Document detailDocument = Jsoup.connect(detailURL).get();
            gongmoDataList.add(data);

            Elements company = detailDocument.select("table[summary='기업개요'] td");

            for (Element element : company) {
                if (element.attr("bgcolor").equals("#F1F4F7")) {
                    System.out.print(element.text() + " : ");
                } else {
                    System.out.println(element.text());
                }
            }
            System.out.println("==========");

            Elements info = detailDocument.select("table[summary='공모정보'] td");
            for (Element element : info) {
                if (element.attr("bgcolor").equals("#F1F4F7")) {
                    System.out.print(element.text() + " : ");
                } else {
                    System.out.println(element.text());
                }
            }
            System.out.println("==========");

            Elements day = detailDocument.select("table[summary='공모청약일정'] td");
            for (Element element : day) {
                if (element.attr("bgcolor").equals("#F1F4F7")) {
                    System.out.print(element.text() + " : ");
                } else if (element.attr("bgcolor").equals("#F5F5F2")) {
                    System.out.print(element.text() + " : ");
                } else {
                    System.out.println(element.text());
                }
            }

            break;
        }
        gongmoRepository.saveAll(gongmoDataList);

        return "";
    }
}
