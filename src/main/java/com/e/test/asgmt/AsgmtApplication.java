package com.e.test.asgmt;

import com.e.test.asgmt.service.WordFreqSearchService;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@Slf4j
@EnableAsync
@SpringBootApplication
public class AsgmtApplication implements CommandLineRunner {

  @Autowired WordFreqSearchService wordFreqSearchService;

  public static void main(String[] args) {
    SpringApplication.run(AsgmtApplication.class, args);
  }

  // This method will run run after app init, find and print top 10 frequent words
  @Override
  public void run(String... args) throws Exception {
    log.info("process started - (time::{})", LocalDateTime.now());
    wordFreqSearchService.findWordsByFreq("https://www.314e.com/");
    log.info("process ended - (time::{})", LocalDateTime.now());
  }
}
