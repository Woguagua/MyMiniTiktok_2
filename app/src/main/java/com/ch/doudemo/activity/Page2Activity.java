package com.ch.doudemo.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ch.doudemo.R;
import com.ch.doudemo.base.BaseRecAdapter;
import com.ch.doudemo.base.BaseRecViewHolder;
import com.ch.doudemo.recycler.Data;
import com.ch.doudemo.recycler.DataSet;
import com.ch.doudemo.widget.MyVideoPlayer;
import com.longsh.optionframelibrary.OptionMaterialDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.curzbin.library.BottomDialog;
import me.curzbin.library.Item;
import me.curzbin.library.OnItemClickListener;

/**
 * 翻页2
 */
public class Page2Activity extends AppCompatActivity {

    @BindView(R.id.rv_page2)
    RecyclerView rvPage2;
    private List<String> urlList;
    private ListVideoAdapter videoAdapter;
    private PagerSnapHelper snapHelper;
    private LinearLayoutManager layoutManager;
    private int currentPosition;
//    private BottomNavigationView bottomMenu;
//    private MenuItem menuRecord;
    private Button btn_record;
    private Button btn_mine;
//    private ImageButton btn_like;
//    private boolean islike=false;
//    private AnimatorSet animatorSet;


    private BottomSheetDialog bottomSheetDialog;
    private BottomSheetBehavior mDialogBehavior;
    private RecyclerView recyclerView;
//    private TodoListAdapter mainAdapter;
    private List<String> titleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page2);
        ButterKnife.bind(this);
        btn_record = findViewById(R.id.btn_record);
        btn_mine = findViewById(R.id.btn_mine);
//        btn_like = findViewById(R.id.like);
        btn_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Page2Activity.this, MyRecordActivity.class);
                startActivity(intent);
            }
        });
        btn_mine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Page2Activity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
//        bottomMenu = findViewById(R.id.navigation);
//        menuRecord = findViewById(R.id.navigation_record);

//        btn_like.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                btn_like.setForegroundTintList();
//                if(!islike){
//                    setTargetLikeAnimation();
////                    btn_like.setColorFilter(Color.TRANSPARENT);
//                }
//                else{
//                    btn_like.setColorFilter(Color.WHITE);
//                }
//                islike = !islike;
//
//            }
//        });
        initView();
        addListener();

    }


