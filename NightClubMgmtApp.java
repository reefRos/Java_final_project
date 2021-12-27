import java.awt.*;
import javax.swing.*;
import javax.sound.sampled.*;
import java.io.*;

//File: NightClubMgmtApp.java
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.*;



/**
*@author Gal Erez, Reef Rosenblat and Golan Parsha
*A class for the main application of B.K. Club Mgmt system.
*Here we use the {@link Menu} sub class to operate the whole system.
*/
public class NightClubMgmtApp
{
    //Night-Club Regular Customers Repository
    private static ArrayList<ClubAbstractEntity> clubbers;



    //NightClubMgmtClub constructor
    /**
    *NightClubMgmtClub Constructor - initializes ArrayList for the clubbers
    *and uses the {@link #loadClubbersDBFromFile} method inorder to read an external databse
    *from a binary file.
    *@throws UnsupportedAudioFileException throws if an audio file exception accured.
    *@throws LineUnavailableException throws if line unavailable exception accured.
    *@throws IOException throws if an input\output exception accured.
    *@throws ClassNotFoundException throws if a class exception accured.
    */
    public NightClubMgmtApp() throws UnsupportedAudioFileException, LineUnavailableException, IOException, ClassNotFoundException {
        clubbers = new ArrayList<>(); //initialize a new arraylist which will hold all of the clubbers
        loadClubbersDBFromFile(); //activate the loadClubberDBFromFile method

        Menu m = new Menu(); //initializing a new Menu object
    }



                                    //NightClubMgmtApp methods//


    /**
    * This method checks if a clubber with a certain key already exists in the database.
    * It uses the {@link Person#validateData} method to check if the key matches an ID that already exists.
    * If it's a Solider, it will also check the Personal Number using {@link Solider#validateData}.
    * If it's a Student, it will also check the Student ID using {@link Student#validateData}.
    * @param key An identifier, can either be an ID/personal number/student ID.
    * @param newClubber the new clubber, an {@link ClubAbstractEntity} object.
    * @return If a clubber already exists the method will return true, else return false.
    */
    public static boolean doesExist(String key, ClubAbstractEntity newClubber)
    {
        for(ClubAbstractEntity clubber : clubbers)  //enhanced for loop to go over the clubbers arraylist
        {
            if ((clubber != newClubber)&&(clubber.match(key))) //checks if the key matches but it isn't the same clubber
                return true;
        }
        return false;
    }


//Method made for loading a binary file filled with data about clubbers
    /**
    *This function reads binary files that hold a database about clubbers.
    *@throws IOException throws if an input\output exception accured.
    *@throws ClassNotFoundException throws if a class exception accured.
    *Initializes a {@link java.io} fileInputStream inorder to read a binary file
    *and inserts the information to the ArrayList clubbers.
    */
    private void loadClubbersDBFromFile() throws IOException, ClassNotFoundException
    {
        File file = new File("BKCustomers.dat"); //Open a binary file which holds data about certain clubbers
        if(file.length()!=0) {
            ObjectInputStream input = new ObjectInputStream(new FileInputStream("BKCustomers.dat")); //initializing the input stream
            clubbers = (ArrayList<ClubAbstractEntity>) input.readObject(); //insert set data to the clubbers arrayList
            input.close(); //Close file stream
        }
    }


    //Method for writing a binary file with new clubbers data
    /**
    *This method writes all the clubbers that were added by the user to a binary file.
    *Uses {@link java.io.FileOutputStream} to write the clubbers Array to a binary file.
    */
    private void writeClubbersDBtoFile()
    {
        try {
            FileOutputStream file = new FileOutputStream("BKCustomers.dat"); //Write to this specific file. if file doesnt exist, then create one.
            ObjectOutputStream output = new ObjectOutputStream(file); //initializing the output stream
            output.writeObject(clubbers); //This line writes the data which clubbers holds to the binary file
            output.flush();
            output.close(); //Close file stream
            file.close(); //closes the file
        } catch (IOException e) {
            e.printStackTrace(); //prints the throwable along with other details like the line number and class name where the exception occurred.
        }
    }



