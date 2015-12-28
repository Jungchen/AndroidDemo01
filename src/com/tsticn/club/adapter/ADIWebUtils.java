package com.tsticn.club.adapter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.os.StatFs;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Reminders;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joinandroidapp01.R;

public class ADIWebUtils {
	private static final String ZERO = "0";
	static Toast toast = null;

	/**
	 * 弹出信息
	 * 
	 * @param context
	 * @param msg
	 */
	public static void showToast(final Context context, final String msg) {
		if (msg.equals("")) {
			return;
		}
		if (toast != null) {
			toast.cancel();
		}
		// Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				Looper.prepare();
				toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				Looper.loop();
			}
		}).start();
		// final AlertDialog dlg = new AlertDialog.Builder(context).create();
		// dlg.show();
		// Window window = dlg.getWindow();
		// // *** 主要就是在这里实现这种效果的.
		// // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
		// window.setContentView(R.layout.alertost);
		// // 为确认按钮添加事件,执行退出应用操作
		// LinearLayout ok = (LinearLayout) window.findViewById(R.id.ok);
		// TextView tostMsg = (TextView) window.findViewById(R.id.tostMsg);
		// tostMsg.setText(msg);
		// ok.setOnClickListener(new View.OnClickListener() {
		// public void onClick(View v) {
		// dlg.cancel();
		// }
		// });

	}

	// sqlite 中获得的空字符串 有为null 并为 "null"(字符串非空) 的情况 防止
	public static String nvl(Object obj) {
		return obj == null ? "" : (("null").equals(obj.toString()) == true ? ""
				: obj.toString());
	}

	public static String nvl(Object obj, String def) {
		return obj == null ? def : obj.toString();
	}

	public static boolean isNvl(Object obj) {
		if (null == obj || obj.equals("")) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public static double toLatDouble(Object obj) {// 成都
		return obj == null ? 30.663724 : Double.parseDouble(obj.toString());
	}

	public static double toLngDouble(Object obj) {// 成都
		return obj == null ? 104.072187 : Double.parseDouble(obj.toString());
	}

	public static int toInt(Object obj) {
		if (obj != null && !obj.equals("null")) {
			return nvl(obj).equals("") ? 0 : (new BigDecimal(obj.toString()))
					.intValue();
		}
		return 0;
	}

	public static int toInt(Object obj, int def) {
		if (obj != null && !obj.equals("null")) {
			return nvl(obj).equals("") ? def : (new BigDecimal(obj.toString()))
					.intValue();
		}
		return def;
	}

	public static String toIntString(Object obj) {
		if (obj != null && !obj.equals("null")) {
			return nvl(obj).equals("") ? ZERO : String.valueOf((new BigDecimal(
					obj.toString())).intValue());
		}
		return ZERO;
	}

	public static Double toDouble(Object obj) {
		if (obj != null && !obj.equals("")) {
			return nvl(obj).equals("") ? 0.0 : Double.parseDouble(obj
					.toString());
		}
		return 0.0;
	}

	public static float toFloat(Object obj) {
		if (obj != null && !obj.equals("")) {
			return nvl(obj).equals("") ? 0f : Float.parseFloat(obj.toString());
		}
		return 0f;
	}

	public static int per2Int(Object obj) {
		return obj == null ? 0 : Integer.parseInt(obj.toString().substring(0,
				obj.toString().length() - 1));
	}

	/**
	 * String[] 转换为List<Map>形式
	 * 
	 * @param keys
	 * @param values
	 * @return List<Map>
	 */
	public static List<Map<String, Object>> singleGenerator(String[] keys,
			Object[] values) {
		List<Map<String, Object>> rtnList = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < keys.length; i++) {
			map.put(keys[i], values[i]);
		}
		rtnList.add(map);
		return rtnList;
	}

	/**
	 * 包装Map为List<Map>形式
	 * 
	 * @param obj
	 * @return List<Map>
	 */
	public static List<Map<String, Object>> wrapperList(Map<String, Object> obj) {
		List<Map<String, Object>> rtnList = new ArrayList<Map<String, Object>>();
		rtnList.add(obj);
		return rtnList;
	}

	/**
	 * wifi模块入口-网络连接判断
	 * 
	 * @param context
	 * @return false情况下不进入下一个模块
	 */
	public static boolean isConnect(Context context) {
		// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				// 获取网络连接管理的对象
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return Boolean.TRUE;
					}
				}
			}
		} catch (Exception e) {
			Log.d("error", e.toString());
		}
		return Boolean.FALSE;
	}

	/**
	 * 比较日期大小
	 * 
	 * @param sync
	 * @param last
	 * @return sync > last = true
	 */
	public static boolean compareDate(String sync, String last) {
		if (last == null || last.equals("")) {
			return true;
		}
		java.text.DateFormat df = new java.text.SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		java.util.Calendar c1 = java.util.Calendar.getInstance();
		java.util.Calendar c2 = java.util.Calendar.getInstance();
		try {
			c1.setTime(df.parse(sync));
			c2.setTime(df.parse(last));
		} catch (java.text.ParseException e) {
			return true;
		}
		int result = c1.compareTo(c2);
		if (result > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 取得系统时间
	 * 
	 * @return String
	 */
	public static String getCurrentTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		return df.format(date);
	}

	// 取得本机号码
	public static String getLinePhone(Context context) {
		TelephonyManager phoneMgr = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return phoneMgr.getLine1Number();
	}

	// 取得当前系统时间
	public static String getDateTime() {
		String paramFormat = "yyyy-MM-dd";
		SimpleDateFormat sdf = new SimpleDateFormat(paramFormat);
		return sdf.format(System.currentTimeMillis()).toString();
	}

	// 取得当前系统时间yyyy-mm-dd hh:mm
	public static String getDateTimeHHMM() {
		String paramFormat = "yyyy-MM-dd hh:mm";
		SimpleDateFormat sdf = new SimpleDateFormat(paramFormat);
		return sdf.format(System.currentTimeMillis()).toString();
	}

	public static String getDateTimeHHMM(String time) {
		String paramFormat = "yyyy-MM-dd hh:mm";
		SimpleDateFormat sdf = new SimpleDateFormat(paramFormat);
		return sdf.format(time).toString();
	}

	public static String getDateTime(String time) {
		String paramFormat = "yyyy-MM-dd";
		SimpleDateFormat sdf = new SimpleDateFormat(paramFormat);
		return sdf.format(time).toString();
	}

	// 实现月份补0
	public static String getMonth(int month) {
		String mo = "";
		if (month < 9) {
			mo = "0" + (++month);
		} else {
			mo = (++month) + "";
		}
		return mo;
	}

	// 实现天补0
	public static String getDay(int day) {
		String mo = "";
		if (day < 10) {
			mo = "0" + (day);
		} else {
			mo = (day) + "";
		}
		return mo;
	}

	/**
	 * 获得当前时间==推荐
	 * 
	 * @return 2012-10-10 10:10
	 */
	public static String getTime() {
		Time time = new Time();
		time.setToNow();
		int year = time.year;
		int month = time.month;
		int day = time.monthDay;
		int minute = time.minute;
		int hour = time.hour;
		int sec = time.second;
		return year + "-" + getMonth(month) + "-" + getDay(day) + " "
				+ getDay(hour) + ":" + getDay(minute) + ":" + getDay(sec);

	}

	/**
	 * 根据当前时间获得 ID yyyyMMddHHmms
	 * 
	 * @return
	 */
	public static String getMID() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmms");
		return dateFormat.format(System.currentTimeMillis());
	}

	/**
	 * 类型转换为Date
	 * 
	 * @return
	 * @throws ParseException
	 */
	public static Date string2Date(String format, String date)
			throws ParseException {
		Date rtDate = null;
		if (!("").equals(date)) {
			SimpleDateFormat dateFormat = new SimpleDateFormat(format);
			rtDate = dateFormat.parse(date);
		}
		return rtDate;
	}

	public static final String DATE = "yyyy-MM-dd";
	public static final String DATETIME = "yyyy-MM-dd hh:mm";

	private static String day = "";

	/**
	 * 显示选择日期的弹出框
	 * 
	 * @param context
	 *            M03A_01_BasicInfo.this
	 * @param tv
	 *            m03_signing_date上显示日期内容
	 */
