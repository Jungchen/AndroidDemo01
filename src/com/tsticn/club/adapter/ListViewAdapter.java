package com.tsticn.club.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter {
	
	private List<Map<String, Object>> list;
	private LayoutInflater inflater = null;
	private int layoutID;
	private String flag[]; //CheckBox用
	
	private int ItemIDs[];
	
	private boolean styleFlag = false; //text样式，中划线等 仅用于TextField
	
	private int style;
	
	private HashMap<Integer, String> lstMap = new HashMap<Integer, String>();
	private TextWatcher textListener = null;

	public HashMap<Integer, String> getLstMap() {
		return lstMap;
	}

	public void setLstMap(HashMap<Integer, String> lstMap) {
		this.lstMap = lstMap;
	}
	
	public void setTextListenter(TextWatcher textListener){
		this.textListener = textListener;
	}
	
	public void setStyleFlag(Boolean flag){
		this.styleFlag = flag;
	}
	
	public void setStyle(int style){
		this.style = style;
	}

	/**
	 * @param context
	 *            MainActivity.this
	 * @param list
	 *            数据集
	 * @param layoutID
	 *            包含ListView的xml文件id名
	 * @param flag
	 *            Map数据的key跟具体组件对应
	 * @param ItemIDs
	 *            ListView item中需要设值的组件
	 */
	public ListViewAdapter(Context context, List<Map<String, Object>> list,
			int layoutID, String flag[], int ItemIDs[]) {
		this.inflater = LayoutInflater.from(context);
		this.list = list;
		this.layoutID = layoutID;
		this.flag = flag;
		this.ItemIDs = ItemIDs;
		styleFlag = false;
	}

	public void refresh(List<Map<String, Object>> list) {
		this.list = list;
		notifyDataSetChanged();
	}
	//刷新价格事件
	public void refreshPriceText(View convertView,int position){}
	//刷新文本事件
	public void refreshEditText(int position){}
	//刷新按钮页面事件
	public void refreshButton(int position){}
	
	
	public List<Map<String, Object>> getList(){
		return list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		convertView = inflater.inflate(layoutID, null);
//		 Log.d("Adapter", "----getItemId----" + flag.length);
		for (int i = 0; i < flag.length; i++) {
//			Log.d("Adapter", i +"[]" + ItemIDs[i] +  "----getItemId----" + convertView.findViewById(ItemIDs[i]));
//			Log.d("getView--->", i+ "  " + convertView.findViewById(ItemIDs[i]).getClass());
			if (convertView.findViewById(ItemIDs[i]) instanceof CheckBox) {
				CheckBox cb = (CheckBox) convertView.findViewById(ItemIDs[i]);
				if (list.get(position).get(flag[i]) != null) {
					cb.setChecked(true);
				}
			} else if (convertView.findViewById(ItemIDs[i]) instanceof Button) {
				Log.d("Button", "This is button->" + i);
				Button btn = (Button)convertView.findViewById(ItemIDs[i]);  
				btn.setOnClickListener(new OnClickListener(){
					//点击删除按钮
					@Override
					public void onClick(View view) {
						Log.d("Button", "This is button onclick=" + position);
						refreshButton(position);
					}

				});
			} else if (convertView.findViewById(ItemIDs[i]) instanceof ImageView) {
				ImageView iv = (ImageView) convertView.findViewById(ItemIDs[i]);
				if (list.get(position).get(flag[i]) != null) {
					iv.setBackgroundResource(ADIWebUtils.toInt(list.get(position).get(flag[i])));
				}
			} else if(convertView.findViewById(ItemIDs[i]) instanceof EditText){
				EditText editText = (EditText)convertView.findViewById(ItemIDs[i]);  
				editText.setText(String.valueOf(list.get(position).get(flag[i])) ); 
				if(textListener == null){
					
					editText.addTextChangedListener(new TextWatcher() {  
		                @Override  
		                public void onTextChanged(CharSequence s, int start, int before, int count) {       
		                }  
		                
		                @Override  
		                public void beforeTextChanged(CharSequence s, int start,   
		                        int count,int after) {       
		                }  
		                  
		                @Override  
		                public void afterTextChanged(Editable s) {  
		                    //将editText中改变的值设置的HashMap中  
		                    lstMap.put(position, s.toString());  
//		                    Log.d("afterTextChanged", s.toString() + " <----new map----->" + lstMap);
		                    String txt = s.toString();
		                    if(!txt.equals("")){
		                    	refreshEditText(position);
		                    }
		                    
		                }  
		            });  
					
					
					if(lstMap.get(position) != null){  
		                editText.setText(lstMap.get(position));
		            }  
				}else{
					editText.addTextChangedListener(textListener);
				}
			} else if (convertView.findViewById(ItemIDs[i]) instanceof TextView) {
				TextView tv = (TextView) convertView.findViewById(ItemIDs[i]);
				tv.setText(String.valueOf(list.get(position).get(flag[i])) );
				if(styleFlag){
					refreshPriceText(convertView,position);
				}
			} 
		}
		// addListener(convertView);
		return convertView;
	}

	// public void addListener(View convertView) {
	// ((Button)convertView.findViewById(R.id.btn)).setOnClickListener(
	// new View.OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// new AlertDialog.Builder(MainActivity.ma)
	// .setTitle("自定义通用SimpleAdapter")
	// .setMessage("按钮成功触发监听事件！")
	// .show();
	// }
	// });
	// }

}