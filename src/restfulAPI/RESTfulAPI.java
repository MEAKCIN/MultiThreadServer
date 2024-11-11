package restfulAPI;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;




public class RESTfulAPI {
    // erenin api: 4XWGHHUU6COAQC1T
    //benim api :LG69CLBAG4O31J1Z


    public HashMap<String,String> gasData(HashMap<String,String> message) throws URISyntaxException, IOException {
        //Getting Data
        URI uri = new URI("https://www.alphavantage.co/query?function=NATURAL_GAS&apikey=O7NK1J4VMTFYGD00&interval=daily");

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
        JSONObject jsonObject = new JSONObject(inline);
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        HashMap<LocalDate, String> data = new HashMap<LocalDate, String>();
        DateTimeFormatter formatter= DateTimeFormatter.ofPattern("yyyy-MM-dd");


        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject dataPoint=jsonArray.getJSONObject(i);

            String dateString=dataPoint.getString("date");
            LocalDate date = LocalDate.parse(dateString,formatter);
            String value=dataPoint.getString("value");
            data.put(date,value);

        }
        System.out.println(data);
        //I get the data now I am getting specific data
        HashMap<String,String> result = new HashMap<>();
        if(message.get("change_date")==null && message.get("average_date")==null){
            result=specificGasPrice(data,message);
        } else if (message.get("change_date")!=null && message.get("average_date")==null) {
            result=changeInGasPrices(data,message);//first date is the key and value shows it is increase or decrease

        }
        else if(message.get("change_date")==null && message.get("average_date")!=null){
        return averageGasPrice(data,message);

        }


        return result;
    }



    public HashMap<String,String> changeInGasPrices(HashMap<LocalDate,String>data,HashMap<String,String> message)  {
        HashMap<String,String> result = new HashMap<>();
        LocalDate date1= LocalDate.parse(message.get("date"),DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate date2= LocalDate.parse(message.get("change_date"),DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        float date1Price=Float.parseFloat(data.get(date1));
        float date2Price=Float.parseFloat(data.get(date2));
        if(date1Price>date2Price){
            result.put(message.get("date"),"First date gas price is greater that second date gas price");
        } else if (date1Price<date2Price) {
            result.put(message.get("date"),"Second date gas price is greater than first date gas price");
        }
        else{
            result.put(message.get("date"),"Prices are equal for two date");
        }

        return result;
    }

    public HashMap<String,String> specificGasPrice(HashMap<LocalDate,String> data,HashMap<String,String>message) {
        HashMap<String,String> result = new HashMap<>();
        LocalDate date1= LocalDate.parse(message.get("date"),DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        result.put(message.get("date"), data.get(date1));
        return result;

    }
    public HashMap<String,String> averageGasPrice(HashMap<LocalDate,String>data,HashMap<String,String>message) {
        HashMap<String,String> result = new HashMap<>();
        LocalDate date1= LocalDate.parse(message.get("date"),DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate date2= LocalDate.parse(message.get("average_date"),DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        if (date1.isBefore(date2)){
            float index=0;
            float sum=0;
            for(LocalDate i:data.keySet()){

                if((date1.equals(i) || date1.isBefore(i) ) && (date2.isAfter(i)  || date2.isBefore(i))    ){
                    index++;
                    sum+=Float.parseFloat(data.get(i));
                }

            }
            result.put(message.get("date"),Float.toString(sum/index));


        }else if(date2.isBefore(date1)){
            float index=0;
            float sum=0;
            for (LocalDate i:data.keySet()){
                if (date2.equals(i) || date2.isBefore(i) && (date1.isAfter(i) || date1.isEqual(i))    ){
                    index++;
                    sum+=Float.parseFloat(data.get(i));
                }

            }
            result.put(message.get("date"),Float.toString(sum/index));
        }

        return result;
    }



    public HashMap<String,ArrayList<String>> exchange(HashMap<String,String>message) throws URISyntaxException, IOException {
        //https://www.alphavantage.co/query?function=CURRENCY_EXCHANGE_RATE&from_currency=USD&to_currency=TRY&apikey=YOUR_API_KEY
        //"https://www.alphavantage.co/query?function=CURRENCY_EXCHANGE_RATE&from_currency="+message.get("-from")+"&to_currency="+message.get("-to")+"&apikey=4XWGHHUU6COAQC1T";
        //

        String uri_string="https://www.alphavantage.co/query?function=CURRENCY_EXCHANGE_RATE&from_currency=USD&to_currency=TRY&apikey=YOUR_API_KEY";
        URI uri = new URI(uri_string);

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
        JSONObject jsonObject = new JSONObject(inline);
        JSONObject exchangeObject= jsonObject.getJSONObject("Realtime Currency Exchange Rate");

        HashMap<String,ArrayList<String>> result = new HashMap<>();
        String from_currency=exchangeObject.getString("1. From_Currency Code");
        String from_currency_name=exchangeObject.getString("2. From_Currency Name");
        String to_currency=exchangeObject.getString("3. To_Currency Code");
        String to_currency_name=exchangeObject.getString("4. To_Currency Name");
        String exchange_Rate=exchangeObject.getString("5. Exchange Rate");
        String last_refresh=exchangeObject.getString("6. Last Refresh");
        ArrayList<String> resultArray=new ArrayList<>();
        resultArray.add(from_currency_name);
        resultArray.add(to_currency);
        resultArray.add(to_currency_name);
        resultArray.add(exchange_Rate);
        resultArray.add(last_refresh);
        result.put(from_currency,resultArray);
        return result;



    }       //value:to float, from_name:String,to_name:String, last_refresh:String



}