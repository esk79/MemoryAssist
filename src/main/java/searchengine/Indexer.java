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
import utils.LuceneConstants;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Created by EvanKing on 7/12/17.
 */
public class Indexer {
    private IndexWriter writer;

    //TODO: make sure error handling is at correct level of abstraction beacuse right now it is not

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
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private void deleteDocument(String uid){
        try {
            writer.deleteDocuments(new Term(LuceneConstants.UID, uid));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteAll() {
        try {
            writer.deleteAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void commit() {
        try {
            writer.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
