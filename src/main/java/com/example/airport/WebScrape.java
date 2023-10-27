package com.example.airport;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class WebScrape {


    public static void scrapeDepartures() {

        final String url = "https://www.flightradar24.com/data/airports/krk/departures";

        try {
            final Document document = Jsoup.connect(url).get();

            for (Element row : document.select("table.table.table-condensed.table-hover.data-table.m-n-t-15 tr")
            ) {
//                if (row.select("td:nth-of-type(4)").text().equals("")) {
//                    continue;
//                } else {
                final String ticker =
                        row.select("td:nth-of-type(3)").text();
                System.out.println(ticker);
            }
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
