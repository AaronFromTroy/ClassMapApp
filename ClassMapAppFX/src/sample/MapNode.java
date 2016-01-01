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
 * @author Aaron Martin
 */
public class MapNode {

    public enum classification {
        image, link, string
    }

    public int uniqueId;
    public int votes;
    private boolean userVote = false;
    public String createdBy;
    public String nodePerm;
    public ArrayList<MapNode> children = new ArrayList<>();
    public int parent;
    boolean sinceLastLog;
    public classification type;
    public Timestamp timeCreated;

    public Boolean getUserVote()
    {
        return userVote;
    }

    public void setUniqueId(int id) { this.uniqueId = id; }

    public void setUserVote(Boolean value) { this.userVote = value; }

    public void incrementVoteCounter() { this.votes++; }

    public void decrementVoteCounter() { this.votes--; }

    public int getVotes()
    {
        return this.votes;
    }

    public int getParent()
    {
        return this.parent;
    }

    public void AddNode(MapNode addition)
    {
        this.children.add(addition);
    }

    public long getSeconds()
    {
        return this.timeCreated.getTime();
    }

    public String getType() { return this.type.toString(); }

}