    //sub-class which handles the main menu operations
    /**
    *A sub-class for {@link NightClubMgmtApp} that initializes main menu operations in the BK club app
    *and GUI components for the main menu.
    */
    private class Menu
    {
        private JButton  createButton, mainMenuSearch, mainMenuCreate, muteButton, exitButton;
        private JPanel mainPanel, buttonsPanel, createPanel;
        private JLabel mainLabel, createLabel;
        private JFrame mainFrame, createFrame;
        private JComboBox<String> comboBox; //combo-box for objects' names
        private final Icon menuGif = new ImageIcon("menuGif2.gif"); //File for gif which is playing in the main menu
        private final ButtonHandler handler = new ButtonHandler(); //initializing a button handler
        private final File song = new File("jellyfish_jam.wav"); //File for audio which is playing in the main menu
        private final AudioInputStream audioStream = AudioSystem.getAudioInputStream(song); //AudioStream for main menu song
        private final Clip clip = AudioSystem.getClip();
        private final Icon mute = new ImageIcon("mute2.png"); //PNG file for the mute button when the music is playing
        private final Icon unmute = new ImageIcon("unmute2.png"); //PNG file for the mute button when the music is paused
        private String[] types = {"Person","Solider","Student"}; //array for the objects' names


        //Constructor for the main menu. it instantiates the main frame and other imprtant GUI components such as buttons
        //and panels.
        /**
        *Menu Constructor - initializing the menu GUI Components and Layouts for thos Components.
        *@throws LineUnavailableException throws if line in unavailable exception accured.
        *@throws IOException throws if an input\output exception accured.
        *@throws UnsupportedAudioFileException throws if an audio file exception accured.
        */
        private Menu() throws LineUnavailableException, IOException, UnsupportedAudioFileException {
            //initializing the main frame which will hold all the GUI compnents for the main menu
            mainFrame = new JFrame("B.K. Night Club"); //initializing the main frame
            mainFrame.setSize(500, 320); //sets the size of the main frame
            mainFrame.setVisible(true); //makes the main frame visible by default
            mainFrame.setLocationRelativeTo(null); //sets the main frame location to the center of the screen
            mainFrame.setResizable(false); //disable the ability to resize the frame
            mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); //disable the ability to press the "X" button

            GridLayout grid = new GridLayout(1, 4, 10, 10); //New Layout manager initialized as GridLayout

            mainPanel = new JPanel(new BorderLayout()); //initializing the main panel
            buttonsPanel = new JPanel(grid); //initializing the button panel


            //initializing the buttons which will operate the create/search interface and adding them to the buttons panel
            mainMenuSearch = new JButton("Search");
            mainMenuCreate = new JButton("Create");
            exitButton = new JButton("Exit");
            muteButton = new JButton(unmute);
            muteButton.setPreferredSize(new Dimension(25,25));

            //Adding the mouse handler to the corresponding buttons
            mainMenuSearch.addActionListener(handler);
            mainMenuCreate.addActionListener(handler);
            exitButton.addActionListener(handler);
            muteButton.addActionListener(handler);

            //adds the buttons to the button panel
            buttonsPanel.add(muteButton);
            buttonsPanel.add(mainMenuSearch);
            buttonsPanel.add(mainMenuCreate);
            buttonsPanel.add(exitButton);
            buttonsPanel.setBackground(Color.DARK_GRAY); //sets the buttons panel background to dark grey

            JLabel menuGifLabel = new JLabel(menuGif); //initializing the main menu gif
            mainLabel = new JLabel("Welcome to B.K. Club! What would you like to do?", SwingConstants.CENTER); //initializing the main menu label
            mainLabel.setFont(new Font("Calibri", Font.PLAIN, 20)); //sets the font and size of the main label
            mainLabel.setForeground(Color.WHITE); //sets the color of the menu label to white
            mainPanel.add(mainLabel, BorderLayout.NORTH); //adds the main label to the main panel
            mainPanel.add(menuGifLabel, BorderLayout.CENTER); //adds the menu gif to the main panel
            mainPanel.add(buttonsPanel, BorderLayout.SOUTH); //adds the buttons panel to the main panel
            mainPanel.setBackground(Color.DARK_GRAY); //sets the main panel as dark grey

            mainFrame.add(mainPanel); //adds the main panel to the main frame
            mainFrame.setVisible(true); //sets the main frame visible by default

            //Open and start playing the audio file for main menu
            clip.open(audioStream);
            clip.start();
        }



                                          //Main Menu methods//

