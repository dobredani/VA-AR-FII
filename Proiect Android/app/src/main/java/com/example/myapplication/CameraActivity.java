package com.example.myapplication;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.GradientDrawable;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.List;

public class CameraActivity extends AppCompatActivity {

    private static final String[] PERMISSIONS = {
            Manifest.permission.CAMERA
    };
    private static final int REQUEST_PERMISSIONS = 34;
    private static final int PERMISSIONS_COUNT = 1;
    private static SurfaceHolder myHolder;
    private static CameraPreview mPreview;
    private static OrientationEventListener orientationEventListener = null;
    private static boolean fM; //focus mode
    private static List<String> camEffects;
    private static int rotation;
    private static boolean whichCamera = true;
    private static Camera.Parameters p;
    private boolean isCameraInitialized;
    private Camera mCamera = null;
    private RelativeLayout preview;
    private boolean isFlashOn;
    private Snackbar mySnackbar;
    protected String currentInstruction;

    private static boolean hasFlash() {
        camEffects = p.getSupportedColorEffects();
        final List<String> flashModes = p.getSupportedFlashModes();
        if (flashModes == null) {
            return false;
        }
        for (String flashMode : flashModes) {
            if (Camera.Parameters.FLASH_MODE_ON.equals(flashMode)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
    }

    public TextView addTextViewOverlay(int id) {
        final TextView t = findViewById(id);
        return t;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isCameraInitialized) {
            mCamera = Camera.open();
            mPreview = new CameraPreview(this, mCamera);
            preview = findViewById(R.id.camera_preview);
            preview.addView(mPreview);
            rotateCamera();

            final TextView helloTextView = addTextViewOverlay(R.id.text_view_id);
            DisplayController displayController = new DisplayController();
            displayController.addOverlay(helloTextView, currentInstruction);

            Button flashB = findViewById(R.id.flash);
            flashB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isFlashOn) {
                        p = mCamera.getParameters();
                        p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                        mCamera.setParameters(p);
                        isFlashOn = false;
                    } else {
                        p = mCamera.getParameters();
                        p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                        mCamera.setParameters(p);
                        isFlashOn = true;
                    }
                }
            });

            if (hasFlash()) {
                flashB.setVisibility(View.VISIBLE);


            } else {
                flashB.setVisibility(View.GONE);
            }

            orientationEventListener = new OrientationEventListener(this) {
                @Override
                public void onOrientationChanged(int orientation) {
                    rotateCamera();
                }
            };
            orientationEventListener.enable();
            preview.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (whichCamera) {
                        if (fM) {
                            p = mCamera.getParameters();
                            p.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                            mCamera.setParameters(p);
                        } else {
                            p = mCamera.getParameters();
                            p.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                            mCamera.setParameters(p);
                        }
                        try {
                            mCamera.setParameters(p);
                        } catch (Exception e) {

                        }
                        fM = !fM;
                    }
                    return true;
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    private void releaseCamera() {
        if (mCamera != null) {
            preview.removeView(mPreview);
            mCamera.release();
            orientationEventListener.disable();
            mCamera = null;
            whichCamera = !whichCamera;
        }
    }

    private void rotateCamera() {
        if (mCamera != null) {
            rotation = this.getWindowManager().getDefaultDisplay().getRotation();
            if (rotation == 0) {
                rotation = 90;
            } else if (rotation == 1) {
                rotation = 0;
            } else if (rotation == 2) {
                rotation = 270;
            } else {
                rotation = 180;
            }
            mCamera.setDisplayOrientation(rotation);
            if (whichCamera != true) {
                if (rotation == 90) {
                    rotation = 270;
                } else if (rotation == 270) {
                    rotation = 90;
                }
            }
            p = mCamera.getParameters();
            p.setRotation(rotation);
            mCamera.setParameters(p);
        }
    }


    private static class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
        private static SurfaceHolder mHolder;
        private static Camera mCamera;

        private CameraPreview(Context context, Camera camera) {
            super(context);
            mCamera = camera;
            mHolder = getHolder();
            mHolder.addCallback(this);
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        public void surfaceCreated(SurfaceHolder holder) {
            myHolder = holder;
            try {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void surfaceDestroyed(SurfaceHolder holder) {

        }

        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {

        }
    }
}