import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import searchengine.Category;
import searchengine.Indexer;
import models.Resource;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by EvanKing on 7/13/17.
 */
public class IndexerTest {

    private ArrayList<Resource> resources = new ArrayList<Resource>();
    private Indexer indexer;

    @Before
    public void setUp() throws Exception {
        Resource resource1 = new Resource(Category.ERROR, "This is an error", "This is a document with things in it");
        Resource resource2 = new Resource(Category.PEOPLE, "Samantha King", "My sister. She is a nurse and works in Seattle.");
        Resource resource3 = new Resource(Category.QUESTION, "How to start the production server", "Run the startup script located at this url in the company wiki");
        Resource resource4 = new Resource(Category.QUESTION, "How to start the local server", "Run the startup script located at this url in the company wiki");

        resources.add(resource1);
        resources.add(resource2);
        resources.add(resource3);
        resources.add(resource4);


        Path indexDirectory = Paths.get("src/test/resources/index");
        indexer = new Indexer(indexDirectory);
        indexer.deleteAll();
    }


    @Test
    public void testCreateIndex() throws Exception {
        assertEquals(4, indexer.createIndex(resources));

    }

    @After
    public void tearDown() throws Exception {
        indexer.close();
    }
}