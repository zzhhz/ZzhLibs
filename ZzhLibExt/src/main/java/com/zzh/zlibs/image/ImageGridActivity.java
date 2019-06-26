package com.zzh.zlibs.image;

import android.content.ContentResolver;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zzh.zlibs.R;
import com.zzh.zlibs.base.BaseActivity;
import com.zzh.zlibs.image.adapter.ImageAdapter;
import com.zzh.zlibs.image.model.FileItem;
import com.zzh.zlibs.image.runnable.ScanImageRunnable;
import com.zzh.zlibs.pop.PopImageDialog;

import java.util.List;

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
public class ImageGridActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    ImageView zh_back;
    TextView zh_title;
    TextView zh_select_num;
    TextView zh_folder_name;
    TextView zh_preview;
    FrameLayout zh_confirm;
    GridView zh_grid;

    public PopImageDialog popImageFolder;
    private ScanImageRunnable.Builder mBuilder;
    private ScanImageRunnable scanImageRunnable;
    private ImageAdapter imageAdapter;

    @Override
    protected int setLayoutId() {
        return R.layout.zzh_image_grid;
    }

    @Override
    protected void initView() {
        setSwipeBackEnable(false);
        zh_grid = (GridView) findViewById(R.id.zh_grid);
        zh_folder_name = (TextView) findViewById(R.id.zh_folder_name);
        zh_back = (ImageView) findViewById(R.id.zh_back);
        zh_title = (TextView) findViewById(R.id.zh_title);
        zh_select_num = (TextView) findViewById(R.id.zh_select_num);
        zh_preview = (TextView) findViewById(R.id.zh_preview);
        zh_confirm = (FrameLayout) findViewById(R.id.zh_confirm);
    }

    @Override
    protected void initData() {
        mBuilder = new ScanImageRunnable.Builder();
        mBuilder.setBucketDisplayName("");
        scanImageRunnable = new ScanImageRunnable(this, mBuilder);
        imageAdapter = new ImageAdapter(null, this);
        zh_grid.setAdapter(imageAdapter);
        imageAdapter.setDataList(scanImageRunnable.getImageFile());
    }

    @Override
    protected void initSetListener() {
        zh_folder_name.setOnClickListener(this);
    }

    @Override
    protected void handlerMessage(Message msg) {

    }

    @Override
    public void onClick(View v) {
        if (v == zh_folder_name) {
            if (popImageFolder == null) {
                popImageFolder = new PopImageDialog().popImageDialog(this, this);
            }
            popImageFolder.showBottom(zh_confirm);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String title = popImageFolder.getFileItem(position).getTitle();
        mBuilder.setBucketDisplayName(title);
        List<FileItem> imageFile = scanImageRunnable.setBuilder(mBuilder).getImageFile();
        imageAdapter.setDataList(imageFile);
    }
}
