package com.xianv.apkshow;
import org.apkplug.app.FrameworkFactory;
import org.apkplug.app.FrameworkInstance;
import org.osgi.framework.BundleContext;

import com.apkplug.AdsPlug.AdsCloudAgent;
import com.apkplug.AdsPlug.ApkplugAdsPlugAgent;
import com.apkplug.AppUpdate.AppUpdateAgent;
import com.apkplug.CloudService.ApkplugCloudAgent;
import com.apkplug.Feedback.ApkplugFeedbackAgent;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

public class ProxyApplication extends Application {
	private FrameworkInstance frame=null;
	private Analytics analytics=null;
	public FrameworkInstance getFrame() {
		return frame;
	}

	public void onCreate() {   
		 super.onCreate(); 
		 	try
	        {
				frame=FrameworkFactory.getInstance().start(null,this);
				Log.d("umeng", getDeviceInfo(this));
				BundleContext context =frame.getSystemBundleContext();
				AnalyticsConfig.setAppkey("5435f138fd98c5a6fb0309f2");
				MobclickAgent.updateOnlineConfig( context.getAndroidContext() );
				MobclickAgent.setDebugMode( false );
				UmengUpdateAgent.setAppkey("5435f138fd98c5a6fb0309f2");
				UmengUpdateAgent.setUpdateCheckConfig(false);
				//开始统计
				analytics=new Analytics(context);
				//友盟反馈
				AppUpdateAgent.getInstance(context);
				AppUpdateAgent.getInstance(context).setUpdateOnlyWifi(true);
				ApkplugFeedbackAgent.getInstance(context);
				//启动云服务包括插件搜索 下载 更新功能
				ApkplugCloudAgent.getInstance(context).init();
	        }
	        catch (Exception ex)
	        {
	            System.err.println("Could not create : " + ex);
	            ex.printStackTrace();
	            int nPid = android.os.Process.myPid();
				android.os.Process.killProcess(nPid);
	        }
	}

	public static String getDeviceInfo(Context context) {
	    try{
	      org.json.JSONObject json = new org.json.JSONObject();
	      android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
	          .getSystemService(Context.TELEPHONY_SERVICE);
	  
	      String device_id = tm.getDeviceId();
	      
	      android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);
	          
	      String mac = wifi.getConnectionInfo().getMacAddress();
	      json.put("mac", mac);
	      
	      if( TextUtils.isEmpty(device_id) ){
	        device_id = mac;
	      }
	      
	      if( TextUtils.isEmpty(device_id) ){
	        device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),android.provider.Settings.Secure.ANDROID_ID);
	      }
	      
	      json.put("device_id", device_id);
	      
	      return json.toString();
	    }catch(Exception e){
	      e.printStackTrace();
	    }
	  return null;
	}
                  
}
