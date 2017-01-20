package br.com.sailboat.zerotohero.view.ui.workout.list;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.List;

import br.com.sailboat.canoe.base.BasePresenter;
import br.com.sailboat.canoe.helper.ApiLevelHelper;
import br.com.sailboat.zerotohero.helper.Extras;
import br.com.sailboat.zerotohero.model.Exercise;
import br.com.sailboat.zerotohero.model.Workout;
import br.com.sailboat.zerotohero.view.async_tasks.DeleteWorkoutAsyncTask;
import br.com.sailboat.zerotohero.view.async_tasks.LoadWorkoutsAsyncTask;
import br.com.sailboat.zerotohero.view.async_tasks.SaveWorkoutAsyncTask;

public class WorkoutListPresenter extends BasePresenter<WorkoutListPresenter.View> {

    private WorkoutListViewModel viewModel = new WorkoutListViewModel();

    public WorkoutListPresenter(WorkoutListPresenter.View view) {
        super(view);
    }

    @Override
    protected void onResumeFirstSession() {
        loadWorkouts();
    }

    @Override
    protected void onResumeAfterRestart() {
        getView().updateContentViews();
    }

    public void onClickNewWorkout() {
        getView().startNewWorkoutActivity();
    }

    public void onClickWorkout(int position) {
        Workout workout = getWorkouts().get(position);

        if (ApiLevelHelper.isLowerThan(Build.VERSION_CODES.LOLLIPOP)) {
            getView().startWorkoutDetailsActivity(workout);
        } else {
            getView().startWorkoutDetailsActivityWithAnimation(workout);
        }
    }

    public void onResultCanceledWorkoutDetails() {
        loadWorkouts();
    }

    public void onActivityResultOkInsertOrEditWorkout(Intent data) {
        Workout workout = Extras.getWorkout(data);
        List<Exercise> exercises = Extras.getExercises(data);

        getViewModel().getWorkoutList().add(workout);
        getView().updateContentViews();
        saveWorkout(workout, exercises);
    }

    public void onActivityResultOkWorkoutDetails(Intent data) {
        if (Extras.hasWorkoutToDelete(data)) {
            Workout workoutToDelete = Extras.getWorkout(data);
            removeWorkoutFromListAndDeleteFromDatabase(workoutToDelete);
        }
    }

    private void removeWorkoutFromListAndDeleteFromDatabase(Workout workoutToDelete) {
        List<Workout> workoutList = getViewModel().getWorkoutList();

        int position = -1;

        for (int i = 0; i < workoutList.size(); i++) {
            if (workoutList.get(i).getId() == workoutToDelete.getId()) {
                position = i;
                break;
            }
        }

        if (position != -1) {
            workoutList.remove(position);
            getView().updateWorkoutRemoved(position);
            deleteWorkout(workoutToDelete);
        } else {
            getView().showToast("Workout not found to delete");
        }
    }

    private void loadWorkouts() {
        new LoadWorkoutsAsyncTask(getContext(), new LoadWorkoutsAsyncTask.Callback() {

            @Override
            public void onSuccess(List<Workout> workoutList) {
                getViewModel().getWorkoutList().clear();
                getViewModel().getWorkoutList().addAll(workoutList);
                getView().updateContentViews();
            }

            @Override
            public void onFail(Exception e) {
                printLogAndShowDialog(e);
            }

        }).execute();
    }

    private void deleteWorkout(Workout workoutToDelete) {
        new DeleteWorkoutAsyncTask(getContext(), workoutToDelete, new DeleteWorkoutAsyncTask.Callback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onFail(Exception e) {
                printLogAndShowDialog(e);
            }
        }).execute();
    }

    private void saveWorkout(Workout workout, List<Exercise> exercises) {
        new SaveWorkoutAsyncTask(getContext(), workout, exercises, new SaveWorkoutAsyncTask.Callback() {

            @Override
            public void onSuccess() {
            }

            @Override
            public void onFail(Exception e) {
                printLogAndShowDialog(e);
            }

        }).execute();
    }


    private WorkoutListViewModel getViewModel() {
        return viewModel;
    }

    public List<Workout> getWorkouts() {
        return getViewModel().getWorkoutList();
    }


    public interface View extends BasePresenter.View{
        Context getActivityContext();
        void updateContentViews();
        void showToast(String message);
        void startNewWorkoutActivity();
        void startWorkoutDetailsActivity(Workout workout);
        void updateWorkoutRemoved(int position);
        void startWorkoutDetailsActivityWithAnimation(Workout workout);
    }

}
