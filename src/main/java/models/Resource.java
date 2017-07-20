package models;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import searchengine.Category;
import utils.LuceneConstants;

import java.util.StringJoiner;

/**
 * Created by EvanKing on 7/12/17.
 */
public class Resource {
    private Category category;
    private String title;
    private String documentContent;

    public Resource(Category category, String title, String documentContent) {
        this.category = category;
        this.title = title;
        this.documentContent = documentContent;
    }

    //TODO: potentially remove all but defaultContentField
    public Document getDocument() {
        Document document = new Document();

        StringField categoryField = new StringField(LuceneConstants.CATEGORY,
                category.name(), Field.Store.YES);

        TextField titleField = new TextField(LuceneConstants.TITLE,
                title, Field.Store.YES);

        TextField documentContentField = new TextField(LuceneConstants.DOCUMENT_CONTENT,
                documentContent, Field.Store.YES);

        // defaultContentField is used as the default search field as to search all fields of the resource
        TextField defaultContentField = new TextField(LuceneConstants.DEFAULT_FIELD,
                getDefaultContentField(), Field.Store.YES);

        document.add(categoryField);
        document.add(titleField);
        document.add(documentContentField);
        document.add(defaultContentField);

        return document;
    }

    private String getDefaultContentField(){
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add(category.name())
                .add(title)
                .add(documentContent);

        return joiner.toString();
    }
}

