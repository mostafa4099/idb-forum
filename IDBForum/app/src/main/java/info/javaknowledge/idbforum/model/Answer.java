package info.javaknowledge.idbforum.model;

import java.io.Serializable;

/**
 * Created by User on 7/23/2018.
 */

public class Answer implements Serializable{
    private String ans_id, ques_id, ans_desc, up_vote, user_id, ans_time;

    public Answer(String ans_id, String ques_id, String ans_desc, String up_vote, String user_id, String ans_time) {
        this.ans_id = ans_id;
        this.ques_id = ques_id;
        this.ans_desc = ans_desc;
        this.up_vote = up_vote;
        this.user_id = user_id;
        this.ans_time = ans_time;
    }

    public Answer() {
    }

    public String getAns_id() {
        return ans_id;
    }

    public void setAns_id(String ans_id) {
        this.ans_id = ans_id;
    }

    public String getQues_id() {
        return ques_id;
    }

    public void setQues_id(String ques_id) {
        this.ques_id = ques_id;
    }

    public String getAns_desc() {
        return ans_desc;
    }

    public void setAns_desc(String ans_desc) {
        this.ans_desc = ans_desc;
    }

    public String getUp_vote() {
        return up_vote;
    }

    public void setUp_vote(String up_vote) {
        this.up_vote = up_vote;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAns_time() {
        return ans_time;
    }

    public void setAns_time(String ans_time) {
        this.ans_time = ans_time;
    }
}
