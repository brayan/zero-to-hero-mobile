package com.brayanbedritchuk.zerotohero.view.workout.insert_or_edit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.brayanbedritchuk.zerotohero.R;
import com.brayanbedritchuk.zerotohero.base.BaseFragment;
import com.brayanbedritchuk.zerotohero.helper.DialogHelper;
import com.brayanbedritchuk.zerotohero.helper.ExtrasHelper;
import com.brayanbedritchuk.zerotohero.helper.UIHelper;
import com.brayanbedritchuk.zerotohero.model.Exercise;
import com.brayanbedritchuk.zerotohero.model.Workout;
import com.brayanbedritchuk.zerotohero.view.adapter.ExercisesListAdapter;
import com.brayanbedritchuk.zerotohero.view.exercise.chooser.ExerciseChooserActivity;
import com.brayanbedritchuk.zerotohero.view.workout.insert_or_edit.presenter.InsertOrEditWorkoutPresenter;
import com.brayanbedritchuk.zerotohero.view.workout.insert_or_edit.presenter.InsertOrEditWorkoutView;

import java.util.ArrayList;
import java.util.List;

public class InsertOrEditWorkoutFragment extends BaseFragment<InsertOrEditWorkoutPresenter> implements InsertOrEditWorkoutView {

    private static final int REQUEST_EXERCISE_CHOOSER = 0;

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private FloatingActionButton fabAddExercises;
    private EditText etWorkoutName;

    private View emptyList;

    private ExercisesListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_insert_or_edit_workout, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_insert_edit, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_insert_edit_save: {
                getPresenter().onClickMenuSave();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    protected boolean hasMenu() {
        return true;
    }

    @Override
    protected void getExtrasFromIntent(Intent intent) {
        getPresenter().onReceiveWorkout(ExtrasHelper.getWorkout(intent));
    }

    @Override
    protected InsertOrEditWorkoutPresenter newPresenterInstance() {
        return new InsertOrEditWorkoutPresenter(this);
    }

    @Override
    protected void onActivityResultOk(int requestCode, Intent data) {
        switch (requestCode) {
            case REQUEST_EXERCISE_CHOOSER: {
                getPresenter().onResultOkExerciseChooser(data);
            }
        }
    }

    private void initViews(View view) {
        inflateViews(view);
        initToolbar();
        initRecyclerView();
        initFab();
        initEmptyView();
    }

    @Override
    public void updateExercisesListAndVisibility() {
        adapter.notifyDataSetChanged();
        updateVisibilityOfViews();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void startExercisesChooserActivity(ArrayList<Exercise> exercises) {
        ExerciseChooserActivity.start(this, exercises, REQUEST_EXERCISE_CHOOSER);
    }

    @Override
    public String getTextFromWorkoutName() {
        return etWorkoutName.getText().toString();
    }

    @Override
    public void showDialog(String message) {
        DialogHelper.showErrorMessage(getFragmentManager(), message);
    }

    @Override
    public void closeActivityWithResultCanceled() {
        getActivity().setResult(Activity.RESULT_CANCELED);
        getActivity().finish();
    }

    @Override
    public void closeActivityWithResultOk(Workout workout, List<Exercise> exercises) {
        Intent intent = new Intent();
        ExtrasHelper.putWorkout(workout, intent);
        ExtrasHelper.putExercises(exercises, intent);

        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    public void updateWorkoutNameView(String name) {
        etWorkoutName.setText(name);
        etWorkoutName.setSelection(etWorkoutName.getText().length());
    }

    @Override
    public void hideKeyboard() {
        UIHelper.hideKeyboard(getActivity());
    }

    @Override
    public void updateToolbarTitle(String title) {
        toolbar.setTitle(title);
    }

    @Override
    public Context getActivityContext() {
        return getActivity();
    }

    private void inflateViews(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        fabAddExercises = (FloatingActionButton) view.findViewById(R.id.fab);
        etWorkoutName = (EditText) view.findViewById(R.id.frag_insert_or_edit_workout__et__name);
        emptyList = view.findViewById(R.id.empty_list);
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ExercisesListAdapter(getPresenter().getExercises(), null);
        recyclerView.setAdapter(adapter);
    }

    private void initToolbar() {
        initAppCompatActivity();
    }

    protected void initAppCompatActivity() {
        AppCompatActivity appCompatActivity = ((AppCompatActivity) getActivity());
        appCompatActivity.setSupportActionBar(toolbar);
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    private void initFab() {
        fabAddExercises.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPresenter().onClickAddExercises();
            }
        });
    }

    private void initEmptyView() {
        ImageView imgEmpty = (ImageView) emptyList.findViewById(R.id.empty_list_image);
        imgEmpty.setColorFilter(ContextCompat.getColor(getActivity(), R.color.teal_300), PorterDuff.Mode.SRC_ATOP);

        TextView tvTitle = (TextView) emptyList.findViewById(R.id.empty_list_title);
        tvTitle.setText("No exercises");

        TextView tvMessage = (TextView) emptyList.findViewById(R.id.empty_list_message);
        tvMessage.setText("Add exercises by tapping the + button");

        emptyList.setVisibility(View.GONE);
    }

    @Override
    public void updateVisibilityOfViews() {
        boolean emptyList = getPresenter().getExercises().isEmpty();

        if (emptyList) {
            recyclerView.setVisibility(View.GONE);
            this.emptyList.setVisibility(View.VISIBLE);

        } else {
            this.emptyList.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

    }

}
