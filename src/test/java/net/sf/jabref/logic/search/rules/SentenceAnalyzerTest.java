package net.sf.jabref.logic.search.rules;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

public class SentenceAnalyzerTest {

    @Test
    public void testGetWords() throws Exception {
        assertEquals(Arrays.asList("a","b"), new SentenceAnalyzer("a b").getWords());
        assertEquals(Arrays.asList("a","b"), new SentenceAnalyzer(" a b ").getWords());
        assertEquals(Collections.singletonList("b "), new SentenceAnalyzer("\"b \" ").getWords());
        assertEquals(Collections.singletonList(" a"), new SentenceAnalyzer(" \\ a").getWords());
    }
    
}