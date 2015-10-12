package com.demo.videothumbnailfromapi;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import wseemann.media.FFmpegMediaMetadataRetriever;

public class VideoList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.row_videos);

        final ImageView iv = (ImageView) findViewById(R.id.iv_videoThumbnail);
        final ProgressDialog progressDialog = new ProgressDialog(VideoList.this);
        progressDialog.setMessage("Loading ...");
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Bitmap bm = retriveVideoFrameFromURL("http://mixmp3.in/video/down/37158495/1057644/MmExNUJyT0JidnIrR0F6WUd6cUJVOHdhYTF2MkhxOW1TVmpLWlBsZWI3dytGd002amc=/Prem+Leela+HD+720p+%5BMixmp3.In%5D.mp4");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            iv.setImageBitmap(bm);
                            progressDialog.dismiss();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }).start();
    }


    public static Bitmap retriveVideoFrameFromURL(String videoPath)
            throws Throwable {
        Bitmap bitmap = null;
        FFmpegMediaMetadataRetriever fmmr = new FFmpegMediaMetadataRetriever();
        try {
            fmmr.setDataSource(videoPath);

            Bitmap b = fmmr.getFrameAtTime();

            if (b != null) {
                Bitmap b2 = fmmr.getFrameAtTime(4000000, FFmpegMediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                if (b2 != null) {
                    b = b2;
                }
            }

            if (b != null) {
                Log.i("Thumbnail", "Extracted frame");
                return b;
            } else {
                Log.e("Thumbnail", "Failed to extract frame");
            }
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        } finally {
            fmmr.release();
        }
        return null;
    }

}
