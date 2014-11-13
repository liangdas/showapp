package com.apkplug.bundle.manager.adapter;

import java.io.IOException;
import java.util.List;

import org.apkplug.Bundle.installCallback;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

import com.apkplug.CloudService.ApkplugCloudAgent;
import com.apkplug.CloudService.download.AppDownload;
import com.apkplug.CloudService.download.AppDownloadCallBack;
import com.apkplug.CloudService.model.appModel;
import com.apkplug.bundle.manager.adapter.base.LListAdapter;


import com.squareup.picasso.Picasso;
import com.xianv.apkshow.R;
import com.xianv.apkshow.activity.PlugInfoActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SearchBundleAdapter extends LListAdapter<appModel>{
	private BundleContext context=null;
	public SearchBundleAdapter(Context c, BundleContext context,List<appModel> data) {
		super(c, data);
		this.context=context;
	}
	
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	
	public appModel getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	
	public View getView(int position, View convertView, ViewGroup arg2) {
		final ListViewHolder viewHolder;
		final appModel ab=list.get(position);
		if(convertView == null) {
			convertView = mInflater.inflate(R.layout.index_item_listview, null);
			viewHolder = new ListViewHolder();
			viewHolder.imageViewIcon = (ImageView)convertView.findViewById(R.id.image_item_1);
			viewHolder.appName = (TextView)convertView.findViewById(R.id.text_item_1);
			viewHolder.appSize = (TextView)convertView.findViewById(R.id.text_item_2);
			viewHolder.appinfo = (TextView)convertView.findViewById(R.id.text_item_3);
			viewHolder.dsize = (TextView)convertView.findViewById(R.id.text_item_4);
			viewHolder.imgSplit = (ImageView)convertView.findViewById(R.id.image_item_2);
			viewHolder.details = (Button)convertView.findViewById(R.id.details);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ListViewHolder)convertView.getTag();
		}
		Picasso.with(this.mContext) 
        .load(ab.getIconurl()) 
        .placeholder(R.drawable.ic_launcher) 
        .error(R.drawable.ic_launcher) 
        .fit() 
        .into(viewHolder.imageViewIcon);
		viewHolder.appName.setText(ab.getAppname());
		viewHolder.dsize.setText(String.format("已下载%d次",ab.getD_num()));
		viewHolder.appinfo.setText(ab.getInfo());
		viewHolder.appSize.setText(String.format("%2.2fM", (float)ab.getSize()/(1024*1024)));
		//查询一下是否已经安装过该插件
		org.osgi.framework.Bundle b=gatHadBundle(context,ab);
    	if(b==null){
    		//没有安装过该插件
    		//viewHolder.download.setText("下  载") ;
    	}else{
    		//已经安装过该插件
    		if(ab.getBundlevarsion().equals(b.getVersion())){
    			//插件存在且版本相同
    			//viewHolder.download.setText("运 行") ;
    		}else{
    			//viewHolder.download.setText("更 新") ;
    		}
    	}	
		viewHolder.details.setOnClickListener(
				new OnClickListener(){
					public void onClick(View v) {
						Intent intent = new Intent();  
			            intent.setClass(mContext, PlugInfoActivity.class);  
			            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			            //create a bundle, then put it into intent  
			            Bundle bundle = new Bundle();  
			            bundle.putSerializable("appModel", ab);
			            intent.putExtras(bundle);  
			              
			            //use intent to define the page direction  
			            mContext.startActivity(intent);  
					}
			});
		convertView.setOnLongClickListener(
				new OnLongClickListener(){

					@Override
					public boolean onLongClick(View v) {
						AlertDialog.Builder alertbBuilder = new AlertDialog.Builder(mContext);
						alertbBuilder
								.setMessage("")
								.setNegativeButton("卸载",
										new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog,
													int which) {
														//直接使用 Bundle.uninstall()卸载
												org.osgi.framework.Bundle b= gatHadBundle(context,ab);
												if(b!=null)	{	
													try {
															b.uninstall();
													} catch (Exception e) {
															// TODO Auto-generated catch block
															e.printStackTrace();
													}
												}
												dialog.cancel();
											}
										}).create();
						alertbBuilder.show();
						return false;
					}
			
		});
			return convertView;
		}
	/**
	 * AppDownload 插件下载服务
	 * @param context
	 * @param ab
	 * @param viewHolder
	 * @throws IOException
	 */
    public void download(BundleContext context,final appModel ab,final ListViewHolder viewHolder) throws IOException{
    	AppDownload service=ApkplugCloudAgent.getInstance(null).getAppDownload();
    	if(service!=null){	
				  service.download(ab,mContext, 
						  new AppDownloadCallBack(){
						public void onDownLoadSuccess(final String info) {
							//插件apk文件下载成功
							viewHolder.appinfo.post(new Runnable(){
					    		public void run(){
					    			viewHolder.appinfo.setText(info) ;
					    		}
					    	});
						}
						public void onFailure(int arg1, final String arg2) {
							//插件下载失败
							viewHolder.appinfo.post(new Runnable(){
				        		public void run(){
				        			viewHolder.appinfo.setText(arg2) ;
				        		}
				        	});
						}
						public void onInstallSuccess(final int stutas,org.osgi.framework.Bundle arg0) {
							//插件安装成功
							viewHolder.appinfo.post(new Runnable(){
				        		public void run(){
				        			viewHolder.download.setText("运 行") ;
				        			viewHolder.appinfo.setText(stutasToStr(stutas)) ;
				        		}
				        	});
						}
						public void onProgress(final int bytesWritten, final int totalSize,final String Speed) {
							//插件文件下载进度
							viewHolder.appinfo.post(new Runnable(){
				        		public void run(){
				        			viewHolder.appinfo.setText(String.format("(%d%%) speed %s", (totalSize > 0) ? (int)(((float)bytesWritten / (float)totalSize) * 100) : -1,Speed)) ;
				        		}
				        	});
						}
					});
    	}
    }
	/**
	 * appBean比较查询已安装插件
	 * @param context
	 * @param ab
	 * @return
	 */
	public org.osgi.framework.Bundle gatHadBundle(BundleContext context,appModel ab) {
		// TODO Auto-generated method stub
    	 org.osgi.framework.Bundle[] bs=context.getBundles();
    	 for(int i=0;i<bs.length;i++)
 		{
 			if(bs[i].getSymbolicName().equals(ab.getSymbolicName())){
 				return bs[i];
 			}    	        
 		}
 		return null;
	}
	private String stutasToStr(int stutas){
		if(stutas==installCallback.stutas){
			return "缺少SymbolicName";
		}else if(stutas==installCallback.stutas1){
			return "已是最新版本";
		}else if(stutas==installCallback.stutas2){
			return "版本号不正确";
		}else if(stutas==installCallback.stutas3){
			return " 版本相等";
		}else if(stutas==installCallback.stutas4){
			return "无法获取正确的证书";
		}else if(stutas==installCallback.stutas5){
			return "更新成功";
		}else if(stutas==installCallback.stutas6){
			return "证书不一致";
		}else if(stutas==installCallback.stutas7){
			return "安装成功";
		}
		return "状态信息不正确";
	}
	private final class ListViewHolder {
    	public ImageView imageViewIcon;
    	public TextView appName;
    	public TextView dsize;
    	public TextView appinfo;
    	public TextView appSize;
    	public TextView download;
    	public ImageView imgSplit;
    	public Button details;
    	public TextView appPriceFlag;
    }
}
