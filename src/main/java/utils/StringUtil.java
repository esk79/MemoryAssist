package utils;

/**
 * Created by EvanKing on 8/4/17.
 */
public class StringUtil {

    public static String createPreviewText(String s){
        return sliceEnd(s,200) + "...";
    }


    private static String sliceEnd(String s, int endIndex) {
        if (endIndex < 0) endIndex = s.length() + endIndex;
        return s.substring(0, endIndex);
    }

}
