/*
 * MapNode Class.
 * Stores all information for each node in the database.
 */

import javax.swing.*;
import java.util.*;
import java.sql.*;

/**
 *
 * @author acous
 */
public class MapNode {



    private JButton upVoteButton;
    private JFormattedTextField formattedTextField1;
    private JTextArea textArea1;

    public enum classification {
        Image, Link, String
    }
    
    public int votes;
    private boolean userVote;
    public ArrayList<MapNode> children = new ArrayList<MapNode>();
    String data;
    public int uniqueId;
    public int parent;
    boolean sinceLastLog;
    public classification type; 
    
    public MapNode() {
        
    }
    
    public MapNode(String in, int id, int pid) {
        
        uniqueId = id;
        parent = pid;
        data = in;
        if (in.contains("http://")) {
            this.type = classification.Link;
        }
        // If else for image will go here.
        else {
            this.type = classification.String;
        }
     
    }
    
}
