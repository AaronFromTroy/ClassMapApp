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
        image, link, string
    }

    public int uniqueId;
    public int votes;
    private boolean userVote;
    public ArrayList<MapNode> children = new ArrayList<>();
    public int parent;
    boolean sinceLastLog;
    public classification type;
    public Timestamp timeCreated;

    public Boolean getUserVote()
    {
        return userVote;
    }

    public void setUserVote(Boolean value) { this.userVote = value; }

    public void incrementVoteCounter() { this.votes++; }

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