//	public static void getDateDialog(Activity activity, final TextView tv) {
//		showTimeDialog(activity, tv, false);
//	}
//
//	/**
//	 * 显示选择日期的弹出框
//	 * 
//	 * @param context
//	 *            M03A_01_BasicInfo.this
//	 * @param tv
//	 *            m03_signing_date上显示日期内容
//	 */
//	public static void getTimeDialog(Activity activity, final TextView tv) {
//		showTimeDialog(activity, tv, true);
//	}
//
//	private static void showTimeDialog(Activity activity, final TextView tv,
//			boolean isDateTime) {
//		SimpleDateFormat dateFormat = null;
//		if (isDateTime) {
//			dateFormat = new SimpleDateFormat(DATETIME);
//		} else {
//			dateFormat = new SimpleDateFormat(DATE);
//		}
//
//		LayoutInflater inflater = LayoutInflater.from(activity
//				.getApplicationContext());
//		final View timepickerview = inflater.inflate(R.layout.timepicker, null);
//		ScreenInfo screenInfo = new ScreenInfo(activity);
//		// 是否选择时间
//		final WheelMain wheelMain = new WheelMain(timepickerview, isDateTime);
//		wheelMain.screenheight = screenInfo.getHeight();
//		String time = tv.getText().toString();
//		Calendar calendar = Calendar.getInstance();
//		try {
//			calendar.setTime(dateFormat.parse(time));
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		int year = calendar.get(Calendar.YEAR);
//		int month = calendar.get(Calendar.MONTH);
//		int day = calendar.get(Calendar.DAY_OF_MONTH);
//		int h = calendar.get(Calendar.HOUR_OF_DAY);
//		int m = calendar.get(Calendar.MINUTE);
//		wheelMain.initDateTimePicker(year, month, day, h, m);
//		final AlertDialog dlg = new AlertDialog.Builder(activity).create();
//		dlg.show();
//		Window window = dlg.getWindow();
//		window.setContentView(timepickerview);
//		window.setGravity(Gravity.BOTTOM);
//		WindowManager man = activity.getWindowManager();
//		Display d = man.getDefaultDisplay();
//		android.view.WindowManager.LayoutParams p = dlg.getWindow()
//				.getAttributes();
//		p.width = (int) (d.getWidth());
//		dlg.getWindow().setAttributes(p);
//		// 为确认按钮添加事件,执行退出应用操作
//		Button ok = (Button) window.findViewById(R.id.ok);
//		ok.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//				tv.setText(wheelMain.getTime());
//				dlg.cancel();
//			}
//		});
//		Button cancel = (Button) window.findViewById(R.id.cancel);
//		cancel.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//				dlg.cancel();
//			}
//		});
//
//	}

	/**
	 * 正则验证是否为数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		return isNum.matches();
	}

	/**
	 * 正则验证是否为数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean regexIs(String str, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher isNum = pattern.matcher(str);
		return isNum.matches();
	}

	/**
	 * 缩小图片大小 推荐(节约内存)
	 * 
	 * @param path
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap resizeImage(String path, int width) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;// 不加载bitmap到内存中
		BitmapFactory.decodeFile(path, options);
		int outWidth = options.outWidth;
		int outHeight = options.outHeight;
		options.inDither = false;
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		options.inSampleSize = 1;
		if (outWidth != 0 && outHeight != 0 && width != 0) {
			int sampleSize = outWidth / width;
			if (!(sampleSize < 1)) {
				options.inSampleSize = sampleSize;
			}
		}
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(path, options);
	}

	/**
	 * drawableToBitmap 类型转化
	 * 
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	@SuppressLint("NewApi")
	public Bitmap stringtoBitmap(String string) { // 将字符串转换成Bitmap类型
		Bitmap bitmap = null;
		try {
			byte[] bitmapArray;
			bitmapArray = Base64.decode(string, Base64.DEFAULT);
			bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
					bitmapArray.length);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;

	}

	/**
	 * 保存图片
	 * 
	 * @param bitmap
	 * @param p
	 *            save路径
	 * @throws FileNotFoundException
	 */
	public static void saveThePicture(Bitmap bitmap, String p) throws Exception {
		File file = new File(p);

		FileOutputStream fos = new FileOutputStream(file);
		// bitmap.
		if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)) {
			fos.flush();
			fos.close();
		}

	}

	/**
	 * 图片路径转化为 Bitmap
	 * 
	 * @param path
	 * @param w
	 * @param h
	 * @return
	 */
	public static Bitmap convert2Bitmap(String path, int w, int h) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		// 设置为ture只获取图片大小
		opts.inJustDecodeBounds = true;
		opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
		// 返回为空
		BitmapFactory.decodeFile(path, opts);
		int width = opts.outWidth;
		int height = opts.outHeight;
		float scaleWidth = 0.f, scaleHeight = 0.f;
		if (width > w || height > h) {
			// 缩放
			scaleWidth = ((float) width) / w;
			scaleHeight = ((float) height) / h;
		}
		opts.inJustDecodeBounds = false;
		float scale = Math.max(scaleWidth, scaleHeight);
		opts.inSampleSize = (int) scale;
		WeakReference<Bitmap> weak = new WeakReference<Bitmap>(
				BitmapFactory.decodeFile(path, opts));
		return Bitmap.createScaledBitmap(weak.get(), w, h, true);
	}

	/**
	 * copy 文件到 指定的目录下的方法
	 * 
	 * @param con
	 * @param strOutFileName
	 * @param name
	 * @throws IOException
	 */
	public static void copyDataT2TSD(Context con, String strOutFileName,
			String strInFileName) throws IOException {
		InputStream myInput;
		OutputStream myOutput = new FileOutputStream(strOutFileName);
		myInput = new FileInputStream(strInFileName);
		byte[] buffer = new byte[1024];
		int length = myInput.read(buffer);
		while (length > 0) {
			myOutput.write(buffer, 0, length);
			length = myInput.read(buffer);
		}
		myOutput.flush();
		myInput.close();
		myOutput.close();
	}

	/**
	 * copy Assets目录下的文件到 指定的目录下的方法
	 * 
	 * @param con
	 * @param strOutFileName
	 * @param name
	 * @throws IOException
	 */
	public static void copyBigDataToSD(Context con, String strOutFileName,
			String name) throws IOException {
		InputStream myInput;
		OutputStream myOutput = new FileOutputStream(strOutFileName);
		myInput = con.getAssets().open(name);
		byte[] buffer = new byte[1024];
		int length = myInput.read(buffer);
		while (length > 0) {
			myOutput.write(buffer, 0, length);
			length = myInput.read(buffer);
		}

		myOutput.flush();
		myInput.close();
		myOutput.close();
	}

	/**
	 * 修改日历活动
	 * 
	 * @param conRes
	 *            调用日历活动的上下文
	 * @param id
	 *            时间ID
	 * @param cv
	 *            事件内容
	 * @param cr
	 *            提醒内容
	 */
	@SuppressLint("NewApi")
	public static void UpdateCalendar(ContentResolver conRes, long id,
			ContentValues cv, ContentValues cr) {
		String[] wheArg = { id + "" };
		if (cv != null) {
			String where1 = CalendarContract.Events._ID + "= ? ";
			conRes.update(CalendarContract.Events.CONTENT_URI, cv, where1,
					wheArg);
		}
		if (cr != null) {
			String where2 = Reminders.EVENT_ID + "= ? ";
			conRes.update(Reminders.CONTENT_URI, cr, where2, wheArg);
		}
	}

	/**
	 ** 
	 * 新增日历活动 返回 eventID
	 * 
	 * @param st
	 *            开始时间
	 * @param dt
	 *            结束时间
	 * @param title
	 *            标题
	 * @param desc
	 *            说明
	 * @param loca
	 *            地点
	 * @param i
	 *            提前分钟默认为0
	 * @throws ParseException
	 */
	@SuppressLint("NewApi")
	public static long InsertCalendar(ContentResolver conRes, String st,
			String title, String desc, String loca, Integer i) throws Exception {
		Uri uri = CalendarContract.Events.CONTENT_URI; // 获取URI 位置

		// 提醒事件
		long date = Date2Long(st, "yyyy-MM-dd HH:mm");
		ContentValues cv = new ContentValues();
		cv.put(CalendarContract.Events.EVENT_TIMEZONE, "GMT + 8");
		cv.put(CalendarContract.Events.DTSTART, date);
		cv.put(CalendarContract.Events.DTEND, date);
		cv.put(CalendarContract.Events.TITLE, title); // 主题
		cv.put(CalendarContract.Events.DESCRIPTION, desc); // 描述
		cv.put(CalendarContract.Events.EVENT_LOCATION, loca); // 地点
		cv.put(CalendarContract.Events.CALENDAR_ID, 1);
		Uri ci = conRes.insert(uri, cv);
		long eventID = Long.parseLong(ci.getLastPathSegment());
		// 给时间添加提醒
		ContentValues values = new ContentValues();
		values.put(Reminders.MINUTES, i);
		values.put(Reminders.EVENT_ID, eventID);
		values.put(Reminders.METHOD, Reminders.METHOD_ALERT);
		conRes.insert(Reminders.CONTENT_URI, values);

		return eventID;
	}

	/**
	 * 删除指定ID 的日历活动及提醒
	 * 
	 * @param id
	 */
	@SuppressLint("NewApi")
	public static void DeleteCalendar(ContentResolver conRes, String id) {

		String[] wheArg = { id };
		String where1 = CalendarContract.Events._ID + "= ? ";
		conRes.delete(CalendarContract.Events.CONTENT_URI, where1, wheArg);

		String where2 = Reminders.EVENT_ID + "= ? ";
		conRes.delete(Reminders.CONTENT_URI, where2, wheArg);

	}

	/**
	 * 传入String 类型的date 转化为时间戳
	 * 
	 * @param date
	 *            String 日期
	 * @param format
	 *            格式类型
	 * @return Long time
	 * @throws ParseException
	 */
	public static long Date2Long(String date, String format)
			throws ParseException {
		SimpleDateFormat _format = new SimpleDateFormat(format);// 设置格式
		Date _date = _format.parse(date);
		return _date.getTime();

	}

	// 检查版本信息
	public static int getVerCode(Context context) {
		int verCode = -1;
		try {
			verCode = context.getPackageManager().getPackageInfo(
					"com.chuangye", 0).versionCode;
		} catch (Exception e) {
			Log.e("getVerCode", e.getMessage());
		}
		return verCode;
	}

	public static String getVerName(Context context) {
		String verName = "";
		try {
			verName = context.getPackageManager().getPackageInfo(
					"com.chuangye", 0).versionName;
		} catch (Exception e) {
			Log.e("getVerName", e.getMessage());
		}
		return verName;
	}

	public static List<Map<String, Object>> nvlList(
			List<Map<String, Object>> list, String[] isNvl) {
		List<Map<String, Object>> _list = new ArrayList<Map<String, Object>>();
		if (list != null) {
			for (Map<String, Object> m : list) {
				for (String obj : isNvl) {

					m.put(obj, ADIWebUtils.nvl(m.get(obj)));
				}
				_list.add(m);
			}
			list.clear();
			list.addAll(_list);
		}

		return list;
	}

	/**
	 * 图片压缩 通过 路径传值(不推荐 会消耗内存)
	 * 
	 * @param path
	 * @param i
	 * @return
	 */
