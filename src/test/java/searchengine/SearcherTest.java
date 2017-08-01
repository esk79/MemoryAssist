package searchengine;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.junit.Before;
import org.junit.Test;
import utils.LuceneConstants;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

/**
 * Created by EvanKing on 7/13/17.
 */
public class SearcherTest {

    private Searcher searcher;

    @Before
    public void setUp() throws Exception {
        Path indexDirectory = Paths.get("src/test/resources/index");
        searcher = new Searcher(indexDirectory);
    }

    @Test
    public void testSearchWithEmptyResult() throws Exception {
        TopDocs hits = searcher.search("zhaljkahsdf");
        // Should not return any totalHits because "zhaljkahsdf" not in any documents
        assertEquals(0, hits.totalHits);
    }

    @Test
    public void testSearchReturnsCorrectResult() throws Exception {
        TopDocs hits = searcher.search("sister");
        // Should only return a hit on the document with sister in it
        assertEquals(1, hits.totalHits);

        for(ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = searcher.getDocument(scoreDoc);
            assertEquals("Samantha King", doc.getField(LuceneConstants.TITLE).stringValue());
        }
    }

    @Test
    public void testSearchReturnsCorrectMultipleResult() throws Exception {
        TopDocs hits = searcher.search("how do i start the server");
        // Should return a hit on two document with "how to start" in them
        assertEquals(2, hits.totalHits);
    }
}