package utils;

/**
 * Created by EvanKing on 8/4/17.
 */

import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.options.MutableDataSet;

import java.util.Arrays;

public class MarkdownProcessor {
    public String convertMarkdownToHTML(String markdown) {
        MutableDataSet options = new MutableDataSet();

        // sets optional extensions
        options.set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create(), StrikethroughExtension.create()));

        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        // You can re-use parser and renderer instances
        Node document = parser.parse(markdown);
        String html = renderer.render(document);
        return html;
    }

    public static String createPreviewText(String s){
        return sliceEnd(s,200) + "...";
    }

    private static String sliceEnd(String s, int endIndex) {
        if (endIndex < 0) endIndex = s.length() + endIndex;
        return s.substring(0, Math.min(endIndex, s.length()));
    }
}
