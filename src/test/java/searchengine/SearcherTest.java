package searchengine;

import models.Resource;
import org.junit.Before;
import org.junit.Test;

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

    }
}