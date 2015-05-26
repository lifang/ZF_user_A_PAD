
package com.example.zf_pad;

 
 
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.baidu.frontia.FrontiaApplication;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.epalmpay.userPad.R;
import com.example.zf_pad.entity.User;
import com.example.zf_pad.entity.UserEntity;
import com.example.zf_pad.entity.MyShopCar.Good;
import com.example.zf_pad.trade.common.CommonUtil;
import com.example.zf_pad.trade.entity.City;
import com.example.zf_pad.trade.entity.Province;
import com.loopj.android.http.AsyncHttpClient;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
 
 
 

public class MyApplication extends Application{
	public TextView mLocationResult,logMsg;
	private static MyApplication  mInstance=null;
	public LocationClient mLocationClient;
	public GeofenceClient mGeofenceClient;
	public MyLocationListener mMyLocationListener;
	public Vibrator mVibrator;
	public Boolean isLogin=false;
	//private ArrayList<Order> orderList = new ArrayList<Order>();
	/**
	 * 验证信息token
	 */
	private static  String versionCode="";
	private static int notifyId=0;
	private static Boolean isSelect=false;
	private static int CITYID=1;
	private List<City> mCities = new ArrayList<City>();
	public static int getCITYID() {
		return CITYID;
	}
	public static void setCITYID(int cITYID) {
		CITYID = cITYID;
	}
	public static Boolean getIsSelect() {
		return isSelect;
	}
	public static void setIsSelect(Boolean isSelect) {
		MyApplication.isSelect = isSelect;
	}
	public static int getNotifyId() {
		return notifyId;
	}
	public static void setNotifyId(int notifyId) {
		MyApplication.notifyId = notifyId;
	}
	public static String getVersionCode() {
		return versionCode;
	}
	public static void setVersionCode(String versionCode) {
		MyApplication.versionCode = versionCode;
	}


	public static List<Good> comfirmList=new LinkedList<Good>();
	
	public static List<Good> getComfirmList() {
		return comfirmList;
	}
	public static void setComfirmList(List<Good> comfirmList) {
		MyApplication.comfirmList = comfirmList;
	}
	private static String token="";
	AsyncHttpClient client = new AsyncHttpClient(); //  
	
	public AsyncHttpClient getClient() {
		//client.setTimeout(6000);
		client.setTimeout(10000);// 设置超时时间
    	client.setMaxConnections(10);
		return client;
	}
	public void setClient(AsyncHttpClient client) {
		this.client = client;
	}
	public static String getToken() {
		return token;
	}
	public static void setToken(String token) {
		MyApplication.token = token;
	}
	

	/**
	 * 存储当前用户对象信息,需在welcome初始化用户信息
	 */

	//运用list来保存们每一个activity是关键  
    private List<Activity> mList = new LinkedList<Activity>();   
 // add Activity     
    public void addActivity(Activity activity) {    
        mList.add(activity);    
    }    
  //关闭每一个list内的activity    
    public void exit() {    
        try {    
            for (Activity activity:mList) {    
                if (activity != null)    
                    activity.finish();    
            }    
        } catch (Exception e) {    
            e.printStackTrace();    
        } 
        
    }  
 

