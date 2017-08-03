package searchengine;

import annotations.IndexDirectoryString;
import com.google.inject.Inject;
import models.Resource;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Created by EvanKing on 7/12/17.
 */
public class Indexer {
    private IndexWriter writer;

    @Inject
    public Indexer(@IndexDirectoryString String indexDirectoryString) throws IOException {
        Path indexDirectoryPath = Paths.get(indexDirectoryString);

        //this directory will contain the indexes
        Directory indexDirectory = FSDirectory.open(indexDirectoryPath);

        //create the indexer with a standard analyzer
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
