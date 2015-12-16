
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.*;
import java.sql.*;
/**
 *
 * @author acous
 */
public class ClassMapApp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {



        ArrayList<MapNode> map = new ArrayList<MapNode>();
        buildMap(map);
        System.out.println(map.size());

        DictParser dictionary = new DictParser();

        dictionary.searchForWord("porch");

        String firstResult = dictionary.getIndex(0); //This returns the string for the first result that the dictionary finds.
        System.out.println(firstResult);

        NodeRender out = new NodeRender();
        out.generateNode();

        //dictionary.printResults();
    }
    
    
    static void buildMap(ArrayList<MapNode> collection) {
        try
        {   
            Connection conn = DriverManager.getConnection("jdbc:ucanaccess://C://Users//clink_000//Desktop//Students.accdb/");
            
            Statement st = conn.createStatement();
            
            String sql = "Select * from MapNodes";
            
            //All information below is completely dependent on how database is set up.
            ResultSet rs = st.executeQuery(sql); 
            collection.add(new MapNode(rs.getString(3), rs.getInt(1), rs.getInt(2)));
            
            while(rs.next())
            {
                //Again all this information is dependent on database.
                collection.add(new MapNode(rs.getString(3), rs.getInt(1), rs.getInt(2)));
            }
            
            sortChildren(collection);
        }
        catch (Exception e) 
        {
            System.out.println(e);
        }
    }
    
    static void sortChildren(ArrayList<MapNode> collection) {
        for (int i = 1; i < collection.size(); i++) {
            if (collection.get(i).parent == collection.get(0).uniqueId) {
                collection.get(0).children.add(collection.get(i));
            }
            
        }
        
        for (int i = 1; i < collection.size(); i++) {
            for (int y = 1; y < collection.size(); i++) {
                    if(collection.get(y).parent == collection.get(i).uniqueId) {
                        collection.get(i).children.add(collection.get(y));
                    }
                }
        }
        
        while (collection.size() > 1) {
            collection.remove(1);
        }
    }
}