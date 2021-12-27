import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import javax.swing.*;


//ClubAbstractEntity is the base class for all the clubber types. it implements the serializable interface inorder to
//have it's children able to be converted to binary file
/**
*@author Gal Erez, Reef Rosenblat and Golan Parsha
*An abstract class for the main data variables of B.K. Club mgmt system. It extends from JFrame and acts as the
*base for all clubber types.
*ClubAbstractEntity also implements Serializable interface inorder to inherit it for binary file reading and writing
*/
public abstract class ClubAbstractEntity extends JFrame implements Serializable{

    private JButton okButton, cancelButton;
    private JPanel centerPanel, buttonPanel;
    private ButtonHandler handler;


    //Constructor for ClubAbstractEntity
    /**
    *ClubAbstractEntity Constructor - initializes the main GUI components for all clubber types
    *but doesn't initialize certain methods.
    */
    public ClubAbstractEntity()
    {
        okButton = new JButton("ok"); //initializing the "ok" button
        cancelButton = new JButton("cancel"); //initializing the "cancel" button
        cancelButton.setEnabled(false); //set the default for "cancel" button as disabled
        centerPanel = new JPanel(); //initializing the center panel
        buttonPanel = new JPanel(); //initializing the button panel
        handler = new ButtonHandler(); //initializing a handler for mouse events


        setLayout(new BorderLayout()); //sets the layout of the frame as Border Layout
        setResizable(false); //disable the ability to resize the frame
        setVisible(false); //makes the frame unvisible by default
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); //disable the ability to press the "X" button

        add(centerPanel,BorderLayout.CENTER); //adds the center panel to the frame
        add(buttonPanel,BorderLayout.SOUTH); //adds the button panel to the frame

        buttonPanel.add(okButton); //adds the "ok" button to the button panel
        buttonPanel.add(cancelButton); //adds the "cancel" button to the button panel
        buttonPanel.setVisible(true); //makes the button panel visible by default
        okButton.addActionListener(handler); //adds an action listener for the "ok" button
        cancelButton.addActionListener(handler); //adds an action listener for the "cancel" button
    }


    //Main method for adding gui components to all the clubber types
    /**
    *Adds GUI components to the centerPanel and is inharited by ClubAbstractEntity's children.
    *@param guiComponent A GUI component that is added to the frame.
    */
    protected void addToCenter(Component guiComponent) {
        centerPanel.add(guiComponent);
    }


    //An abstract method that the heirs will have to implement
    /**
    *An abstract method which checks if a clubber exists by reciving a certain "key".
    *@param key a string which represents an identifier for a certian object.
    *@return It's inheriting classes will return true if the clubber was found, else returns false.
    */
    public abstract boolean match(String key);


    //An abstract method that the heirs will have to implement
    /**
    *An abstract method which validated the data entered by user to create a clubber.
    *@return It's inheriting classes will return true if the validation succeeds, else returns false.
    */
    protected abstract boolean  validateData();


    //An abstract method that the heirs will have to implement
    /**
    *An abstract method which saves the given data inside verious text fields.
    */
    protected abstract void commit();


    //An abstract method that the heirs will have to implement
    /**
     *An abstract method which copies the text from inner fields to the relevant text fields.
     */
    protected abstract void rollBack();


    //A sub-class for handling the mouse events
    /**
    *A sub-class which operates the ok\cancel buttons
    */
    public class ButtonHandler implements ActionListener, Serializable
    {
        /**
        *Method which operates the ActionEvent.
        *@param e A parameter for identifing mouse events.
        */
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if(e.getSource()==okButton) //checks if the "ok" button was pressed
            {
                if(validateData()) //checks if the validation succeed
                {
                    commit(); //active the commit method
                    cancelButton.setEnabled(true); //enabling the "cancel" button
                    setVisible(false); //makes the object unvisible
                }
            }

            if(e.getSource()==cancelButton) //checks if the "cancel" button was pressed
            {
                rollBack(); //active the rollBack method
                setVisible(false); //makes the object unvisible
            }
        }
    }
} //end of ClubAbstractEntity