import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

/**
 * Created by James Davis on 12/13/2015.
 */
public class DictParser {

    private int count;
    private List list = Collections.synchronizedList(new ArrayList());

    public void searchForWord(String word)
    {
        String url = "http://api.pearson.com/v2/dictionaries/ldoce5/entries?headword=" + word;

        JSONObject pearsonObject = getJSONfromURL(url);
        if(pearsonObject == null)
        {
            System.err.println("Failed to load URL");
        }

        long amount = (long) pearsonObject.get("count");
        count = (int) amount;


        JSONArray resultArray = (JSONArray) pearsonObject.get("results");

        for(int x = 0; x < amount; x++)
        {
            JSONObject result = (JSONObject) resultArray.get(x);

            String headword = result.get("headword").toString();

            String partOfSpeech;
            if(result.get("part_of_speech") != null)
            {
                partOfSpeech = result.get("part_of_speech").toString();
            }
            else {
                partOfSpeech = "No part of speech given";
            }

            JSONArray senseArray = (JSONArray) result.get("senses");
            JSONObject def = (JSONObject) senseArray.get(0);
            String definition = def.get("definition").toString();
            definition = definition.replace("[","");
            definition = definition.replace("]","");

            String finalString = "Headword: " + headword + "\nPart of Speech: " + partOfSpeech + "\nDefinition: " + definition;
            list.add(x,finalString);



        }




    }

    public String getIndex(int index)
    {
        if(index  >= list.size())
        {
            return "Index out of range";
        }
        return (String) list.get(index);
    }

    public int getCount()
    {
        return count;
    }

    public void printResults()
    {
        for(int x = 0; x < list.size(); x++)
        {
            System.out.println(list.get(x) + "\n");
        }
    }

    private JSONObject getJSONfromURL(String url)
    {
        JSONObject pObject = null;
        try{
            String JSONString = IOUtils.toString(new URL(url));
            pObject = (JSONObject) JSONValue.parseWithException(JSONString);
        }catch(IOException | ParseException e)
        {
            e.printStackTrace();
        }

        return pObject;
    }
}
