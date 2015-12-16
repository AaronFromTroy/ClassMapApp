import javax.swing.*;

/**
 * Created by James Davis on 12/15/2015.
 */
public class NodeRender {
    private JButton upVoteButton;
    private JTextField textField1;
    private JTextField textField2;
    private JTextPane textPane1;
    private JPanel Node;
    private JPanel Image;

    /*public static void main(String[] args) {
        JFrame frame = new JFrame("NodeRender");
        frame.setContentPane(new NodeRender().Node);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }*/

    public void generateNode()
    {
        JFrame frame = new JFrame("NodeRender");
        frame.setContentPane(new NodeRender().Node);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);


    }

    private void createUIComponents(){
        upVoteButton = new JButton();
        textField1 = new JTextField();
        textField2 = new JTextField();
        textPane1 = new JTextPane();
        Image = new JPanel();

    }

    private void $$$setupUI$$$(){
        createUIComponents();
    }
}
