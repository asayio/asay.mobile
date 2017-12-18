package asay.asaymobile.model;

import java.util.ArrayList;

/**
 * Created by s123725 on 21/11/2017.
 */

public class UserDTO {
    private double mid;
    private String mname;
    private ArrayList<Long> msavedBills;

    public UserDTO(){
        //empty constructor
    }
    public UserDTO(final UserDTO userDTO){
        mid = userDTO.getid();
        mname = userDTO.getname();
        msavedBills = userDTO.getbillsSaved();
    }

    public UserDTO(final int id, final String name, ArrayList<Long> savedBills){
        this.mid = id;
        this.mname = name;
        this.msavedBills = savedBills;
    }

    public double getid() { return mid;}

    public void setid(double id) { this.mid = id; }

    public String getname() { return mname; }

    public void setname(String name) { this.mname = name; }

    public ArrayList<Long> getbillsSaved() { return msavedBills; }

    public void setbillsSaved(ArrayList<Long> savedBills) { this.msavedBills = savedBills; }

}
