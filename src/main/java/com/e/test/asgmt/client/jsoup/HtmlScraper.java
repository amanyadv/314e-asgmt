package com.e.test.asgmt.client.jsoup;

import com.e.test.asgmt.model.Word;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HtmlScraper {
  @Value("${test.url.expand.max.depth:4}")
  private Integer MAX_DEPTH;

  private String startUrl = "";
  private HashSet<String> links = new HashSet<>();

  /**
   * Method to get all link from url till max depth using jsoup
   *
   * @param url
   * @return
   */
  public HashSet<String> getAllLinksForDepth(String url) {
    if (links.size() > 0) {
      links.clear();
    }
    startUrl = url;
    findAllLinksForDepth(url, 0);
    return links;
  }

  /**
   * Method to find all unique word from url using jsoup
   *
   * @param link
   * @param wordsMap
   * @return
   */
  @Async
  public CompletableFuture<Boolean> getAllWordsWithFreq(
      String link, ConcurrentHashMap<String, Word> wordsMap) {
    boolean done = true;
    // log.info("[getAllWordsWithFreq] link::{}, map size::{}", link, wordsMap.size());
    BufferedReader reader = null;
    try {
      Document doc = Jsoup.connect(link).get();

      // Get the actual text from the page, excluding the HTML
      String text = doc.body().text();
      // log.info("[getAllWordsWithFreq] text::{}", text);

      // Create BufferedReader for reading lines and creating word map
      reader =
          new BufferedReader(
              new InputStreamReader(
                  new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8))));
      String line;
      while ((line = reader.readLine()) != null) {
        String[] words = line.split("[^A-ZÃƒâ€¦Ãƒâ€žÃƒâ€“a-zÃƒÂ¥ÃƒÂ¤ÃƒÂ¶]+");
        for (String word : words) {
          if ("".equals(word)) {
            continue;
          }
          Word wordObj = wordsMap.get(word);
          if (wordObj == null) {
            wordObj = new Word();
            wordObj.setWord(word);
            wordObj.setCount(0);
            wordsMap.put(word, wordObj);
          }
          wordObj.incrCount();
        }
      }
    } catch (Exception e) {
      log.error("[getAllWordsWithFreq] Error for link::{} ", link, e);
      log.info("Process will continue ....");
      done = false;
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (Exception e) {
          log.error("[getAllWordsWithFreq] Error for link::{} ", link, e);
          log.info("Process will continue .....");
        }
      }
    }
    // log.info("[getAllWordsWithFreq] ended process map count::{}", wordsMap.size());
    return CompletableFuture.completedFuture(done);
  }

  // ----------   private methods   --------------
  /**
   * @param url
   * @param depth
   */
  private void findAllLinksForDepth(String url, int depth) {
    if (!links.contains(url) && depth <= MAX_DEPTH) {
      //log.info("[visitPages] url::{}, depth::{}", url, depth);
      try {
        links.add(url);

        Document document = Jsoup.connect(url).get();
        document.body().text();
        document.head().text();
        //ignore external links
        Elements linksOnPage = document.select("a[href^="+startUrl+"]");

        depth++;

        for (Element page : linksOnPage) {
          findAllLinksForDepth(page.attr("abs:href"), depth);
        }
      } catch (Exception e) {
        log.error("[visitPages] Error for url::{} ", url, e);
        log.info("Process will continue ...");
      }
    }
  }
}
