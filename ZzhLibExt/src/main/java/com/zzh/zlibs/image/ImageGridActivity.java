package com.zzh.zlibs.image;

import android.content.ContentResolver;
import android.os.Message;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzh.zlibs.R;
import com.zzh.zlibs.base.BaseActivity;

/**
 * Created by Administrator.
 *
 * @date: 2019/2/21
 * @email: zzh_hz@126.com
 * @QQ: 1299234582
 * @author: zzh
 * @description: ZzhLibs 图片选择界面
 * @since 1.0
 */
public class ImageGridActivity extends BaseActivity {
    ImageView zh_back;
    TextView zh_title;
    TextView zh_select_num;
    TextView zh_folder_name;
    TextView zh_preview;
    FrameLayout zh_confirm;
    GridView zh_grid;

    @Override
    protected int setLayoutId() {
        return R.layout.zzh_image_grid;
    }

    @Override
    protected void initView() {
        setSwipeBackEnable(false);
        zh_grid = (GridView) findViewById(R.id.zh_grid);


    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initSetListener() {

    }

    @Override
    protected void handlerMessage(Message msg) {

    }

    @Override
    public void onClick(View v) {

    }
}
