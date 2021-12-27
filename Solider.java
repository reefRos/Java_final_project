import javax.swing.*;
import java.util.regex.*;


//This class inherits from Person and adds another information field - PersonalNum.
/**
*@author Gal Erez, Reef Rosenblat and Golan Parsha
*A class which extends from {@link Person} and creates a Soldier type object
*and adding an extra information field.
*/
public class Solider extends Person {

    private String personalNum; //personal number field
    private JTextField personalNumText; //a text field for the personal number
    private JLabel personalNumLabel; //a label for the personal number


    //constructor for solider class type. Calls the super constructor inorder to insert the base info fields.
    /**
    *Soldier Constructor - initializes the basic {@link Person} fields using a super constructor
    *@param id Contains the id of the soldier
    *@param name Contains the private name of the soldier
    *@param surname Contains the last name of the soldier
    *@param phoneNum Contains the phone number of the soldier
    *@param personalNum Contains the personal number of the soldier
    */
    public Solider(String id, String name, String surname, String phoneNum,String personalNum) {
        super(id, name, surname, phoneNum); //calls the father's constructor
        this.personalNum = personalNum; //inserts the personal number that were given into the object's field
        setTitle("Solider Clubber's Data"); //sets the frame's title
        setSize(450,250); //sets the frame's size

        personalNumLabel = new JLabel("Personal No.", SwingConstants.RIGHT); //initializing the personal number label
        personalNumText = new JTextField(30); //initializing the personal number text field

        labelPanel.add(personalNumLabel); //adds the personal number label to the label panel
        textPanel.add(personalNumText); //adds the personal number text field to the text panel

        addToCenter(labelPanel); //adds the label panel to the frame
        addToCenter(textPanel); //adds the text panel to the frame
        addToCenter(errorPanel); //adds the error panel to the frame
    }


    //Implements match method from person, and also checks if the key entered was a personal number
    /**
    *Implementation of the {@link ClubAbstractEntity#match} method from {@link ClubAbstractEntity}.
    *It checks if such a Soldier exists by matching a certain key, using {@link Person#match} method from person class
    *and if that fails, matching the key to the Solider's personal number.
    *@param key A string entered to check whether such soldier exists.
    *@return If the solider exists the method returns true, else returns false.
    */
    @Override
    public boolean match(String key)
    {
        if(super.match(key)) //active the father's match method
        {
            rollBack(); //activate the rollBack method
            return true;
        }

        else if(personalNum.equals(key)) //checks if the peronsl number and key are equal
        {
            rollBack(); //activate the rollBack method
            return true;
        }

        return false;
    }


    //Implements validate method from Person using super, and also checks if the regular expression for personal number is following the
    //correct pattern.
    /**
    *Implementation of the {@link ClubAbstractEntity#validateData} method.
    *It checks if the data given by the user is written as the right pattern of regular expression, by invoking the {@link Person#validateData} method.
    *It also checks the soldier's personal number pattern to the corresponding.
    *@return If the data validation is correct then returns true, else returns false.
    */
    @Override
    protected boolean validateData() {

        if(super.validateData()==false) //activate the father's validateData method
            return false;

        if(NightClubMgmtApp.doesExist(personalNumText.getText(),this)) //checks if a clubber with the same personal number already exists
        {
            errorLabel[4].setVisible(true); //makes the error label visible
            JOptionPane.showMessageDialog(this, "A clubber with this Personal Number already exists","Duplication error", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        String regeX = "^[ROC]{1}\\/[1-9]{1}[0-9]{6}$"; //a regular expression for the personal number validation
        Pattern pattern = Pattern.compile(regeX); //initializing a pattern with the regular expression
        Matcher matcher = pattern.matcher(this.personalNumText.getText()); //initializing a matcher with the personal number

        if(matcher.matches()==false)
        {
            this.errorLabel[4].setVisible(true); //makes the error label visible
            return false;
        }
        else if(matcher.matches()==true)
        {
            this.errorLabel[4].setVisible(false); //makes the error label unvisible
            return true;
        }
        return true;
    }



    //Implementation of rollBack method, it fills the text fields with the object's personal info
    /**
    *Implementation of the {@link ClubAbstractEntity#rollBack} method.
    *Fills the various text fields with the corresponding info fields including personal number field.
    */
    @Override
    protected void rollBack() {
        super.rollBack(); //activate the father's rollBack method
        personalNumText.setText(personalNum); //writes the information of the personal number that is saved in the object to the text field
        errorLabel[4].setVisible(false); //makes the error label unvisible
    }


    //Implementation of the commit method, it saves the data from the text fields to the object's personal info fields
    /**
    *Implementation of {@link ClubAbstractEntity#commit} method.
    *The method saves the data entered by user by invoking the super method and personal number to it's inner fields.
    */
    @Override
    protected void commit() {
        super.commit(); //activate the father's commit method
        personalNum = personalNumText.getText(); //inserts the information from the text field to the personal number field
    }
}