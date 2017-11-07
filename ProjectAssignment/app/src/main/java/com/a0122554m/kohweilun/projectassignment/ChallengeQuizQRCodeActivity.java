package com.a0122554m.kohweilun.projectassignment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class ChallengeQuizQRCodeActivity extends Activity {

    SurfaceView cameraView;

    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;

    private static final int REQUEST_CAMERA_PERMISSION = 300;
    private boolean permissionToUseCameraAccepted = false;
    private String[] permissions = {Manifest.permission.CAMERA};


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 1) {
            permissionToUseCameraAccepted = grantResults[1] ==
                    PackageManager.PERMISSION_GRANTED;
        }

        if (!permissionToUseCameraAccepted)
            finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_quiz_qrcode);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, permissions,
                    REQUEST_CAMERA_PERMISSION);
        }

        cameraView = findViewById(R.id.camera_view);

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();

        cameraSource = new CameraSource
                .Builder(this, barcodeDetector)
                .setRequestedPreviewSize(640, 480)
                .build();

        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ContextCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.CAMERA) ==
                            PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(cameraView.getHolder());

                    }
                } catch (IOException ie) {
                    Log.e("CAMERA SOURCE", ie.getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            String scannedResult;

            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barCodes = detections.getDetectedItems();
                if (barCodes.size() != 0) {
                    scannedResult = barCodes.valueAt(0).displayValue;
                    System.out.println("QR Activity:" + scannedResult);
                    Intent myIntent = new Intent();
                    myIntent.putExtra("scannedResult", scannedResult);
                    setResult(RESULT_OK, myIntent);
                    finish();
                }
            }
        });
    }
}








