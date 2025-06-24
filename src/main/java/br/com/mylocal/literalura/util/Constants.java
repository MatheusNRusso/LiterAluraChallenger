package br.com.mylocal.literalura.util;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public final class Constants {

    private Constants() {}

    public static final String BASE_URL = "https://gutendex.com";

    public static final String SEARCH_URL = "/books/?search=";

    public static String buildSearch(String search) {

        String encodedSearch = URLEncoder.encode(search, StandardCharsets.UTF_8);
        return BASE_URL + SEARCH_URL + encodedSearch;
    }
}
