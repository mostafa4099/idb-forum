package info.javaknowledge.idbforum.model;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by User on 7/20/2018.
 */

public class Question implements Serializable{
    private int ques_id, up_vote, comment_count ;
    private String  ques_title, ques_desc,  ques_date, image_url, user_id;
    private List<Tag> tags;

    public Question(int ques_id, int up_vote, int comment_count, String user_id, String ques_title, String ques_desc, String ques_date, String image_url, List<Tag> tags) {
        this.ques_id = ques_id;
        this.up_vote = up_vote;
        this.comment_count = comment_count;
        this.user_id = user_id;
        this.ques_title = ques_title;
        this.ques_desc = ques_desc;
        this.ques_date = ques_date;
        this.image_url = image_url;
        this.tags = tags;
    }

    public Question() {
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public int getQues_id() {
        return ques_id;
    }

    public void setQues_id(int ques_id) {
        this.ques_id = ques_id;
    }

    public int getUp_vote() {
        return up_vote;
    }

    public void setUp_vote(int up_vote) {
        this.up_vote = up_vote;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getQues_title() {
        return ques_title;
    }

    public void setQues_title(String ques_title) {
        this.ques_title = ques_title;
    }

    public String getQues_desc() {
        return ques_desc;
    }

    public void setQues_desc(String ques_desc) {
        this.ques_desc = ques_desc;
    }

    public String getQues_date() {
        return ques_date;
    }

    public void setQues_date(String ques_date) {
        this.ques_date = ques_date;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
