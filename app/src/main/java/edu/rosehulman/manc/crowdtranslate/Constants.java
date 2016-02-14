package edu.rosehulman.manc.crowdtranslate;

/**
 * Created by manc on 2/7/2016.
 */
public class Constants {

    // Firebase "table" keys
    public static final String USER_KEY = "user";
    public static final String PROJECT_KEY = "project";
    public static final String LINE_KEY = "line";
    public static final String TRANSLATION_KEY = "translation";
    private static final String VOTE_KEY = "vote";


    // Firebase urls
    public static final String BASE_FIREBASE_URL = "https://crowd-translate.firebaseio.com/";
    public static final String USER_PATH = BASE_FIREBASE_URL + "/" + USER_KEY;
    public static final String PROJECT_PATH = BASE_FIREBASE_URL + "/" + PROJECT_KEY;
    public static final String LINE_PATH = BASE_FIREBASE_URL + "/" + LINE_KEY;
    public static final String TRANSLATION_PATH = BASE_FIREBASE_URL + "/" + TRANSLATION_KEY;
    public static final String VOTE_PATH = BASE_FIREBASE_URL + "/" + VOTE_KEY;

}
