package me.bakumon.moneykeeper.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Flowable;
import me.bakumon.moneykeeper.database.entity.Project;

@Dao
public interface ProjectDao {
    @Insert
    void insertProject(Project...projects);

    @Update
    void updateProject(Project... projects);

    @Delete
    void deleteProject(Project...projects);

    @Query("SELECT * FROM Project WHERE state=0 ORDER BY ranking")
    Flowable <List<Project>> getAllProjects();

    @Query("SELECT count(project.id) FROM Project")
    long getProjectCount();

    @Query("SELECT * FROM Project WHERE name = :name")
    Project getProjectByName(String name);
}
