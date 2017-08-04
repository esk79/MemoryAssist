package models;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import utils.LuceneConstants;
import utils.StringUtil;

import java.util.StringJoiner;
import java.util.UUID;

/**
 * Created by EvanKing on 7/12/17.
 */
public class Resource {
    private String title;
    private String markdown;
    private String uid;

    public Resource(String title, String markdown) {
        this.title = title;
        this.markdown = markdown;
        this.uid = UUID.randomUUID().toString();
    }

    public Resource(String title, String markdown, String uid) {
        this.title = title;
        this.markdown = markdown;
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public String getMarkdown() {
        return markdown;
    }

    public String getUid() {
        return uid;
    }

    public String getMarkdownPreview() {
        return StringUtil.createPreviewText(markdown);
    }


    //TODO: potentially remove all but defaultContentField
    public Document getDocument() {
        Document document = new Document();

        TextField uidField = new TextField(LuceneConstants.UID,
                uid, Field.Store.YES);

        TextField titleField = new TextField(LuceneConstants.TITLE,
                title, Field.Store.YES);

        TextField documentContentField = new TextField(LuceneConstants.MARKDOWN,
                markdown, Field.Store.YES);

        // defaultContentField is used as the default search field as to search all fields of the resource
        TextField defaultContentField = new TextField(LuceneConstants.DEFAULT_FIELD,
                getDefaultContentField(), Field.Store.YES);

        document.add(uidField);
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

