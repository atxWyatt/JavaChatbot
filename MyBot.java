 import org.jibble.pircbot.*;

 import twitter4j.TwitterFactory;
 import twitter4j.conf.ConfigurationBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;

 import org.jibble.pircbot.PircBot;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import twitter4j.Status;
 import twitter4j.TwitterException;
 
 import java.net.MalformedURLException;
 import java.net.URL;

 import java.io.BufferedReader;
 import java.io.IOException;
 import java.io.InputStreamReader;
 import java.net.HttpURLConnection;
 import java.net.URL;
  
 import com.google.gson.JsonElement;
 import com.google.gson.JsonObject;
 import com.google.gson.JsonParser;



 
public class MyBot extends PircBot {
    
    public MyBot() {
        this.setName("gwypaas");
    }
    
    
    public void onMessage(String channel, String sender,
                       String login, String hostname, String message) {
        if (message.equalsIgnoreCase("time")) {
            String time = new java.util.Date().toString();
            sendMessage(channel, sender + ": The time is now " + time);
        }

		if(message.equalsIgnoreCase("my tweets")) {
				ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
				configurationBuilder.setDebugEnabled(true).setOAuthConsumerKey("dAhGUalqTHQmLRNSmgsqxQ8w5").setOAuthConsumerSecret("KFNyDaoUtfzSTwym8xh3Pys1q75n1RIh1Y8K3yjoNex9x53LNJ").setOAuthAccessToken("2820305305-rsV85o3fsw0PwJczvLa6vmpPHgBH3CmWWSUOSaI").setOAuthAccessTokenSecret("wkbrjVtzIAP3IHQJO9jne7zfww4TsoMersPwa7aZNlxr7");
				
				TwitterFactory tf = new TwitterFactory(configurationBuilder.build());
				twitter4j.Twitter twitter = tf.getInstance();
				
				List<Status> status = null;
				try {
					status = twitter.getHomeTimeline();
				} catch (TwitterException e) {
					e.printStackTrace();
				}
				for(Status s:status) {
					sendMessage(channel, sender+(s.getUser().getName()+"      " + s.getText()));
			}
		}
		if(message.equalsIgnoreCase("weather 75080")) {
			try {
				sendMessage(channel, startWebRequest("dallas"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    }
    
       
    String startWebRequest(String city) throws IOException
    {
        String weatherURL = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&APPID=9b3f816b60d19f5e2f8b65fca37e91fb"; //apptoken
           
          StringBuilder result = new StringBuilder(); //holds the JSON Response from server
          URL url = new URL(weatherURL);
          HttpURLConnection conn = (HttpURLConnection) url.openConnection();
          conn.setRequestMethod("GET");
          BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
          String line;
          while ((line = rd.readLine()) != null) {
             result.append(line);
          }
          rd.close();
 
         double minTemp =  parseMinimumTempurature(result.toString());
         double maxTemp = parseMaximumTempurature(result.toString());
         double curTemp = parseTemp(result.toString());
         DecimalFormat df = new DecimalFormat("#.##");
         String out = "Current Temperature: " + df.format(curTemp) + "\u00b0F Minimum Temperature: " + df.format(minTemp) + "\u00b0F Maximum Temperature: " +df.format(maxTemp) + "\u00b0F";
         
          return out;
       }
       
        static double parseJson(String json) //json is a string of json, we get this from making our request
        {
            JsonElement jelement = new JsonParser().parse(json); //you will parse it first into a JSONElement
            JsonObject  MasterWeatherObject = jelement.getAsJsonObject();  //You will then take that jelement, and then break it down into a json object. Use the JSONEDITORONLINE website, basically, you are trying narrow down to whatever you want
           
            JsonObject  coordinateObject = MasterWeatherObject.getAsJsonObject("coord"); //we will get the coordinate object
            double  longitude = coordinateObject.get("lon").getAsDouble(); //now we will narrow down to get the value of the longitute
            return longitude;  //return our longitude
          
           
        }
        
        static double parseMinimumTempurature(String json)
        {    
            JsonElement jelement = new JsonParser().parse(json);    
            JsonObject MasterWeatherObject = jelement.getAsJsonObject();    
            JsonObject mainObject = MasterWeatherObject.getAsJsonObject("main");    
            double minTemp = mainObject.get("temp_min").getAsDouble();    
            double minTempF = minTemp * 9/5 - 459.67;    //farenheit formula
            Double minT = minTempF;    
            return minTempF;    
        }
        
        static double parseMaximumTempurature(String json)
        {    
            JsonElement jelement = new JsonParser().parse(json);    
            JsonObject MasterWeatherObject = jelement.getAsJsonObject();    
            JsonObject mainObject = MasterWeatherObject.getAsJsonObject("main");    
            double minTemp = mainObject.get("temp_max").getAsDouble();    
            double minTempF = minTemp * 9/5 - 459.67;    // farenheit formula
            Double minT = minTempF;    
            return minTempF;    
        }
        
        static double parseTemp(String json)
        {    
            JsonElement jelement = new JsonParser().parse(json);    
            JsonObject MasterWeatherObject = jelement.getAsJsonObject();    
            JsonObject mainObject = MasterWeatherObject.getAsJsonObject("main");    
            double minTemp = mainObject.get("temp").getAsDouble();    
            double minTempF = minTemp * 9/5 - 459.67;  //farenheit formula   
            Double minT = minTempF;    
            return minTempF;    
        }
           
     }
