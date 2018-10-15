package net.anomalija.portal.anomalia.Model;


public class Constants {
//    localbase url, use only for local test
//    public static String baseUrl = "http://192.168.0.105/";
    public static String baseUrl = "https://www.anomalija.net/dPs4f6SgJN/";
    public static final String getNews = baseUrl + "json.php";
    public static final String gettingContent =  baseUrl + "getting_content.php?id=";
    public static final String gettingNewsFromId = baseUrl + "getNewsById.php?id=";
    public static final String gettingCategory = baseUrl + "getting_category.php";
    public static final String gettingCategoryFromPost = baseUrl + "getting_category_for_post.php?id=";
    public static final String gettingPostsForCategory = baseUrl + "geting_posts_for_category.php?id=";
    public static final String registre = baseUrl + "register.php";
    public static final String ERROR = "Connected faild";
}
