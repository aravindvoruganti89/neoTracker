package com.test;


import com.test.Objects.NearEarthObject;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Properties;

/**
 * Created by avorugan on 8/25/17.
 */
public class ExecuteNeoTracker {

    public static final Logger executeNeoTrackerLogger = Logger.getLogger("ExecuteNeoTracker");
    public String neoBaseDir = null;
    public Client neoRestClient;
    public WebTarget neoWebTarget;
    public String neoRestURI = "https://api.nasa.gov/neo/rest/v1";
    public WebTarget neoFeedTarget;
    public WebTarget neoLookupTarget;
    public WebTarget neoStatsTarget;
    public WebTarget neoBrowseTarget;
    public JSONParser jsonParser = new JSONParser();
    public SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public HashMap<String, NearEarthObject> nearEarthObjects = new HashMap<String, NearEarthObject>();
    public JSONObject neoSettingsObj = new JSONObject();
    ObjectMapper mapper = new ObjectMapper();

    ExecuteNeoTracker (String baseDir){
        neoBaseDir = baseDir;
    }

    // use this method to set all the necessary configs of the application to run
    public boolean setConfigs(){
        try{
            executeNeoTrackerLogger.info("Setting the NEO tracker configs");
            neoSettingsObj = (JSONObject) jsonParser.parse(new FileReader(neoBaseDir + File.separator + "conf" + File.separator + "neoSettings.json"));
            neoRestClient = ClientBuilder.newClient();
            neoWebTarget = neoRestClient.target(neoRestURI);

            // get the total NEO count
            executeStatsApi();

            //feed API for today
            executeFeedApi();

            if (Boolean.parseBoolean(neoSettingsObj.get("closestNeoToday").toString()))
                findNearestNEO();

            if (Boolean.parseBoolean(neoSettingsObj.get("largestNeoToday").toString()))
                findLargestNeo();


            //browse API
            if (Boolean.parseBoolean(neoSettingsObj.get("largestNeoAllTime").toString())){
                executeBrowseApi();
                findLargestNeo();
            }

        }catch(Exception e){
            executeNeoTrackerLogger.error("Problem reading configs ",e);
        }
        return true;
    }

    public boolean executeStatsApi(){
        try{
            neoStatsTarget = neoWebTarget.path("stats").queryParam("api_key", neoSettingsObj.get("api_key"));
            executeNeoTrackerLogger.info("Invoking the HTTP GET request: "+neoStatsTarget);

            Invocation.Builder invocationBuilder = neoStatsTarget.request(MediaType.APPLICATION_JSON_TYPE);
            Response response = invocationBuilder.get();

            String obj = response.readEntity(String.class);
            JSONObject responseObj = (JSONObject) jsonParser.parse(obj);

            if (responseObj.get("near_earth_object_count") == null){
                executeNeoTrackerLogger.error("Failed to get the near_earth_object_count...skipping this method");
                return  false;
            }

            int elementCount = Integer.valueOf(responseObj.get("near_earth_object_count").toString());
            executeNeoTrackerLogger.info("Total near_earth_object_count: "+elementCount);
        }catch(Exception e){
            executeNeoTrackerLogger.error("Problem executing the executeStatsApi ",e);
        }
        return true;
    }

    // this method executes the feed api to pull the NEO objects for today
    public boolean executeFeedApi(){
        try{
            jsonParser = new JSONParser();

            neoFeedTarget = neoWebTarget.path("feed").path("today").queryParam("detailed", "true").queryParam("api_key", neoSettingsObj.get("api_key"));

            executeNeoTrackerLogger.info("Invoking HTTP GET Request: "+neoFeedTarget);

            Invocation.Builder invocationBuilder = neoFeedTarget.request(MediaType.APPLICATION_JSON_TYPE);
            Response response = invocationBuilder.get();

            String obj = response.readEntity(String.class);
            JSONObject responseObj = (JSONObject)jsonParser.parse(obj);

            executeNeoTrackerLogger.debug("DEBUG enabled. Full response from Client: "+responseObj);

            int elementCount = Integer.valueOf(responseObj.get("element_count").toString());

            JSONObject allObjects = (JSONObject) responseObj.get("near_earth_objects");

            String date = simpleDateFormat.format(new Date());

            if (allObjects.get(date) == null){
                executeNeoTrackerLogger.error("Date "+date+" is not found in the response");
                return false;
            }

            JSONArray neoObjects = (JSONArray) allObjects.get(date);

            for (Object object : neoObjects){
                JSONObject neoObj = (JSONObject) object;
                NearEarthObject neo = mapper.readValue(neoObj.toString(), NearEarthObject.class);
                executeNeoTrackerLogger.info("Converting NEO object to NearEarthObject class..."+neo.getNeoName());
                nearEarthObjects.put(neo.getNeoName(), neo);
                executeNeoTrackerLogger.info("Adding "+neo.getNeoName()+" to the HashMap...Successful");
            }

        }catch(Exception e){
            executeNeoTrackerLogger.error("Problem executing the NEO today Feed API ",e);

        }
        return true;
    }

