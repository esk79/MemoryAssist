package models;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import utils.LuceneConstants;

import java.util.StringJoiner;

/**
 * Created by EvanKing on 7/12/17.
 */
public class Resource {
    private String title;
    private String markdown;

    public Resource(String title, String markdown) {
        this.title = title;
        this.markdown = markdown;
    }

    public String getTitle() {
        return title;
    }

    public String getMarkdown() {
        return markdown;
    }

    //TODO: potentially remove all but defaultContentField
    public Document getDocument() {
        Document document = new Document();

        TextField titleField = new TextField(LuceneConstants.TITLE,
                title, Field.Store.YES);

        TextField documentContentField = new TextField(LuceneConstants.DOCUMENT_CONTENT,
                markdown, Field.Store.YES);

        // defaultContentField is used as the default search field as to search all fields of the resource
        TextField defaultContentField = new TextField(LuceneConstants.DEFAULT_FIELD,
                getDefaultContentField(), Field.Store.YES);

        document.add(titleField);
        document.add(documentContentField);
        document.add(defaultContentField);

        return document;
    }

    private String getDefaultContentField() {
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add(title)
                .add(markdown);

        return joiner.toString();
    }
}

