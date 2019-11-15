package com.project.usmansh.ingrumidreal;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.Placeholder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;

import java.util.concurrent.ConcurrentSkipListMap;

public class TestAnimAct extends AppCompatActivity {

    ConstraintLayout layout;
    Placeholder placeholder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_anim);


            layout = (ConstraintLayout)findViewById(R.id.layout);
            placeholder = (Placeholder)findViewById(R.id.placeholder);

    }



    public void swapView(View v){

        TransitionManager.beginDelayedTransition(layout);
        placeholder.setContentId(v.getId());
    }
}
