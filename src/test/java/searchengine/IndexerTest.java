package searchengine;

import models.Resource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
        Resource resource1 = new Resource("This is an error", "This is a document with things in it");
        Resource resource2 = new Resource("Samantha King", "My sister. She is a nurse and works in Seattle.");
        Resource resource3 = new Resource("How to start the production server", "Run the startup script located at this url in the company wiki");
        Resource resource4 = new Resource("How to start the local server", "Run the startup script located at this url in the company wiki");

        resources.add(resource1);
        resources.add(resource2);
        resources.add(resource3);
        resources.add(resource4);



        indexer = new Indexer("src/test/resources/index");
        indexer.deleteAll();
    }


    @Test
    public void testCreateIndex() throws Exception {
        assertEquals(4, indexer.createIndex(resources));
    }

    @Test
    public void testAddNewResource() throws Exception {
        Resource resource5 = new Resource("Test resource", "adding resource 5");
        System.out.println(resource5.getUid());


        indexer.addNewResource(resource5);
        assertEquals(5, indexer.createIndex(resources));
    }

    @After
    public void tearDown() throws Exception {
        indexer.close();
    }
}