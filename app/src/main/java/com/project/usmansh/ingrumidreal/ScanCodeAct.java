package com.project.usmansh.ingrumidreal;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.zxing.Result;

import Fragments.UserProfile;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanCodeAct extends AppCompatActivity implements ZXingScannerView.ResultHandler{


    ZXingScannerView ScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Setting QR Code FrontEnd
        ScannerView = new ZXingScannerView(this);
        setContentView(ScannerView);

        //setContentView(R.layout.activity_scan_code);
    }


    @Override
    public void handleResult(Result rawResult) {

        Log.d("Data Scanned: ","Successfully "+rawResult.getText());

//        String url = "https://www.ingrumid.com/"+rawResult.getText();
        String url = rawResult.getText();

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);

        onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ScannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ScannerView.setResultHandler(this);
        ScannerView.startCamera();
    }
}
