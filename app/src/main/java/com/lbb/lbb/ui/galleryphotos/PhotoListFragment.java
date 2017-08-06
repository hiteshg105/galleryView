package com.lbb.lbb.ui.galleryphotos;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lbb.lbb.R;
import com.lbb.lbb.adapter.PhotoAdapter;
import com.lbb.lbb.dagger.component.DaggerApplicationComponent;
import com.lbb.lbb.dagger.module.PresenterModule;
import com.lbb.lbb.data.callbacks.ImageUploadCallback;
import com.lbb.lbb.data.model.PhotoItem;
import com.lbb.lbb.manager.PhotoManager;
import com.lbb.lbb.ui.base.BaseFragment;
import com.lbb.lbb.ui.customviews.Preview;
import com.lbb.lbb.util.Utils;
import com.lbb.lbb.util.presenter.gallery.GalleryPageContract;
import com.lbb.lbb.util.presenter.gallery.GalleryPresenter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.lbb.lbb.util.Utils.TAKE_PICTURE;

public class PhotoListFragment extends BaseFragment
        implements LoaderManager.LoaderCallbacks<List<PhotoItem>>, View.OnClickListener, View.OnLongClickListener, GalleryPageContract.View {

    protected PhotoAdapter         mAdapter;
    protected ArrayList<PhotoItem> mPhotoListItem;
    protected ProgressDialog       mLoadingProgressDialog;

    @BindView(R.id.empty)
    protected TextView             mEmptyTextView;
    @BindView(R.id.grid_view)
    protected GridView             mGridView;
    @BindView(R.id.preview_camera)
    protected Preview              preview;
    @Inject
    protected GalleryPresenter     presenter;
    ProgressBar                    pb;
    File                           output;
    PhotoListFragment              fragment;

    public PhotoListFragment() {
        super();
    }

    int                 countUploaded       = 0;
    int                 totalUploaded       = 0;

    ImageUploadCallback imageUploadCallback= new ImageUploadCallback() {

        @Override
        public void onNext() {
            countUploaded++;
            if (countUploaded == totalUploaded)
                onCompleted();
            Log.d("CountNext", String.valueOf(countUploaded));
        }

        @Override
        public void onCompleted() {
            mLoadingProgressDialog.dismiss();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Utils.showMessage(getActivity(), "Upload Completed");
                    mAdapter.resetSelectedItems();
                    updateDoneButton();
                }
            });
        }

        @Override
        public void onError() {
            countUploaded++;
            Log.d("CountError", String.valueOf(countUploaded));
            if (countUploaded == totalUploaded)
                onCompleted();
        }
    };


    public static PhotoListFragment newInstance(int sectionNumber) {
        PhotoListFragment fragment = new PhotoListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerApplicationComponent.builder().presenterModule(new PresenterModule(getActivity(), this)).build().inject(this);
        setHasOptionsMenu(true);
        fragment = this;
        mPhotoListItem = new ArrayList<>();
        mAdapter = new PhotoAdapter(this, R.layout.item_recycler_view, mPhotoListItem, false);
        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions( new String[] { Manifest.permission.READ_EXTERNAL_STORAGE }, Utils.PERMISSIONS_REQUEST_WRITE_STORAGE);
        } else {
            getLoaderManager().initLoader(0, null, this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_photo_gallery, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        preview.setUpSurface(getActivity());
        // Set the mAdapter
        ((AdapterView<ListAdapter>) mGridView).setAdapter(mAdapter);
        preview.setOnClickListener(this);
        // Show the empty text / message.
        resolveEmptyText();
    }

    /**
     * Used to show a generic empty text warning. Override in inheriting classes.
     */
    @Override
    public void resolveEmptyText() {
        if (mAdapter.isEmpty()) {
            mEmptyTextView.setVisibility(View.VISIBLE);
            setEmptyText();
        } else {
            mEmptyTextView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        cancelProgressDialog();
    }

    @Override
    public void onPause() {
        super.onPause();
        cancelProgressDialog();
    }

    @Override
    public void onStop() {
        super.onStop();
        preview.releaseCamera();
        cancelProgressDialog();

    }

    public void setEmptyText() {
        mEmptyTextView.setText("No Photos!");
    }

    /**
     * Loader Handlers for loading the photos in the background.
     */
    @Override
    public Loader<List<PhotoItem>> onCreateLoader(int id, Bundle args) {
        return presenter.getAsyncLoader();

    }

    @Override
    public void onLoadFinished(Loader<List<PhotoItem>> loader, List<PhotoItem> data) {
        presenter.onLoadFinished(data);

    }

    @Override
    public void onLoaderReset(Loader<List<PhotoItem>> loader) {
        mPhotoListItem.clear();
        mAdapter.notifyDataSetChanged();
        resolveEmptyText();
        cancelProgressDialog();
    }

    @Override
    public void cancelProgressDialog() {
        if (mLoadingProgressDialog != null) {
            if (mLoadingProgressDialog.isShowing()) {
                mLoadingProgressDialog.cancel();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PICTURE) {

            //Run Media Scanner
            MediaScannerConnection.scanFile(getActivity(), new String[] { output.getAbsolutePath() }, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {

                }
            });
            restartLoader();
        } else {
            Utils.showMessage(getActivity(), "Picture Not Taken");
        }
    }

    public void restartLoader() {
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public void onClick(View v) {
        presenter.getGalleryPhotos();
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.image_menu, menu);
        if (mAdapter.getmSparseBooleanArray() != null && mAdapter.getCheckedItems() != null && mAdapter.getCheckedItems().size() > 0) {
            menu.findItem(R.id.menu_done).setVisible(true);
        } else {
            menu.findItem(R.id.menu_done).setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.menu_done:
                if (Utils.isNetworkAvailable(getActivity())) {
                    uploadFileToCloudinary();
                    showProgressDialog("Uploading Photos. Please wait!!");
                } else {
                    Utils.showMessage(getActivity(), "Please connect to network");
                }
                break;
        }
        return true;
    }

    private void uploadFileToCloudinary() {
        if (mAdapter.getmSparseBooleanArray() != null && mAdapter.getCheckedItems() != null && mAdapter.getCheckedItems().size() > 0) {
            String[] photoArray = new String[mAdapter.getCheckedItems().size()];
            countUploaded = 0;
            totalUploaded = mAdapter.getCheckedItems().size();
            for (int i = 0; i < mAdapter.getCheckedItems().size(); i++) {
                photoArray[i] = mAdapter.getCheckedItems().get(i).getThumbnailUri().getPath();
            }
            PhotoManager.getInstance().uploadMultiImage(photoArray, imageUploadCallback);
        }
    }

    public void updateDoneButton() {
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void openCamera() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.getActivity(), new String[] { Manifest.permission.CAMERA }, Utils.PERMISSIONS_REQUEST_CAMERA);
        } else {
            takePhoto();
        }

    }

    @Override
    public void showProgressDialog(String message) {
        try {
            // Show a progress dialog.
            mLoadingProgressDialog = new ProgressDialog(getActivity());
            mLoadingProgressDialog.setMessage(message);
            mLoadingProgressDialog.setCancelable(false);
            mLoadingProgressDialog.show();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void finishLoading(List<PhotoItem> data) {
        mPhotoListItem.clear();

        for (int i = 0; i < data.size(); i++) {
            PhotoItem item = data.get(i);
            mPhotoListItem.add(item);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Utils.PERMISSIONS_REQUEST_WRITE_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                getLoaderManager().initLoader(0, null, this);
            } else {
                // User refused to grant permission.
            }
        } else if (requestCode == Utils.PERMISSIONS_REQUEST_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                openCamera();
            } else {
                // User refused to grant permission.
            }

        }
    }

    public void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            // Error occurred while creating the File
            Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            startActivityForResult(intent, TAKE_PICTURE);
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        output = new File(storageDir, imageFileName + ".jpeg");
        return output;
    }

}