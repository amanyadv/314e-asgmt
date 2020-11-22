package com.e.test.asgmt.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Word implements Comparable<Word> {
  String word;
  int count;

  @Override
  public int hashCode() {
    return word.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return word.equals(((Word) obj).word);
  }

  @Override
  public int compareTo(Word b) {
    return b.count - count;
  }

  public void incrCount() {
    this.count++;
  }
}
