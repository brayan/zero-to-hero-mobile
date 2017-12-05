package br.com.sailboat.zerotohero.view.workout.list;

import java.util.ArrayList;
import java.util.List;

import br.com.sailboat.canoe.base.BasePresenter;
import br.com.sailboat.canoe.helper.AsyncHelper;
import br.com.sailboat.zerotohero.model.sqlite.Workout;
import br.com.sailboat.zerotohero.persistence.sqlite.WorkoutSQLite;

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
        updateContentViews();
    }

    @Override
    public void onClickFab() {
        getView().startNewWorkoutActivity();
    }

    @Override
    protected void onQueryTextChange() {
        loadWorkouts();
    }

    public void onClickWorkout(int position) {
        Workout workout = getWorkouts().get(position);
        getView().startWorkoutDetailsActivity(workout.getId());
    }

    public void postActivityResult() {
        loadWorkouts();
    }

    public List<Workout> getWorkouts() {
        return viewModel.getWorkoutList();
    }

    private void loadWorkouts() {
        AsyncHelper.execute(new AsyncHelper.Callback() {

            List<Workout> workouts = new ArrayList<>();

            @Override
            public void doInBackground() throws Exception {
                workouts =  WorkoutSQLite.newInstance(getContext()).getAll(getSearchText());
            }

            @Override
            public void onSuccess() {
                viewModel.getWorkoutList().clear();
                viewModel.getWorkoutList().addAll(workouts);
                updateContentViews();
            }

            @Override
            public void onFail(Exception e) {
                printLogAndShowDialog(e);
                updateContentViews();
            }
        });

    }

    private void updateContentViews() {
        getView().updateRecycler();
        updateVisibilityOfViews();
    }

    private void updateVisibilityOfViews() {
        if (getWorkouts().isEmpty()) {
            getView().hideRecycler();
            getView().showEmptyView();
        } else {
            getView().showRecycler();
            getView().hideEmptyView();
        }
    }


    public interface View extends BasePresenter.View{
        void startNewWorkoutActivity();
        void startWorkoutDetailsActivity(long workoutId);
    }

}
