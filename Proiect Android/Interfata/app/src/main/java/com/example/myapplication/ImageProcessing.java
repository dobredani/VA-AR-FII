package com.example.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.TextView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.aruco.Aruco;
import org.opencv.aruco.CharucoBoard;
import org.opencv.aruco.Dictionary;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class ImageProcessing  extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private static final String     TAG = "ImageProcessing";
    private Size SIZE = new Size();

    private Mat mRgba, mRgb, mRgbT, mRgbF;
    private Dictionary dict;
    private List<Mat> detectedMarkers;
    private Mat                 ids, cameraMatrix, distCoeffs;
    private Scalar borderColor;
    private int                 counter = 0;
    private CharucoBoard board;
    private ArrayList<Mat> charucoCorners, charucoIds, rvecs, tvecs;

    private CameraBridgeViewBase mOpenCvCameraView;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    dict = Aruco.getPredefinedDictionary(Aruco.DICT_6X6_1000);
                    board = CharucoBoard.create(5, 7, (float) 0.04, (float) 0.02, dict);
                    charucoCorners = new ArrayList<>();
                    charucoIds = new ArrayList<>();
                    borderColor = new Scalar(0,255,0);
                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    public ImageProcessing() {
        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.camera_view);

        mOpenCvCameraView = findViewById(R.id.surface_view);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(height, width, CvType.CV_8UC4);
        mRgbT = new Mat(width, width, CvType.CV_8UC3);
        mRgbF = new Mat(height, width, CvType.CV_8UC3);
        mRgb = new Mat(height, width, CvType.CV_8UC3);
        SIZE = new Size(width, height);
    }

    public void onCameraViewStopped() {
        mRgba.release();
        mRgb.release();
        mRgbT.release();
        mRgbF.release();
        SIZE = null;
    }

    /**
     * TODO this class will take a callback function as argument and this is where the detected
     * markers will be sent back to the caller
     * @param ids
     */
    private void returnDetectedMarkers(Mat ids){
        if (ids.rows() == 0){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                ((TextView) findViewById(R.id.camera_view_overlay)).setText("");
            }
        });
        }
        else {
            if ((int) ids.get(0, 0)[0] % 2 == 0) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        ((TextView) findViewById(R.id.camera_view_overlay)).setText("Turn Left");
                        ((TextView) findViewById(R.id.camera_view_overlay)).bringToFront();
                        ((TextView) findViewById(R.id.camera_view_overlay)).invalidate();
                    }
                });
            } else {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        ((TextView) findViewById(R.id.camera_view_overlay)).setText("Turn Right");
//                        getTextView(R.id.camera_view_overlay).invalidate();
                    }
                });
            }
        for (int i = 0; i < ids.rows(); i++) {
            Log.i(TAG,"+++ Found marker ID " + Integer.toString((int)ids.get(0, 0)[0]));
        }
    }
}

    public Mat detectMarkers() {
        detectedMarkers = new ArrayList<>();
        ids = new Mat();
        Aruco.detectMarkers(mRgb, dict, detectedMarkers, ids);
        Aruco.drawDetectedMarkers(mRgb, detectedMarkers, ids, borderColor);
        returnDetectedMarkers(ids);
        return mRgb;
    }

    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        Imgproc.cvtColor(mRgba, mRgb, Imgproc.COLOR_RGBA2RGB);
        Core.transpose(mRgb, mRgbT);
        Imgproc.resize(mRgbT, mRgbF, mRgbF.size(),0,0,0);
        Core.flip(mRgbF, mRgb, 1);

        return detectMarkers();
    }


}
