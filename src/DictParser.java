/**
 * Created by James Davis on 12/10/2015.
 */

import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;



public class DictParser{
    DictParser(String word)
    {
        String urlString = "http://api.pearson.com/v2/dictionaries/ldoce5/entries?headword=" + word;
        JSONObject pearsonObject = createURL(urlString);

    }

    JSONObject createURL(String url)
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

}
