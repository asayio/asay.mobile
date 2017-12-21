package asay.asaymobile.model;

import java.util.ArrayList;

/**
 * Created by s123725 on 15/12/2017.
 */

public class BillDTO {
    public String createdBy;
    public String deadline;
    public String department;
    public int forumId;
    public int id;
    public String number;
    public String title;
    public String titleShort;
    public String resume;
    public ArrayList<Vote> votes;

    public BillDTO(){

    }

    public BillDTO(String createdBy, String deadline, String department, int forumId, int id, String number, String title, String titleShort, String resume,ArrayList<Vote> votes){
        this.createdBy = createdBy;
        this.deadline = deadline;
        this.department = department;
        this.forumId = forumId;
        this.id = id;
        this.number = number;
        this.title = title;
        this.titleShort = titleShort;
        this.resume = resume;
        this.votes = votes;
    }

    public BillDTO(BillDTO billDTO){
        this.createdBy = billDTO.createdBy;
        this.deadline = billDTO.deadline;
        this.department = billDTO.department;
        this.forumId = billDTO.forumId;
        this.id = billDTO.id;
        this.number = billDTO.number;
        this.title = billDTO.title;
        this.titleShort = billDTO.titleShort;
        this.resume = billDTO.resume;
        this.votes = billDTO.votes;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getForumId() {
        return forumId;
    }

    public void setForumId(int forumId) {
        this.forumId = forumId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleShort() {
        return titleShort;
    }

    public void setTitleShort(String titleShort) {
        this.titleShort = titleShort;
    }

    public String getResume(){
        return resume;
    }

    public void setResume(String resume){
        this.resume = resume;
    }

    public ArrayList<Vote> getVotes() {
        return votes;
    }

    public void setVotes(ArrayList<Vote> votes) {
        this.votes = votes;
    }



    public static class Vote{
        public int id;
        public String userHash;
        public ArgumentType vote;
        public Vote(){}

        public Vote(int id, String userhash, ArgumentType argumentType){
            this.id = id;
            this.userHash = userhash;
            this.vote = argumentType;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUserHash() {
            return userHash;
        }

        public void setUserHash(String userHash) {
            this.userHash = userHash;
        }

        public ArgumentType getVote() {
            return vote;
        }

        public void setVote(ArgumentType vote) {
            this.vote = vote;
        }


    }
}

