package com.xianv.apkshow.Fragment;


import com.apkplug.Feedback.ApkplugFeedbackAgent;
import com.umeng.fb.FeedbackAgent;
import com.xianv.apkshow.R;
import com.xianv.apkshow.R.id;
import com.xianv.apkshow.R.layout;
import com.xianv.apkshow.activity.MainActivity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LeftSlidingMenuFragment extends Fragment implements OnClickListener{
	private View yixinBtnLayout;  //左侧菜单的易信功能
	private View circleBtnLayout; //左侧菜单的朋友圈功能
	private View settingBtnLayout; //左侧菜单的设置功能
	private TextView feedback=null;
	private FeedbackAgent agent=null;
     @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	ApkplugFeedbackAgent.getInstance(null).sync();
    	agent = new FeedbackAgent(this.getActivity());
    	agent.sync();
    }
     
     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	 View view = inflater.inflate(R.layout.main_left_fragment, container,
 				false);
    	  yixinBtnLayout = view.findViewById(R.id.yixinBtnLayout);
    	  yixinBtnLayout.setOnClickListener(this);
    	  circleBtnLayout = view.findViewById(R.id.circleBtnLayout);
    	  circleBtnLayout.setOnClickListener(this);
    	  settingBtnLayout = view.findViewById(R.id.settingBtnLayout);
    	  settingBtnLayout.setOnClickListener(this);
    	  feedback=(TextView) view.findViewById(R.id.feedback);
    	  feedback.setOnClickListener(this);
 		System.out.println();
    	return view;
    }

	@Override
	public void onClick(View v) {
		Fragment newContent = null;
		switch (v.getId()) {
		case R.id.yixinBtnLayout: //易信的点击事件
			//newContent = new Fragment_yixin();
			newContent = new Fragment_tuijie();
			yixinBtnLayout.setSelected(true);
			circleBtnLayout.setSelected(false);
			settingBtnLayout.setSelected(false);
			
			break;
		case R.id.circleBtnLayout: //朋友圈的点击事件
			newContent = new Fragment_ui();
			yixinBtnLayout.setSelected(false);
			circleBtnLayout.setSelected(true);
			settingBtnLayout.setSelected(false);
			break;
		case R.id.settingBtnLayout: //设置的点击事件
			newContent = new Fragment_demo();
			yixinBtnLayout.setSelected(false);
			circleBtnLayout.setSelected(false);
			settingBtnLayout.setSelected(true);
		    break;
		case R.id.feedback: //设置的点击事件
			ApkplugFeedbackAgent.getInstance(null).startFeedbackActivity();
			agent.startFeedbackActivity();
		    break;
		default:
			break;
		}
		
		if (newContent != null)
			switchFragment(newContent);
		
	}
	
	/*
	 * 切换到不同的功能内容
	 */
	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;	
			MainActivity ra = (MainActivity) getActivity();
			ra.switchContent(fragment);
		
	}
}
