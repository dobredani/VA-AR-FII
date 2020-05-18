package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.util.AsyncQR;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanQR extends Fragment implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    private AsyncQR listener;

    // empty public constructor
    public ScanQR() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() instanceof AsyncQR)
            listener = (AsyncQR) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mScannerView = new ZXingScannerView(getActivity());
        //mScannerView.setFlash(true);
        List<BarcodeFormat> formats = new ArrayList<>();
        formats.add(BarcodeFormat.QR_CODE);
        mScannerView.setFormats(formats);
        return mScannerView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        //Toast.makeText(getActivity(), "Contents = " + rawResult.getText() , Toast.LENGTH_SHORT).show();
        if (listener != null) listener.onScanCompleted(rawResult.getText());
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    public void resumeScan() {
        mScannerView.resumeCameraPreview(ScanQR.this);
    }

    public void flashON() {
        mScannerView.setFlash(true);
    }

    public void flashOFF(){
        mScannerView.setFlash(false);
    }
}