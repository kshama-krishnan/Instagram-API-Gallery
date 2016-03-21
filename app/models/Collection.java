package models;

import javax.persistence.*;
import java.util.*;
import javax.persistence.*;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import play.data.format.*;
import play.data.validation.*;

@Entity
public class Collection extends Model{

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public String id;

    @Constraints.Required
    public String name;


    public String hashTag;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-mm-dd")
    public Date startDate;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-mm-dd")
    public Date endDate;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getHashTag() {
        return hashTag;
    }

    public void setHashTag(String hashTag) {
        this.hashTag = hashTag;
    }
    public static Finder<String, Collection> find = new Finder<String,Collection>(Collection.class);

    public String validate() {
        if (hashTag == "") {
            return "Enter hashtag";
        }
        return null;
    }
}
