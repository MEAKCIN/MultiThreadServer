package protocol;
//todo Parse the reeuest coming from client and query required informatiıon in api
//Parse the api response and sent required information

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Parser{

    public HashMap<String, String> clientMessageParse(String message){
        List<String> words = List.of(message.split(" "));


        if(message.contains("EXC")){
            String refresh="false";
            HashMap<String,String> dict = new HashMap<>();
            dict.put("-from",null);
            dict.put("-to",null);
            dict.put("-from_name",null);
            dict.put("-to_name",null);
            dict.put("-refresh",null);

            if(message.contains("-from")){
                int from_index=words.indexOf("-from");
                String from_currency=words.get(from_index+1);
                dict.put("-from",from_currency);

            }
            if(message.contains("-to")){
                int index=words.indexOf("-to");
                String to_currency=words.get(index+1);
                dict.put("-to",to_currency);
            }
            if(message.contains("-from_name")){
                int index=words.indexOf("-from_name");
                String from_name=words.get(index+1);
                dict.put("-from_name",from_name);
            }
            if(message.contains("-to_name")){
                int index=words.indexOf("-to_name");
                String to_name=words.get(index+1);
                dict.put("-to_name",to_name);
            }
            if(message.contains("-refresh")){
                refresh="true";
                dict.put("-refresh",refresh);
            }









        return dict;
        }
        else if(message.contains("GAS")){
            HashMap<String,String> dict = new HashMap<>();
            dict.put("date",null);
            dict.put("change_date",null);
            dict.put("average_date",null);
            int gas_date_index=words.indexOf("-date");
            dict.put("date",words.get(gas_date_index+1));

            if(message.contains("-change")){
                int change_index=words.indexOf("-change");
                dict.put("change_date",words.get(change_index+1));
            }
            if (message.contains("-average")){
                int average_index=words.indexOf("-average");
                dict.put("average_date",words.get(average_index+1));
            }

            return dict;
        }
        return null;
    }



}