	@Override
	public void onCreate() {
		super.onCreate();
		
		FrontiaApplication.initFrontiaApplication(getApplicationContext()); 
		
		mInstance = this;
		initImageLoader(mInstance);
		mLocationClient = new LocationClient(this.getApplicationContext());
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		mGeofenceClient = new GeofenceClient(getApplicationContext());
		mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
		mySharedPreferences = getSharedPreferences(Config.SHARED, MODE_PRIVATE);
		
		if(mySharedPreferences.getBoolean("islogin", false)){
			UserEntity ue=new UserEntity();
			ue.setId(mySharedPreferences.getInt("id", -1));
			this.NewUser=ue;
		}
		
//		initImageLoader(getApplicationContext());
//		SDKInitializer.initialize(this);
//		  PackageManager packageManager = getPackageManager();
//          // getPackageName()锟斤拷锟姐当前锟斤拷陌锟斤拷锟斤拷锟�0锟斤拷锟斤拷锟角伙拷取锟芥本锟斤拷息
//          PackageInfo packInfo;
//		try {
//			packInfo = packageManager.getPackageInfo(getPackageName(),0);
//			  int version = packInfo.versionCode;
//			 setVersionCode(version+"");
//		} catch (NameNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
//	private void initImageLoader(Context context) {
//		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
//				context).memoryCache(new LruMemoryCache(2 * 1024 * 1024))
//				.memoryCacheSize(2 * 1024 * 1024)
//				.discCacheSize(50 * 1024 * 1024).discCacheFileCount(100)
//				.build();
//		ImageLoader.getInstance().init(config);
//	}
	public static MyApplication getInstance() {
		return mInstance;
	}
	public static UserEntity NewUser = null;
	private SharedPreferences mySharedPreferences;
	
	
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			//Receive Location 
			StringBuffer sb = new StringBuffer(256);
//			sb.append("time : ");
//			sb.append(location.getTime());
//			sb.append("\nerror code : ");
//			sb.append(location.getLocType());
//			sb.append("\nlatitude : ");
//			sb.append(location.getLatitude());
//			sb.append("\nlontitude : ");
//			sb.append(location.getLongitude());
//			sb.append("\nradius : ");
//			sb.append(location.getRadius());
//			if (location.getLocType() == BDLocation.TypeGpsLocation){
//				sb.append("\nspeed : ");
//				sb.append(location.getSpeed());
//				sb.append("\nsatellite : ");
//				sb.append(location.getSatelliteNumber());
//				sb.append("\ndirection : ");
//				sb.append("\naddr : ");
//				sb.append(location.getAddrStr());
//				sb.append(location.getDirection());
//			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
//				sb.append("\naddr : ");
//				sb.append(location.getAddrStr());
//				//锟斤拷营锟斤拷锟斤拷息
//				sb.append("\noperationers : ");
//				sb.append(location.getOperators());
//			}
			sb.append(location.getAddrStr());
			if(location.getCity()!=null&&!location.getCity().equals("")){
			Config.CITY=location.getCity();
			logMsg(location.getCity());
	        List<Province> provinces = CommonUtil.readProvincesAndCities(getApplicationContext());
            for (Province province : provinces) {
                List<City> cities = province.getCities();
              
                mCities.addAll(cities);
                 
            }
			 for(City cc:mCities ){
				 if(location.getCity()!=null&&!location.getCity().equals("")){
					 if(cc.getName().endsWith(location.getCity())){
						 System.out.println("当前城市 ID----"+cc.getId());
						 setCITYID(cc.getId());
						 
					 }
				 } 
			 }
			 Config.isFRIST=true;
			Log.i("BaiduLocationApiDem", sb.toString());
		}
	}
	}
	/**
	 * 显示请求字符串
	 * @param str
	 */
	public void logMsg(String str) {
		try {
			if (mLocationResult != null)
				mLocationResult.setText(str);
			mLocationClient.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//隐藏软键盘
	public static void hideSoftKeyboard(Activity activity){
		View view = activity.getWindow().peekDecorView(); 
		if (view != null) {
			InputMethodManager inputmanger = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}
	@SuppressWarnings("deprecation")
	public static DisplayImageOptions getDisplayOption() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.moren) // ����ͼƬ�����ڼ���ʾ��ͼƬ
				.showImageForEmptyUri(R.drawable.moren) // ����ͼƬUriΪ�ջ��Ǵ����ʱ����ʾ��ͼƬ
				.showImageOnFail(R.drawable.moren) // ����ͼƬ���ػ��������з���������ʾ��ͼƬ
				.cacheInMemory(true) // �������ص�ͼƬ�Ƿ񻺴����ڴ���
				.cacheOnDisc(true) // �������ص�ͼƬ�Ƿ񻺴���SD����
				.build(); // �������ù���DisplayImageOption����
		return options;
	}
	public static void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		ImageLoader.getInstance().init(config);
	}
	private boolean hasOrderPaid;

	public boolean isHasOrderPaid() {
		return hasOrderPaid;
	}

	public void setHasOrderPaid(boolean hasOrderPaid) {
		this.hasOrderPaid = hasOrderPaid;
	}
	private ArrayList<Context> historyList = new ArrayList<Context>();

	public ArrayList<Context> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(ArrayList<Context> historyList) {
		this.historyList = historyList;
	}
	
	public void clearHistoryForPay(){
		try {
			for (Context activity : historyList) {
				if (activity != null)
					((Activity)activity).finish();
			}
			historyList.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

