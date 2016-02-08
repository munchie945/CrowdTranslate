package edu.rosehulman.manc.crowdtranslate;

/**
 * Created by manc on 2/7/2016.
 */
public class Constants {

    // Firebase "table" keys
    public static String USER_KEY = "user";
    public static String PROJECT_KEY = "project";
    public static String LINE_KEY = "line";
    public static String TRANSLATION_KEY = "translation";

    // Firebase urls
    public static String BASE_FIREBASE_URL = "https://crowd-translate.firebaseio.com/";
    public static String USER_PATH = BASE_FIREBASE_URL + "/" + USER_KEY;
    public static String PROJECT_PATH = BASE_FIREBASE_URL + "/" + PROJECT_KEY;
    public static String LINE_PATH = BASE_FIREBASE_URL + "/" + LINE_KEY;
    public static String TRANSLATION_PATH = BASE_FIREBASE_URL + "/" + TRANSLATION_KEY;

}
