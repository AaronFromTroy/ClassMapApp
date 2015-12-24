package sample;

import java.awt.*;
import java.io.*;
import java.sql.*;
import javax.swing.*;
import java.util.concurrent.Semaphore;
import java.util.*;

import javafx.scene.image.Image;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DataConnection {

    static int userID;
    static Semaphore counting = new Semaphore(3);
    static ArrayList<MapNode> collection = new ArrayList<>();
    static User loggedUser; //ALL LOGGED IN USER INFORMATION WILL APPEAR HERE
    static String token = null;
    static ArrayList<String> htmlList = new ArrayList<>();
    static ArrayList<String> topicNameList = new ArrayList<>();
    static Boolean getTopic = Boolean.FALSE;

    public static Boolean getTopic()
    {
        return getTopic;
    }

    public static void setTopic()
    {
        getTopic = Boolean.TRUE;
    }

    private static Connection dbConnector() {
        Connection conn = null;
        try {
            final String url = "jdbc:mysql://107.180.20.80/ClassMapApp";
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, "ClassMaster", "ClassMap1");
            //JOptionPane.showMessageDialog(null, "Connection Successful");
            return conn;

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, "Failed to Establish Connection!");
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
    login function should be handled like so
    while false, repeat login
     */
    public static boolean login(String user, String pass) {
        Connection conn = dbConnector();
        String query = "select * from members where username=? and password=? " ;
        try
        {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, user);
            pst.setString(2, pass);

            ResultSet rst = pst.executeQuery();
            if(rst.next()) {
                JOptionPane.showMessageDialog(null, "Found User!");
                User newloggedUser = new User(rst.getString("first_name"), rst.getString("last_name"), rst.getString("username"),
                        rst.getInt("id"), rst.getTimestamp("log_out"), rst.getString("account_permissions"));
                loggedUser = newloggedUser;
                return true;
            }
            else {

                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Incorrect Login. Try again.");
            return false;
        }
    }

    public static void getToken() {
        Auth authToken = new Auth("acousticguy9.teacher@yahoo.com", "ed26fLi4");
        token = authToken.getToken();
        System.out.println(token);

        String data = JsonExtract.getJson("https://api.learningstudio.com/courses/12468893/threadeddiscussions");

        JSONObject pearsonThread;
        try
        {
            pearsonThread =  (JSONObject) JSONValue.parseWithException(data);
        } catch (ParseException e) {
            e.printStackTrace();
            pearsonThread = null;
        }
        if(pearsonThread != null)
        {
            JSONArray threadedDiscussion = (JSONArray) pearsonThread.get("threadedDiscussions");

            for(int x = 0; x < threadedDiscussion.size(); x++)
            {
                JSONObject temp = (JSONObject) threadedDiscussion.get(x);
                long id = (long) temp.get("id");
                String html = (String) temp.get("introductoryText");
                htmlList.add(html);

                String topics = JsonExtract.getJson("https://api.learningstudio.com/courses/12468893/threadeddiscussions/"+id+"/topics");

                JSONObject topicObject;
                try
                {
                    topicObject = (JSONObject) JSONValue.parseWithException(topics);
                } catch (ParseException e) {
                    e.printStackTrace();
                    topicObject = null;
                }

                if(topicObject != null)
                {
                    JSONArray topicArray = (JSONArray) topicObject.get("topics");
                    JSONObject topicTemp = (JSONObject) topicArray.get(0);
                    String topicName = topicTemp.get("title").toString();
                    topicName = topicName.replace("&#58;",":");
                    topicNameList.add(topicName);

                }
            }
        }


//        StringBu
//        StringBuffer newBuff = new String(stuff);
//        char[] stuff2 = new CharArrayReader(stuff);

    }

    /*
    Gets all node information from database.
    Returns the root MapNode.
     */
    public static MapNode populate() throws InterruptedException {
        Connection conn = dbConnector();
        String query = "Select * from nodes";
        String query2 = "Select * from node_votes where id=?";
        try {

//            PreparedStatement ps = conn.prepareStatement(query2);
//            ps.setInt(1, loggedUser.getId());
//            ResultSet rst = ps.executeQuery();
//            while(rst.next()) {
//                if (rst.getBoolean("vote") == true) {
//
//                }
//            }


            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                if (new String(rs.getString("type").toString()).equals("string")) {
                    collection.add(new TextNode(rs.getInt("id"), rs.getInt("parent_id"),
                            rs.getString("string_data"), rs.getTimestamp("time_created")));
                }
                else if (new String(rs.getString("type").toString()).equals("image")) {
                    collection.add(new ImageNode(rs.getInt("id"), rs.getInt("parent_id"), rs.getTimestamp("time_created")));
                    counting.acquire();
                    loadImg((collection.size() - 1), rs.getInt("id"));
                }
            }


            for (int i = 0; i < collection.size(); i++) {
                for (int y = 0; y < collection.size(); y++) {
                    if (collection.get(y).parent == collection.get(i).uniqueId) {
                        collection.get(i).children.add(collection.get(y));
                    }
                }
            }

            while (counting.availablePermits() < 3) {

            }
            for (int i = (collection.size() - 1); i > 0; i--) {
                collection.remove(i);
            }
            rs.close();
            pst.close();
            return collection.get(0);

        } catch (SQLException e) {
            System.out.println(e);

            JOptionPane.showMessageDialog(null, "Errors Occured Building Map!");
            return null;
        }

    }

    /*
    Sends created node information to Database
     */
    public static void addTextNode(TextNode node) {
        Connection conn = dbConnector();
        String query = "insert into nodes (parent_id, string_data, type, created_by) " + " values(?,?,?,?) ";
        String query2 = "SELECT id FROM nodes WHERE created_by=? and string_data=?";
        int id = -1;
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, node.parent);
            ps.setString(2, node.getContents());
            ps.setString(3, "String");
            ps.setString(4, loggedUser.getUser());
            ps.executeUpdate();

            PreparedStatement pst = conn.prepareStatement(query2);
            pst.setString(1, loggedUser.getUser());
            pst.setString(2, node.getContents());
            ResultSet rs = pst.executeQuery();
            if(rs.next()) {
                id = rs.getInt("id");
                node.setUniqueId(id);
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    public static void addImageNode(ImageNode node) {
        Connection conn =  dbConnector();
        String query = "insert into nodes (parent_id, string_data, type, created_by)" + "values(?,?,?,?)";
        String query2 = "select id from nodes where string_data=? and type='image'";
        String query3 = "insert into images (id, stored_image)" + "values(?, ?)";
        int id = -1;

        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, node.getParent());
            ps.setString(2, node.getFormattedDate() + loggedUser.getUser());
            ps.setString(3, node.getType().toString());
            ps.setString(4, loggedUser.getUser());
            ps.executeUpdate();
            ps.close();

            PreparedStatement pst = conn.prepareStatement(query2);
            pst.setString(1, node.getFormattedDate() + loggedUser.getUser());
            ResultSet rs = pst.executeQuery();
            if(rs.next()) {
                id = rs.getInt("id");
                node.setUniqueId(id);
            }
            rs.close();
            pst.close();

            PreparedStatement ps2 = conn.prepareStatement(query3);
            ps2.setInt(1, id);
            ps2.setBytes(2, node.imageToByteArray());
            ps2.executeUpdate();
            ps2.close();
            conn.close();
        } catch (Exception E) {
            JOptionPane.showMessageDialog(null, "Error");
        }
    }

    /*
    Determines if user has voted for the incoming node and adjust values
    in database accordingly.
     */
    public static void addUpvote(MapNode node) {
        Connection conn = dbConnector();
        String query = null;
        if(node.getUserVote() == false) {
            query = "UPDATE nodes SET votes = votes + 1 WHERE id=?";
        }
        else {
            query = "UPDATE nodes SET votes = votes - 1 WHERE id=?";
        }
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, node.uniqueId);
            ps.executeUpdate();
            ps.close();
            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Didnt Work Sorry!");
        }
    }

    public static void addMember(String email, String username, String password, String first_name,
                                 String last_name, String accountperms) {
        Connection conn = dbConnector();
        try {
            String query = "insert into members (email, username, password, first_name, last_name) " + " values(?,?,?,?,?) ";

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, username);
            ps.setString(3, password);
            ps.setString(4, first_name);
            ps.setString(5, last_name);

            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void deleteMember(String username) {
        Connection conn = dbConnector();
        String query = "delete from members where username=?"; //put in number for ? and take out ps.setInt(); to make it work also
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, username); //set id for the second number
            ps.executeUpdate();
            System.out.println("Deleted record...");
            ps.close();
            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "User could not be deleted.");
        }
    }

    public static void deleteNode(MapNode node) {
        Connection conn = dbConnector();
        String query = "delete from nodes where id=?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, node.uniqueId);
            ps.executeUpdate();
            ps.close();
            if (node.getType() == "Image") {
                String query2 = "delete from images where id=?";
                try {
                    PreparedStatement pst = conn.prepareStatement(query2);
                    pst.setInt(1, node.uniqueId);
                    pst.executeUpdate();
                    pst.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    Used with populate to grab images from the database.
     */
    private static void loadImg(int id, int unique) {

        class Minion extends Thread {
            Connection conn = null;
            int minionId;
            int uniqueid;

            public Minion(int id) {
                minionId = id;
                uniqueid = unique;
                conn = dbConnector();
            }

            @Override
            public void run() {
                String query = "select * from images where id=?";
                //BufferedImage image = null;
                //InputStream fis = null;
                try {
                    PreparedStatement pst = conn.prepareStatement(query);
                    pst.setInt(1, uniqueid);
                    ResultSet rs = pst.executeQuery();

                    while(rs.next()) {
                        System.out.println("GOOD RIGHT NOW");
                        Blob blob = rs.getBlob("stored_image");
                        ByteArrayInputStream in = new ByteArrayInputStream
                                (blob.getBytes(1, (int) blob.length()));
                        Image im = new Image(in);
                        ((ImageNode) (collection.get(id))).setImage(im);
                        ((ImageNode) (collection.get(id))).drawNode();
                        System.out.println("GOOD SO FAR!");
                    }

                    pst.close();
                    rs.close();
                    conn.close();
                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, "Failed to load img at ID: " + minionId);
                    counting.release();

                }

                counting.release();
            }
        }

        Minion thread = new Minion(id);
        thread.start();

    }

}