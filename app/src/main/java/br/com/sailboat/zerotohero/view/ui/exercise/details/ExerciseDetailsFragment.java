package br.com.sailboat.zerotohero.view.ui.exercise.details;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import br.com.sailboat.zerotohero.R;
import br.com.sailboat.zerotohero.base.BaseFragment;
import br.com.sailboat.zerotohero.helper.ExtrasHelper;
import br.com.sailboat.zerotohero.model.Exercise;
import br.com.sailboat.zerotohero.view.ui.exercise.details.presenter.ExerciseDetailsPresenter;
import br.com.sailboat.zerotohero.view.ui.exercise.insert_or_edit.InsertOrEditExerciseActivity;

public class ExerciseDetailsFragment extends BaseFragment<ExerciseDetailsPresenter> implements ExerciseDetailsPresenter.View {

    private static final int REQUEST_EDIT_EXERCISE = 0;

    private Toolbar toolbar;
    private TextView tvName;
    private TextView tvWeight;
    private TextView tvSets;
    private TextView tvReps;
    private FloatingActionButton fab;

    @Override
    protected int getLayoutId() {
        return R.layout.frag_exercise_details;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_details, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_delete: {
                getPresenter().onClickMenuDelete();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    protected ExerciseDetailsPresenter newPresenterInstance() {
        return new ExerciseDetailsPresenter(this);
    }

    @Override
    protected void onActivityResultOk(int requestCode, Intent data) {
        switch (requestCode) {
            case REQUEST_EDIT_EXERCISE: {
                getPresenter().onResultOkEditExercise(data);
                return;
            }
        }
    }

    @Override
    protected void initViews(View view) {
        inflateViews(view);
        initToolbar();
        initFab();
    }

    private void initToolbar() {
        AppCompatActivity appCompatActivity = ((AppCompatActivity) getActivity());
        appCompatActivity.setSupportActionBar(toolbar);
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Exercise Details");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPresenter().onClickNavigation();
            }
        });
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void startEditExerciseActivity(Exercise exercise) {
        InsertOrEditExerciseActivity.start(this, exercise, REQUEST_EDIT_EXERCISE);
    }

    @Override
    public void closeActivityWithResultCanceled() {
        getActivity().setResult(Activity.RESULT_CANCELED);
        getActivity().finish();
    }

    @Override
    public void closeActivityWithResultOkAndDeleteExercise(Exercise exercise) {
        Intent intent = new Intent();
        ExtrasHelper.putExercise(exercise, intent);
        ExtrasHelper.deleteExercise(intent);

        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    public void setRepetition(String reps) {
        tvReps.setText(reps);
    }

    @Override
    public void setWeight(String weight) {
        tvWeight.setText(weight);
    }

    @Override
    public void setSet(String set) {
        tvSets.setText(set);
    }

    @Override
    public void setName(String name) {
       tvName.setText(name);
    }

    @Override
    public Context getActivityContext() {
        return getActivity();
    }

    private void inflateViews(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        tvName = (TextView) view.findViewById(R.id.frag_exercise_details__tv__name);
        tvWeight = (TextView) view.findViewById(R.id.frag_exercise_details__tv__weight);
        tvSets = (TextView) view.findViewById(R.id.frag_exercise_details__tv__sets);
        tvReps = (TextView) view.findViewById(R.id.frag_exercise_details__tv__reps);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
    }

    private void initFab() {
        fab.setImageResource(R.drawable.ic_edit_white_24dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPresenter().onClickEditExercise();
            }
        });
    }

}