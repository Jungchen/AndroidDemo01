package com.tsticn.club.adapter;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.joinandroidapp01.R;
 

public class PullToRefreshLayout extends RelativeLayout
{
	public static final String TAG = "PullToRefreshLayout";
	// 初始状态
	public static final int INIT = 0;
	// 释放刷新
	public static final int RELEASE_TO_REFRESH =1;
	// 正在刷新
	public static final int REFRESHING = 2;
	// 释放加载
	public static final int RELEASE_TO_LOAD = 3;
	// 正在加载
	public static final int LOADING = 4;
	// 操作完毕
	public static final int DONE = 5;
	// 当前状态
	private int state = INIT;
	// 刷新回调接口
	private OnRefreshListener mListener;
	// 刷新成功
	public static final int SUCCEED = 0;
	// 刷新失败
	public static final int FAIL = 1;
	// 按下Y坐标，上一个事件点Y坐标
	private float downY, lastY;

	// 下拉的距离
	public float pullDownY = 0;
	// 上拉的距离
	private float pullUpY = 0;

	// 释放刷新的距离
	private float refreshDist = 200;
	// 释放加载的距离
	private float loadmoreDist = 200;

	private MyTimer timer;
	// 回滚速度
	public float MOVE_SPEED = 8;
	// 第一次执行布局
	private boolean isLayout = false;
	// 在刷新过程中滑动操作
	private boolean isTouch = false;
	// 手指滑动距离与下拉头的滑动距离比，中间会随正切函数变化
	private float radio = 2;

	// 下拉箭头的转180°动画
	private RotateAnimation rotateAnimation;
	// 均匀旋转动画
	private RotateAnimation refreshingAnimation;

	// 下拉头
	private View refreshView;
	// 下拉的箭头
	private View pullView;
	// 正在刷新的图标
	private View refreshingView;
	// 刷新结果图标
	private View refreshStateImageView;
	// 刷新结果：成功或失败
	private TextView refreshStateTextView;

	// 上拉头
	private View loadmoreView;
	// 上拉的箭头
	private View pullUpView;
	// 正在加载的图标
	private View loadingView;
	// 加载结果图标
	private View loadStateImageView;
	// 加载结果：成功或失败
	private TextView loadStateTextView;

	// 实现了Pullable接口的View
	private View pullableView,pullframe;
	// 过滤多点触碰
	private int mEvents;

	private boolean canPullDown = false;
	private boolean canPullUp = false;
	/**
	 * 默认为true  不能刷新 =false
	 */
	public boolean isLoading = true;
	/**
	 * 默认为true  不能加载 =false
	 */
	public boolean isRefresh = true;
	
	/**
	 * 默认为true  不能刷新 =false  显示文字 图片
	 */
	public boolean isLoadingShow = true;
	/**
	 * 默认为true  不能加载 =false 显示文字 图片
	 */
	public boolean isRefreshShow = true;

