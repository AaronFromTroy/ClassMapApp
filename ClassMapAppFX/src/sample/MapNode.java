package sample;/*
 * MapNode Class.
 * Stores all information for each node in the database.
 */

import javafx.scene.Node;
import javafx.scene.image.Image;
import java.awt.Image.*;

import javax.swing.*;
import java.time.LocalDateTime;
import java.util.*;
import java.sql.*;
import java.util.Date;

/**
 *
 * @author acous
 */
public class MapNode {

    public enum classification {
        Image, Link, String
    }

    public int uniqueId;
    public int votes;
    private boolean userVote;
    public ArrayList<MapNode> children = new ArrayList<MapNode>();
    public int parent;
    boolean sinceLastLog;
    public classification type;
    public Timestamp timeCreated;

    public Boolean getUserVote()
    {
        return userVote;
    }


    public int getVotes()
    {
        return this.votes;
    }

//    public int getUniqueId()
//    {
//        return this.uniqueId;
//    }

    public int getParent()
    {
        return this.parent;
    }

    public void AddNode(MapNode addition)
    {
        this.children.add(addition);
    }

//    //public void setTime()
//    {
//        timeCreated = new Timestamp();
//    }

    public long getSeconds()
    {
        return this.timeCreated.getTime();
    }

    public String getType() { return this.type.toString(); }

}

