package searchengine;

import models.Resource;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.junit.Before;
import org.junit.Test;
import utils.LuceneConstants;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by EvanKing on 7/13/17.
 */
public class SearcherTest {

    private Searcher searcher;

    @Before
    public void setUp() throws Exception {
        searcher = new Searcher("src/test/resources/index");
    }

    @Test
    public void testSearchWithEmptyResult() throws Exception {
        List<Resource> resources = searcher.search("zhaljkahsdf");
        // Should not return any totalHits because "zhaljkahsdf" not in any documents
        assertEquals(0, resources.size());
    }

    @Test
    public void testSearchReturnsCorrectResult() throws Exception {
        List<Resource> resources = searcher.search("sister");
        // Should only return a hit on the document with sister in it
        assertEquals(1, resources.size());

        for(Resource resource : resources) {
            assertEquals("Samantha King", resource.getTitle());
        }
    }

    @Test
    public void testSearchReturnsCorrectMultipleResult() throws Exception {
        List<Resource> resources = searcher.search("how do i start the server");
        // Should return a hit on two document with "how to start" in them
        assertEquals(2, resources.size());
    }

    @Test
    public void testSearchSpecificFieldValue() throws Exception {

        QueryParser queryParser = new QueryParser(LuceneConstants.UID,
                new StandardAnalyzer());

        Query query = queryParser.parse("4ea1354b-d398-4bd6-8ee0-c0d7cb344a30");
        System.out.println(query);


        TopDocs topdoc = searcher.indexSearcher.search(query, 1);
        System.out.println(topdoc.totalHits);

        List<Resource> result = searcher.convertTopDocsToResourceList(topdoc);
        System.out.println(result);
    }
}