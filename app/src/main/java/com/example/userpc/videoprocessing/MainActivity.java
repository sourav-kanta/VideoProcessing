package com.example.userpc.videoprocessing;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import wseemann.media.FFmpegMediaMetadataRetriever;

public class MainActivity extends AppCompatActivity implements CallBackListener{

    MediaMetadataRetriever med;
    private static final int FILE_REQUEST=1;
    RelativeLayout firstactivity;

    public void send(View v)
    {
        startActivityForResult(new Intent(Intent.ACTION_GET_CONTENT).setType("file/*"),FILE_REQUEST);
    }

    public void receive(View v)
    {
        ReceiveThread receiveThread=new ReceiveThread(MainActivity.this);
        receiveThread.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firstactivity= (RelativeLayout) findViewById(R.id.main_screen);
        med=new MediaMetadataRetriever();

    }

    void openFile(Uri uri)
    {
        med.setDataSource(MainActivity.this,uri);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==FILE_REQUEST)
        {
            if(resultCode==RESULT_OK)
            {
                if(data!=null)
                {
                    Snackbar.make(firstactivity,"File open successful",Snackbar.LENGTH_LONG).show();
                    Uri uri=data.getData();
                    openFile(uri);
                    FrameSeparator frameSeparator=new FrameSeparator(med,MainActivity.this);
                    frameSeparator.execute();
                }
            }
            else
            {
                Snackbar.make(firstactivity,"File open was unsuccessful",Snackbar.LENGTH_LONG).show();
            }
        }
        //super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onTaskCompleted() {

    }
}
