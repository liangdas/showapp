package com.xianv.apkshow.Fragment;
import java.util.ArrayList;
import com.xianv.apkshow.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 *  推荐Fragment
 * 	@author 梁前武
 *	北京点豆科技
 *	www.apkplug.com
 */
public class Fragment_tuijie extends Fragment{
	private ViewPager m_vp;
	private Fragment_t_ui f_friend;
	private Fragment_t_demo f_setting;
	private Fragment_t_game f_yixin;
	//页面列表
	private ArrayList<Fragment> fragmentList;
	//标题列表
	ArrayList<String>   titleList    = new ArrayList<String>();
	//通过pagerTabStrip可以设置标题的属性
	private PagerTabStrip pagerTabStrip;

	private PagerTitleStrip pagerTitleStrip;
	View mMainView=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.v("huahua", "fragment1-->onCreate()");
		
		LayoutInflater inflater = getActivity().getLayoutInflater();
		mMainView = inflater.inflate(R.layout.activity_main1, null, false);
		
		m_vp = (ViewPager)mMainView.findViewById(R.id.viewpager);
		
		pagerTabStrip=(PagerTabStrip) mMainView.findViewById(R.id.pagertab);
		//设置下划线的颜色
		pagerTabStrip.setTabIndicatorColor(getResources().getColor(android.R.color.holo_green_dark)); 
		//设置背景的颜色
		pagerTabStrip.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));
		
//		pagerTitleStrip=(PagerTitleStrip) findViewById(R.id.pagertab);
//		//设置背景的颜色
//		pagerTitleStrip.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));
		
		f_friend = new Fragment_t_ui();
		f_setting = new Fragment_t_demo();
		f_yixin = new Fragment_t_game();

		fragmentList = new ArrayList<Fragment>();
		fragmentList.add(f_friend);
		fragmentList.add(f_setting);
		fragmentList.add(f_yixin);
		
	    titleList.add("推荐开源组件");
	    titleList.add("推荐DEMO应用");
	    titleList.add("推荐DEMO游戏");
		
		m_vp.setAdapter(new MyViewPagerAdapter(this.getChildFragmentManager()));
//		m_vp.setOffscreenPageLimit(2);
	}
	
	public class MyViewPagerAdapter extends FragmentPagerAdapter{
		public MyViewPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public Fragment getItem(int arg0) {
			return fragmentList.get(arg0);
		}

		@Override
		public int getCount() {
			return fragmentList.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			return titleList.get(position);
		}

		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.v("huahua", "fragment1-->onCreateView()");
		
		
		return mMainView;
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.v("huahua", "fragment1-->onDestroy()");
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.v("huahua", "fragment1-->onPause()");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.v("huahua", "fragment1-->onResume()");
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.v("huahua", "fragment1-->onStart()");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.v("huahua", "fragment1-->onStop()");
	}

}
