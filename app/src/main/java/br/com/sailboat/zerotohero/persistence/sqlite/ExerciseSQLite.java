package br.com.sailboat.zerotohero.persistence.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import br.com.sailboat.canoe.exception.EntityNotFoundException;
import br.com.sailboat.zerotohero.base.BaseSQLite;
import br.com.sailboat.zerotohero.model.Exercise;

public class ExerciseSQLite extends BaseSQLite {

    public ExerciseSQLite(Context context) {
        super(context);
    }

    @Override
    public String getQueryCreateTable() {
        StringBuilder sb = new StringBuilder();
        sb.append(" CREATE TABLE Exercise ( ");
        sb.append(" id INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sb.append(" name TEXT, ");
        sb.append(" weight REAL, ");
        sb.append(" exerciseSet INTEGER, ");
        sb.append(" repetition INTEGER ");
        sb.append(" ); ");

        return  sb.toString();
    }

    public Exercise getExerciseById(long exerciseId) throws EntityNotFoundException {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT Exercise.* FROM Exercise ");
        sb.append(" WHERE Exercise.id = " + exerciseId);

        Cursor cursor = performQuery(sb.toString());

        if (cursor.moveToNext()) {
            Exercise exercise = buildFromCursor(cursor);
            cursor.close();
            return exercise;
        }

        throw new EntityNotFoundException();
    }

    public List<Exercise> getAll() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT Exercise.* FROM Exercise ");

        return getExerciseList(sb.toString());
    }

    public List<Exercise> getFromWorkout(long workoutId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT Exercise.* FROM Exercise ");
        sb.append(" INNER JOIN WorkoutExercise ");
        sb.append(" ON Exercise.id = WorkoutExercise.exerciseId ");
        sb.append(" WHERE WorkoutExercise.workoutId = " + workoutId);

        return getExerciseList(sb.toString());
    }

    public long save(Exercise exercise) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" INSERT INTO Exercise ");
        sb.append(" (name, weight, exerciseSet, repetition) ");
        sb.append(" VALUES (?, ?, ?, ?); ");

        SQLiteStatement statement = compileStatement(sb.toString());
        bindExerciseToInsertStatement(exercise, statement);

        long id = executeInsert(statement);

        return id;
    }

    public void update(Exercise exercise) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" UPDATE Exercise SET ");
        sb.append(" name = ?, ");
        sb.append(" weight = ?, ");
        sb.append(" exerciseSet = ?, ");
        sb.append(" repetition = ? ");
        sb.append(" WHERE id = ? ");

        SQLiteStatement statement = compileStatement(sb.toString());
        bindExerciseToUpdateStatement(exercise, statement);

        executeUpdateOrDelete(statement);
    }

    public void delete(long exerciseId) throws Exception {
        String query = "DELETE FROM Exercise WHERE Exercise.id = ?";
        SQLiteStatement statement = compileStatement(query);
        statement.bindLong(1, exerciseId);

        executeUpdateOrDelete(statement);
    }

    private List<Exercise> getExerciseList(String query) {
        Cursor cursor = performQuery(query);
        List<Exercise> exercises = new ArrayList<>();

        while (cursor.moveToNext()) {
            Exercise exercise = buildFromCursor(cursor);
            exercises.add(exercise);
        }

        cursor.close();

        return exercises;
    }

    private Exercise buildFromCursor(Cursor cursor) {
        Exercise exercise = new Exercise();
        exercise.setId(cursor.getLong(cursor.getColumnIndexOrThrow("id")));
        exercise.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
        exercise.setRepetition(cursor.getInt(cursor.getColumnIndexOrThrow("repetition")));
        exercise.setSet(cursor.getInt(cursor.getColumnIndexOrThrow("exerciseSet")));
        exercise.setWeight(cursor.getDouble(cursor.getColumnIndexOrThrow("weight")));

        return exercise;
    }

    private void bindExerciseToInsertStatement(Exercise exercise, SQLiteStatement statement) {
        statement.bindString(1, exercise.getName());
        statement.bindDouble(2, exercise.getWeight());
        statement.bindLong(3, exercise.getSet());
        statement.bindLong(4, exercise.getRepetition());
    }

    private void bindExerciseToUpdateStatement(Exercise exercise, SQLiteStatement statement) {
        bindExerciseToInsertStatement(exercise, statement);
        statement.bindLong(5, exercise.getId());
    }

}
