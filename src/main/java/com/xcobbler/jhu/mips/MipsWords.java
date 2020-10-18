package com.xcobbler.jhu.mips;

import java.util.List;

public abstract class MipsWords {
  private int wordSize = 32;
  private List<String> words;

  public MipsWords(int wordSize, List<String> words) {
    this.wordSize = wordSize;
    this.words = words;
  }

  public int getWordSize() {
    return wordSize;
  }

  public List<String> getWords() {
    return words;
  }

  public void setWords(List<String> words) {
    this.words = words;
  }

}
