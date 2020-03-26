package com.zzh.zlibs.image;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.zzh.zlibs.R;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by Administrator.
 *
 * @date: 2019/7/12
 * @email: zzh_hz@126.com
 * @QQ: 1299234582
 * @author: zzh
 * @description: ZzhLibs
 * @since 1.0
 */
public class ImagePreviewDialog extends Dialog {
    private ViewPager zzh_view_pager;

    public ImagePreviewDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zzh_image_preview);
        zzh_view_pager = findViewById(R.id.zzh_view_pager);
        PagerAdapter mPagerAdapter = null;
        zzh_view_pager.setAdapter(mPagerAdapter);
    }
}