//	public static Bitmap comprePic(String path, int i) {
//		Bitmap bitmap = BitmapFactory.decodeFile(path);
//		Bitmap newBitmap = null;
//		float w = bitmap.getWidth();
//		float h = bitmap.getHeight();
//		if (!(w > (float) i && h > (float) i)) {
//			return bitmap;
//		}
//		if (w > 0 && h > 0) {
//			int _w = (int) (w / (w / i));
//			int _h = (int) (h / (h / i));
//			if (_w == w && _h == h) {
//				_h = _h - 1;
//				_w = _w - 1;
//			}
//			newBitmap = BitmapUtil.getBitmap(bitmap, _w, _h);
//			bitmap.recycle();
//		}
//
//		return newBitmap;
//	}

	/*
	 * 图片压缩 通过 路径传值(不推荐 会消耗内存)
	 * 
	 * @param path
	 * 
	 * @param i
	 * 
	 * @return
	 */
//	public static Bitmap comprePicBitmap(Bitmap bitmap, int i) {
//		Bitmap newBitmap = null;
//		float w = bitmap.getWidth();
//		float h = bitmap.getHeight();
//		if (!(w > (float) i && h > (float) i)) {
//			return bitmap;
//		}
//		if (w > 0 && h > 0) {
//			int _w = (int) (w / (w / i));
//			int _h = (int) (h / (h / i));
//			if (_w == w && _h == h) {
//				_h = _h - 1;
//				_w = _w - 1;
//			}
//			newBitmap = BitmapUtil.getBitmap(bitmap, _w, _h);
//			bitmap.recycle();
//		}
//
//		return newBitmap;
//	}

	/**
	 * Bitmap 转化String
	 * 
	 * @param bitmap
	 * @return
	 */
	@SuppressLint("NewApi")
	public static String Bitmap2String(Bitmap bitmap) {
		if (bitmap == null) {
			return "错误图片";
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
		bitmap.compress(CompressFormat.PNG, 100, baos);
		byte[] appicon = baos.toByteArray();// 转为byte数组
		return Base64.encodeToString(appicon, Base64.DEFAULT);

	}

	/**
	 * String 转化 Bitmap
	 * 
	 * @param String
	 * @return Bitmap
	 */
	@SuppressLint("NewApi")
	public static Bitmap string2Bitmap(String string) {
		// 将字符串转换成Bitmap类型
		Bitmap bitmap = null;
		try {
			byte[] bitmapArray = string.getBytes();
			// bitmapArray = Base64.decode(string, Base64.DEFAULT);
			bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
					bitmapArray.length);
		} catch (Exception e) {
			Log.e("string2Bitmap error", e.getMessage());
		}
		return bitmap;
	}

	/**
	 * 判断是否为图片格式
	 * 
	 * @param path
	 * @return
	 */
	public static boolean CheckPicture(String path) {
		Bitmap bitmap = ADIWebUtils.resizeImage(path, 50);
		if (bitmap == null) {
			return false;
		}
		return true;

	}

	/**
	 * 判断SD是否存在
	 * 
	 * @return
	 */
	public static boolean ExistSDCard() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		} else
			return false;
	}

	/**
	 * SD卡剩余空间 MB
	 * 
	 * @return
	 */
	public static long getSDFreeSize() {
		// 取得SD卡文件路径
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		// 获取单个数据块的大小(Byte)
		long blockSize = sf.getBlockSize();
		// 空闲的数据块的数量
		long freeBlocks = sf.getAvailableBlocks();
		// 返回SD卡空闲大小
		return (freeBlocks * blockSize) / 1024 / 1024; // 单位MB
	}

	/**
	 * 修改日期格式
	 */
	public static List<Map<String, Object>> date2List(
			List<Map<String, Object>> list, String[] dates,
			SimpleDateFormat[] sdfs) {
		List<Map<String, Object>> _list = new ArrayList<Map<String, Object>>();
		if (list != null) {
			for (Map<String, Object> m : list) {
				for (int i = 0; i < dates.length; i++) {
					if (ADIWebUtils.nvl(m.get(dates[i])).equals("")) {
						m.put(dates[i], ADIWebUtils.nvl(m.get(dates[i])));
					} else {

						m.put(dates[i],
								getStrTime(ADIWebUtils.nvl(m.get(dates[i])),
										sdfs[i]));

					}
				}
				_list.add(m);
			}
			list.clear();
			list.addAll(_list);
		}

		return list;
	}

	public static String getStrTime(String cc_time, SimpleDateFormat sdf) {
		String re_StrTime = null;
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));

		return re_StrTime;

	}

	public static boolean compareDate(String first, String last, String dfString) {
		if (last == null || last.equals("") || first == null
				|| first.equals("")) {
			return false;
		}
		java.text.DateFormat df = new java.text.SimpleDateFormat(dfString);
		java.util.Calendar c1 = java.util.Calendar.getInstance();
		java.util.Calendar c2 = java.util.Calendar.getInstance();
		try {
			c1.setTime(df.parse(first));
			c2.setTime(df.parse(last));
		} catch (java.text.ParseException e) {
			return false;
		}
		int i = c1.compareTo(c2);
		if (i != 1) {
			return true;
		}
		return false;
	}

	// 取本机通讯录
	public static List<Map<String, Object>> getPhoneContracts(Context mContext) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		ContentResolver resolver = mContext.getContentResolver();
		// 获取手机联系人
		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI, null, null,
				null, null); // 传入正确的uri
		if (phoneCursor != null) {
			while (phoneCursor.moveToNext()) {
				Map<String, Object> map = new HashMap<String, Object>();
				int nameIndex = phoneCursor.getColumnIndex(Phone.DISPLAY_NAME); // 获取联系人name
				String name = phoneCursor.getString(nameIndex);
				String phoneNumber = phoneCursor.getString(phoneCursor
						.getColumnIndex(Phone.NUMBER)); // 获取联系人number
				if (TextUtils.isEmpty(phoneNumber)) {
					continue;
				}
				map.put("NAME", name);
				map.put("PhoneNumber", phoneNumber);
				list.add(map);
			}
			phoneCursor.close();
		}
		return list;
	}

	// 接下来看获取sim卡的方法，sim卡的uri有两种可能content://icc/adn与content://sim/adn
	// （一般情况下是第一种） "content://icc/adn"
	public static List<Map<String, Object>> getSimContracts(Context mContext,
			String strcontext) {
		// 读取SIM卡手机号,有两种可能:content://icc/adn与content://sim/adn
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		ContentResolver resolver = mContext.getContentResolver();
		Uri uri = Uri.parse(strcontext);
		Cursor phoneCursor = resolver.query(uri, null, null, null, null);
		if (phoneCursor != null) {
			while (phoneCursor.moveToNext()) {
				Map<String, Object> map = new HashMap<String, Object>();
				String name = phoneCursor.getString(phoneCursor
						.getColumnIndex("name"));
				String phoneNumber = phoneCursor.getString(phoneCursor
						.getColumnIndex("number"));
				if (TextUtils.isEmpty(phoneNumber)) {
					continue;
				}
				map.put("NAME", name);
				map.put("PhoneNumber", phoneNumber);
				list.add(map);
			}
			phoneCursor.close();
		}
		return list;
	}

	/**
	 * 排序
	 * 
	 * @param list
	 * @param orderby
	 *            排序的字段
	 * @param desc
	 *            default 升序 false 降序 true
	 * @return
	 */
	public static List<Map<String, Object>> listSort(
			List<Map<String, Object>> list, final String orderby, boolean desc) {
		Collections.sort(list, new Comparator<Map<String, Object>>() {
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				int ret = 0;
				String dt1 = nvl(o1.get(orderby));
				String dt2 = nvl(o2.get(orderby));
				if (dt1.equals("") || dt2.equals("")) {
					return ret;
				}
				return ret = dt1.compareTo(dt2);
			}
		});
		if (desc) {
			Collections.reverse(list);
		}
		return list;
	}

	/**
	 * 数字排序
	 * 
	 * @param list
	 * @param orderby
	 *            排序的字段
	 * @param desc
	 *            default 升序 false 降序 true
	 * @return
	 */
	public static List<Map<String, Object>> listNumericSort(
			List<Map<String, Object>> list, final String orderby, boolean desc) {
		Collections.sort(list, new Comparator<Map<String, Object>>() {
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				Double dt1 = toDouble(o1.get(orderby));
				Double dt2 = toDouble(o2.get(orderby));
				return dt1.compareTo(dt2);
			}
		});
		if (desc) {
			Collections.reverse(list);
		}
		return list;
	}

	public static List<Map<String, Object>> listFilterByMap(
			List<Map<String, Object>> list, Map<String, Object> mapFilter,
			String all) {
		if (list == null) {
			return null;
		}
		List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
		retList.addAll(list);
		for (Map<String, Object> map : list) {
			for (String key : map.keySet()) {
				for (String _key : mapFilter.keySet()) {
					if (key.equals(_key)
							&& !nvl(mapFilter.get(_key)).equals(all)) {
						if (!ADIWebUtils.nvl(map.get(key)).equals(
								ADIWebUtils.nvl(mapFilter.get(_key)))) {
							retList.remove(map);
						}
					}
				}
			}
		}
		return retList;
	}

	public static List<Map<String, Object>> listFilterByKey(
			List<Map<String, Object>> list, String key, String value) {
		if (list == null) {
			return null;
		}
		List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : list) {
			if (nvl(map.get(key)).equals(value)) {
				retList.add(map);
			}

		}
		return retList;
	}

	public static Map<Integer, Object> getArrayIntgerMap(Context mContext,
			int _value) {
		Map<Integer, Object> map = new HashMap<Integer, Object>();
		String[] value = mContext.getResources().getStringArray(_value);
		int i = 0;
		for (String s : value) {
			map.put(i, s);
			i++;
		}
		return map;
	}

	public static void ClearViewProperties(Object[] orig) {
		for (int i = 0; i < orig.length; i++) {
			if (orig[i] instanceof TextView) {
				((TextView) orig[i]).setText("");
			} else if (orig[i] instanceof EditText) {
				((EditText) orig[i]).setText("");
			}
		}
	}

	public static String ReadTxtFile(String strFilePath) {
		String path = strFilePath;
		String content = ""; // 文件内容字符串
		try {
			InputStream instream = ADIWebUtils.class
					.getResourceAsStream(strFilePath);
			if (instream != null) {
				InputStreamReader inputreader = new InputStreamReader(instream);
				BufferedReader buffreader = new BufferedReader(inputreader);
				String line;
				// 分行读取
				while ((line = buffreader.readLine()) != null) {
					content += line + "\n";
				}
				instream.close();
			}
		} catch (java.io.FileNotFoundException e) {
			Log.d("TestFile", "The File doesn't not exist.");
		} catch (IOException e) {
			Log.d("TestFile", e.getMessage());
		}

		return content;
	}

	public static void setPopupWindowTouchModal(PopupWindow popupWindow,
			boolean touchModal) {
		if (null == popupWindow) {
			return;
		}
		Method method;
		try {
			method = PopupWindow.class.getDeclaredMethod("setTouchModal",
					boolean.class);
			method.setAccessible(true);
			method.invoke(popupWindow, touchModal);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static HashMap<String, String> generateTutorAdapterMap(
			String reserveTxt, String tutorName, String tutorCompany,
			String tutorCity, String tutorIndustry, String position,
			String imagehead) {
		HashMap<String, String> temp = new HashMap<String, String>();
		temp.put("reserveTxt", reserveTxt);
		temp.put("tutorName", tutorName);
		temp.put("tutorCompany", tutorCompany);
		temp.put("tutorCity", tutorCity);
		temp.put("tutorIndustry", tutorIndustry);
		temp.put("position", position);
		temp.put("imagehead", imagehead);
		return temp;
	}

	/**
	 * 
	 * 
	 * reserveTxt=, tutorName=, tutorCompany, tutorCity, tutorIndustry,
	 * position, reserve1, reserve2, reserve3
	 * 
	 * @return
	 */
	public static HashMap<String, String> generateMaxTutorAdapterMap(
			String imagehead, String reserveTxt, String tutorName,
			String tutorCompany, String tutorCity, String tutorIndustry,
			String position, String reserve1, String reserve2, String reserve3) {
		HashMap<String, String> temp = new HashMap<String, String>();
		temp.put("imagehead", imagehead);
		temp.put("reserveTxt", reserveTxt);
		temp.put("tutorName", tutorName);
		temp.put("tutorCompany", tutorCompany);
		temp.put("tutorCity", tutorCity);
		temp.put("tutorIndustry", tutorIndustry);
		temp.put("position", position);
		temp.put("reserve1", reserve1);
		temp.put("reserve2", reserve2);
		temp.put("reserve3", reserve3);
		return temp;
	}
	/**
	 * 
	 * 
	 * reserveTxt=, tutorName=, tutorCompany, tutorCity, tutorIndustry,
	 * position, reserve1, reserve2, reserve3
	 * 
	 * @return
	 */
	public static HashMap<String, String> generateMaxTutorAdapterMap(
			String imagehead, String reserveTxt, String tutorName,
			String tutorCompany, String tutorCity, String tutorIndustry,
			String position, String reserve1, String reserve2, String reserve3,String check) {
		HashMap<String, String> temp = new HashMap<String, String>();
		temp.put("imagehead", imagehead);
		temp.put("reserveTxt", reserveTxt);
		temp.put("tutorName", tutorName);
		temp.put("tutorCompany", tutorCompany);
		temp.put("tutorCity", tutorCity);
		temp.put("tutorIndustry", tutorIndustry);
		temp.put("position", position);
		temp.put("reserve1", reserve1);
		temp.put("reserve2", reserve2);
		temp.put("reserve3", reserve3);
		temp.put("check", check);
		return temp;
	}

	public static void AlertOkDialog(Context mContext, String message,
			OnClickListener rtListener) {
		new AlertDialog.Builder(mContext).setIcon(R.drawable.ic_launcher)
				.setTitle("一融赋").setMessage(message)
				.setPositiveButton("确定", rtListener).show();
	}

	public static long getlist(File f) {// 递归求取目录大小
		if (!f.exists()) {
			f.mkdirs();
		}
		long size = 0;
		File flist[] = f.listFiles();
		for (File flie : flist) {
			size += flie.length();
		}
		return size;
	}

	public static void dellist(File f) {// 递归求取目录大小
		File flist[] = f.listFiles();
		for (File flie : flist) {
			flie.delete();
		}
		f.deleteOnExit();
	}

	/**
	 * 验证6-16位 密码 字母和数字
	 * 
	 * @param str
	 * @param regex
	 *            ^[\u4e00-\u9fa5]+[a-zA-Z]+$
	 * @return
	 */
	public static boolean regexPwd(String str) {
		Pattern pattern = Pattern.compile("^.{6,20}$");
		Matcher isNum = pattern.matcher(str);
		return isNum.matches();
	}

	/**
	 * 验证4-16位 用户名 字母和中文
	 * 
	 * @param str
	 * @param regex
	 *            ^[]+[a-zA-Z]+$
	 * @return
	 */
	public static boolean regexName(String str) {
		Pattern pattern = Pattern.compile("[\u4e00-\u9fa5a-zA-Z]{2,24}");
		Matcher isNum = pattern.matcher(str);
		return isNum.matches();
	}

	/**
	 * 验证4-16位 账户 字母和数字 中文
	 * 
	 * @param str
	 * @param regex
	 *            ^[]+[a-zA-Z]+$
	 * @return
	 */
	public static boolean regexCode1(String str) {
		Pattern pattern = Pattern.compile("^[\u4e00-\u9fa5a-zA-Z0-9]{2,10}$"); // TODO
		Matcher isNum = pattern.matcher(str);
		return isNum.matches();
	}

	/**
	 * 验证4-16位 账户 不能为纯数字
	 * 
	 * @param str
	 * @param regex
	 *            ^[]+[a-zA-Z]+$
	 * @return
	 */
	public static boolean regexCode2(String str) {
		Pattern pattern = Pattern.compile("^[0-9]*$"); // TODO
		Matcher isNum = pattern.matcher(str);
		return isNum.matches();
	}

	/**
	 * mail 正则
	 * 
	 * @param str
	 * @return
	 */
	public static boolean regexMail(String str) {
		Pattern pattern = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}");
		Matcher isNum = pattern.matcher(str);
		return isNum.matches();
	}

	/**
	 * PHONE 正则
	 * 
	 * @param str
	 * @return
	 */
	public static boolean regexPhone(String str) {
		Pattern pattern = Pattern.compile("^((13|14|15|17|18)+)\\d{9}$");
		Matcher isNum = pattern.matcher(str);
		return isNum.matches();
	}

	public static Class<?> generateClass(Class<?> clazz) {
		// if (clazz == null) {
		// return null;
		// }
		if (clazz.getCanonicalName().endsWith("_")) {
			return clazz;
		}
		String name = clazz.getCanonicalName() + "_";
		try {
			Class<?> result = Class.forName(name);
			return result;
		} catch (ClassNotFoundException e) {
			new RuntimeException("Cannot find class for" + name, e);
		}
		return null;
	}

	/**
	 * 控制对象旋转
	 * 
	 * @param view
	 */
	public static void startLoading(Context mContext, View view) {
		view.setVisibility(View.VISIBLE);
		view.clearAnimation();
		Animation animation = AnimationUtils.loadAnimation(mContext,
				R.anim.rotating);
		view.startAnimation(animation);
	}

	/**
	 * 停止对象旋转
	 * 
	 * @param view
	 */
	public static void stopLoading(View view) {
		view.setVisibility(View.INVISIBLE);
		view.clearAnimation();
	}

	/**
	 * 停止对象旋转 不隐藏
	 * 
	 * @param view
	 */
	public static void stopLoading2(View view) {
		view.clearAnimation();
	}

//	private static void copy(InputStream in, OutputStream out)
//			throws IOException {
//		byte[] b = new byte[TclubConst.IO_BUFFER_SIZE];
//		int read;
//		while ((read = in.read(b)) != -1) {
//			out.write(b, 0, read);
//		}
//	}

	/**
	 * 得到本地或者网络上的bitmap url - 网络或者本地图片的绝对路径,比如:
	 * 
	 * A.网络路径: url="http://blog.foreverlove.us/girl2.png" ;
	 * 
	 * B.本地路径:url="file://mnt/sdcard/photo/image.png";
	 * 
	 * C.支持的图片格式 ,png, jpg,bmp,gif等等
	 * 
	 * @param url
	 * @return
	 */
//	public static Bitmap GetLocalOrNetBitmap(String url) {
//		Bitmap bitmap = null;
//		InputStream in = null;
//		BufferedOutputStream out = null;
//		try {
//			in = new BufferedInputStream(new URL(url).openStream(),
//					TclubConst.IO_BUFFER_SIZE);
//			final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
//			out = new BufferedOutputStream(dataStream,
//					TclubConst.IO_BUFFER_SIZE);
//			copy(in, out);
//			out.flush();
//			byte[] data = dataStream.toByteArray();
//			bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
//			data = null;
//			return bitmap;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	}

	public static TelephonyManager getPhoneInfo(Context mContext) {
		return (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);

	}

//	public static void dialogReLogin(Context mContext, String title) {
//		Intent intent = new Intent();
//		if (!nvl(title).equals("")) {
//			Bundle bundle = new Bundle();
//			bundle.putString("TITLE", title);
//			intent.putExtras(bundle);
//		}
//		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		intent.setClass(mContext, DialogActivity_.class);
//		mContext.startActivity(intent);
//
//	}

	private static long lastClickTime;

	public synchronized static boolean isFastClick() {
		long time = System.currentTimeMillis();
		if (time - lastClickTime < 500) {
			return true;
		}
		lastClickTime = time;
		return false;
	}

	static ProgressDialog proDialog;

	public static void openProgerss(final Context context, final String title,
			final String msg) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Looper.prepare();
				if (proDialog == null) {
					proDialog = ProgressDialog.show(context, title, msg, true,
							false);
				}
				Looper.loop();
			}
		}).start();
	}

	public static void closeDlg() {
		try {
			if (proDialog != null) {
				proDialog.dismiss();
				proDialog = null;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	/**
	 * 比较日期大小
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isDateBefore(String date1) {
		if (date1 == null || date1.equals("")) {
			return false;
		}

		if (date1.compareTo(getDateTimeHHMM()) < 0) {
			return false;
		} else {
			return true;
		}

	}

	/**
	 * 比较日期大小
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isDateBefore(String date1, String date2) {
		if (date1 == null || date1.equals("")) {
			return false;
		}

		if (date1.compareTo(date2) < 0) {
			return false;
		} else {
			return true;
		}

	}

//	/**
//	 * 把一个文件转化为字节
//	 * 
//	 * @param file
//	 * @return byte[]
//	 * @throws Exception
//	 */
//	public static String getPath2S(String path) throws Exception {
//		InputStream  in = new FileInputStream(path);
//		byte[] data = new byte[in.available()];
//		in.read(data);
//		in.close();
//		return ADIWebUtils.nvl(com.tsticn.club.pay.Base64.encode(data).getBytes("UTF-8"));
//	}
}
