package sample;

import java.awt.HeadlessException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.*;
import javax.swing.*;
import java.util.concurrent.Semaphore;
import java.util.*;

public class DataConnection {

    static int userID;
    static Semaphore counting = new Semaphore(3);
    static ArrayList<MapNode> collection = new ArrayList<>();

    private static Connection dbConnector() {
        try {
            final String url = "jdbc:mysql://107.180.20.80/ClassMapApp";
            Connection conn = DriverManager.getConnection(url, "ClassMapMaster", "PearsonComp1");
            JOptionPane.showMessageDialog(null, "Connection Successful");
            return conn;

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, "Failed to Establish Connection!");
            return null;
        }
    }

    /*
    login function should be handled like so
    while false, repeat login
     */
    public static boolean login(String username, String password) {
        Connection conn = dbConnector();
        try {
            String query = "Select * from members where username=? and password=? ";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, username);
            pst.setString(2, password);
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Incorrect Login. Try again.");
            return false;
        }
    }

    /*
    Gets all node information from database.
    Returns the root MapNode.
     */
    public static MapNode populate() throws InterruptedException {
        Connection conn = dbConnector();
        String query = "Select * from nodes";
        try {
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                if (rs.getString("type") == "string") {
                    collection.add(new TextNode(rs.getInt("id"), rs.getInt("parent_id"),
                            rs.getString("string_date"), rs.getTimestamp("time_created")));
                }
//                else if (rs.getString("type") == "image") {
//                    collection.add(new ImageNode(rs.getInt("id"), rs.getInt("parent_id"),
//                        rs.getString("string_date"), rs.getBoolean("img_data"), rs.getTimestamp("time_created")));
//                    counting.acquire();
//                    loadImg(collection.size() - 1);
//                }
            }

            for (int i = 0; i < collection.size(); i++) {
                for (int y = 0; y < collection.size(); y++) {
                    if (collection.get(y).parent == collection.get(i).uniqueId) {
                        collection.get(i).children.add(collection.get(y));
                    }
                }
            }

            while (collection.size() > 1) {

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

    public static void addTextNode(TextNode node) {
        Connection conn = dbConnector();
        String query = "insert into nodes (parent_id, string_data, type) " + " values(?,?,?) ";
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, node.parent);
            ps.setString(2, node.getContents());
            ps.setString(3, "string");
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    public static void addMember(String email, String username, String password, String first_name,
                                 String last_name) {
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
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "User could not be deleted.");
        }
    }

    public static void saveImage(String file) {
        Connection conn = dbConnector();
        PreparedStatement ps;
        FileInputStream fis;

        try {
            // create a file object for image by specifying full path of image as parameter.
            File image = new File(file);

            /* prepareStatement() is used for create statement object that is
            used for sending sql statements to the specified database. */
            ps = conn.prepareStatement("insert into images (id, stored_image) " + " values(?,?) ");

            ps.setInt(1, 3);

            fis = new FileInputStream(image);
            ps.setBinaryStream(2, (InputStream) fis, (int) (image.length()));

            /* executeUpdate() method execute specified sql query. Here this query
            insert data and image from specified address.*/
            int s = ps.executeUpdate();

            if (s > 0) {
                System.out.println("Uploaded successfully!");
            } else {
                System.out.println("Failed to upload image.");
            }
        } catch (SQLException | FileNotFoundException ex) {
            System.out.println("Found some error : " + ex);
        }
    }

    /*
    Used with populate to grab images from the database.
     */
//    private static void loadImg(int id) {
//
//        class Minion extends Thread {
//
//            Connection conn = dbConnector();
//            int minionId;
//
//            public Minion(int id) {
//                minionId = id;
//            }
//
//            @Override
//            public void run() {
//                String query = "select * from images where id=" + "'" + minionId + "'";
//                try {
//                    PreparedStatement pst = conn.prepareStatement(query);
//                    ResultSet rs = pst.executeQuery();
//                    int count = 0;
//                    while (rs.next()) {
//                        count++;
//                    }
//                    if (count == 1) {
//                        collection.get(minionId).image = rs.getBytes("stored_image");
//                    } else if (count > 1) {
//                        JOptionPane.showMessageDialog(null, "Multiple rows by ID. Error Occured.");
//                    } else {
//                        JOptionPane.showMessageDialog(null, "No image available. Error Occured.");
//                    }
//                    pst.close();
//                    rs.close();
//                    conn.close();
//                } catch (SQLException | HeadlessException e) {
//                    JOptionPane.showMessageDialog(null, "Failed to load img at ID: " + minionId);
//                    counting.release();
//
//                }
//
//                    counting.release();
//            }
//        }
//
//        Minion thread = new Minion(id);
//        thread.start();
//
//    }

//    private static String parseFile() {
//
//    }

}