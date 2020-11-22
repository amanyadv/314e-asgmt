package com.e.test.asgmt.service;

import com.e.test.asgmt.client.jsoup.HtmlScraper;
import com.e.test.asgmt.model.Word;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WordFreqSearchService {

  @Value("${test.top.frequent.words.count:10}")
  private Integer TOP_WORDS_COUNT;

  @Autowired private HtmlScraper htmlScraper;

  /**
   * Method to find and print top K frequent words
   *
   * @param url
   */
  public void findWordsByFreq(String url) {
    ConcurrentHashMap<String, Word> wordsMap = new ConcurrentHashMap<>();
    //get all the links for url till m depth
    HashSet<String> links = htmlScraper.getAllLinksForDepth(url);
    List<CompletableFuture<Boolean>> futures = new ArrayList<>();
    //loop through links to find all freq words in that
    links.forEach(
        link -> futures.add(htmlScraper.getAllWordsWithFreq(link, wordsMap)));
    // Join the completion of all the threads
    CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

    //sort words by freq
    // log.info("[findWordsByFreq] total map size::{}", wordsMap.size());
    SortedSet<Word> sortedWords = new TreeSet<Word>(wordsMap.values());
    // log.info("[findWordsByFreq] sorted map size::{}", sortedWords.size());
    //build string of top k most freq words and print
    StringBuilder stringBuilder = new StringBuilder();
    int i = 0;
    for (Word word : sortedWords) {
      if (i >= TOP_WORDS_COUNT) {
        break;
      }
      if (i == 0) {
        stringBuilder.append('[');
      }
      stringBuilder.append('(');
      stringBuilder.append(word.getWord());
      stringBuilder.append(", ");
      stringBuilder.append(word.getCount());
      stringBuilder.append(')');
      if (i == 9) {
        stringBuilder.append(']');
      }
      i++;
    }
    log.info("Top " + TOP_WORDS_COUNT + " frequent words - {}", stringBuilder.toString());
  }
}
