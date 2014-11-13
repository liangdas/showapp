package com.xianv.apkshow.activity;
import org.apkplug.app.FrameworkInstance;
import com.apkplug.AdsPlug.ApkplugAdsPlugAgent;
import com.apkplug.AdsPlug.AdView.AdsPlugAdViewService;
import com.apkplug.AdsPlug.Listener.AdViewServiceListener;
import com.apkplug.AppUpdate.AppUpdateAgent;
import com.apkplug.Feedback.ApkplugFeedbackAgent;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;
import com.umeng.update.UmengUpdateAgent;
import com.xianv.apkshow.R;
import com.xianv.apkshow.Fragment.Fragment_tuijie;
import com.xianv.apkshow.Fragment.Fragment_t_game;
import com.xianv.apkshow.Fragment.LeftSlidingMenuFragment;
import com.xianv.apkshow.R.dimen;
import com.xianv.apkshow.R.drawable;
import com.xianv.apkshow.R.id;
import com.xianv.apkshow.R.layout;
import com.xianv.apkshow.SeachManager.SeachManager;
import com.xianv.apkshow.ProxyApplication;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends SlidingFragmentActivity implements OnClickListener{
	protected SlidingMenu mSlidingMenu;
	private ImageButton ivTitleBtnLeft;
	private Fragment mContent;
	private FrameworkInstance frame=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initSlidingMenu();
		setContentView(R.layout.activity_main);
		initView();
		//友盟版本更新功能检测
		AppUpdateAgent.getInstance(null).update(this);
		UmengUpdateAgent.setDefault();
		UmengUpdateAgent.update(this);
	
		frame=((ProxyApplication)this.getApplication()).getFrame();
		//AdsPlug广告聚合初始化
		ApkplugAdsPlugAgent.getInstance(frame.getSystemBundleContext()).InitAdsPlug(this);
		//设置为自定在Activity顶部和底部添加广告条
		ApkplugAdsPlugAgent.getInstance(frame.getSystemBundleContext()).setAdsShowAuto(ApkplugAdsPlugAgent.top_bottom_activity_auto_show);
		//设置以下Activity不显示广告条  
		ApkplugAdsPlugAgent.getInstance(frame.getSystemBundleContext()).addFilterActivity(PlugInfoActivity.class.getCanonicalName());
		ApkplugAdsPlugAgent.getInstance(frame.getSystemBundleContext()).addFilterActivity(MainActivity.class.getCanonicalName());
		ApkplugAdsPlugAgent.getInstance(frame.getSystemBundleContext()).addFilterActivity("com.umeng.fb.ContactActivity");
		ApkplugAdsPlugAgent.getInstance(frame.getSystemBundleContext()).addFilterActivity("com.umeng.fb.ConversationActivity");
		ApkplugAdsPlugAgent.getInstance(frame.getSystemBundleContext()).addFilterActivity("com.umeng.update.UpdateDialogActivity");
		/*ApkplugAdsPlugAgent.getInstance(frame.getSystemBundleContext()).getAdViewService(
				new AdViewServiceListener(){
					@Override
					public void AdViewService(AdsPlugAdViewService arg0) {
						View v=arg0.getAdsView(MainActivity.this, 0);
						FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT,
								FrameLayout.LayoutParams.WRAP_CONTENT);
						// 设置广告条的悬浮位置
						layoutParams.gravity = Gravity.BOTTOM | Gravity.RIGHT; // 这里示例为右下角
						// 实例化广告条
						// 调用Activity的addContentView函数
						MainActivity.this.addContentView(v, layoutParams);
					}
		});*/
	}
	
	
	private void initView() {
		ivTitleBtnLeft = (ImageButton)this.findViewById(R.id.ivTitleBtnLeft);
		ivTitleBtnLeft.setOnClickListener(this);
	}

	//初始化左侧菜单
	private void initSlidingMenu() {
		mContent = new Fragment_tuijie();
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, mContent)
		.commit();
		
		
		setBehindContentView(R.layout.main_left_layout);//设置左边的菜单布局
		
		FragmentTransaction mFragementTransaction = getSupportFragmentManager()
				.beginTransaction();
		Fragment mFrag = new LeftSlidingMenuFragment();
		mFragementTransaction.replace(R.id.main_left_fragment, mFrag);
		mFragementTransaction.commit();
		
//		mSlidingMenu = getSlidingMenu();
//		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
//		mSlidingMenu.setShadowWidthRes(R.dimen.shadow_width);
//		mSlidingMenu.setShadowDrawable(R.drawable.shadow);
//		mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
//		mSlidingMenu.setFadeDegree(0.7f);
//		mSlidingMenu.setBehindWidth(250);
//		mSlidingMenu.setBehindScrollScale(0.0f);
////		mSlidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
//		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		
		mSlidingMenu = getSlidingMenu();
		mSlidingMenu.setMode(SlidingMenu.LEFT);// 设置是左滑还是右滑，还是左右都可以滑，我这里只做了左滑
		mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);// 设置菜单宽度
		mSlidingMenu.setFadeDegree(0.35f);// 设置淡入淡出的比例
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);//设置手势模式
		mSlidingMenu.setShadowDrawable(R.drawable.shadow);// 设置左菜单阴影图片
		mSlidingMenu.setFadeEnabled(true);// 设置滑动时菜单的是否淡入淡出
		mSlidingMenu.setBehindScrollScale(0.0f);// 设置滑动时拖拽效果
		mSlidingMenu.setBehindWidth(360);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ivTitleBtnLeft:
			//点击标题左边按钮弹出左侧菜单
			mSlidingMenu.showMenu(true);
			break;
		default:
			break;
		}
		
	}
   
	/**
	 *    左侧菜单点击切换首页的内容
	 */
	public void switchContent(Fragment fragment) {
		mContent = fragment;
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, fragment)
		.commit();
		getSlidingMenu().showContent();
		
	}
	
	 public boolean dispatchKeyEvent(KeyEvent event) {
			if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
					&& event.getAction() != KeyEvent.ACTION_UP) {
				AlertDialog.Builder alertbBuilder = new AlertDialog.Builder(this);
				alertbBuilder
						.setTitle("真的要退出？")
						.setMessage("你确定要退出？")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										int nPid = android.os.Process.myPid();
										android.os.Process.killProcess(nPid);
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.cancel();
									}
								}).create();
				alertbBuilder.show();
				return true;
			} else {
				return super.dispatchKeyEvent(event);
			}
		} 
	
}
