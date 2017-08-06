package com.lbb.lbb.ui.galleryphotos;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;

import com.lbb.lbb.R;
import com.lbb.lbb.ui.customviews.Preview;
import com.lbb.lbb.ui.base.BaseFragment;

import butterknife.BindView;

/**
 * Created by hitesh on 8/2/17.
 */

public class ImageActivity extends AppCompatActivity {

    Preview preview;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        BaseFragment targetFragment = null;
        targetFragment = PhotoListFragment.newInstance(0);
        fragmentManager.beginTransaction().replace(R.id.container, targetFragment).commit();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

    }

    public void popBackStack() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            onBackPressed();
        }
    }

}