	/**
	 * 执行自动回滚的handler
	 */
	Handler updateHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			// 回弹速度随下拉距离moveDeltaY增大而增大
			MOVE_SPEED = (float) (8 + 5 * Math.tan(Math.PI / 2
					/ getMeasuredHeight() * (pullDownY + Math.abs(pullUpY))));
			if (!isTouch)
			{
				// 正在刷新，且没有往上推的话则悬停，显示"正在刷新..."
				if (state == REFRESHING && pullDownY <= refreshDist)
				{
					pullDownY = refreshDist;
					timer.cancel();
				} else if (state == LOADING && -pullUpY <= loadmoreDist)
				{
					pullUpY = -loadmoreDist;
					timer.cancel();
				}

			}
			if (pullDownY > 0)
				pullDownY -= MOVE_SPEED;
			else if (pullUpY < 0)
				pullUpY += MOVE_SPEED;
			if (pullDownY < 0)
			{
				// 已完成回弹
				pullDownY = 0;
				pullView.clearAnimation();
				// 隐藏下拉头时有可能还在刷新，只有当前状态不是正在刷新时才改变状态
				if (state != REFRESHING && state != LOADING)
					changeState(INIT);
				timer.cancel();
			}
			if (pullUpY > 0)
			{
				// 已完成回弹
				pullUpY = 0;
				pullUpView.clearAnimation();
				// 隐藏下拉头时有可能还在刷新，只有当前状态不是正在刷新时才改变状态
				if (state != REFRESHING && state != LOADING)
					changeState(INIT);
				timer.cancel();
			}
			// 刷新布局,会自动调用onLayout
			requestLayout();
		}

	};

	public void setOnRefreshListener(OnRefreshListener listener)
	{
		mListener = listener;
	}

	public PullToRefreshLayout(Context context)
	{
		super(context);
		initView(context);
	}

	public PullToRefreshLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initView(context);
	}

	public PullToRefreshLayout(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		initView(context);
	}

	private void initView(Context context)
	{
		timer = new MyTimer(updateHandler);
		rotateAnimation = (RotateAnimation) AnimationUtils.loadAnimation(
				context, R.anim.reverse_anim);
		refreshingAnimation = (RotateAnimation) AnimationUtils.loadAnimation(
				context, R.anim.rotating);
		// 添加匀速转动动画
		LinearInterpolator lir = new LinearInterpolator();
		rotateAnimation.setInterpolator(lir);
		refreshingAnimation.setInterpolator(lir);
	}

	private void hide()
	{
		timer.schedule(5);
	}

	public void refreshFinish(int refreshResult)
	{
		refreshingView.clearAnimation();
		refreshingView.setVisibility(View.GONE);
		switch (refreshResult)
		{
		case SUCCEED:
			// 刷新成功
			if(isRefreshShow){
				refreshStateImageView.setVisibility(View.VISIBLE);
				
				refreshStateTextView.setText(R.string.refresh_succeed);
				
				refreshStateImageView
						.setBackgroundResource(R.drawable.pull_succeed);
			}
			break;
		case FAIL:
		default:
			// 刷新失败
			if(isRefreshShow){
				refreshStateImageView.setVisibility(View.VISIBLE);
				refreshStateTextView.setText(R.string.refresh_fail);
				refreshStateImageView
						.setBackgroundResource(R.drawable.pull_failed);
			}
			break;
		}
		// 刷新结果停留1秒
		new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				changeState(DONE);
				hide();
			}
		}.sendEmptyMessageDelayed(0, 50);
	}

	/**
	 * 加载完毕，显示加载结果。注意：加载完成后一定要调用这个方法
	 * 
	 * @param refreshResult
	 *            PullToRefreshLayout.SUCCEED代表成功，PullToRefreshLayout.FAIL代表失败
	 */
	public void loadmoreFinish(int refreshResult)
	{
		loadingView.clearAnimation();
		loadingView.setVisibility(View.GONE);
		switch (refreshResult)
		{
		case SUCCEED:
			// 加载成功
			if(isLoadingShow){
				loadStateImageView.setVisibility(View.VISIBLE);
				loadStateTextView.setText(R.string.load_succeed);
				loadStateImageView.setBackgroundResource(R.drawable.pull_succeed);
			}
			break;
		case FAIL:
		default:
			// 加载失败
			if(isLoadingShow){
				loadStateImageView.setVisibility(View.VISIBLE);
				loadStateTextView.setText(R.string.load_fail);
				loadStateImageView.setBackgroundResource(R.drawable.pull_failed);
			}
			break;
		}
		// 刷新结果停留1秒
		new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				changeState(DONE);
				hide();
			}
		}.sendEmptyMessageDelayed(0, 50);
	}

	private void changeState(int to)
	{
		state = to;
		switch (state)
		{
		case INIT:
			// 下拉布局初始状态
			if(isRefreshShow){
				refreshStateImageView.setVisibility(View.GONE);
				refreshStateTextView.setText(R.string.pull_to_refresh);
				pullView.clearAnimation();
				pullView.setVisibility(View.VISIBLE);
				// 上拉布局初始状态
			}
			if(isLoadingShow){
				loadStateImageView.setVisibility(View.GONE);
				loadStateTextView.setText(R.string.pullup_to_load);
				pullUpView.clearAnimation();
				pullUpView.setVisibility(View.VISIBLE);
			}
			break;
		case RELEASE_TO_REFRESH:
			// 释放刷新状态
			if(isRefreshShow){
				refreshStateTextView.setText(R.string.release_to_refresh);
				pullView.startAnimation(rotateAnimation);
			}
			break;
		case REFRESHING:
			// 正在刷新状态
			if(isRefreshShow){
				pullView.clearAnimation();
				refreshingView.setVisibility(View.VISIBLE);
				pullView.setVisibility(View.INVISIBLE);
				refreshingView.startAnimation(refreshingAnimation);
				refreshStateTextView.setText(R.string.refreshing);
			}
			break;
		case RELEASE_TO_LOAD:
			// 释放加载状态
			if(isLoadingShow){
				loadStateTextView.setText(R.string.release_to_load);
				pullUpView.startAnimation(rotateAnimation);
			}
			break;
		case LOADING:
			// 正在加载状态
			if(isLoadingShow){
				pullUpView.clearAnimation();
				loadingView.setVisibility(View.VISIBLE);
				pullUpView.setVisibility(View.INVISIBLE);
				loadingView.startAnimation(refreshingAnimation);
				loadStateTextView.setText(R.string.loading);
			}
			break;
		case DONE:
			break;
		}
	}
	private void releasePull()
	{
		if (isRefresh) {
			canPullDown = true;
		}else{
			canPullDown = false;
		}
		
		if (isLoading) {
			canPullUp = true;
		}else{
			canPullUp = false;
		}
	}


	@SuppressLint("NewApi")
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{
		switch (ev.getActionMasked())
		{
		case MotionEvent.ACTION_DOWN:
			downY = ev.getY();
			lastY = downY;
			timer.cancel();
			mEvents = 0;
			releasePull();
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
		case MotionEvent.ACTION_POINTER_UP:
			// 过滤多点触碰
			mEvents = -1;
			break;
		case MotionEvent.ACTION_MOVE:
			if (mEvents == 0)
			{
				if (((Pullable) pullableView).canPullDown() && canPullDown
						&& state != LOADING)
				{
					pullDownY = pullDownY + (ev.getY() - lastY) / radio;
					if (pullDownY < 0)
					{
						pullDownY = 0;
						canPullDown = false;
						if (isLoading) {
							canPullUp = true;
						}
					}
					if (pullDownY > getMeasuredHeight())
						pullDownY = getMeasuredHeight();
					if (state == REFRESHING)
					{
						isTouch = true;
					}
				} else if (((Pullable) pullableView).canPullUp() && canPullUp
						&& state != REFRESHING)
				{
					pullUpY = pullUpY + (ev.getY() - lastY) / radio;
					if (pullUpY > 0)
					{
						pullUpY = 0;
						if (isRefresh) {
							canPullDown = true;
						}
						canPullUp = false;
					}
					if (pullUpY < -getMeasuredHeight())
						pullUpY = -getMeasuredHeight();
					if (state == LOADING)
					{
						isTouch = true;
					}
				} else
					releasePull();
			} else
				mEvents = 0;
			lastY = ev.getY();
			radio = (float) (2 + 2 * Math.tan(Math.PI / 2 / getMeasuredHeight()
					* (pullDownY + Math.abs(pullUpY))));
			requestLayout();
			if (pullDownY <= refreshDist && state == RELEASE_TO_REFRESH)
			{
				changeState(INIT);
			}
			if (pullDownY >= refreshDist && state == INIT)
			{
				changeState(RELEASE_TO_REFRESH);
			}
			if (-pullUpY <= loadmoreDist && state == RELEASE_TO_LOAD)
			{
				changeState(INIT);
			}
			if (-pullUpY >= loadmoreDist && state == INIT)
			{
				changeState(RELEASE_TO_LOAD);
			}
			if ((pullDownY + Math.abs(pullUpY)) > 8)
			{
				ev.setAction(MotionEvent.ACTION_CANCEL);
			}
			break;
		case MotionEvent.ACTION_UP:
			if (pullDownY > refreshDist || -pullUpY > loadmoreDist)
				isTouch = false;
			if (state == RELEASE_TO_REFRESH)
			{
				changeState(REFRESHING);
				// 刷新操作
				if (mListener != null)
					mListener.onRefresh(this);
			} else if (state == RELEASE_TO_LOAD)
			{
				changeState(LOADING);
				// 加载操作
				if (mListener != null)
					mListener.onLoadMore(this);
			}
			hide();
		default:
			break;
		}
		super.dispatchTouchEvent(ev);
		return true;
	}

	private void initView()
	{
		// 初始化下拉布局
		pullView = refreshView.findViewById(R.id.pull_icon);
		refreshStateTextView = (TextView) refreshView
				.findViewById(R.id.state_tv);
		refreshingView = refreshView.findViewById(R.id.refreshing_icon);
		refreshStateImageView = refreshView.findViewById(R.id.state_iv);
		// 初始化上拉布局
		pullUpView = loadmoreView.findViewById(R.id.pullup_icon);
		loadStateTextView = (TextView) loadmoreView
				.findViewById(R.id.loadstate_tv);
		loadingView = loadmoreView.findViewById(R.id.loading_icon);
		loadStateImageView = loadmoreView.findViewById(R.id.loadstate_iv);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b)
	{
		if (!isLayout)
		{
			refreshView = getChildAt(0);
			pullframe = getChildAt(1);
//			if(((ViewGroup) pullframe).getChildCount()>0){
//				pullableView = ((ViewGroup) pullframe).getChildAt(0);
//			}else{
				pullableView =pullframe;
//			}
			
			loadmoreView = getChildAt(2);
			isLayout = true;
			initView();
			refreshDist = ((ViewGroup) refreshView).getChildAt(0)
					.getMeasuredHeight();
			loadmoreDist = ((ViewGroup) loadmoreView).getChildAt(0)
					.getMeasuredHeight();
		}
		refreshView.layout(0,
				(int) (pullDownY + pullUpY) - refreshView.getMeasuredHeight(),
				refreshView.getMeasuredWidth(), (int) (pullDownY + pullUpY));
		pullframe.layout(0, (int) (pullDownY + pullUpY),
				pullframe.getMeasuredWidth(), (int) (pullDownY + pullUpY)
						+ pullframe.getMeasuredHeight());
		loadmoreView.layout(0,
				(int) (pullDownY + pullUpY) + pullframe.getMeasuredHeight(),
				loadmoreView.getMeasuredWidth(),
				(int) (pullDownY + pullUpY) + pullframe.getMeasuredHeight()
						+ loadmoreView.getMeasuredHeight());
	}

	class MyTimer
	{
		private Handler handler;
		private Timer timer;
		private MyTask mTask;

		public MyTimer(Handler handler)
		{
			this.handler = handler;
			timer = new Timer();
		}

		public void schedule(long period)
		{
			if (mTask != null)
			{
				mTask.cancel();
				mTask = null;
			}
			mTask = new MyTask(handler);
			timer.schedule(mTask, 0, period);
		}

		public void cancel()
		{
			if (mTask != null)
			{
				mTask.cancel();
				mTask = null;
			}
		}

		class MyTask extends TimerTask
		{
			private Handler handler;

			public MyTask(Handler handler)
			{
				this.handler = handler;
			}

			@Override
			public void run()
			{
				handler.obtainMessage().sendToTarget();
			}

		}
	}

	public interface OnRefreshListener
	{
		/**
		 * 刷新操作
		 */
		void onRefresh(PullToRefreshLayout pullToRefreshLayout);

		/**
		 * 加载操作
		 */
		void onLoadMore(PullToRefreshLayout pullToRefreshLayout);
	}

}

