package info.javaknowledge.idbforum.model;

import java.io.Serializable;

/**
 * Created by User on 7/22/2018.
 */

public class Tag implements Serializable {
    String tag_id, tag_name, tag_color;

    public Tag(String tag_id, String tag_name, String tag_color) {
        this.tag_id = tag_id;
        this.tag_name = tag_name;
        this.tag_color = tag_color;
    }

    public Tag() {
    }

    public String getTag_color() {
        return tag_color;
    }

    public void setTag_color(String tag_color) {
        this.tag_color = tag_color;
    }

    public String getTag_id() {
        return tag_id;
    }

    public void setTag_id(String tag_id) {
        this.tag_id = tag_id;
    }

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }
}