//    private void setTargetLikeAnimation(){
//        if(animatorSet != null){
//            animatorSet.cancel();
//        }
//        ObjectAnimator animator1 = ObjectAnimator.ofFloat(btn_like, "scaleX", 1f, 0f);
//        animator1.setDuration(Integer.parseInt("1000"));
////        animator1.setRepeatCount(1);
//        ObjectAnimator animator2 = ObjectAnimator.ofFloat(btn_like, "scaleY", 1f, 0f);
//        animator2.setDuration(Integer.parseInt("1000"));
////        animator2.setRepeatCount(1);
//        animatorSet = new AnimatorSet();
//        animatorSet.playTogether(animator1);
//        animatorSet.playTogether(animator2);
//        animatorSet.start();
//        btn_like.setColorFilter(Color.TRANSPARENT);
//
//        animator1 = ObjectAnimator.ofFloat(btn_like, "scaleX", 0f, 1.2f, 1f);
//        animator1.setDuration(Integer.parseInt("1000"));
////        animator1.setRepeatCount(1);
//        animator2 = ObjectAnimator.ofFloat(btn_like, "scaleY", 0f, 1.2f, 1f);
//        animator2.setDuration(Integer.parseInt("1000"));
//        animatorSet.cancel();
//        animatorSet.playTogether(animator1);
//        animatorSet.playTogether(animator2);
//        animatorSet.start();
//    }

    //显示dialog
    private void showSheetDialog() {
        View view = View.inflate(Page2Activity.this, R.layout.dialog_bottomsheet, null);
//        recyclerView = findViewById(R.id.dialog_recycleView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mainAdapter = new TodoListAdapter();
//        recyclerView.setAdapter(mainAdapter);

        bottomSheetDialog = new BottomSheetDialog(Page2Activity.this);
        bottomSheetDialog.setContentView(view);
        mDialogBehavior = BottomSheetBehavior.from((View) view.getParent());
        mDialogBehavior.setPeekHeight(getWindowHeight());//dialog的高度
        mDialogBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (i == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetDialog.dismiss();
                    mDialogBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
    }

//    void showBottomSheetDialog(){
//        BottomSheetDialog bottomSheet = new BottomSheetDialog(this);//实例化
////        BottomSheetDialog bottomSheet.setCancelable(true);//设置点击外部是否可以取消
//        bottomSheet.setContentView(R.layout.dialog_bottomsheet);//设置对框框中的布局
//        bottomSheet.show();
//        //显示弹窗
//    }

    private int getWindowHeight() {
        Resources res = Page2Activity.this.getResources();
        DisplayMetrics displayMetrics = res.getDisplayMetrics();
        return displayMetrics.heightPixels;
    }



    private void initView() {
//        bottomMenu.findViewById(R.id.navigation);
//        bottomMenu.setItemIconTintList(null);
//        bottomMenu.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        urlList = new ArrayList<>();
        urlList.add("http://vfx.mtime.cn/Video/2019/02/04/mp4/190204084208765161.mp4");
        urlList.add("http://vfx.mtime.cn/Video/2019/03/21/mp4/190321153853126488.mp4");
        urlList.add("http://vfx.mtime.cn/Video/2019/03/19/mp4/190319222227698228.mp4");
        urlList.add("http://vfx.mtime.cn/Video/2019/03/19/mp4/190319212559089721.mp4");
        urlList.add("http://vfx.mtime.cn/Video/2019/03/18/mp4/190318231014076505.mp4");
        urlList.add("http://vfx.mtime.cn/Video/2019/03/18/mp4/190318214226685784.mp4");
        urlList.add("http://vfx.mtime.cn/Video/2019/03/19/mp4/190319104618910544.mp4");
        urlList.add("http://vfx.mtime.cn/Video/2019/03/19/mp4/190319125415785691.mp4");


        snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(rvPage2);


        videoAdapter = new ListVideoAdapter(urlList, DataSet.getData());
        layoutManager = new LinearLayoutManager(Page2Activity.this, LinearLayoutManager.VERTICAL, false);
        rvPage2.setLayoutManager(layoutManager);
        rvPage2.setAdapter(videoAdapter);

    }

//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.navigation_record:
//                    Intent intent = new Intent(Page2Activity.this, MyRecordActivity.class);
//                    startActivity(intent);
////                    //这里因为需要对3个fragment进行切换
////                    //start
////                    if (lastfragment != 0) {
////                        switchFragment(lastfragment, 0);
////                        lastfragment = 0;
////                    }
////                    //end
////                    //如果只是想测试按钮点击，不管fragment的切换，可以把start到end里面的内容去掉
//                    return true;
////                case R.id.main_music:
////                    if (lastfragment != 1) {
////                        switchFragment(lastfragment, 1);
////                        lastfragment = 1;
////                    }
////                    return true;
////                case R.id.main_my:
////                    if (lastfragment != 2) {
////                        switchFragment(lastfragment, 2);
////                        lastfragment = 2;
////                    }
////                    return true;
//                default:
//                    break;
//            }
//            return false;
//        }
//    };

    private void addListener() {



        rvPage2.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {


            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE://停止滚动
                        View view = snapHelper.findSnapView(layoutManager);

                        //当前固定后的item position
                        int position = recyclerView.getChildAdapterPosition(view);
                        if (currentPosition != position) {
                            //如果当前position 和 上一次固定后的position 相同, 说明是同一个, 只不过滑动了一点点, 然后又释放了
                            MyVideoPlayer.releaseAllVideos();
                            RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(view);
                            if (viewHolder != null && viewHolder instanceof VideoViewHolder) {
                                ((VideoViewHolder) viewHolder).mp_video.startVideo();
                            }
                        }
                        currentPosition = position;
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING://拖动
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING://惯性滑动
                        break;
                }

            }
        });
    }


    @Override
    public void onPause() {
        super.onPause();
        MyVideoPlayer.releaseAllVideos();
    }



    class ListVideoAdapter extends BaseRecAdapter<String, VideoViewHolder> {
        private List<Data> mDataset = new ArrayList<>();

        public ListVideoAdapter(List<String> list, List<Data> myDataset) {
            super(list);
            mDataset.addAll(myDataset);
        }

        @Override
        public void onHolder(VideoViewHolder holder, String bean, int position) {
            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;

            holder.mp_video.setUp(bean, "第" + position + "个视频", MyVideoPlayer.STATE_NORMAL);
            if (position == 0) {
                holder.mp_video.startVideo();
            }
            Glide.with(context).load(bean).into(holder.mp_video.thumbImageView);
            holder.tv_title.setText("第" + position + "个视频");

            holder.onBind(position, mDataset.get(position));
        }

        @Override
        public VideoViewHolder onCreateHolder() {
            return new VideoViewHolder(getViewByRes(R.layout.item_page2));

        }


    }

