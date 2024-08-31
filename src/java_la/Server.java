package java_la;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.net.*;


public class Server implements ActionListener {
    static JFrame frame = new JFrame();

    JTextField text;
    JPanel chat_area;
   static Box vertical = Box.createVerticalBox();
    static DataOutputStream dout;
    Server(){
        //back image
        ImageIcon img1 = new ImageIcon("C:\\Users\\siddh\\java_la\\java_la\\src\\java_la\\3.png");  //creates a new image class
        Image tempImg = img1.getImage().getScaledInstance(25,25, Image.SCALE_DEFAULT);          //scaling the image
        ImageIcon img2 = new ImageIcon(tempImg);                                                            //placing image in new class

        //profile image
        ImageIcon img3 = new ImageIcon("C:\\Users\\siddh\\java_la\\java_la\\src\\java_la\\1.png");
        Image tempImg1 = img3.getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT);
        ImageIcon img4 = new ImageIcon(tempImg1);

        //video call image
        ImageIcon img5 = new ImageIcon("C:\\Users\\siddh\\java_la\\java_la\\src\\java_la\\video.png");
        Image tempImg2 = img5.getImage().getScaledInstance(30,30, Image.SCALE_DEFAULT);
        ImageIcon img6 = new ImageIcon(tempImg2);

        //voice call image
        ImageIcon img7 = new ImageIcon("C:\\Users\\siddh\\java_la\\java_la\\src\\java_la\\phone.png");
        Image tempImg3 = img7.getImage().getScaledInstance(30,30, Image.SCALE_DEFAULT);
        ImageIcon img8 = new ImageIcon(tempImg3);

        //more otems image
        ImageIcon img9 = new ImageIcon("C:\\Users\\siddh\\java_la\\java_la\\src\\java_la\\3icon.png");
        Image tempImg4 = img9.getImage().getScaledInstance(10,25, Image.SCALE_DEFAULT);
        ImageIcon img10 = new ImageIcon(tempImg4);

        //adding all image to a label
        JLabel back = new JLabel(img2);
        JLabel profile = new JLabel(img4);
        JLabel video = new JLabel(img6);
        JLabel phone = new JLabel(img8);
        JLabel more = new JLabel(img10);

        //setting bounds and function to the label
        back.setBounds(5,20,25,25);
        back.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });

        profile.setBounds(40,10,50,50);
        video.setBounds(300,15,40,40);
        phone.setBounds(350,15,40,40);
        more.setBounds(400,15,20,40);

        //adding names
        JLabel name = new JLabel("sender");
        name.setBounds(100,25,100,20);
        name.setFont(new Font("SAN_SERIF", Font.BOLD,20));
        name.setForeground(Color.white);

        //crreating top panel and adding all the labels to the panel
        JPanel p1 = new JPanel();
        p1.setBackground(new Color(7,94,84));
        p1.setBounds(0,0,450,70);
        p1.setLayout(null);
        p1.add(back);
        p1.add(profile);
        p1.add(video);
        p1.add(phone);
        p1.add(more);
        p1.add(name);

        //creating new text panel
        chat_area = new JPanel();
        chat_area.setBounds(5,75,425,540);

        //creating a textfield
        text = new JTextField();
        text.setBounds(5,620,310,40);
        text.setFont(new Font("SAN_SERIF", Font.PLAIN,20));

        //creating the send button
        JButton send = new JButton("Send");
        send.setBounds(320,620,110,40);
        send.setFont(new Font("SAN_SERIF", Font.BOLD,16));
        send.setBackground(new Color(7,94,84));
        send.setForeground(Color.white);
        send.setFocusable(false);
        send.addActionListener(this);

        //creating a frame
        frame.setLayout(null);
        frame.setTitle("Socialize Me");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450,700);
        frame.getContentPane().setBackground(Color.white);
        frame.add(p1);
        frame.add(chat_area);
        frame.add(text);
        frame.add(send);
//        frame.setUndecorated(true);

        frame.setVisible(true);

    }
    private static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(Calendar.getInstance().getTime());
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String output = text.getText();
            JPanel msg = formatlabel(output,getCurrentTime());

            chat_area.setLayout(new BorderLayout());
            JPanel right = new JPanel(new BorderLayout());
            right.add(msg, BorderLayout.LINE_END);
            vertical.add(right);
            vertical.add(Box.createVerticalStrut(10));
            chat_area.add(vertical, BorderLayout.PAGE_START);

            dout.writeUTF(output);

            text.setText("");
            frame.repaint();
            frame.invalidate();
            frame.validate();

        }
        catch(Exception e1){
            e1.printStackTrace();
        }
    }

    public static  JPanel formatlabel(String msg,String timeStr){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        JLabel output = new JLabel("<html><p style = \"width: 150px \">"+msg+"</p></html>");
        output.setFont(new Font("Tahoma",Font.PLAIN,16));
        output.setBackground(new Color(37,211,102));
        output.setOpaque(true);
        output.setBorder(new EmptyBorder(10,15,10,15));
        panel.add(output);
//        Calendar date = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:MM");
        JLabel time  = new JLabel();
        time.setText(timeStr);



        panel.add(time);
        return panel;
    }


    public static void main(String[] args) {
        new Server();

        try{
            ServerSocket skt = new ServerSocket(6001);
            while (true){
                Socket s = skt.accept();
                DataInputStream din = new DataInputStream(s.getInputStream());
                dout = new DataOutputStream(s.getOutputStream());

                while (true){
                    String msg = din.readUTF();
                    JPanel panel = formatlabel(msg,getCurrentTime());
                    JPanel left = new JPanel(new BorderLayout());
                    left.add(panel,BorderLayout.LINE_START);
                    vertical.add(left);
                    frame.validate();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }


}
