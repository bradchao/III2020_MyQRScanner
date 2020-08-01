package tw.brad.apps.myqrscanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity
        implements ZXingScannerView.ResultHandler{
    private ZXingScannerView mScannerView;
    private TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                        this, Manifest.permission.CALL_PHONE) ==
                        PackageManager.PERMISSION_GRANTED){
            init();
        }else{
            requestPermissions(
                    new String[] { Manifest.permission.CAMERA, Manifest.permission.CALL_PHONE},
                    8);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 8){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                init();
            }else{
                finish();
            }
        }
    }

    private void init(){
        setContentView(R.layout.activity_main);
        resultText = findViewById(R.id.resultText);
        mScannerView = findViewById(R.id.scanner);
        mScannerView.setAspectTolerance(0.5f);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }
    @Override
    public void handleResult(Result rawResult) {
        String result = rawResult.getText();
        Log.v("bradlog", result);
        resultText.setText(result);
        mScannerView.resumeCameraPreview(this);
    }

    public void goAction(View view) {
//        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
//                Uri.parse(resultText.getText().toString()));
//        startActivity(browserIntent);

        Intent dial = new Intent();
        dial.setAction("android.intent.action.CALL");
        dial.setData(Uri.parse("tel:" + resultText.getText().toString()));
        startActivity(dial);

    }
}