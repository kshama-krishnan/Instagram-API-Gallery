package models;

import javax.persistence.*;
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public String id;

    public String contents;
}
