package com.viniciuspessoni.cc1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class LogProcessorTest {
    @Test
    @DisplayName("When the path to crawl is correct, then file list has correct list of files")
    public void testFileListCorrectlyRetrieve() {
      LogProcessor lp = new LogProcessor();
      List<String> expectedFileList = new ArrayList<>();
      expectedFileList.add("./testLog1.txt");
      expectedFileList.add("./testLog2.txt");

      List<String> resultedFileList = lp.crawlDirectory("./");
      Collections.sort(expectedFileList);
      Collections.sort(resultedFileList);

      assertEquals(expectedFileList, resultedFileList);
    }
}
