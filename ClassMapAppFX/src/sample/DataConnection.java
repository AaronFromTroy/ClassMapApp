package sample;

import java.awt.HeadlessException;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import javax.swing.*;
import java.util.concurrent.Semaphore;
import java.util.*;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class DataConnection {

    static int userID;
    static Semaphore counting = new Semaphore(3);
    static ArrayList<MapNode> collection = new ArrayList<>();

    private static Connection dbConnector() {
        Connection conn = null;
        try {
            final String url = "jdbc:mysql://107.180.20.80/ClassMapApp";
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, "amartin125971", "kjedi285");
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
            int count = 0;
            while(rst.next()) {
                count++;
            }
            if(count == 1) {
                JOptionPane.showMessageDialog(null, "Found User!");
                return true;
            }
            else {
                JOptionPane.showMessageDialog(null, "More Than One User By That Username.");
                return false;
            }
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
                else if (rs.getString("type") == "image") {
                    collection.add(new ImageNode(rs.getInt("id"), rs.getInt("parent_id"), rs.getTimestamp("time_created")));
                    counting.acquire();
                    loadImg(collection.size() - 1);
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

    public static void addTextNode(TextNode node) {
        Connection conn = dbConnector();
        String query = "insert into nodes (parent_id, string_data, type) " + " values(?,?,?) ";
        try
        {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, node.parent);
            ps.setString(2, node.getContents());
            ps.setString(3, "String");
            ps.executeUpdate();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    public static void addImageNode(ImageNode node) {
        Connection conn =  dbConnector();
        String query = "insert into nodes (parent_id, string_data, type)" + "values(?,?,?)";
        String query2 = "select id from nodes where string_data=? and type='image'";
        String query3 = "insert into images (id, stored_image)" + "values(?, ?)";
        int id;

        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, node.getParent());
            ps.setString(2, node.getFormattedDate());
            ps.setString(3, node.getType().toString());
            ps.executeUpdate();
            ps.close();

            PreparedStatement pst = conn.prepareStatement(query2);
            pst.setString(1, node.getFormattedDate());
            ResultSet rs = pst.executeQuery();
            rs.next();
            id = rs.getInt("id");
            rs.close();
            pst.close();

            PreparedStatement ps2 = conn.prepareStatement(query3);
            ps2.setInt(1, id);
            ps2.setBytes(2, node.imageToByteArray());
            ps2.executeUpdate();
            ps2.close();
            conn.close();
        } catch (Exception E) {
            JOptionPane.showMessageDialog(null, "Happened at 1");
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
    private static void loadImg(int id) {

        class Minion extends Thread {
            Connection conn = null;
            int minionId;

            public Minion(int id) {
                minionId = id;
                conn = dbConnector();
            }

            @Override
            public void run() {
                String query = "select * from images where id=" + "'" + minionId + "'";
                BufferedImage image = null;
                InputStream fis = null;
                try {
                    PreparedStatement pst = conn.prepareStatement(query);
                    ResultSet rs = pst.executeQuery();
                    fis = rs.getBinaryStream("stored_img");
                    image = javax.imageio.ImageIO.read(fis);
                    Image im = SwingFXUtils.toFXImage(image, null);
                    ((ImageNode)(collection.get(id))).setImage(im);
                    ((ImageNode)(collection.get(id))).drawNode();
                    pst.close();
                    rs.close();
                    conn.close();
                } catch (SQLException | HeadlessException | IOException e) {
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