    public boolean findNearestNEO(){
        try{
            double lunarDistance = 0 , shortestNeoDistance = 0;
            NearEarthObject shortestNeo = null;
            executeNeoTrackerLogger.info("Finding the nearest NEO for today ["+simpleDateFormat.format(new Date())+"]");

            for (NearEarthObject neo : nearEarthObjects.values()){
                executeNeoTrackerLogger.info("NEO: "+neo.getNeoName()+" Lunar Distance: "+neo.getCloseApproachDatas().get(0).getMissDistance().get("lunar"));
                lunarDistance = Double.parseDouble(neo.getCloseApproachDatas().get(0).getMissDistance().get("lunar").toString());

                if (shortestNeoDistance == 0){
                    shortestNeoDistance = lunarDistance;
                    shortestNeo = neo;
                }

                if (lunarDistance < shortestNeoDistance){
                    shortestNeoDistance = lunarDistance;
                    shortestNeo = neo;
                }
            }

            executeNeoTrackerLogger.info("The Shortest NEO from Earth: "+shortestNeo.getNeoName()+" with lunar distance: "+shortestNeo.getCloseApproachDatas().get(0).getMissDistance().get("lunar"));
            JSONObject shortestNeoJson = (JSONObject) jsonParser.parse(mapper.writeValueAsString(shortestNeo));

            executeNeoTrackerLogger.info(shortestNeoJson);

        }catch(Exception e){
            executeNeoTrackerLogger.error("Problem finding the nearest NEO ",e);

        }
        return true;
    }



    public boolean executeBrowseApi(){
        try{
            executeNeoTrackerLogger.info("Cleaning up the NEO HashMap...neoEarthObjects");

            int pageCounter = 0; int totalPages = 0;
            neoBrowseTarget = neoWebTarget.path("neo").path("browse").queryParam("page",pageCounter).queryParam("size", 20).queryParam("api_key",neoSettingsObj.get("api_key"));

            executeNeoTrackerLogger.info("Executing the HTTP GET NEO browse API: "+neoBrowseTarget);
            Invocation.Builder invocationBuilder = neoBrowseTarget.request(MediaType.APPLICATION_JSON_TYPE);
            Response response = invocationBuilder.get();

            String obj = response.readEntity(String.class);
            JSONObject responseObj = (JSONObject) jsonParser.parse(obj);

            //check if the object contains the "page section"
            executeNeoTrackerLogger.info("Looking for the page object");

            if (responseObj.containsKey("page")){
                JSONObject pageObj = (JSONObject) responseObj.get("page");
                totalPages = Integer.parseInt(pageObj.get("total_pages").toString());
                executeNeoTrackerLogger.info("Total pages: "+totalPages);
            }else{
                executeNeoTrackerLogger.error("Page object is not found in the response. Something is wrong..exiting");
                return false;
            }


            //now iterate over all the pages

            for (pageCounter = 0; pageCounter < totalPages; pageCounter++){
                neoBrowseTarget = neoWebTarget.path("neo").path("browse").queryParam("page",pageCounter).queryParam("size", 20).queryParam("api_key", neoSettingsObj.get("api_key"));
                executeNeoTrackerLogger.info("Retrieving Page: "+pageCounter+" "+neoBrowseTarget);

                invocationBuilder = neoBrowseTarget.request(MediaType.APPLICATION_JSON_TYPE);
                response = invocationBuilder.get();

                obj = response.readEntity(String.class);
                responseObj = (JSONObject) jsonParser.parse(obj);

                JSONArray neoObjects = (JSONArray) responseObj.get("near_earth_objects");

                for (Object object : neoObjects){
                    JSONObject neoObj = (JSONObject) object;
                    NearEarthObject neo = mapper.readValue(neoObj.toString(), NearEarthObject.class);
                    executeNeoTrackerLogger.info("Converting NEO object to NearEarthObject class..."+neo.getNeoName());
                    nearEarthObjects.put(neo.getNeoName(), neo);
                    executeNeoTrackerLogger.info("Adding "+neo.getNeoName()+" to the HashMap...Successful");
                }
            }


        }catch(Exception e){
            executeNeoTrackerLogger.error("Failed to execute the NEO browse API ",e);
        }
        return true;
    }

    public boolean findLargestNeo(){
        try{
            double diameter = 0; double largestDiameter = 0;
            NearEarthObject largestNearEarthNeo = null;

            executeNeoTrackerLogger.info("Finding the larget NEO ");

            for (NearEarthObject neo : nearEarthObjects.values()){

                diameter = Double.parseDouble( ((LinkedHashMap)neo.getEstimatedDiameter().get("kilometers")).get("estimated_diameter_max").toString() );

                executeNeoTrackerLogger.info("NEO: "+neo.getNeoName()+" Max. Diameter: "+neo.getEstimatedDiameter().get("kilometers"));

                if (largestDiameter == 0){
                    largestDiameter = diameter;
                    largestNearEarthNeo = neo;
                }

                if (diameter > largestDiameter){
                    largestDiameter = diameter;
                    largestNearEarthNeo = neo;
                }
            }

            executeNeoTrackerLogger.info("Largest NEO: "+largestNearEarthNeo.getNeoName()+" Diameter: "+largestDiameter);
            JSONObject largestNeoJson = (JSONObject) jsonParser.parse(mapper.writeValueAsString(largestNearEarthNeo));

            executeNeoTrackerLogger.info(largestNeoJson);


        }catch(Exception e){
            executeNeoTrackerLogger.error("Problem finding the larget NEO ",e);
        }
        return true;
    }


    public static void main(String args[]){
        try{
            Properties props = new Properties();
            props.load(new FileInputStream(args[0] + File.separator + "conf" + File.separator + "log4j.properties"));
            props.setProperty("log.dir",args[0]);
            PropertyConfigurator.configure(props);

            ExecuteNeoTracker executeNeoTracker = new ExecuteNeoTracker(args[0]);
            executeNeoTracker.setConfigs();

        }catch(Exception e){
            executeNeoTrackerLogger.error("NeoTracker Failed ",e);
        }
    }
}
