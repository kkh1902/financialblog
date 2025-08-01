package com.financeblog.util;

import com.github.slugify.Slugify;

public class SlugUtil {
    
    private static final Slugify slugify = Slugify.builder().build();
    
    public static String generateSlug(String text) {
        return slugify.slugify(text);
    }
    
    private SlugUtil() {
        // Utility class
    }
}