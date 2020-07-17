package com.ch.doudemo.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.ExifInterface;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.SortedList;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.ch.doudemo.R;
import com.ch.doudemo.api.IMiniDouyinService;
import com.ch.doudemo.model.PostVideoResponse;
import com.ch.doudemo.model.GetVideosResponse;
import com.ch.doudemo.model.Video;
import com.ch.doudemo.util.ResourceUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

//import com.bytedance.androidcamp.network.dou.view.CircleImageView;
//import com.bytedance.androidcamp.network.lib.util.ImageHelper;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class MyRecordActivity extends AppCompatActivity {
    private Button btn_picture;
    private Button btn_record;
    private Camera camera;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private ImageView mImageView;
    private VideoView mVideoView;
    private MediaRecorder mMediaRecorder;
    private boolean isRecording = false;
    private String mp4Path;
    private boolean isChoosePhoto = false;
    private boolean isChooseVideo = false;


    //####################
    private Button btn_upload;
    private Uri uriPicture = Uri.parse("file:/storage/emulated/0/Android/data/com.ch.doudemo/files/Movies/VIDEO_20200716_173804.mp4");
    private Uri uriVideo = Uri.parse("file:/storage/emulated/0/Android/data/com.ch.doudemo/files/Movies/VIDEO_20200716_173804.mp4");

    private TextView hintText;

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(IMiniDouyinService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private IMiniDouyinService miniDouyinService = retrofit.create(IMiniDouyinService.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myrecord);
        btn_picture = findViewById(R.id.btn_picture);
        btn_record = findViewById(R.id.btn_video);
        mImageView = findViewById(R.id.mImageView);
        surfaceView = findViewById(R.id.surfaceView);
        mVideoView = findViewById(R.id.mVideoView);
        hintText = findViewById(R.id.hint_text);
        //####################
        btn_upload = findViewById(R.id.btn_upload);

        btn_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camera.takePicture(null, null, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        FileOutputStream fos = null;
                        String filePath = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + File.separator + "1.jpg";
                        File file = new File(filePath);
                        uriPicture = Uri.parse(file.toURI().toString());
                        isChoosePhoto = true;
                        hintText.setText("再录制一段视频作为视频内容 ^-^");

                        Log.i("MyRecordActivity","uriPicture:" + uriPicture);

                        try{
                            fos = new FileOutputStream(file);
                            fos.write(data);
                            fos.flush();
                            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                            Bitmap rotateBitmap = rotateImage(bitmap, filePath);
                            mImageView.setVisibility(View.VISIBLE);
                            mVideoView.setVisibility(View.GONE);
                            mImageView.setImageBitmap(rotateBitmap);
//                            mImageView.setImageBitmap(bitmap);
                        } catch(Exception e){
                            e.printStackTrace();
                        } finally{
                            camera.stopPreview();
                            if(fos != null){
                                try{
                                    fos.close();
                                } catch (IOException e){
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
            }
        });
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback(){
            @Override
            public void surfaceCreated(SurfaceHolder holder){
                try{
                    camera.setPreviewDisplay(holder);
                    camera.startPreview();
                } catch(IOException e){
                    e.printStackTrace();
                }
            }
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){
                if(holder.getSurface() == null){
                    return;
                }
                camera.stopPreview();
                try{
                    camera.setPreviewDisplay(holder);
                    camera.startPreview();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
            @Override
            public void surfaceDestroyed(SurfaceHolder holder){
                camera.stopPreview();
                camera.release();
                camera = null;
            }

        });
        btn_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                record(view);
            }
        });

        //###########################
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isChoosePhoto){
                    Toast.makeText(MyRecordActivity.this, "请先拍摄一张照片", Toast.LENGTH_SHORT)
                            .show();
                } else if(!isChooseVideo){
                    Toast.makeText(MyRecordActivity.this, "请先录制一段视频", Toast.LENGTH_SHORT)
                            .show();
                } else{
                    postVideo();
                }

            }
        });
    }

    //##############

    private MultipartBody.Part getMultipartFromUri(String name, Uri uri) {
        File f = new File(ResourceUtils.getRealPath(MyRecordActivity.this, uri));
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), f);
        Log.i("MyRecordActivity","requestFile:"+requestFile.toString());
        return MultipartBody.Part.createFormData(name, f.getName(), requestFile);
    }

    private void postVideo() {
        btn_upload.setText("上传中...");
        btn_upload.setEnabled(false);
        MultipartBody.Part coverImagePart = getMultipartFromUri("cover_image", uriPicture);
        MultipartBody.Part videoPart = getMultipartFromUri("video", uriVideo);
        //@TODO 4下面的id和名字替换成自己的
        miniDouyinService.postVideo("13360206870", "张诗敏", coverImagePart, videoPart).enqueue(
                new Callback<PostVideoResponse>() {
                    @Override
                    public void onResponse(Call<PostVideoResponse> call, Response<PostVideoResponse> response) {
                        if (response.body() != null) {
                            Toast.makeText(MyRecordActivity.this, "上传成功", Toast.LENGTH_SHORT)
                                    .show();
                        }
                        btn_upload.setText("上传");
                        btn_upload.setEnabled(true);
                        Intent intent = new Intent();
                        intent.setClass(MyRecordActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<PostVideoResponse> call, Throwable throwable) {
                        btn_upload.setText("上传");
                        btn_upload.setEnabled(true);
                        if(throwable.getMessage().equals("timeout")){
                            Toast.makeText(MyRecordActivity.this, "网络不太好，请返回刷新查看是否上传成功", Toast.LENGTH_SHORT).show();
                        }
//                        Toast.makeText(MyRecordActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.i("WrongUpload",throwable.getMessage());
                    }
                });
    }

    @Override
    protected  void onResume(){
        super.onResume();
        if(camera == null){
            initCamera();
        }
        camera.startPreview();
    }
    @Override
    protected  void onPause(){
        super.onPause();
        camera.stopPreview();
    }


    private void initCamera(){
        camera = Camera.open();
        Camera.Parameters parameters = camera.getParameters();
        parameters.setPictureFormat(ImageFormat.JPEG);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        parameters.set("orientation", "portrait");
        parameters.set("rotation", 90);
        camera.setParameters(parameters);
        camera.setDisplayOrientation(90);

        surfaceView = findViewById(R.id.surfaceView);

    }

    private Bitmap rotateImage(Bitmap bitmap, String path){
        try{
            ExifInterface srcExif = new ExifInterface(path);
            Matrix matrix = new Matrix();
            int angle = 0;
            int orientation = srcExif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch(orientation){
                case ExifInterface.ORIENTATION_ROTATE_90:{
                    angle = 90;
                    break;
                }
                case ExifInterface.ORIENTATION_ROTATE_180:{
                    angle = 180;
                    break;
                }
                case ExifInterface.ORIENTATION_ROTATE_270:{
                    angle = 270;
                    break;
                }
                default:
                    break;
            }
            matrix.postRotate(angle);
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    private boolean prepareVideoRecorder(){
        mMediaRecorder = new MediaRecorder();
        camera.unlock();
        mMediaRecorder.setCamera(camera);
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
        mp4Path = getOutputMediaPath();
        mMediaRecorder.setOutputFile(mp4Path);
        mMediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
        mMediaRecorder.setOrientationHint(90);
        try{
            mMediaRecorder.prepare();
        }catch (Exception e){
//            releaseMediaRecorder();
            mMediaRecorder.release();
            return false;
        }
        return true;
    }

    public void record(View view){
        if(isRecording){
            btn_record.setText("录制");
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
            camera.lock();
            hintText.setText("点击上传进行上传");

            mVideoView.setVisibility(View.VISIBLE);
            mImageView.setVisibility(View.GONE);
            mVideoView.setVideoPath(mp4Path);
            mVideoView.start();
        } else{
            if(prepareVideoRecorder()){
                btn_record.setText("暂停");
                mMediaRecorder.start();
            }
        }
        isRecording = !isRecording;
    }

    private String getOutputMediaPath(){
        File mediaStorageDir = getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir, "VIDEO_" + timeStamp + ".mp4");
        if(!mediaFile.exists()){
            mediaFile.getParentFile().mkdirs();
        }
        uriVideo = Uri.parse(mediaFile.toURI().toString());
        isChooseVideo = true;
        Log.i("MyRecordActivity","uriVideo:" + uriVideo);
        return mediaFile.getAbsolutePath();
    }
}
