package com.lbb.lbb.ui.customviews;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import static android.content.Context.WINDOW_SERVICE;

/**
 * Created by hitesh on 8/1/17.
 */

public class Preview extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder mSurfaceHolder;
    public Camera         mCamera;
    public Activity       mActivity;

    public Preview(Context context) {
        super(context);
    }

    public Preview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setUpSurface(Activity activity) {
        try {
            mActivity = activity;
            this.mSurfaceHolder = getHolder();
            this.mSurfaceHolder.setFormat(PixelFormat.JPEG);
            this.mSurfaceHolder.addCallback(this);
            this.mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private SurfaceHolder getSurfaceHolder() {
        return this.mSurfaceHolder;
    }

    // SurfaceHolder.Callback
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        try {
            mCamera = Camera.open();
            mCamera.setPreviewDisplay(getSurfaceHolder());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // SurfaceHolder.Callback
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Camera.Parameters params = null;

        try {

            if (mCamera != null) {
                params = mCamera.getParameters();

                params.setPictureFormat(PixelFormat.JPEG);

                Display display = ((WindowManager) mActivity.getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
                if (display.getRotation() == Surface.ROTATION_0) {
                    params.setPreviewSize(height, width);
                    mCamera.setDisplayOrientation(90);
                }

                if (display.getRotation() == Surface.ROTATION_90) {
                    params.setPreviewSize(width, height);
                }

                if (display.getRotation() == Surface.ROTATION_180) {
                    params.setPreviewSize(height, width);
                }

                if (display.getRotation() == Surface.ROTATION_270) {
                    params.setPreviewSize(width, height);
                    mCamera.setDisplayOrientation(180);
                }
                mCamera.setParameters(params);

                mCamera.startPreview();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // SurfaceHolder.Callback
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();
    }

    public void releaseCamera() {
        if (mCamera != null) {
            try {
                mCamera.stopPreview();
                mCamera.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mCamera = null;
    }

}
