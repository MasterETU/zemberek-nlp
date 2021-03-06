package zemberek.morphology.apps;

import com.google.common.collect.Sets;
import junit.framework.Assert;
import org.junit.Test;
import zemberek.morphology.analysis.WordAnalysis;
import zemberek.morphology.analysis.tr.TurkishMorphology;
import zemberek.morphology.analysis.tr.UnidentifiedTokenAnalyzer;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

public class UnidentifiedTokenAnalyzerTest {

    @Test
    public void shouldCreateUnidentifiedTokenParserSuccessfully() throws IOException {
        TurkishMorphology parser = TurkishMorphology.createWithDefaults();
        UnidentifiedTokenAnalyzer uiParser = new UnidentifiedTokenAnalyzer(parser);
        List<WordAnalysis> results = uiParser.parse("Ankara'ya");
        for (WordAnalysis result : results) {
            System.out.println(result);
        }
    }

    @Test
    public void shouldParseSmallCaseProperNounsWithSingleQuote() throws IOException {
        HashSet<String> expected = Sets.newHashSet(
                "[(İstanbul:istanbul) (Noun,Prop;A3sg+P2sg:un+Nom)]",
                "[(İstanbul:istanbul) (Noun,Prop;A3sg+Pnon+Gen:un)]");

        TurkishMorphology parser = TurkishMorphology.builder().addTextDictionaryResources("dev-lexicon.txt").build();
        UnidentifiedTokenAnalyzer uiParser = new UnidentifiedTokenAnalyzer(parser);
        List<WordAnalysis> results = uiParser.parse("İstanbul'un");
        Assert.assertEquals(2, results.size());
        for (WordAnalysis result : results) {
            Assert.assertTrue(expected.contains(result.formatLong()));
        }

        results = uiParser.parse("istanbul'un");
        Assert.assertEquals(2, results.size());
        for (WordAnalysis result : results) {
            Assert.assertTrue(expected.contains(result.formatLong()));
        }

        results = uiParser.parse("Ankara'ya");
        Assert.assertEquals(1, results.size());
        Assert.assertEquals("[(Ankara:ankara) (Noun,Prop;A3sg+Pnon+Dat:ya)]", results.get(0).formatLong());

        results = uiParser.parse("ankara'ya");
        Assert.assertEquals(1, results.size());
        Assert.assertEquals("[(Ankara:ankara) (Noun,Prop;A3sg+Pnon+Dat:ya)]", results.get(0).formatLong());

        // Karaman does not exist in dictionary
        results = uiParser.parse("Karaman");
        Assert.assertEquals(1, results.size());
        Assert.assertEquals("[(Karaman:karaman) (Noun,Prop;A3sg+Pnon+Nom)]", results.get(0).formatLong());

        results = uiParser.parse("karaman'a");
        Assert.assertEquals(1, results.size());
        Assert.assertEquals("[(Karaman:karaman) (Noun,Prop;A3sg+Pnon+Dat:a)]", results.get(0).formatLong());

        results = uiParser.parse("karaman");
        Assert.assertEquals(0, results.size());
    }


}
