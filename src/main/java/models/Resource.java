package models;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import utils.Log;
import utils.LuceneConstants;
import utils.MarkdownProcessor;

import java.util.StringJoiner;
import java.util.UUID;

/**
 * Created by EvanKing on 7/12/17.
 */
public class Resource {
    private static final Log LOGGER = Log.forClass(Resource.class);

    private String title;
    private String markdown;
    private String uid;
    private Boolean update = false;

    public Resource(String title, String markdown) {
        this.title = title;
        this.markdown = markdown;
        this.uid = generateUID();
    }

    public Resource(String title, String markdown, String uid) {
        this.title = title;
        this.markdown = markdown;
        this.uid = uid;
        this.update = true;
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
        return MarkdownProcessor.createPreviewText(markdown);
    }

    public boolean isUpdate(){
        return update;
    }


    public Document getDocument() {
        Document document = new Document();

        TextField uidField = new TextField(LuceneConstants.UID,
                uid, Field.Store.YES);

        TextField titleField = new TextField(LuceneConstants.TITLE,
                title, Field.Store.YES);

        TextField markdownField = new TextField(LuceneConstants.MARKDOWN,
                markdown, Field.Store.YES);

        // defaultContentField is used as the default search field as to search all fields of the resource
        TextField defaultContentField = new TextField(LuceneConstants.DEFAULT_FIELD,
                getDefaultContentField(), Field.Store.YES);

        document.add(uidField);
        document.add(titleField);
        document.add(markdownField);
        document.add(defaultContentField);

        return document;
    }

    private String getDefaultContentField() {
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add(title)
                .add(markdown);

        return joiner.toString();
    }

    private String generateUID(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    @Override
    public String toString() {
        return "Resource{" +
                "title='" + title + '\'' +
                ", markdown='" + markdown + '\'' +
                ", uid='" + uid + '\'' +
                ", update=" + update +
                '}';
    }
}