        //This method instantiates an interface for creating and adding a new clubber to the database.
        /**
        *Initializing the create interface - Creates all the GUI components for the create interface.
        *It uses {@link FlowLayout} and a few panels to set some GUI components in certain areas.
        */
        private void createInterface() {
            //Initialize the frame which will hold the GUI components for create interface
            createFrame = new JFrame("Create new clubber"); //initializing the create frame
            createFrame.setSize(500, 400); //sets the create frame's size
            createFrame.setVisible(true); //makes the create frame visible by default
            createFrame.setResizable(false); //disable the ability to resize the frame
            createFrame.setLocationRelativeTo(mainFrame); //sets the create frame location relative to the main frame

            String[] images = {"person.gif","solider.gif","student.gif"}; //String array which holds the names for each gif used for clubber types
            Icon[] icons = {new ImageIcon(images[0]), new ImageIcon(images[1]), new ImageIcon(images[2])}; //Initialize the icons for the create interface

            FlowLayout bl = new FlowLayout(); //initializing a FlowLayout
            createPanel = new JPanel(bl); //initializing the create panel
            createLabel = new JLabel("Which kind of clubber would you like to add?"); //initializing the create label
            createLabel.setForeground(Color.WHITE); //sets the create label as white
            JLabel imageLabel = new JLabel(icons[0]); //initializing the image label

            comboBox = new JComboBox(types);//Initialize the list to choose specific clubber

            comboBox.addItemListener( //Add an item listener to our clubber list so we can change the gif of the corresponding clubber type
                    new ItemListener() {
                        @Override
                        public void itemStateChanged(ItemEvent e) {
                            if(e.getStateChange()== ItemEvent.SELECTED) //gets the index that is selected
                                imageLabel.setIcon(icons[comboBox.getSelectedIndex()]); //sets the gif relative to the index of the combo box
                        }
                    }
            );

            createButton = new JButton("Create"); //initializing the create button
            createButton.addActionListener(handler); //adds an action listener to the create button

            createPanel.add(createLabel); //adds the create label to the create panel
            createPanel.add(comboBox); //adds the combo box to the create panel
            createPanel.add(createButton); //adds the create button to the create panel
            createPanel.add(imageLabel); //adds the image label to the create panel
            createPanel.setBackground(Color.DARK_GRAY); //sets the create panel backgroud as dark grey

            createFrame.add(createPanel); //adds the create panel to the create frame
        }



        //Method which searches the DB for a certain key. If it can't find it a clubber not found message will pop up.
        /**
        *Searching method - it checks if a person exists by a given key. If the text "exit" is entered, the
        *program will close and will use {@link NightClubMgmtApp#writeClubbersDBtoFile} to write
        *the clubbers to a binery file.
        *If the input key is valid, the method will use {@link Person#match} method to match
        *the key.
        *@return If the person exists the method returns true, else returns false.
        */
        private boolean search()
        {
            String input = JOptionPane.showInputDialog(mainFrame,"Please Enter The Clubber's Key:", "Search Data Base", JOptionPane.PLAIN_MESSAGE);
            if(input == null) //This if statement makes sure that no null value has been entered somehow
                return false;


            if(input.trim().equalsIgnoreCase("exit")) //If the user enters "exit" to the search box, the program will close and activate writeClubbersDBtoFile()
            {writeClubbersDBtoFile(); System.exit(0);}
            for(ClubAbstractEntity clubber : clubbers) { //Enhanced for loop which goes through all the clubbers and tries to match the key entered by the user
                if (clubber.match(input)) {
                    clubber.setVisible(true); //makes the clubber's frame visible
                    clubber.setLocationRelativeTo(mainFrame); //makes the clubber's frame location relative to the main frame
                    clubber.toFront(); //bring the clubber's frame to the front
                    clubber.rollBack(); //activate clubber's rollBack method to show it's personal info
                    return true;
                }
            }
            JOptionPane.showMessageDialog(mainFrame,"Clubber with key " +input+ " does not exist","Not Found",JOptionPane.INFORMATION_MESSAGE);
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //closes the program
            return false;
        }


        //Method for creating and adding a new clubber to the database
        /**
        *Creates an object using a specific constructor as selected by the user and adds it to clubbers array.
        *@param type A string that is entered to define if it's a person, soldier or a student.
        */
        private void createNewObject(String type) //The method will compare the string "type" to each corresponding clubber type.
        {                                         //if the string and type name match then create that clubber and add it to clubbers array list.
            if (type.equals("Person"))
            {
                clubbers.add(new Person(null, null, null, null)); //initializing a new person clubber
                clubbers.get((clubbers.size()) - 1).setVisible(true); //makes the clubber's frame visible
            }
            if (type.equals("Solider"))
            {
                clubbers.add(new Solider(null, null, null, null, null)); //initializing a new solider clubber
                clubbers.get((clubbers.size())-1).setVisible(true); //makes the clubber's frame visible
            }
            if (type.equals("Student"))
            {
                clubbers.add(new Student(null, null, null, null, null)); //initializing a new student clubber
                clubbers.get((clubbers.size())-1).setVisible(true); //makes the clubber's frame visible
            }
            clubbers.get((clubbers.size())-1).setLocationRelativeTo(createFrame); //sets the clubber's frame location relative to the create frame
        }



