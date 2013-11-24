package program4;

import javax.swing.JOptionPane;

public class StudentListing {

    private String name;  // key field
    private String idNumber;
    private String gpa;

    public StudentListing(String n, String a, String num) {
        name = n;
        idNumber = a;
        gpa = num;
    }

    public StudentListing() {
        name = "UNDEFINED9999999";
    }

    @Override
    public String toString() {
        return ("Name is " + name
                + "\nID Number is " + idNumber
                + "\nGPA is " + gpa);
    }

    public StudentListing deepCopy() // a clone method
    {
        StudentListing clone = new StudentListing(name, idNumber, gpa);
        return clone;
    }

    public int compareTo(String targetKey) {
        return (name.compareTo(targetKey));
    }

    public String getKey() {
        return name;
    }

    public void input() {
        name = JOptionPane.showInputDialog("Enter a name");
        idNumber = JOptionPane.showInputDialog("Enter " + name + "'s ID number");
        gpa = JOptionPane.showInputDialog("Enter " + name + "'s GPA");
    }
}
