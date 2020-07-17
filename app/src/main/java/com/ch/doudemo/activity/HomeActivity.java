package com.ch.doudemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.ch.doudemo.R;
import com.ch.doudemo.api.IMiniDouyinService;
import com.ch.doudemo.model.GetVideosResponse;
import com.ch.doudemo.model.Video;
import com.ch.doudemo.util.ResourceUtils;
import com.ch.doudemo.util.ImageHelper;
import com.ch.doudemo.view.CircleImageView;

import java.io.File;
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

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PICK_IMAGE = 1;
    private static final int PICK_VIDEO = 2;
    private static final String TAG = "HomeActivity";
    private RecyclerView mRv;
    private List<Video> mVideos = new ArrayList<>();
    public Uri mSelectedImage;
    private Uri mSelectedVideo;
//    public Button mBtn;
//    private Button mBtnRefresh;
    private Button btn_record;
    private Button btn_page2;

    private CircleImageView ivHead;

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(IMiniDouyinService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private IMiniDouyinService miniDouyinService = retrofit.create(IMiniDouyinService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initRecyclerView();
        initBtns();
//        fetchFeed(mBtnRefresh);
        ivHead = findViewById(R.id.iv_head);
        ivHead.setImageResource(R.drawable.tutu);
        ivHead.setOnClickListener(this);
        fetchFeed();
    }

    @Override
    public void onClick(View view) {

    }

    private void initBtns() {
        btn_record = findViewById(R.id.btn_record);
        btn_page2 = findViewById(R.id.btn_page2);

        btn_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, MyRecordActivity.class);
                startActivity(intent);
            }
        });

        btn_page2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, Page2Activity.class);
                startActivity(intent);
            }
        });
//        mBtn = findViewById(R.id.btn);
//        mBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String s = mBtn.getText().toString();
//                if (getString(R.string.select_an_image).equals(s)) {
//                    //@TODO 1填充选择图片的功能
//                    chooseImage();
//                } else if (getString(R.string.select_a_video).equals(s)) {
//                    //@TODO 2填充选择视频的功能
//                    chooseVideo();
//                } else if (getString(R.string.post_it).equals(s)) {
//                    if (mSelectedVideo != null && mSelectedImage != null) {
//                        //@TODO 3调用上传功能
//                        postVideo();
//                    } else {
//                        throw new IllegalArgumentException("error data uri, mSelectedVideo = "
//                                + mSelectedVideo
//                                + ", mSelectedImage = "
//                                + mSelectedImage);
//                    }
//                } else if ((getString(R.string.success_try_refresh).equals(s))) {
//                    mBtn.setText(R.string.select_an_image);
//                }
//            }
//        });
//
//        mBtnRefresh = findViewById(R.id.btn_refresh);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
        }

        public void bind(final Activity activity, final Video video) {
            ImageHelper.displayWebImage(video.imageUrl, img);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VideoActivity.launch(activity, video.videoUrl);
                }
            });
        }
    }

    private void initRecyclerView() {
        mRv = findViewById(R.id.rv);
        GridLayoutManager layoutManager = new GridLayoutManager(this,3, GridLayoutManager.VERTICAL, false);
//        mRv.setLayoutManager(new LinearLayoutManager(this));
        mRv.setLayoutManager(layoutManager);
        mRv.setAdapter(new RecyclerView.Adapter<MyViewHolder>() {
            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                return new MyViewHolder(
                        LayoutInflater.from(HomeActivity.this)
                                .inflate(R.layout.video_item_view, viewGroup, false));
            }

            @Override
            public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {
                final Video video = mVideos.get(i);
                viewHolder.bind(HomeActivity.this, video);
            }

            @Override
            public int getItemCount() {
                return mVideos.size();
            }
        });
    }

    public void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                PICK_IMAGE);
    }

    public void chooseVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Video"), PICK_VIDEO);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        Log.d(TAG, "onActivityResult() called with: requestCode = ["
//                + requestCode
//                + "], resultCode = ["
//                + resultCode
//                + "], data = ["
//                + data
//                + "]");
//
//        if (resultCode == RESULT_OK && null != data) {
//            if (requestCode == PICK_IMAGE) {
//                mSelectedImage = data.getData();
//                Log.d(TAG, "selectedImage = " + mSelectedImage);
//                mBtn.setText(R.string.select_a_video);
//            } else if (requestCode == PICK_VIDEO) {
//                mSelectedVideo = data.getData();
//                Log.d(TAG, "mSelectedVideo = " + mSelectedVideo);
//                mBtn.setText(R.string.post_it);
//            }
//        }
//    }

    private MultipartBody.Part getMultipartFromUri(String name, Uri uri) {
        File f = new File(ResourceUtils.getRealPath(HomeActivity.this, uri));
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), f);
        return MultipartBody.Part.createFormData(name, f.getName(), requestFile);
    }

//    private void postVideo() {
//        mBtn.setText("POSTING...");
////        Log.d(TAG, "POSTING...");
//        mBtn.setEnabled(false);
//        MultipartBody.Part coverImagePart = getMultipartFromUri("cover_image", mSelectedImage);
//        MultipartBody.Part videoPart = getMultipartFromUri("video", mSelectedVideo);
//        //@TODO 4下面的id和名字替换成自己的
//        miniDouyinService.postVideo("13360206870", "张诗敏", coverImagePart, videoPart).enqueue(
//                new Callback<PostVideoResponse>() {
//                    @Override
//                    public void onResponse(Call<PostVideoResponse> call, Response<PostVideoResponse> response) {
//                        if (response.body() != null) {
//                            Toast.makeText(MainActivity.this, response.body().toString(), Toast.LENGTH_SHORT)
//                                    .show();
//                        }
////                        Log.i(TAG,"OK");
//                        mBtn.setText(R.string.select_an_image);
//                        mBtn.setEnabled(true);
//                    }
//
//                    @Override
//                    public void onFailure(Call<PostVideoResponse> call, Throwable throwable) {
//                        mBtn.setText(R.string.select_an_image);
//                        mBtn.setEnabled(true);
//                        Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
////                        Log.i(TAG,"Fail");
//                    }
//                });
//    }

    public void fetchFeed() {
//        mBtnRefresh.setText("requesting...");
//        mBtnRefresh.setEnabled(false);
        miniDouyinService.getVideos().enqueue(new Callback<GetVideosResponse>() {
            @Override
            public void onResponse(Call<GetVideosResponse> call, Response<GetVideosResponse> response) {
                if (response.body() != null && response.body().videos != null) {
//                    mVideos = response.body().videos;
                    //@TODO  5服务端没有做去重，拿到列表后，可以在端侧根据自己的id，做列表筛选。
                    Set<String> videoset = new HashSet();
                    mVideos.clear();
                    int size = response.body().videos.size();
                    for (int i = 0; i < size; i++) {
                        if (response.body().videos.get(i).studentId.equals("13360206870")) {
                            if (!videoset.contains(response.body().videos.get(i).videoUrl)) {
                                mVideos.add(response.body().videos.get(i));
                                videoset.add(response.body().videos.get(i).videoUrl);
                            }
                        }
                    }
                    mRv.getAdapter().notifyDataSetChanged();
                }
//                mBtnRefresh.setText(R.string.refresh_feed);
//                mBtnRefresh.setEnabled(true);
            }

            @Override
            public void onFailure(Call<GetVideosResponse> call, Throwable throwable) {
//                mBtnRefresh.setText(R.string.refresh_feed);
//                mBtnRefresh.setEnabled(true);
                Toast.makeText(HomeActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
