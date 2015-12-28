package com.tsticn.club.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.example.joinandroidapp01.R;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.bitmap.callback.DefaultBitmapLoadCallBack;

public class XUtilsImageLoader {
	private BitmapUtils bitmapUtils;

	private Context mContext;
	private boolean isClick = true;

	public XUtilsImageLoader(Context context) {

		// TODO Auto-generated constructor stub

		this.mContext = context;

		bitmapUtils = new BitmapUtils(mContext);

//		bitmapUtils.configDefaultLoadingImage(R.drawable.loading);// 默认背景图片

		bitmapUtils.configDefaultLoadFailedImage(R.drawable.pull_failed);// 加载失败图片

		bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);// 设置图片压缩类型

	}

	/**
	 * 
	 * 
	 * 
	 * @author sunglasses
	 * 
	 * @category 图片回调函数
	 */

	public class CustomBitmapLoadCallBack extends

	DefaultBitmapLoadCallBack<ImageView> {

		@Override
		public void onLoading(ImageView container, String uri,

		BitmapDisplayConfig config, long total, long current) {
//			ADIWebUtils.startLoading(mContext, container);
		}

		@Override
		public void onLoadCompleted(ImageView container, final String uri,

		Bitmap bitmap, BitmapDisplayConfig config, BitmapLoadFrom from) {
			// super.onLoadCompleted(container, uri, bitmap, config, from);
//			ADIWebUtils.stopLoading2(container);
			fadeInDisplay(container, bitmap);
			if (isClick) {
				container.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
//						Intent intent = new Intent();
//						Bundle b = new Bundle();
//						b.putString("ZOOM", uri);
//						intent.putExtras(b);
//						intent.setClass(mContext, ADIWebUtils
//								.generateClass(Picture_Zoom_Acivity.class));
//						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//						mContext.startActivity(intent);
					}
				});
			}
		}

		@Override
		public void onLoadFailed(final ImageView container, final String uri,

		Drawable drawable) {
//			ADIWebUtils.stopLoading2(container);
			container.setImageResource(R.drawable.pull_failed);
			// TODO Auto-generated method stub
			// container.setOnClickListener(new OnClickListener() {
			// @Override
			// public void onClick(View v) {
			// bitmapUtils.display(container, uri,
			// new CustomBitmapLoadCallBack());
			// }
			// });

		}

	}

	private static final ColorDrawable TRANSPARENT_DRAWABLE = new ColorDrawable(

	android.R.color.transparent);

	/**
	 * 
	 * @author sunglasses
	 * 
	 * @category 图片加载效果
	 * 
	 * @param imageView
	 * 
	 * @param bitmap
	 */

	private void fadeInDisplay(ImageView imageView, Bitmap bitmap) {// 目前流行的渐变效果

		final TransitionDrawable transitionDrawable = new TransitionDrawable(

		new Drawable[] { TRANSPARENT_DRAWABLE,

		new BitmapDrawable(imageView.getResources(), bitmap) });

		imageView.setImageDrawable(transitionDrawable);

		transitionDrawable.startTransition(500);

	}

	/**
	 * 为图片赋值 默认图片 点击事件为查看大图
	 * 
	 * @param container
	 * @param url
	 */
	public void display(ImageView container, String url) {// 外部接口函数
		display(container, url, true);

	}

	/**
	 * 为图片赋值 isClick 判断图片点击事件自定义 默认图片 点击事件为查看大图
	 * 
	 * @param container
	 * @param url
	 * @param isClick
	 */
	public void display(ImageView container, String url, boolean isClick) {// 外部接口函数
//		ADIWebUtils.startLoading(mContext, container);
		this.isClick = isClick;
		bitmapUtils.display(container, url, new CustomBitmapLoadCallBack());

	}

	public void clearMemoryCache() {
		bitmapUtils.clearMemoryCache();
	}

	public void clearCacheUri(String uri) {
		bitmapUtils.clearCache(uri);
	}
}
