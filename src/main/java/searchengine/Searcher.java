package searchengine;

import models.Resource;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import utils.Log;
import utils.LuceneConstants;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by EvanKing on 7/12/17.
 */
public class Searcher {
    private static final Log LOGGER = Log.forClass(Searcher.class);

    IndexSearcher indexSearcher;
    QueryParser queryParser;
    IndexReader reader;

    public Searcher(String indexDirectoryString) throws IOException {
        Path indexDirectoryPath = Paths.get(indexDirectoryString);
        Directory indexDirectory = FSDirectory.open(indexDirectoryPath);
        reader = DirectoryReader.open(indexDirectory);
        indexSearcher = new IndexSearcher(reader);
        queryParser = new QueryParser(LuceneConstants.DEFAULT_FIELD,
                new StandardAnalyzer());
    }


    public List<Resource> search(String searchQuery) {
        TopDocs topDocs = searchHelper(searchQuery);

        List<Resource> resourceList = convertTopDocsToResourceList(topDocs);
        return resourceList;
    }

    //TODO: make private
    public List<Resource> convertTopDocsToResourceList(TopDocs topDocs) {
        List<Resource> result = new ArrayList<>();

        if (topDocs != null) {
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                Document doc = getDocumentFromScoreDoc(scoreDoc);
                result.add(convertDocumentToResource(doc));
            }
        }
        return result;
    }

    private Resource convertDocumentToResource(Document doc) {
        assert doc != null;
        String title = doc.get(LuceneConstants.TITLE);
        String markdown = doc.get(LuceneConstants.MARKDOWN);
        String uid = doc.get(LuceneConstants.UID);
        return new Resource(title, markdown, uid);
    }

    private TopDocs searchHelper(String searchTerms) {
        Query query;
        TopDocs topDocs;
        try {
            query = queryParser.parse(searchTerms);
            topDocs = indexSearcher.search(query, LuceneConstants.MAX_NUMBER_OF_RESULTS);
        } catch (ParseException | IOException e) {
            LOGGER.severe("[-] Error searching index: %s", e.getMessage());
            return null;
        }
        return topDocs;
    }


    public Document getDocumentFromScoreDoc(ScoreDoc scoreDoc) {
        try {
            return indexSearcher.doc(scoreDoc.doc);
        } catch (IOException e) {
            LOGGER.severe("[-] Error getting document from ScoreDoc object: %s", e.getMessage());
        }
        return null;
    }

    public Resource getResourceByUID(String uid) throws IOException, ParseException {
        QueryParser queryParser = new QueryParser(LuceneConstants.UID,
                new StandardAnalyzer());
        Query query = queryParser.parse(uid);
        TopDocs topdoc = indexSearcher.search(query, 1);
        List<Resource> result = convertTopDocsToResourceList(topdoc);
        return result.get(0);
    }

    public static boolean indexExists(String indexDirectoryString){
        Path indexDirectoryPath = Paths.get(indexDirectoryString);
        Directory indexDirectory = null;
        boolean result = true;
        try {
            indexDirectory = FSDirectory.open(indexDirectoryPath);
            result = DirectoryReader.indexExists(indexDirectory);
        } catch (IOException e) {
            LOGGER.severe("[-] Error checking if index is present: %s", e.getMessage());
        }
        return result;
    }

}
