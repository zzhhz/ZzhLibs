package com.zzh.zlibs.image;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzh.zlibs.R;
import com.zzh.zlibs.base.BaseActivity;
import com.zzh.zlibs.image.adapter.ImageDetailAdapter;
import com.zzh.zlibs.image.model.FileItem;

import java.util.ArrayList;

import androidx.viewpager.widget.ViewPager;

import static com.zzh.zlibs.utils.ZUtils.fileItems;

/**
 * Created by Administrator.
 *
 * @date: 2019/8/26
 * @email: zzh_hz@126.com
 * @QQ: 1299234582
 * @author: zzh
 * @description: ZzhLibs 图片预览界面。
 * @since 1.0
 */
public class ImageDetailActivity extends BaseActivity {

    ViewPager mViewPager;

    ImageView zh_back;

    TextView zh_title;

    TextView zh_select_num;
    ImageDetailAdapter mAdapter;

    public static void open(Context context, ArrayList<FileItem> dataList, int position) {
        Intent intent = new Intent(context, ImageDetailActivity.class);
        intent.putExtra("position", position);
        fileItems = dataList;
        context.startActivity(intent);
    }


    @Override
    protected int setLayoutId() {
        return R.layout.zzh_image_preview;
    }

    @Override
    protected void initView() {
        mViewPager = (ViewPager) findViewById(R.id.zzh_view_pager);
        zh_back = (ImageView) findViewById(R.id.zh_back);
        zh_title = (TextView) findViewById(R.id.zh_title);
        zh_select_num = (TextView) findViewById(R.id.zh_select_num);
        int position = getIntent().getIntExtra("position", 0);
        if (fileItems == null) {
            finish();
        }
        mAdapter = new ImageDetailAdapter(this, fileItems);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(position, true);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initSetListener() {
        zh_back.setOnClickListener(this);
        zh_title.setOnClickListener(this);
        zh_select_num.setOnClickListener(this);

    }

    @Override
    protected void handlerMessage(Message msg) {

    }

    @Override
    public void onClick(View v) {
        if (v == zh_back) {
            finish();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fileItems = null;
    }
}
