package models;

import javax.persistence.*;
import java.util.*;
import javax.persistence.*;

import com.avaje.ebean.Model;
import play.data.format.*;
import play.data.validation.*;

@Entity
public class Collection extends Model{

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public String id;

    public String name;

    public String hashTag;

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

}
