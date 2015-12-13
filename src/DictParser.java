/**
 * Created by James Davis on 12/10/2015.
 */

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



public class DictParser{

    long amount;
    List list = Collections.synchronizedList(new ArrayList<>());
    void findWord(String word) {
        String urlString = "http://api.pearson.com/v2/dictionaries/ldoce5/entries?headword=" + word;
        JSONObject pearsonObject = createJSONfromURL(urlString);

        amount = (long) pearsonObject.get("count");
        list.add(0,amount);

        JSONArray pearsonResultArray = (JSONArray) pearsonObject.get("results");


        for (int x = 0; x < amount; x++)
        {
            JSONObject pearsonResult = (JSONObject) pearsonResultArray.get(x);

            //Gets the associated headword
            String headword = (String) pearsonResult.get("headword");

            //Part of Speech
            String partOfSpeech = (String) pearsonResult.get("part_of_speech");
            if(partOfSpeech == null)
            {
                partOfSpeech = "There is no given part of speech";
            }

            /*
            String to hold the definition or the example, the definition for the case of
            a noun
            */
            JSONArray senses = (JSONArray) pearsonResult.get("senses");

            String defOrExample;
            if(senses.get(0) == null)
            {
                defOrExample = "There is no given definition or example for the given word.";
            }
            else
            {
                JSONObject def = (JSONObject) senses.get(0);
                defOrExample = def.get("definition").toString();
                defOrExample = defOrExample.replace("[","");
                defOrExample = defOrExample.replace("]","");
            }

            String fullString = "Headword: " + headword + "\nPart of Speech: " + partOfSpeech + "\nDefinition: " + defOrExample;
            list.add(x+1,fullString);

        }
    }

    JSONObject createJSONfromURL(String url)
    {
        try{
            String getURL = IOUtils.toString(new URL(url));
            JSONObject pObject = (JSONObject) JSONValue.parseWithException(getURL);
            return pObject;

        } catch(IOException | ParseException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    void printResults()
    {
        long count = (long) list.get(0);
        for(int x = 1; x < count+1; x++)
            System.out.println(list.get(x) + "\n");
    }

    String getEnrty(int entry)
    {
        String retVal;
        if(entry < list.size() - 1)
        {
            retVal = list.get(entry + 1).toString();
        }
        else
        {
            System.err.println("Entry out of range");
            retVal = "\nNo entry";
        }

        return retVal;
    }

}
