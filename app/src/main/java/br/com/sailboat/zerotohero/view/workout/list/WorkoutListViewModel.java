package br.com.sailboat.zerotohero.view.workout.list;

import java.util.ArrayList;
import java.util.List;

import br.com.sailboat.zerotohero.model.sqlite.Workout;

public class WorkoutListViewModel {

    private final List<Workout> workoutList = new ArrayList<>();

    public List<Workout> getWorkoutList() {
        return workoutList;
    }

}
