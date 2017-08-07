package searchengine;

import annotations.IndexDirectoryString;
import com.google.inject.Inject;
import models.Resource;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import utils.Log;
import utils.LuceneConstants;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Created by EvanKing on 7/12/17.
 */
public class Indexer {
    private static final Log LOGGER = Log.forClass(Indexer.class);

    private IndexWriter writer;

    //TODO: make sure error handling is at correct level of abstraction because right now it is not

    @Inject
    public Indexer(@IndexDirectoryString String indexDirectoryString) throws IOException {
        Path indexDirectoryPath = Paths.get(indexDirectoryString);

        //this directory will contain the indexes
        Directory indexDirectory = FSDirectory.open(indexDirectoryPath);

        //create the indexer with a whitespace analyzer
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(new StandardAnalyzer());
        writer = new IndexWriter(indexDirectory, indexWriterConfig);
    }

    public void addNewResource(Resource resource) throws IOException {
        Document document = resource.getDocument();
        writer.addDocument(document);
        commit();
    }

    public int createIndex(ArrayList<Resource> resources) throws IOException {
        for (Resource resource : resources) {
            addNewResource(resource);
        }
        return writer.numDocs();
    }

    public void updateResource(Resource resource){
        Document document = resource.getDocument();
        updateDocument(document, resource.getUid());
        commit();
    }

    public void deleteResource(String uid){
        deleteDocument(uid);
        commit();
    }

    private void updateDocument(Document document, String uid){
        try {
            writer.updateDocument(new Term(LuceneConstants.UID, uid), document);
        } catch (IOException e) {
            LOGGER.severe("[-] Error updating resource: %s", e.getMessage());
        }
    }

    private void deleteDocument(String uid){
        try {
            writer.deleteDocuments(new Term(LuceneConstants.UID, uid));
        } catch (IOException e) {
            LOGGER.severe("[-] Error deleting resource: %s", e.getMessage());
        }
    }

    public void deleteAll() {
        try {
            writer.deleteAll();
        } catch (IOException e) {
            LOGGER.severe("[-] Error deleting all resources: %s", e.getMessage());
        }
    }

    public void commit() {
        try {
            writer.commit();
        } catch (IOException e) {
            LOGGER.severe("[-] Error committing to index: %s", e.getMessage());
        }
    }

    public void close() {
        try {
            writer.close();
        } catch (IOException e) {
            LOGGER.severe("[-] Error closing index: %s", e.getMessage());
        }
    }

}
