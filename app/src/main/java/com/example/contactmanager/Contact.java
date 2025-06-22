package com.example.contactmanager;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity
public class Contact implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String phone;
    public String email;
    public String note;
    public String tag;
}
