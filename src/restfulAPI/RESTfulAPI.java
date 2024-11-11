package restfulAPI;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;




public class RESTfulAPI {
    // erenin api: 4XWGHHUU6COAQC1T
    //benim api :LG69CLBAG4O31J1Z




    public static void main(String[] args) throws MalformedURLException, IOException, URISyntaxException {
        // Uncomment the following if you get an exception related to SSL certificate
		/*
		TrustManager[] trustAllCerts = new TrustManager[]{
        new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            public void checkClientTrusted(
                java.security.cert.X509Certificate[] certs, String authType) {
            }
            public void checkServerTrusted(
                java.security.cert.X509Certificate[] certs, String authType) {
            }
        }};
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {

        }
		*/
        URI uri= new URI("https://www.alphavantage.co/query?function=NATURAL_GAS&apikey=4XWGHHUU6COAQC1T&interval=monthly");

        URL url = uri.toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        String inline = "";
        Scanner scanner = new Scanner(url.openStream());
        while (scanner.hasNext()) {
            inline += scanner.nextLine();
        }
        //Close the scanner
        scanner.close();
        System.out.println(inline);

        JSONArray jsonObject = new JSONArray(inline);
        for (int i = 0; i < jsonObject.length(); i++)
        {
            String id = jsonObject.getJSONObject(i).getString("id");
            String name = jsonObject.getJSONObject(i).getString("name");
            System.out.println(id + "\t" + name);
        }
        System.out.println("========================================");
    }

}