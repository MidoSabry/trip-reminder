package com.example.tripreminderiti.database.trip;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TripDao {
    @Query("SELECT * FROM Trip")
    List<Trip> getAll();

    @Insert
    void insertTrip(Trip trip);

    @Delete
    void delete(Trip trip);

    @Query("update Trip set name = :name , startPoint = :startPoint , endPoint = :endPoint , date = :date , time = :time where id = :id ")
    void updateTrip(String name, String startPoint, String endPoint, String date, String time, int id);
}
