package searchengine;

import annotations.IndexDirectoryString;
import com.google.inject.Inject;
import models.Resource;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
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

    IndexSearcher indexSearcher;
    QueryParser queryParser;

    @Inject
    public Searcher(@IndexDirectoryString String indexDirectoryString)
            throws IOException {
        Path indexDirectoryPath = Paths.get(indexDirectoryString);
        Directory indexDirectory = FSDirectory.open(indexDirectoryPath);
        IndexReader reader = DirectoryReader.open(indexDirectory);
        indexSearcher = new IndexSearcher(reader);
        queryParser = new QueryParser(LuceneConstants.DEFAULT_FIELD,
                new StandardAnalyzer());
    }


    public List<Resource> search(String searchQuery) {
        TopDocs topDocs = searchHelper(searchQuery);

        List<Resource> resourceList = convertTopDocsToResourceList(topDocs);
        return resourceList;
    }

    private List<Resource> convertTopDocsToResourceList(TopDocs topDocs) {
        List<Resource> result = new ArrayList<>();

        if (topDocs != null) {
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                Document doc = getDocument(scoreDoc);
                result.add(convertDocumentToResource(doc));
            }
        }
        return result;
    }

    private Resource convertDocumentToResource(Document doc) {
        assert doc != null;
        String title = doc.get(LuceneConstants.TITLE);
        String markdown = doc.get(LuceneConstants.MARKDOWN);
        return new Resource(title, markdown);
    }

    private TopDocs searchHelper(String searchQuery) {
        Query query;
        TopDocs topDocs;
        try {
            query = queryParser.parse(searchQuery);
            topDocs = indexSearcher.search(query, LuceneConstants.MAX_NUMBER_OF_RESULTS);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
            return null;
        }

        return topDocs;
    }


    public Document getDocument(ScoreDoc scoreDoc) {
        try {
            return indexSearcher.doc(scoreDoc.doc);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
