import java.awt.*;
import javax.swing.*;
import java.util.regex.*;


//Person class inherits from ClubAbstractEntity and sets the foundation for student/solider clubber types
/**
*@author Gal Erez, Reef Rosenblat and Golan Parsha
*A class that extends from {@link ClubAbstractEntity} and creates a person (the basic clubber).
*/
public class Person extends ClubAbstractEntity
{
    protected String[] personalInfo; //array for saving the object's personal info
    protected String[] labelsData = {"ID", "Name", "Surname", "Tel"}; //the labels of the personal info
    protected JTextField[] textFieldsArray = new JTextField[labelsData.length]; //array for the text fields
    protected JLabel[] labelsArray = new JLabel[labelsData.length]; //array for the labels
    protected JLabel[] errorLabel = new JLabel[labelsData.length+1]; //array for the error labels
    protected JPanel labelPanel, textPanel, errorPanel;
    protected String[] regexArr = {"^\\d\\-\\d{7}\\|[1-9]{1}$", "^[A-Z][a-z]{1,}$",
            "([A-Z][a-z]*('|-){0,1})*", "^\\+\\([1-9][0-9]{0,2}\\)[1-9][0-9]{0,2}\\-[0-9]{7}$"}; //array for validating the personal info



    //Person constructor
    /**
    *Person Constructor - it uses the following parameters to keep information.
    *@param id Contains the id of the person.
    *@param name Contains the private name of the person.
    *@param surname Contains the last name of the person.
    *@param phoneNum Contains the phone number of the person.
    */
    public Person(String id, String name, String surname, String phoneNum)
    {
        personalInfo = new String[]{id, name, surname, phoneNum}; //sets the object's personal info with the info given by the constructor

        setSize(450,220); //sets the size of the frame
        GridLayout grid1 = new GridLayout(5, 1, 5, 13); //GridLayout for the text panel
        GridLayout grid2 = new GridLayout(5, 1, 5, 17); //GridLayout for the error panel
        GridLayout grid3 = new GridLayout(5, 1, 5, 18); //GridLayout for the label panel
        this.setTitle("Person Clubber's Data"); //sets the frame's title

        labelPanel = new JPanel(grid3); //initializing the label panel with GridLayout
        labelPanel.setVisible(true); //sets the label panel visible by default

        textPanel = new JPanel(grid1); //initializing the text panel with GridLayout
        textPanel.setVisible(true);  //sets the text panel visible by default

        errorPanel = new JPanel(grid2); //initializing the error panel with GridLayout
        errorPanel.setVisible(true); //sets the error panel visible by default


        for (int i = 0; i < labelsData.length+1; i++)
        {
            errorLabel[i] = new JLabel("*"); //initializing the error labels
            errorLabel[i].setForeground(Color.red); //sets the error labels as red
            errorLabel[i].setVisible(false); //sets the error labels unvisible by default
            errorPanel.add(errorLabel[i]); //adds the error labels to the panel
        }


        for(int i=0;i<labelsData.length;i++)
        {
            labelsArray[i] = new JLabel(labelsData[i], SwingConstants.RIGHT); //initializing the labels
            textFieldsArray[i] = new JTextField(33); //initializing the text fields
            labelPanel.add(labelsArray[i]); //adds the labels to the labels panel
            textPanel.add(textFieldsArray[i]); //adds the text fields to the text panel
        }

        addToCenter(labelPanel); //adds the label panel to the frame
        addToCenter(textPanel); //adds the text panel to the frame
        addToCenter(errorPanel); //adds the error panel to the frame
    }


//Person class methods//

    //Method which receives a certain key and matches it with a different id field. Returns true if they are equal and false if not
    /**
    *Implementation of the {@link ClubAbstractEntity#match} method inorder to
    *check if such a person exists by a given key.
    *@param key A string entered to check whether such person exists.
    *@return If such person exists it will return true and if not it will return false.
    */
    @Override
    public boolean match(String key) {
        if (personalInfo[0].equals(key))  //checks if the ID is equal to the key
        {
            rollBack(); //activate the rollBack method
            return true;
        } else
        {
            rollBack(); //activate the rollBack method
            return false;
        }
    }



    //Method which compiles regular expressions and compares them to the information entered to the various textFields.
    //Return true if the corresponding string in the textField follows the regular expression pattern and false if it doesn't
    /**
    *Implementation of {@link ClubAbstractEntity#validateData}
    *It checks if the data inserted is written as the right pattern of tasks requierments using {@link java.util.regex} library.
    *@return If the the text follows all the regular expression patterns the method will reutrn true, else false.
    */
    @Override
    protected boolean validateData() {
        if(NightClubMgmtApp.doesExist(textFieldsArray[0].getText(),this)) //checks if the clubber already exists
        {
            errorLabel[0].setVisible(true); //makes the error label for the ID field visible
            JOptionPane.showMessageDialog(this, "A clubber with this ID already exists","Duplication error", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        for(int i=0;i<labelsData.length;i++)
        {
            Pattern pattern = Pattern.compile(regexArr[i]); //initializing a pattern from the regular expressions array
            Matcher matcher = pattern.matcher(textFieldsArray[i].getText()); //initializing a matcher from the text fields
            if(matcher.matches()==false)
            {
                errorLabel[i].setVisible(true); //makes the error label visible
                return false;
            }
            errorLabel[i].setVisible(false); //makes the error label unvisible
        }

        return true;
    }



    //Method which inserts the information written inside the textFields to the inner information fields.
    /**
    *Implementation of the {@link ClubAbstractEntity#commit} method.
    *Saves the data entered by user to the various information fields.
    */
    @Override
    protected void commit() {
        for(int i=0;i<labelsData.length;i++)
            personalInfo[i] = textFieldsArray[i].getText(); //inserts the information from the text fields to the personal info array
    }



    //Method which writes information from inner fields to the TextFields
    /**
    *Implementation of the {@link ClubAbstractEntity#rollBack} method.
    *It sets the text on the verious text fields from the inner information fields.
    */
    @Override
    protected void rollBack() {

        for(int i=0;i<labelsData.length;i++)
        {
            textFieldsArray[i].setText(personalInfo[i]); //writes the information that is saved in the object to the text fields
            errorLabel[i].setVisible(false); //makes the error label unvisible
        }
    }

} //End of class Person