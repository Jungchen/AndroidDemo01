package com.example.joinandroidapp01;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;

import com.tsticn.club.adapter.PullToRefreshLayout;
import com.tsticn.club.adapter.PullToRefreshLayout.OnRefreshListener;

@EActivity(R.layout.activity_main)
public class MainActivity extends Activity {
	@ViewById
	PullToRefreshLayout refreshLayout;
//	@ViewById
//	com.tsticn.club.adapter.PullableListView listview;
	//只要使用 PullToRefreshLayout 进行包裹 着 就可以 获取到下拉上拉的事件
	//默认这种页面是可以 弹动的
	/**
	 * 
	 * 默认为true  不能刷新 =false
	 */
//	public boolean isLoading = true;
//	/**
//	 * 默认为true  不能加载 =false
//	 */
//	public boolean isRefresh = true;
//	
//	/**
//	 * 默认为true  不能刷新 =false  显示文字 图片
//	 */
//	public boolean isLoadingShow = true;
//	/**
//	 * 默认为true  不能加载 =false 显示文字 图片
//	 */
//	public boolean isRefreshShow = true;
	@AfterViews
	void init() {
		refreshLayout.isLoadingShow = false;	//是否显示 拖动 时的刷新图标
		refreshLayout.isRefreshShow = true;
		//isRefresh 是否可以 进行上拉操作  false 不可以
		refreshLayout.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
				refreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
			}

			@Override
			public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
				refreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
			}
		});
		
//		List<Map<String, Object>> dataList=new ArrayList<Map<String,Object>>();
//		Map<String, Object> m1=new  HashMap<String, Object>();
//		m1.put("ITEM", "ITEM1");
//		dataList.add(m1);
//		Map<String, Object> m31=new  HashMap<String, Object>();
//		m31.put("ITEM", "ITEM31");
//		dataList.add(m31);
//		Map<String, Object> m11=new  HashMap<String, Object>();
//		m11.put("ITEM", "ITEM11");
//		dataList.add(m11);
//		Map<String, Object> m10=new  HashMap<String, Object>();
//		m10.put("ITEM", "ITEM10");
//		dataList.add(m10);
//		Map<String, Object> m101=new  HashMap<String, Object>();
//		m101.put("ITEM", "ITEM101");
//		dataList.add(m101);
//		Map<String, Object> m2101=new  HashMap<String, Object>();
//		m2101.put("ITEM", "ITEM2101");
//		dataList.add(m2101);
//		Map<String, Object> m2=new  HashMap<String, Object>();
//		m2.put("ITEM", "ITEM2");
//		dataList.add(m2);
//		Map<String, Object> m12=new  HashMap<String, Object>();
//		m12.put("ITEM", "ITEM12");
//		dataList.add(m12);
//		Map<String, Object> m122=new  HashMap<String, Object>();
//		m122.put("ITEM", "ITEM122");
//		dataList.add(m122);
//		ListViewAdapter listAdapter = new ListViewAdapter(getApplicationContext(), dataList,
//				R.layout.item, new String[] { "ITEM"}, new int[] {
//						R.id.textView1});
//		listview.setAdapter(listAdapter);
	}

}
