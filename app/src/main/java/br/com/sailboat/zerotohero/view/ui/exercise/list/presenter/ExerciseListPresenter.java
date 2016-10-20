package br.com.sailboat.zerotohero.view.ui.exercise.list.presenter;

import android.content.Context;
import android.content.Intent;

import java.util.List;

import br.com.sailboat.zerotohero.base.BasePresenter;
import br.com.sailboat.zerotohero.helper.ExtrasHelper;
import br.com.sailboat.zerotohero.helper.ListHelper;
import br.com.sailboat.zerotohero.helper.LogHelper;
import br.com.sailboat.zerotohero.model.Exercise;
import br.com.sailboat.zerotohero.view.async_tasks.DeleteExerciseAsyncTask;
import br.com.sailboat.zerotohero.view.async_tasks.LoadExercisesAsyncTask;
import br.com.sailboat.zerotohero.view.async_tasks.SaveExerciseAsyncTask;
import br.com.sailboat.zerotohero.view.ui.exercise.list.view_model.ExerciseListViewModel;

public class ExerciseListPresenter extends BasePresenter {

    private View view;
    private ExerciseListViewModel viewModel;

    public ExerciseListPresenter(View view) {
        setView(view);
        setViewModel(new ExerciseListViewModel());
    }

    @Override
    protected void onResumeFirstSession() {
        loadExercises();
    }

    @Override
    protected void postResume() {
        getView().updateContentViews();
    }

    public void onClickExercise(int position) {
        Exercise exercise = getExercises().get(position);
        getView().startExerciseDetailsActivity(exercise);
    }

    public void onClickNewExercise() {
        getView().startNewExerciseActivity();
    }

    public void onActivityResultCanceledExerciseDetails() {
        loadExercises();
    }

    public void onActivityResultOkInsertOrEditExercise(Intent data) {
        Exercise exercise = ExtrasHelper.getExercise(data);

        getViewModel().getExercises().add(exercise);
        getView().updateContentViews();
        saveExercise(exercise);
    }

    public void onActivityResultOkExerciseDetails(Intent data) {
        if (ExtrasHelper.hasExerciseToDelete(data)) {
            Exercise exerciseToDelete = ExtrasHelper.getExercise(data);
            removeExerciseFromListAndDeleteFromDatabase(exerciseToDelete);
        }
    }

    private void loadExercises() {

        new LoadExercisesAsyncTask(getContext(), new LoadExercisesAsyncTask.Callback() {

            @Override
            public void onSuccess(List<Exercise> exercises) {
                ListHelper.clearAndAdd(exercises, getViewModel().getExercises());
                getView().updateContentViews();
            }

            @Override
            public void onFail(Exception e) {
                LogHelper.printExceptionLog(e);
                getView().showToast(e.getMessage());
            }

        }).execute();

    }

    private void saveExercise(Exercise exercise) {

        new SaveExerciseAsyncTask(getContext(), exercise, new SaveExerciseAsyncTask.Callback() {

            @Override
            public void onSuccess() {
            }

            @Override
            public void onFail(Exception e) {
                LogHelper.printExceptionLog(e);
                getView().showDialog(e.getMessage());
            }

        }).execute();

    }

    private void removeExerciseFromListAndDeleteFromDatabase(Exercise exerciseToDelete) {
        List<Exercise> exerciseList = getViewModel().getExercises();

        int position = -1;

        for (int i = 0; i < exerciseList.size(); i++) {
            if (exerciseList.get(i).getId() == exerciseToDelete.getId()) {
                position = i;
                break;
            }
        }

        if (position != -1) {
            exerciseList.remove(position);
            getView().updateExerciseRemoved(position);
            deleteExercise(exerciseToDelete);
        } else {
            getView().showToast("Exercise not found to delete");
        }
    }

    private void deleteExercise(Exercise exerciseToDelete) {

        new DeleteExerciseAsyncTask(getContext(), exerciseToDelete, new DeleteExerciseAsyncTask.Callback() {

            @Override
            public void onSuccess() {
            }

            @Override
            public void onFail(Exception e) {
                LogHelper.printExceptionLog(e);
                getView().showToast(e.getMessage());
            }

        }).execute();

    }

    public Context getContext() {
        return getView().getActivityContext();
    }

    public ExerciseListViewModel getViewModel() {
        return viewModel;
    }

    public void setViewModel(ExerciseListViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public List<Exercise> getExercises() {
        return getViewModel().getExercises();
    }



    public interface View {
        Context getActivityContext();
        void updateContentViews();
        void showToast(String message);
        void startExerciseDetailsActivity(Exercise exercise);
        void startNewExerciseActivity();
        void showDialog(String message);
        void updateExerciseRemoved(int position);
    }
}
