package br.com.sailboat.zerotohero.view.ui.exercise.selector;

import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

import br.com.sailboat.zerotohero.base.BaseActivity;
import br.com.sailboat.zerotohero.helper.Extras;
import br.com.sailboat.zerotohero.model.Exercise;

public class ExerciseChooserActivity extends BaseActivity<ExerciseChooserFragment> {

    public static void start(Fragment fragment, ArrayList<Exercise> exercises, int requestCode) {
        Intent starter = new Intent(fragment.getActivity(), ExerciseChooserActivity.class);
        Extras.putExercises(exercises, starter);
        fragment.startActivityForResult(starter, requestCode);
    }

    @Override
    protected ExerciseChooserFragment newFragmentInstance() {
        return new ExerciseChooserFragment();
    }
}