        //Method which initializes the music player
        /**
        *a method to play music using {@link javax.sound.sampled}.
        *It controlls whether the audio stream is playing or not, and changes the icon depending on the audio stream's state.
        *@throws UnsupportedAudioFileException If the chosen audio file isn't supported.
        *@throws IOException Throws if an input\output exception accured.
        *@throws LineUnavailableException An exception indicating that a line cannot be opened because it is unavailable.
        */
        public void musicPlayer() throws UnsupportedAudioFileException, IOException, LineUnavailableException {

            if(clip.isActive()==false) //If statement to check if the music is paused then start playing and change the icon for the mute button
            {
                clip.start(); //starts the music
                muteButton.setIcon(unmute); //sets the icon on the mute button
            }
            else                       //Else stop playing the music and change the mute button icon accordingly
            {
                clip.stop(); //stops the music
                muteButton.setIcon(mute); //sets the icon on the mute button
            }
        }


        //Button handler sub-class
        /**
        *Implementation of the "ButtonHandler" sub-class which implements {@link java.awt.event.ActionListener}.
        *It identifies the mouse clicks on relevant interface's buttons.
        *If the mute button is pressed, "ButtonHandler" will activate {@link #musicPlayer} method.
        *If the "exit" button is pressed, "ButtonHandler" will activate {@link #writeClubbersDBtoFile} method
        *and will use exit(0) to terminate the program.
        */
        public class ButtonHandler implements ActionListener {

            /**
             *Method which operates the ActionEvent.
             *@param e A parameter for identifing mouse events.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == mainMenuSearch) //Section for the search button in main menu. Will activate the search method
                {
                    search(); //activate search method
                }

                if (e.getSource() == mainMenuCreate)//Section for the create button in main menu. Will activate the createInterface method.
                {
                    createInterface(); //activate createInterface method
                }


                if(e.getSource() == createButton) //Section for the create button inside the createInterface. Will finalize the creation of a new clubber.
                {
                    createNewObject(comboBox.getItemAt(comboBox.getSelectedIndex())); //activate the createNewObject method
                    createFrame.setVisible(false); //makes the create frame unvisible
                }

                if(e.getSource() == muteButton) //Section for the mute button in main menu. Will change the state of the audio in main menu.
                {
                    try {
                        musicPlayer(); //activate the musicPlayer method
                    } catch (Exception t){
                        JOptionPane.showMessageDialog(mainFrame,"ERROR OCCURRED WHILE PLAYING AUDIO FILE","ERROR",JOptionPane.WARNING_MESSAGE);
                        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //closes the program
                    }
                }

                if(e.getSource() == exitButton) //Section for the exit button in main menu. Will activate WriteClubbersDBtoFile method and terminate the program.
                {
                    if(clubbers != null)
                        writeClubbersDBtoFile(); //activate writeClubberDBtoFile method
                    JOptionPane.showMessageDialog(new JFrame(), "Thank you for using our system!", "B.K. Night Club", JOptionPane.PLAIN_MESSAGE);
                    System.exit(0);
                }
            }
        }
    }


    //Main for NightClubMgmtApp
    /**
    *Main for NightClubMgmtApp.
    *@throws UnsupportedAudioFileException throws if an audio file exception accured.
    *@throws LineUnavailableException throws if line in unavailable exception accured.
    *@throws IOException throws if an input\output exception accured.
    *@throws ClassNotFoundException throws if a class exception accured.
    *@param args Contains the command-line arguments passed to the Java program upon invocation.
    */
    public static void main(String[] args) throws UnsupportedAudioFileException, LineUnavailableException, IOException, ClassNotFoundException {
        try
        {
            NightClubMgmtApp appliction = new NightClubMgmtApp(); //Calls the NightClubMgmtApp constructor and starts the whole program.
        }catch (Exception e)
        {
            JOptionPane.showMessageDialog(new JFrame(),"ERROR OCCURRED","ERROR",JOptionPane.WARNING_MESSAGE); //Throws an exception if needed
        }
    }

}//End of class NightClubMgmtApp