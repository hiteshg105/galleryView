package com.lbb.lbb.ui.cloudinaryphotos;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.lbb.lbb.R;
import com.lbb.lbb.adapter.ImageViewAdapter;
import com.lbb.lbb.dagger.component.DaggerApplicationComponent;
import com.lbb.lbb.dagger.module.PresenterModule;
import com.lbb.lbb.ui.galleryphotos.ImageActivity;
import com.lbb.lbb.util.presenter.cloudinary.HomePageContract;
import com.lbb.lbb.util.presenter.cloudinary.HomePagePresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements HomePageContract.View {

    @BindView(R.id.recycler_home)
    RecyclerView      recyclerView;
    @Inject
    HomePagePresenter homePagePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerApplicationComponent.builder().presenterModule(new PresenterModule(this)).build().inject(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ImageViewAdapter imageViewAdapter = new ImageViewAdapter(this);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, 1));
        recyclerView.setAdapter(imageViewAdapter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homePagePresenter.openGallery();
            }
        });

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showGalleryPhotos() {
        startActivity(new Intent(MainActivity.this, ImageActivity.class));
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }
}
