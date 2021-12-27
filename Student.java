import javax.swing.*;
import java.util.regex.*;


//This class inherits from Person class type and implements another info field - StudentID
/**
*@author Gal Erez, Reef Rosenblat and Golan Parsha
*A class that extends from {@link Person} and creates a student object type.
*/
public class Student extends Person{

    private String studentID; //String which holds information about the Students ID
    private JTextField studentIDText; //JTextField component for Student ID
    private JLabel studentIDLabel; //JLabel for Student ID TextField


    //Constructor for student class type
    /**
    *Student Constructor - uses the super constructor to add the corresponding fields, and sets StudentID field.
    *@param id Contains the id of the student.
    *@param name Contains the private name of the student.
    *@param surname Contains the last name of the student.
    *@param phoneNum Contains the phone number of the student.
    *@param studentID Contains the ID number of the student.
    */
    public Student(String id,String name,String surname,String phoneNum,String studentID) {
        super(id, name, surname, phoneNum); //Call for the super constructor to fill the fields that are in person class
        this.studentID = studentID; //inserts the student ID that were given into the object's field
        //Initializing the frame for Student class
        setTitle("Student Clubber's Data");
        setSize(450,250);

        //Initializing the GUI components which are unique to Student class and adding them to the corresponding panels
        studentIDText = new JTextField(30);
        studentIDLabel = new JLabel("Student ID", SwingConstants.RIGHT);
        labelPanel.add(studentIDLabel);
        textPanel.add(studentIDText);

        labelPanel.add(studentIDLabel); //adds the student ID label to the label panel
        textPanel.add(studentIDText); //adds the student ID text field to the text panel

        //Adding all the panels to CenterPanel in ClubAbstractEntity class via addToCenter method
        addToCenter(labelPanel);
        addToCenter(textPanel);
        addToCenter(errorPanel);
    }


    //Method that's inherited from the Person class. Can match a key either by actual id or by the last five digits of Student ID.
    /**
    *Implementation of the {@link ClubAbstractEntity#match} method.
    *Checks by matching a given key to either id field or StudentID field.
    *@param key A string entered to check whether such student exists
    *@return If the student exists the method returns true, else returns false.
    */
    @Override
    public boolean match(String key)
    {
        if(key.equals("")) //If statement to check if nothing was entered to the StudentID textField.
            return false;

        String[] studentSplit = studentID.split("/"); //Using the split method in String, we set the last five digits of the students id
                                                      //so we can match it with the key if needed.

        if(super.match(key) && key.length()>5)  //If the key's length is longer than 5 digits, compare it to the normal id field.
        {
            rollBack();
            return true;
        }

        if((key.length()<11) ) //If key entered length is shorter than 11, it means that it refers to the StudentID field
        {
            if(key.length()<=5 && studentSplit[1].equals(key)) //Checks if the key is already the 5 last digits of student id
            {                                                  //If they match use rollBack and return true
                rollBack();
                return true;
            }

            String[] split = key.split("/"); //if the key entered isn't the last 5 digits of student id, then split
                                             //it and compare it to the last five digits of StudentID field.

            if (studentSplit[1].equals(split[1])) { //If they match use rollBack and return true.
                rollBack();
                return true;
            }
        }

        return false; //If all of the statements from before weren't entered then return false by default.
    }


    //Implementation of validateData method from ClubAbstractEntity class
    /**
    *Implementation of {@link ClubAbstractEntity#validateData} method.
    *Checks if the data inserted is written as the right pattern of regular expressions, by invoking the super method
    *and matching the regular expression corresponding with StudentID.
    *@return If the text fields follow the corrisponding regular expression it return true, else return false.
    */
    @Override
    protected boolean validateData() {

        if(super.validateData()==false) //Uses validateData from person to validate student class and person class common fields
            return false;

        if(NightClubMgmtApp.doesExist(studentIDText.getText(), this)) //Calls doesExist method from NightCLubMgmtApp class to
        {                                                             //check if there are any id/studentID/personalNum duplicates
            errorLabel[4].setVisible(true);                           //and shows a message if it does happen.
            JOptionPane.showMessageDialog(this, "A clubber with this Student ID already exists","Duplication error", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        String regeX = "^[A-Z]{3}\\/[1-9]{1}\\d{4}$"; //Regular expression for StudentID field.
        Pattern pattern = Pattern.compile(regeX); //Initialize a pattern for the regex.
        Matcher matcher = pattern.matcher(this.studentIDText.getText()); //Initialize a matcher to check is the information entered
        if(matcher.matches()==false)                                     //to the text field is following the correct pattern.
        {
            this.errorLabel[4].setVisible(true); //If the info doesn't follow the correct pattern, then an error label will appear
            return false;                        //next to the false field.
        }
        else if(matcher.matches()==true)         //If the validation is successful then turn off the error label and return true.
        {
            this.errorLabel[4].setVisible(false);
            return true;
        }
        return true; //In the case when the validation was successful all the way,return true.
    }


    //Implementation of rollBack method from ClubAbstractEntity class
    /**
    *Implementation of {@link ClubAbstractEntity#rollBack} method.
    *Fills the text fields with data from inner fields by invoking the super method.
    *Does the same to StudentID inner field.
    */
    @Override
    protected void rollBack() {
        super.rollBack(); //Use the rollBack method from person class to place on screen students and persons common fields.
        studentIDText.setText(studentID); //Set the text on StudentTextField as the text.
        errorLabel[4].setVisible(false);
    }


    //Implementation of commit method from ClubAbstractEntity
    /**
    *Implementation of {@link ClubAbstractEntity#commit} method.
    *Saves the data entered by user to the inner fields by invoking the super method.
    *Also saves StudentID to the inner field.
    */
    @Override
    protected void commit() {
        super.commit(); //Use person class commit method to save the data inside their common textFields to the inner fields
        studentID = studentIDText.getText(); //Set the inner StudentID field's data as the data written inside StudentIDText field.
    }
}