package com.example.tripreminderiti.database.note;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NoteDao {
    @Query("SELECT * FROM Note where tripId = :id")
    List<Note> getNotes(int id);

    @Insert
    void insertNote(Note note);

    @Delete
    void delete(Note note);
}
