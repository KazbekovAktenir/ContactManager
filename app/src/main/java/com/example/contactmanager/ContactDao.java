package com.example.contactmanager;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContactDao {
    @Insert
    void insert(Contact contact);

    @Update
    void update(Contact contact);

    @Delete
    void delete(Contact contact);

    @Query("SELECT * FROM Contact ORDER BY name ASC")
    List<Contact> getAll();

    @Query("SELECT * FROM Contact WHERE name LIKE '%' || :query || '%' OR phone LIKE '%' || :query || '%'")
    List<Contact> search(String query);

    @Query("SELECT * FROM Contact ORDER BY name ASC")
    List<Contact> getAllSortedAZ();

    @Query("SELECT * FROM Contact ORDER BY name DESC")
    List<Contact> getAllSortedZA();
    @Query("DELETE FROM Contact")
    void deleteAll();

    @Query("SELECT * FROM Contact WHERE tag = :tag ORDER BY name ASC")
    List<Contact> getByTag(String tag);

}
