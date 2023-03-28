package org.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Main {

    public static void main(String[] args) throws IOException {

        final String HOME_PATH = "https://www.agrolandia.sc.gov.br";
        final String ALL_BIDS_PATH = HOME_PATH + "/licitacoes";
        final List<String> TABLE_COLUMNS = new ArrayList<>(Arrays.asList("Modalidades", "divulgado", "andamento", "encerrada", "suspenso", "Total"));

        Document allBidsPage = Jsoup.connect(ALL_BIDS_PATH).get();

        allBidsPage.getElementsByTag("table").first()
                .getElementsByTag("tr").forEach(
                        (tr) -> {
                            String modality = tr.getElementsByClass("titulo").text();
                            Elements columns = tr.getElementsByTag("td");

                            IntStream.range(0, columns.size()).forEach( index -> {
                                Element column = columns.get(index);
                                if(
                                        !column.getElementsByTag("a").isEmpty()
                                        && ( index == TABLE_COLUMNS.indexOf("divulgado") || index == TABLE_COLUMNS.indexOf("andamento"))
                                ) {
                                    System.out.println(modality + " - " + TABLE_COLUMNS.get(index));
                                    String modalityBidsPath = columns.get(index).getElementsByTag("a").attr("href");
                                    try {
                                        Document modalityPage = Jsoup.connect(HOME_PATH + modalityBidsPath).get();

                                        modalityPage.getElementsByClass("item-lista").first()
                                                .getElementsByTag("li").forEach(
                                                        (li) -> {
                                                            System.out.println(li);
                                                        }
                                                );
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            });
                        }
                );
    }
}