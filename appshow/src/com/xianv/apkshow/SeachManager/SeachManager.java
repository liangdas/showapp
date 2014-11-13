package com.xianv.apkshow.SeachManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import android.content.Context;
import android.widget.Toast;
import com.apkplug.CloudService.ApkplugCloudAgent;
import com.apkplug.CloudService.SearchApp.AppSearchCallBack;
import com.apkplug.CloudService.bean.appSearchBean;
import com.apkplug.CloudService.model.AppQueryModel;
import com.apkplug.CloudService.model.appModel;
import com.xianv.apkshow.util.ACache;
/**
 * APKPLUG CloudAPI 数据查询封装器
 * 	@author 梁前武
 *	北京点豆科技
 *	www.apkplug.com
 */
public class SeachManager implements Comparator{
	public static final int up=0,down=1;
	private ArrayList<appModel> apps = null;
	private refreshListener listener=null;
	private appSearchBean bean=null;
	private Context mcontext=null;
	private ACache cache=null;
	private String keywords=null;
	private String cacheKey="ffssdf.%s.%s";
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public ArrayList<appModel> getApps() {
		return apps;
	}
	public SeachManager(Context mcontext,String keywords){
		this.mcontext=mcontext;
		this.keywords=keywords;
		cache=ACache.get(mcontext);
		bean=new appSearchBean();
    	bean.setPagenum(10);
    	bean.setPage(0);
    	bean.BindTime_DESC();
    	bean.setKeywords(keywords);
    	//查询是否有缓存好的数据
    	Object old=cache.getAsObject(String.format(cacheKey,"apps", keywords));
    	if(old!=null){
    		apps=(ArrayList<appModel>)old;
    		Collections.sort(apps, SeachManager.this);
    	}else{
    		apps=new ArrayList<appModel>();
    	}
	} 
	public void refresh(refreshListener listener){
		this.listener=listener;
		bean=new appSearchBean();
    	bean.setPagenum(10);
    	bean.setPage(0);
    	bean.BindTime_DESC();
    	bean.setKeywords(keywords);
		//查询
    	ApkplugCloudAgent.getInstance(null).getAppSearch().search(bean,new ImpAppSearchCallBack(up,true));
	}
	/**
	 * 下拉查询 ，即获取最新添加的数据
	 * @param listener  查询完成后回掉函数
	 */
	public void refreshTop(refreshListener listener){
		this.listener=listener;
		bean=new appSearchBean();
    	bean.setPagenum(10);
    	bean.setPage(0);
    	bean.BindTime_DESC();
    	bean.setKeywords(keywords);
		//获取列表中已有app最新绑定时间
		if(apps.size()>0){
			appModel app=apps.get(0);
			try {
				Date date =sdf.parse(app.getBindtime());
				//获取该日期以后的
				bean.AfterEndTime(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		//查询
    	ApkplugCloudAgent.getInstance(null).getAppSearch().search(bean,new ImpAppSearchCallBack(up,false));
	}
	/**
	 * 列表底部查询，获取稍早的插件数据
	 * @param listener
	 */
	public void refreshDown(refreshListener listener){
		this.listener=listener;
		bean=new appSearchBean();
    	bean.setPagenum(10);
    	bean.setPage(0);
    	bean.BindTime_DESC();
    	bean.setKeywords(keywords);
		//获取列表中已有app最早绑定时间
		if(apps.size()>0){
			appModel app=apps.get(apps.size()-1);
			try {
				Date date =sdf.parse(app.getBindtime());
				//获取该日期以前
				bean.BeforeStartTime(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		//查询
    	ApkplugCloudAgent.getInstance(null).getAppSearch().search(bean,new ImpAppSearchCallBack(down,false));
	}
	//查询回掉接口
	class ImpAppSearchCallBack implements AppSearchCallBack{
		private int type=0;
		private boolean isclear=false;
		public ImpAppSearchCallBack(int type,boolean isclear){
			this.type=type;
			this.isclear=isclear;
		}
		@Override
		public void onFailure(int arg0, String arg1) {
			if(listener!=null){
				//通知列表刷新显示
				listener.onFailure(type,arg0, arg1);
			}
		}
		@Override
		public void onSuccess(int arg0, AppQueryModel<appModel> arg1) {
			if(isclear){
				apps.clear();
			}
			for(int i=0;i<arg1.getData().size();i++){
				appModel app=arg1.getData().get(i);
				apps.add(app);
			}
			Collections.sort(apps, SeachManager.this);
			cache.put(String.format(cacheKey,"apps", keywords), apps);
			if(listener!=null){
				//通知列表刷新显示
				listener.onSuccess(type,arg1.getData().size(), apps);
			}
		}
	}
	@Override
	public int compare(Object arg0, Object arg1) {
		// 排序
		appModel m1=(appModel)arg0;
		appModel m2=(appModel)arg1;
		return -m1.getBindtime().compareTo(m2.getBindtime());
	}
	
}
