package com.xianv.apkshow.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apkplug.app.FrameworkInstance;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.SynchronousBundleListener;

import zrc.widget.SimpleFooter;
import zrc.widget.SimpleHeader;
import zrc.widget.ZrcListView;
import zrc.widget.ZrcListView.OnScrollStateListener;
import zrc.widget.ZrcListView.OnStartListener;

import com.apkplug.CloudService.model.appModel;
import com.apkplug.bundle.manager.adapter.SearchBundleAdapter;
import com.xianv.apkshow.BundleStutes;
import com.xianv.apkshow.ProxyApplication;
import com.xianv.apkshow.R;
import com.xianv.apkshow.SeachManager.SeachManager;
import com.xianv.apkshow.SeachManager.refreshListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
@SuppressLint("ValidFragment")
public class BaseFragment extends Fragment implements refreshListener, OnScrollStateListener{
	private ZrcListView listView;
	private FrameworkInstance frame=null;
	private SearchBundleAdapter adapter;
	private List<BundleStutes> bundles=null;
	private SeachManager seach=null;
	private Context mContext=null;
	private View rootview=null;
	@SuppressLint("ValidFragment")
	private String keywords=null;
	public BaseFragment(String keywords){
		super();
		this.keywords=keywords;
	}
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		seach=new SeachManager(this.getActivity(),keywords);
		
		frame=((ProxyApplication)this.getActivity().getApplication()).getFrame();
		adapter = new SearchBundleAdapter(mContext,frame.getSystemBundleContext(),seach.getApps());
		frame.getSystemBundleContext()
		.addBundleListener(
				new SynchronousBundleListener(){

					public void bundleChanged(BundleEvent event) {
						adapter.notifyDataSetChanged();
					}
				
		});
	}
     public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
    	rootview = inflater.inflate(R.layout.fragment_setting, container, false);
     	listView=(ZrcListView) rootview.findViewById(R.id.zListView);
        listView.setAdapter(adapter);
        // 设置默认偏移量，主要用于实现透明标题栏功能。（可选）
        //float density = getResources().getDisplayMetrics().density;
        //listView.setFirstTopOffset((int) (50 * density));

        // 设置下拉刷新的样式（可选，但如果没有Header则无法下拉刷新）
        SimpleHeader header = new SimpleHeader(this.getActivity());
        header.setTextColor(0xff0066aa);
        header.setCircleColor(0xff33bbee);
        listView.setHeadable(header);

        // 设置加载更多的样式（可选）
        SimpleFooter footer = new SimpleFooter(this.getActivity());
        footer.setCircleColor(0xff33bbee);
        listView.setFootable(footer);
        // 设置列表项出现动画（可选）
        listView.setItemAnimForTopIn(R.anim.topitem_in);
        listView.setItemAnimForBottomIn(R.anim.bottomitem_in);

        // 下拉刷新事件回调（可选）
        listView.setOnRefreshStartListener(new OnStartListener() {
            @Override
            public void onStart() {
            	seach.refreshTop(BaseFragment.this);
            }
        });

        // 加载更多事件回调（可选）
        listView.setOnLoadMoreStartListener(new OnStartListener() {
            @Override
            public void onStart() {
            	
            	seach.refreshDown(BaseFragment.this);
            }
        });
        listView.setOnScrollStateListener(this);
        if(seach.getApps().size()==0){
        	listView.refresh(); // 主动下拉刷新
        }else{
        	seach.refresh(this);
        }
    	return rootview;
    }
		/**
		 * 查询回调接口
		 * @author 梁前武
		 *
		 */
		   public void onFailure(final int upordown,int arg0, final String arg1) {
			   //服务查询失败
			   if(BaseFragment.this.getActivity()!=null){
				   BaseFragment.this.getActivity().runOnUiThread(new Runnable(){
		 					public void run() {
		 						if(upordown==SeachManager.down){
		 							if(listView!=null){
		 								listView.stopLoadMore();
		 							}
						 		}else{
						 			if(listView!=null){
						 				listView.setRefreshFail("加载失败");
						 			}
						 		}
		 						
		 						//listView.stopLoadMore();
		 						Toast.makeText(BaseFragment.this.getActivity(), arg1,
		 					     Toast.LENGTH_SHORT).show();
		 					}
		 			});	
			   }else{
				   Toast.makeText(BaseFragment.this.mContext, arg1,
	 					     Toast.LENGTH_SHORT).show();
			   }
		   }

			@Override
			public void onSuccess(final int upordown,final int refreshNum, ArrayList<appModel> apps) {
				//更新列表
				if(BaseFragment.this.getActivity()!=null){
					BaseFragment.this.getActivity().runOnUiThread(new Runnable(){
					 		public void run(){
					 		
				            //listView.startLoadMore(); // 开启LoadingMore功能
					 		if(upordown==SeachManager.down){
					 			if(listView!=null){
						 			listView.setLoadMoreSuccess();
						 			listView.stopLoadMore(); 
					 			}
					 			Toast.makeText(BaseFragment.this.getActivity(), String.format("成功加载 %d 条数据", refreshNum),
				 					     Toast.LENGTH_SHORT).show();
					 		}else{
					 			if(listView!=null){
					 				listView.setRefreshSuccess(String.format("成功加载 %d 条数据", refreshNum)); // 通知加载成功
					 			}
					 		}
							 adapter.notifyDataSetChanged();
					 		} 
					});
				}
			}
		   /**
			 * 通过appid匹配已安装插件
			 * @param appid
			 * @return
			 */
			public BundleStutes getBundleByAppid(String appid){
				for(int i=0;i<bundles.size();i++){
					if(bundles.get(i).appid.equals(appid)){
						//查找到
						return bundles.get(i);
					}
				}
				return null;
			}
		
		@Override
		public void onChange(int state) {
			if(OnScrollStateListener.DOWN==state){
	            listView.startLoadMore(); 
			}
		}  
}
