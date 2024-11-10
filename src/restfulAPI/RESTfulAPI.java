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
        URI uri= new URI("https://www.alphavantage.co/query?function=NATURAL_GAS&apikey=4XWGHHUU6COAQC1T&datatype=csv");

        URL url = uri.toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        
        ArrayList<String> inline = new ArrayList<String>();
        String line= new String("");

        Scanner scanner = new Scanner(url.openStream());
        while (scanner.hasNextLine()) {
            String nextLine = scanner.nextLine();
            inline.add(nextLine);
            line += nextLine;

        }
        //Close the scanner
        scanner.close();


        FileWriter fileWriter = new FileWriter("output1.txt");
        PrintWriter printWriter = new PrintWriter(fileWriter);

        printWriter.print(inline);
        printWriter.close();




    }

}
