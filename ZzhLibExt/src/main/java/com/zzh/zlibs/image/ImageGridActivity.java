package com.zzh.zlibs.image;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zzh.zlibs.R;
import com.zzh.zlibs.base.BaseActivity;
import com.zzh.zlibs.image.adapter.ImageAdapter;
import com.zzh.zlibs.image.model.FileItem;
import com.zzh.zlibs.image.runnable.ScanImageRunnable;
import com.zzh.zlibs.pop.PopImageDialog;

import java.util.ArrayList;
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
public class ImageGridActivity extends BaseActivity implements AdapterView.OnItemClickListener, ImageAdapter.OnClickImageListener {

    public static final String DATA_ZZH_IMAGE = "zzh_data_image";
    public static final String EXTRA_ZZH_MAX_IMAGE = "zzh_max_count";

    private int maxCount = 9;

    View zh_back;
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

    /**
     * 自定义选择图片多少
     *
     * @param ctx            上下文
     * @param maxSelectCount 最多选择
     * @param requestCode    请求码
     */
    public static void openSelectImage(Activity ctx, int maxSelectCount, int requestCode) {
        Intent intent = new Intent(ctx, ImageGridActivity.class);
        if (maxSelectCount < 1 || maxSelectCount > 9) {
            maxSelectCount = 1;
        }
        intent.putExtra(EXTRA_ZZH_MAX_IMAGE, maxSelectCount);
        ctx.startActivityForResult(intent, requestCode);
    }

    /**
     * 单选图片
     *
     * @param ctx         上下文
     * @param requestCode 请求码
     */
    public static void open(Activity ctx, int requestCode) {
        openSelectImage(ctx, 1, requestCode);
    }

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
        maxCount = getIntent().getIntExtra(EXTRA_ZZH_MAX_IMAGE, 9);
        if (maxCount <= 1 || maxCount > 9) {
            maxCount = 1;
        }

        mBuilder = new ScanImageRunnable.Builder();
        mBuilder.setBucketDisplayName("");
        scanImageRunnable = new ScanImageRunnable(this, mBuilder);
        imageAdapter = new ImageAdapter(null, this, maxCount);
        imageAdapter.setOnClickImageListener(this);
        zh_grid.setAdapter(imageAdapter);
        imageAdapter.setDataList(scanImageRunnable.getImageFile());
        zh_select_num.setText("完成(" + imageAdapter.getSelectList().size() + "/" + maxCount + ")");
    }

    @Override
    protected void initSetListener() {
        zh_folder_name.setOnClickListener(this);
        zh_back.setOnClickListener(this);
        zh_confirm.setOnClickListener(this);
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
        } else if (v == zh_back) {
            finish();
        } else if (zh_confirm == v) {
            if (imageAdapter.getSelectList().size() > 0) {
                setResult(RESULT_OK);
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra(DATA_ZZH_IMAGE, (ArrayList) imageAdapter.getSelectList());
                setResult(RESULT_OK, intent);
            } else {
                setResult(RESULT_CANCELED);
            }
            finish();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        popImageFolder.setSelectPosition(position);
        String title = popImageFolder.getFileItem(position).getTitle();
        zh_folder_name.setText(title);
        zh_title.setText(title);
        mBuilder.setBucketDisplayName(title);
        List<FileItem> imageFile = scanImageRunnable.setBuilder(mBuilder).getImageFile();
        imageAdapter.clear();
        imageAdapter.setDataList(imageFile);
    }

    @Override
    public void onClickImage(View v, int position) {
        if (imageAdapter.contains(position)) {
            imageAdapter.getSelectList().remove(imageAdapter.getItem(position));
        } else {
            if (imageAdapter.getSelectList().size() >= maxCount) {
                Toast.makeText(this, "您最多选择" + maxCount + "图片或视频", Toast.LENGTH_SHORT).show();
            } else {
                imageAdapter.getSelectList().add(imageAdapter.getItem(position));
            }
        }
        imageAdapter.notifyDataSetChanged();
        zh_select_num.setText("完成(" + imageAdapter.getSelectList().size() + "/" + maxCount + ")");
    }

    @Override
    public void onClickPreviewImage(View v, int position) {
        ImageDetailActivity.open(this, imageAdapter.getDataList(), position);

    }
}
