package org.acrobatt.project.weatherapi;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

public class URLParser {

    private static Logger logger = LogManager.getLogger(URLParser.class);

    private URLParser() {}

    /**
     * Parses an URL based on an ApiURL scheme value and a list of tags
     * @param url the tagged URL scheme
     * @param tagMap a list of tags
     * @return the built URL
     */
    public static String parseURL(String url, HashMap<String, String> tagMap) {
        for(String tag : tagMap.keySet()) {
            String tagValue = tagMap.get(tag);
            String tagContainer = String.format("{%1s}", tag);
            url = url.replace(tagContainer, tagValue);
        }

        logger.info("Parsed URL -> "+url);
        return url;
    }
}
