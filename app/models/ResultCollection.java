package models;

import com.avaje.ebean.Model;
import com.avaje.ebean.PagedList;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import scala.collection.immutable.Page;

import java.util.*;
import javax.persistence.*;

import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;
/**
 * Created by kshama on 3/14/2016.
 */

@Entity
public class ResultCollection extends Model {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public String id;

    public String cName;

    public String instagramUsername;

    public String URLName;

    public String img;

    @JsonProperty("dateTime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    public Date tagTime;

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getInstagramUsername() {
        return instagramUsername;
    }

    public void setInstagramUsername(String instagramUsername) {
        this.instagramUsername = instagramUsername;
    }

    public String getURLName() {
        return URLName;
    }

    public void setURLName(String URLName) {
        this.URLName = URLName;
    }


    public Date getTagTime() {
        return tagTime;
    }

    public void setTagTime(Date tagTime) {
        this.tagTime = tagTime;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public static Finder<String, ResultCollection> find = new Finder<String,ResultCollection>(ResultCollection.class);

    public static PagedList<ResultCollection> page(int page,String cName) {
        return
                find.where()
                        .ilike("cName",cName)
                        .findPagedList(page,9);
    }
}
