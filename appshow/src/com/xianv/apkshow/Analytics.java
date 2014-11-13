package com.xianv.apkshow;

import org.apkplug.Bundle.OSGIServiceAgent;
import org.apkplug.Bundle.Activity.BundleActivityLifecycleCallbacks;
import org.apkplug.Bundle.Activity.RegActivityLifecycleCallbacks;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import com.apkplug.AdsPlug.RegisterService.AdsPlugFilterActivityRegister;
import com.apkplug.Analytics.ApkplugAnalyticsAgent;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
/**
 * 自动统计所有Activity的状态
 * @author love
 *
 */
public class Analytics implements BundleActivityLifecycleCallbacks{
	private OSGIServiceAgent<RegActivityLifecycleCallbacks> ActivityLifecycleAgent=null;
	private BundleContext mcontext=null;
	public Analytics(BundleContext mcontext){
		this.mcontext=mcontext;
		ActivityLifecycleAgent=new OSGIServiceAgent<RegActivityLifecycleCallbacks>(mcontext,RegActivityLifecycleCallbacks.class); 
		try {
			ActivityLifecycleAgent.getService().registerActivityLifecycleCallbacks(this);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	@Override
	public void onActivityCreated(Activity arg0, Bundle arg1) {
		
	}

	@Override
	public void onActivityDestroyed(Activity arg0) {
		
	}

	@Override
	public void onActivityPaused(Activity arg0) {
		MobclickAgent.onPageEnd( arg0.getClass().getCanonicalName() );
		MobclickAgent.onPause(arg0);
	}

	@Override
	public void onActivityResumed(Activity arg0) {
		MobclickAgent.onPageStart( arg0.getClass().getCanonicalName() );
		MobclickAgent.onResume(arg0);
	}

	@Override
	public void onActivitySaveInstanceState(Activity arg0, Bundle arg1) {
		
	}

	@Override
	public void onActivityStarted(Activity arg0) {
		
	}

	@Override
	public void onActivityStopped(Activity arg0) {
		
	}

}