//    class ListVideoAdapter extends BaseRecAdapter<String, VideoViewHolder> {
//
//
//        public ListVideoAdapter(List<String> list) {
//            super(list);
//        }
//
//        @Override
//        public void onHolder(VideoViewHolder holder, String bean, int position) {
//            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
//            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
//
//            holder.mp_video.setUp(bean, "第" + position + "个视频", MyVideoPlayer.STATE_NORMAL);
//            if (position == 0) {
//                holder.mp_video.startVideo();
//            }
//            Glide.with(context).load(bean).into(holder.mp_video.thumbImageView);
//            holder.tv_title.setText("第" + position + "个视频");
//        }
//
//        @Override
//        public VideoViewHolder onCreateHolder() {
//            return new VideoViewHolder(getViewByRes(R.layout.item_page2));
//
//        }
//
//
//    }

    public class VideoViewHolder extends BaseRecViewHolder {
        public View rootView;
        public MyVideoPlayer mp_video;
        public TextView tv_title;
        public ImageButton btn_like;
        public ImageButton btn_share;
        private boolean islike=false;
        private AnimatorSet animatorSet;
        public ImageButton btn_comment;

        private TextView tvID;
        private TextView tvUserNmae;
        private TextView tvMessage;
        private TextView tvDianzan;
        private TextView tvPinglun;

        public VideoViewHolder(View rootView) {
            super(rootView);
            this.rootView = rootView;
            this.mp_video = rootView.findViewById(R.id.mp_video);
            this.tv_title = rootView.findViewById(R.id.tv_title);
            this.btn_like = rootView.findViewById(R.id.like);
            this.btn_share = rootView.findViewById(R.id.share);
            this.btn_comment = rootView.findViewById(R.id.comment);
            bind();

            tvID = rootView.findViewById(R.id.tv_id);
            tvUserNmae = rootView.findViewById(R.id.tv_name);
            tvMessage = rootView.findViewById(R.id.tv_jieshao);
            tvDianzan = rootView.findViewById(R.id.tv_like);
            tvPinglun = rootView.findViewById(R.id.tv_comment);
        }

        public void onBind(int position, Data data) {
            tvID.setText(new StringBuilder().append(position).toString());
            tvUserNmae.setText(data.username);
            tvMessage.setText(data.message);
            tvDianzan.setText(data.dianzan);
            tvPinglun.setText(data.pinglun);
        }

        public void bind(){
            btn_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                btn_like.setForegroundTintList();
                if(!islike){
                    setTargetLikeAnimation();
//                    btn_like.setColorFilter(Color.TRANSPARENT);
                }
                else{
                    btn_like.setColorFilter(Color.WHITE);
                }
                islike = !islike;

            }
        });
            btn_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new BottomDialog(Page2Activity.this)
                            .title("分享到")             //设置标题
                            .layout(BottomDialog.GRID)              //设置内容layout,默认为线性(LinearLayout)
                            .orientation(BottomDialog.VERTICAL)     //设置滑动方向,默认为横向
                            .inflateMenu(R.menu.bottom_dialog)         //传人菜单内容
                            .itemClick(new OnItemClickListener() {
                                @Override
                                public void click(Item item) {
                                    Log.i("Itemid",item.getTitle());
                                    if(item.getTitle().equals("朋友圈")){
                                        final OptionMaterialDialog mMaterialDialog = new OptionMaterialDialog(Page2Activity.this);
                                        mMaterialDialog.setTitle("朋友圈分享")
                                        .setTitleTextColor(R.color.colorDark)
                                        .setTitleTextSize((float) 22.5)
                                                .setMessage("确定把你的视频分享到朋友圈吗")
//                                       .setMessageTextColor(R.color.colorGrey)
                                        .setMessageTextSize((float) 16.5)
//                                      .setPositiveButtonTextColor(R.color.colorAccent)
//                                      .setNegativeButtonTextColor(R.color.colorPrimary)
//                                      .setPositiveButtonTextSize(15)
//                                      .setNegativeButtonTextSize(15)
                                                .setPositiveButton("确定", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        mMaterialDialog.dismiss();
                                                        Toast.makeText(Page2Activity.this, "成功分享到朋友圈", Toast.LENGTH_SHORT)
                                                                .show();

                                                    }
                                                })
                                                .setNegativeButton("取消",
                                                        new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                mMaterialDialog.dismiss();
                                                            }
                                                        })
                                                .setCanceledOnTouchOutside(true)
                                                .setOnDismissListener(
                                                        new DialogInterface.OnDismissListener() {
                                                            @Override
                                                            public void onDismiss(DialogInterface dialog) {
                                                                //对话框消失后回调
                                                            }
                                                        })
                                                .show();

                                    }
                                }
                            })
                            .show();
                }
            });
            btn_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    showSheetDialog();
//                    showBottomSheetDialog();
                }
            });
        }

        private void setTargetLikeAnimation(){
            if(animatorSet != null){
                animatorSet.cancel();
            }
            ObjectAnimator animator1 = ObjectAnimator.ofFloat(btn_like, "scaleX", 1f, 0f);
            animator1.setDuration(Integer.parseInt("1000"));
            ObjectAnimator animator2 = ObjectAnimator.ofFloat(btn_like, "scaleY", 1f, 0f);
            animator2.setDuration(Integer.parseInt("1000"));
            animatorSet = new AnimatorSet();
            animatorSet.playTogether(animator1);
            animatorSet.playTogether(animator2);
            animatorSet.start();
            btn_like.setColorFilter(Color.TRANSPARENT);

            animator1 = ObjectAnimator.ofFloat(btn_like, "scaleX", 0f, 1.2f, 1f);
            animator1.setDuration(Integer.parseInt("1000"));
            animator2 = ObjectAnimator.ofFloat(btn_like, "scaleY", 0f, 1.2f, 1f);
            animator2.setDuration(Integer.parseInt("1000"));
            animatorSet.cancel();
            animatorSet.playTogether(animator1);
            animatorSet.playTogether(animator2);
            animatorSet.start();
        }
    }
}
