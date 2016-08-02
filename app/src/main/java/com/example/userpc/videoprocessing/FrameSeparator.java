package com.example.userpc.videoprocessing;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import wseemann.media.FFmpegMediaMetadataRetriever;

/**
 * Created by USER PC on 7/22/2016.
 */
public class FrameSeparator extends AsyncTask<Void,Void,Void> {

    private CallBackListener callBackListener;
    private MediaMetadataRetriever meg;
    ProgressDialog progressDialog;
    Context context;

    FrameSeparator(MediaMetadataRetriever fmeg, Context con)
    {
        meg=fmeg;
        context=con;
        progressDialog=new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.setMessage("Please wait");
//        progressDialog.show();
        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + "Bitmaps");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
        }
        if (success) {
            Log.e("Folder","Created");
        } else {
            Log.e("Folder","Not created");
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        String total_time=meg.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long time=Long.parseLong(total_time);
        Log.e("Time",time+"");
        for(long i=1;i<time;i=i+100)
        {
            Bitmap bmp=meg.getFrameAtTime(i*1000,MediaMetadataRetriever.OPTION_CLOSEST);
            if(bmp==null) {
                Log.e("Bitmap","null");
                continue;
            }
            // convert to  grayscale

            bmp=new GrayScale(bmp).toGrayScale();

            // Save Bitmap
            FileOutputStream out = null;
            try {
                File file=new File(Environment.getExternalStorageDirectory() +
                        File.separator + "Bitmaps"+File.separator+i+".jpeg");
                out = new FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.JPEG, 50, out);
                Log.e("Frame","File written");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            bmp.recycle();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
       // progressDialog.dismiss();
        meg.release();
        //callBackListener.onTaskCompleted();
    }
}
