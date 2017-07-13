import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Created by EvanKing on 7/12/17.
 */
public class Indexer {
    private static String INDEX_PATH;

    private IndexWriter writer;

    public Indexer(Path indexDirectoryPath) throws IOException {
        //this directory will contain the indexes
        Directory indexDirectory = FSDirectory.open(indexDirectoryPath);

        //create the indexer
        //TODO: Construct a legitimate analyzer
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(new StandardAnalyzer());
        writer = new IndexWriter(indexDirectory, indexWriterConfig);

    }


    private void indexResource(Resource resource) {
        Document document = resource.getDocument();
        try {
            writer.addDocument(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int createIndex(ArrayList<Resource> resources) {
        for (Resource resource : resources) {
            indexResource(resource);
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

    public void close() {
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
