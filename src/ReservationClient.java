import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Handles the client side
 *
 * @author Julien Kofoid and Joe Klug
 * @version December 2, 2019
 */

public class ReservationClient {

    private static final Font TITLEFONT = new Font("title", Font.BOLD, 20);
    private static final JFrame JF = new JFrame("Purdue University Flight Reservation System");
    private static Socket socket;
    private static ObjectOutputStream oos;

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                gui();
            }
        });
    }

    private static void gui() {
        boolean workin;
        do {
            workin = true;
            try {
                String hostname = JOptionPane.showInputDialog(null,
                        "What is the hostname you'd like to connect to?",
                        "Hostname?", JOptionPane.QUESTION_MESSAGE);
                if (hostname != null) {
                    String portString = JOptionPane.showInputDialog(null,
                            "What is the port you'd like to connect to?",
                            "Hostname?", JOptionPane.QUESTION_MESSAGE);
                    if (portString != null) {
                        int port = Integer.parseInt(portString);

                        socket = new Socket(hostname, port);
                        oos = new ObjectOutputStream(socket.getOutputStream());

                        JF.setSize(640, 480);
                        JF.setLocation(300, 300);
                        JF.setFocusable(true);
                        JF.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        JF.setResizable(false);
                        JF.add(welcomePanel());
                        JF.setVisible(true);
                    }
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Please enter a valid hostname and port",
                        "Error", JOptionPane.ERROR_MESSAGE);
                workin = false;
            }
        } while (!workin);
    }

    private static JPanel welcomePanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel northPanel = new JPanel();
        JPanel southPanel = new JPanel();

        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        GridLayout northGL = new GridLayout(2, 1);
        northGL.setHgap(20);
        northPanel.setLayout(northGL);

        FlowLayout southFL = new FlowLayout();
        southFL.setHgap(15);
        southPanel.setLayout(southFL);

        mainPanel.setBackground(Color.darkGray);
        northPanel.setBackground(Color.darkGray);
        southPanel.setBackground(Color.darkGray);

        JLabel labelOne = new JLabel("Welcome to the Purdue University Airline Reservation");
        labelOne.setFont(TITLEFONT);
        labelOne.setForeground(Color.lightGray);
        labelOne.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel labelTwo = new JLabel("Management System");
        labelTwo.setFont(TITLEFONT);
        labelTwo.setForeground(Color.lightGray);
        labelTwo.setHorizontalAlignment(SwingConstants.CENTER);

        try {
            double scale = .2;
            ImageIcon preScaledIcon = new ImageIcon(new URL(
                    "http://www.physics.purdue.edu/~cui/pics/mystuff/purdue.png"));
            ImageIcon imageIcon = new ImageIcon(preScaledIcon.getImage()
                    .getScaledInstance((int) (preScaledIcon.getIconWidth() * scale),
                            (int) (preScaledIcon.getIconHeight() * scale), Image.SCALE_DEFAULT));
            JLabel label = new JLabel(imageIcon, JLabel.CENTER);

            mainPanel.add(label, BorderLayout.CENTER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JButton exitButtonWelcome = new JButton("Exit");
        JButton continueButton = new JButton("Book a Flight");

        northPanel.add(labelOne);
        northPanel.add(labelTwo);
        southPanel.add(continueButton);
        southPanel.add(exitButtonWelcome);
        mainPanel.add(northPanel, BorderLayout.NORTH);
        mainPanel.add(southPanel, BorderLayout.SOUTH);

        exitButtonWelcome.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JF.dispose();
            }
        });

        continueButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JF.remove(mainPanel);
                JF.add(confirmPanel());
                JF.revalidate();
                JF.repaint();
            }
        });
        return mainPanel;
    }

    private static JPanel confirmPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel southPanel = new JPanel();

        FlowLayout southFL = new FlowLayout();
        southFL.setHgap(15);
        southPanel.setLayout(southFL);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        mainPanel.setBackground(Color.darkGray);
        southPanel.setBackground(Color.darkGray);

        JLabel label = new JLabel("Do you want to book a flight today?");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setForeground(Color.lightGray);
        label.setFont(TITLEFONT);

        JButton continueButton = new JButton("Yes, I want to book a flight.");
        JButton exitButton = new JButton("Exit");

        southPanel.add(continueButton);
        southPanel.add(exitButton);
        mainPanel.add(label, BorderLayout.NORTH);
        mainPanel.add(southPanel, BorderLayout.SOUTH);


        continueButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JF.remove(mainPanel);
                JF.add(chooseFlightPanel());
                JF.revalidate();
                JF.repaint();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JF.dispose();
            }
        });

        return mainPanel;
    }

    private static JPanel chooseFlightPanel() {
        String alaskaInfoString = "Alaska Airlines is proud to serve the string and knowledgeable Boilermakers.\n" +
                "We primarily fly westward, and often in Alaska and California.\n" +
                "We have first class amenities, even in coach class.\n" +
                "We provide fun snacks, such as pretzels and goldfish.\n" +
                "We also have comfortable seats and free WiFi.\n" +
                "We hope you choose Alaska Airlines for your next itinerary!";
        String southwestInfoString = "Southwest Airlines is proud to offer flights to Purdue University.\n" +
                "We are happy to offer free in flight WiFi, as well as our amazing snacks.\n" +
                "In addition, we offer flights for much cheaper than other airlines," +
                " and offer two free checked bags.\n" +
                "We hope you use Southwest for your next flight.";
        String deltaInfoString = "Delta is proud to be one of the five premier airlines at Purdue University.\n" +
                "We offer exceptional services with free limited WiFi for all customers.\n" +
                "Passengers who use T-Mobile as a cell phone carrier get additional benefits.\n" +
                "We are also happy to offer power outlets in each seat for passenger use.\n" +
                "We hope you choose to fly Delta as your next airline.";

        JPanel chooseFlightPanel = new JPanel(new BorderLayout());
        chooseFlightPanel.setBackground(Color.darkGray);
        JPanel northChoose = new JPanel();
        northChoose.setBackground(Color.darkGray);
        JPanel southChoose = new JPanel();
        southChoose.setBackground(Color.darkGray);

        GridLayout gl = new GridLayout(2, 1);
        gl.setHgap(20);
        northChoose.setLayout(gl);

        JLabel chooseLabel = new JLabel("Choose a flight from the drop down menu.");
        chooseLabel.setForeground(Color.lightGray);
        chooseLabel.setHorizontalAlignment(SwingConstants.CENTER);
        chooseLabel.setFont(TITLEFONT);

        ArrayList<String> test = new ArrayList<>();
        test.add("");

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            oos.writeObject("checkFull//Alaska");
            if (bufferedReader.readLine().equals("false"))
                test.add("Alaska");
            oos.writeObject("checkFull//Delta");
            if (bufferedReader.readLine().equals("false"))
                test.add("Delta");
            oos.writeObject("checkFull//Southwest");
            if (bufferedReader.readLine().equals("false"))
                test.add("Southwest");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        String[] airlines = new String[test.size()];
        for (int x = 0; x < test.size(); x++) {
            airlines[x] = test.get(x);
        }

        JPanel chooseFlightBoxPanel = new JPanel();
        chooseFlightBoxPanel.setMaximumSize(new Dimension(100, 10));
        JComboBox chooseFlightBox = new JComboBox(airlines);
        JPanel flightInfo = new JPanel();
        flightInfo.setBackground(Color.darkGray);

        JButton exitButtonChoose = new JButton("Exit");
        JButton chooseFlightButton = new JButton("Choose this flight");

        chooseFlightBox.setFocusable(true);
        chooseFlightBoxPanel.setBackground(Color.darkGray);
        chooseFlightBoxPanel.add(chooseFlightBox);

        northChoose.add(chooseLabel);
        northChoose.add(chooseFlightBoxPanel);
        southChoose.add(chooseFlightButton);
        southChoose.add(exitButtonChoose);
        chooseFlightPanel.add(northChoose, BorderLayout.NORTH);
        chooseFlightPanel.add(flightInfo, BorderLayout.CENTER);
        chooseFlightPanel.add(southChoose, BorderLayout.SOUTH);
        chooseFlightPanel.setFocusable(true);


        chooseFlightBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String s = (String) chooseFlightBox.getSelectedItem();
                flightInfo.removeAll();
                JPanel infoPanel = new JPanel();
                String[] strings;
                switch (s) {
                    case "Alaska":
                        infoPanel.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));
                        strings = alaskaInfoString.split("\n");
                        break;
                    case "Delta":
                        infoPanel.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));
                        strings = deltaInfoString.split("\n");
                        break;
                    case "Southwest":
                        infoPanel.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));
                        strings = southwestInfoString.split("\n");
                        break;
                    default:
                        infoPanel.setBorder(BorderFactory.createLineBorder(Color.darkGray, 400));
                        strings = new String[]{};
                        break;
                }
                GridLayout gl = new GridLayout(strings.length, 1);
                gl.setVgap(5);
                infoPanel.setLayout(gl);
                infoPanel.setBackground(Color.darkGray);
                flightInfo.setMinimumSize(flightInfo.getMaximumSize());
                for (String str : strings) {
                    JLabel string = new JLabel(str);
                    string.setForeground(Color.white);
                    string.setHorizontalAlignment(SwingConstants.CENTER);
                    infoPanel.add(string);
                }

                flightInfo.add(infoPanel);
                JF.revalidate();
            }
        });

        chooseFlightBox.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                String s = (String) chooseFlightBox.getSelectedItem();
                if (e.getKeyCode() == KeyEvent.VK_BACK_SLASH && !s.equals("")) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            passengerListGui(s);
                        }
                    });
                }
            }
        });

        exitButtonChoose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JF.dispose();
            }
        });

        chooseFlightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String airline = (String) chooseFlightBox.getSelectedItem();
                if (!airline.equals("")) {
                    JF.remove(chooseFlightPanel);
                    JF.add(infoPanel(airline));
                    JF.revalidate();
                    JF.repaint();
                }
            }
        });

        return chooseFlightPanel;
    }

    private static JPanel infoPanel(String airline) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.darkGray);
        JPanel centerPanel = new JPanel(new GridLayout(6, 1));
        centerPanel.setBackground(Color.darkGray);
        JPanel southPanel = new JPanel();
        southPanel.setBackground(Color.darkGray);

        JLabel title = new JLabel("Please input your information below");
        title.setForeground(Color.lightGray);
        title.setFont(TITLEFONT);
        title.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel fnameQuestion = new JLabel("What is your first name?");
        fnameQuestion.setForeground(Color.lightGray);
        JLabel lnameQuestion = new JLabel("What is your last name?");
        lnameQuestion.setForeground(Color.lightGray);
        JLabel ageQuestion = new JLabel("What is your age?");
        ageQuestion.setForeground(Color.lightGray);

        JTextField fnameField = new JTextField();
        fnameField.setBackground(Color.gray);
        JTextField lnameField = new JTextField();
        lnameField.setBackground(Color.gray);
        JTextField ageField = new JTextField();
        ageField.setBackground(Color.gray);

        centerPanel.add(fnameQuestion);
        centerPanel.add(fnameField);
        centerPanel.add(lnameQuestion);
        centerPanel.add(lnameField);
        centerPanel.add(ageQuestion);
        centerPanel.add(ageField);

        JButton exitButton = new JButton("Exit");
        JButton nextButton = new JButton("Next");

        southPanel.add(nextButton);
        southPanel.add(exitButton);

        panel.add(title, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(southPanel, BorderLayout.SOUTH);

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JF.dispose();
            }
        });


        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String firstName = fnameField.getText();
                String lastName = lnameField.getText();
                String ageString = ageField.getText();
                String full = "";

                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    oos.writeObject("checkFull//" + airline);
                    full = bufferedReader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (full.equals("true")) {
                    JOptionPane.showMessageDialog(null, "Flight has filled up!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    JF.remove(panel);
                    JF.add(chooseFlightPanel());
                    JF.revalidate();
                    JF.repaint();
                } else if (firstName.equals("") || lastName.equals("") || ageString.equals("") ||
                        checkValidName(firstName) || checkValidName(lastName) || checkValidAge(ageString)) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid name and age",
                            "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    int age = Integer.parseInt(ageString);

                    int cont = JOptionPane.showConfirmDialog(null,
                            "Are all the passengers details you entered correct?\n" +
                                    "The passenger's name is " + firstName + " " + lastName +
                                    " and their age is " + age + ".\n" +
                                    "If all the information is correct select the yes button below,\n" +
                                    "otherwise select the no button.", "Confirm Info",
                            JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);

                    if (cont == JOptionPane.YES_OPTION) {
                        try {
                            sendPassengerInfo(airline, firstName, lastName, age);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        JF.remove(panel);
                        JF.add(flightConfirmationPanel(airline));
                        JF.revalidate();
                        JF.repaint();
                    }
                }
            }
        });

        return panel;
    }

    private static JPanel flightConfirmationPanel(String airline) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.darkGray);
        JPanel northPanel = new JPanel();
        northPanel.setBackground(Color.darkGray);


        GridLayout gl = new GridLayout(3, 1);
        gl.setVgap(5);
        northPanel.setLayout(gl);

        JLabel topLabel = new JLabel("Flight data displaying for " + airline + " Airlines");
        topLabel.setForeground(Color.lightGray);
        topLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topLabel.setFont(TITLEFONT);
        JLabel midLabel = new JLabel("Enjoy your flight!");
        midLabel.setForeground(Color.lightGray);
        midLabel.setHorizontalAlignment(SwingConstants.CENTER);
        midLabel.setFont(TITLEFONT);
        String gateNum = "";
        String boardingPass = "";
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            oos.writeObject("getGateNum//" + airline);
            gateNum = bufferedReader.readLine();
            oos.writeObject("getBoardingPass//" + airline + "//" + gateNum);
            boardingPass = bufferedReader.readLine();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        JLabel bottomLabel = new JLabel("Flight is boarding at gate " + gateNum);
        bottomLabel.setForeground(Color.lightGray);
        bottomLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bottomLabel.setFont(TITLEFONT);

        JButton refresh = new JButton("Refresh Flight Status");
        JButton exitButtonConfirm = new JButton("Exit");

        JPanel boardingPassPanel = new JPanel();
        boardingPassPanel.setBackground(Color.darkGray);
        String[] boardingStrings = boardingPass.split("//");
        GridLayout grl = new GridLayout(boardingStrings.length + 1, 1);
        boardingPassPanel.setLayout(grl);
        int i = 0;
        for (String s : boardingStrings) {
            JLabel jl = new JLabel(s);
            jl.setForeground(Color.white);
            jl.setHorizontalAlignment(SwingConstants.CENTER);
            boardingPassPanel.add(jl, i);
            i++;
        }

        JPanel southButtonPanel = new JPanel();
        southButtonPanel.setBackground(Color.darkGray);
        JPanel centerPanel = new JPanel(new GridLayout(2, 1));

        northPanel.add(topLabel);
        northPanel.add(midLabel);
        northPanel.add(bottomLabel);
        southButtonPanel.add(refresh);
        southButtonPanel.add(exitButtonConfirm);
        mainPanel.add(southButtonPanel, BorderLayout.SOUTH);
        mainPanel.add(northPanel, BorderLayout.NORTH);
        centerPanel.add(passengerListPanelTwo(airline));
        centerPanel.add(boardingPassPanel);
        mainPanel.add(centerPanel, BorderLayout.CENTER);


        refresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                centerPanel.removeAll();
                centerPanel.add(passengerListPanelTwo(airline));
                centerPanel.add(boardingPassPanel);
                JF.revalidate();
            }
        });

        exitButtonConfirm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Thanks for using the Purdue University Airline Management System",
                        "Thank You", JOptionPane.INFORMATION_MESSAGE);
                JF.dispose();
            }
        });

        return mainPanel;
    }

    private static JPanel passengerListPanelOne(String airline) {
        JPanel panel = new JPanel(new BorderLayout());
        try {
            String[] flightInfo = getFlightInfo(airline);
            assert flightInfo != null;
            JLabel title = new JLabel(airline + " Airlines");
            title.setFont(TITLEFONT);
            JLabel passengerCount = new JLabel(flightInfo[0]);
            String[] passengerList = Arrays.copyOfRange(flightInfo, 1, flightInfo.length);
            GridLayout tgl = new GridLayout(2, 1);
            JPanel titlePanel = new JPanel(tgl);
            GridLayout gl = new GridLayout(passengerList.length, 1);
            JPanel passengers = new JPanel(gl);
            for (String s : passengerList) {
                JLabel jl = new JLabel(s);
                jl.setHorizontalAlignment(SwingConstants.CENTER);
                passengers.add(jl);
            }
            passengers.setPreferredSize(new Dimension(150, 20 * (passengerList.length + 1)));
            JScrollPane slidePanel = new JScrollPane(passengers);
            slidePanel.createVerticalScrollBar();
            titlePanel.add(title, SwingConstants.CENTER, 0);
            titlePanel.add(passengerCount, SwingConstants.CENTER, 1);
            panel.add(slidePanel, BorderLayout.CENTER);
            panel.add(titlePanel, BorderLayout.NORTH);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return panel;
    }

    private static JPanel passengerListPanelTwo(String airline) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.darkGray);
        try {
            String[] flightInfo = getFlightInfo(airline);
            assert flightInfo != null;
            JLabel passengerCount = new JLabel(flightInfo[0]);
            passengerCount.setForeground(Color.lightGray);
            passengerCount.setHorizontalAlignment(SwingConstants.CENTER);
            passengerCount.setVerticalAlignment(SwingConstants.BOTTOM);
            String[] passengerList = Arrays.copyOfRange(flightInfo, 1, flightInfo.length);
            GridLayout tgl = new GridLayout(2, 1);
            JPanel titlePanel = new JPanel(tgl);
            titlePanel.setBackground(Color.darkGray);
            GridLayout gl = new GridLayout(passengerList.length, 1);
            JPanel passengers = new JPanel(gl);
            passengers.setBackground(Color.lightGray);
            for (String s : passengerList) {
                JLabel jl = new JLabel("                                           " +
                        "                                " + s);
                jl.setForeground(Color.darkGray);
                passengers.add(jl);
            }
            passengers.setPreferredSize(new Dimension(150, 20 * (passengerList.length + 1)));
            JScrollPane slidePanel = new JScrollPane(passengers);
            slidePanel.setBackground(Color.lightGray);
            slidePanel.createVerticalScrollBar();
            titlePanel.add(passengerCount);
            panel.add(slidePanel, BorderLayout.CENTER);
            panel.add(titlePanel, BorderLayout.NORTH);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return panel;
    }

    private static void passengerListGui(String airline) {
        try {
            JFrame jf = new JFrame(airline + "Passenger List");
            jf.setSize(200, 300);
            jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            jf.setResizable(false);
            jf.setFocusable(true);

            jf.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        jf.dispose();
                    }
                }
            });

            jf.add(passengerListPanelOne(airline));
            jf.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String[] getFlightInfo(String airline) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            oos.writeObject("getPassengers//" + airline);
            String s = bufferedReader.readLine();

            return s.split("//");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void sendPassengerInfo(String airline, String firstName,
                                          String lastName, int age) throws IOException {
        oos.flush();
        oos.writeObject(new Passenger(airline, firstName, lastName, age));
        oos.flush();
    }

    private static boolean checkValidName(String name) {
        char[] chars = name.toCharArray();
        for (char c : chars) {
            if (!Character.isAlphabetic(c))
                return true;
        }
        return false;
    }

    private static boolean checkValidAge(String age) {
        char[] chars = age.toCharArray();
        for (char c : chars) {
            if (!Character.isDigit(c))
                return true;
        }
        return Integer.parseInt(age) <= 0;
    }
}