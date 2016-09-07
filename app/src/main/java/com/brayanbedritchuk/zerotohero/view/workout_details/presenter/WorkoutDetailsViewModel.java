package com.brayanbedritchuk.zerotohero.view.workout_details.presenter;

import com.brayanbedritchuk.zerotohero.model.Exercise;
import com.brayanbedritchuk.zerotohero.model.Workout;

import java.util.ArrayList;
import java.util.List;

public class WorkoutDetailsViewModel {

    private boolean firstSession = true;
    private Workout workout;
    private final List<Exercise> exerciseList;

    public WorkoutDetailsViewModel() {
        this.exerciseList = new ArrayList<>();
    }

    public boolean isFirstSession() {
        return firstSession;
    }

    public void setFirstSession(boolean firstSession) {
        this.firstSession = firstSession;
    }

    public Workout getWorkout() {
        return workout;
    }

    public void setWorkout(Workout workout) {
        this.workout = workout;
    }

    public List<Exercise> getExerciseList() {
        return exerciseList;
    }

}