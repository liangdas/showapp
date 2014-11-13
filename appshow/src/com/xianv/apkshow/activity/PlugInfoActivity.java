package com.xianv.apkshow.activity;

import java.io.IOException;
import java.util.Map;

import org.apkplug.app.FrameworkInstance;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

import com.apkplug.CloudService.ApkplugCloudAgent;
import com.apkplug.CloudService.AppPlugVar.AppPlugVarCallBack;
import com.apkplug.CloudService.download.AppDownload;
import com.apkplug.CloudService.download.AppDownloadCallBack;
import com.apkplug.CloudService.model.appModel;
import com.xianv.apkshow.ProxyApplication;
import com.xianv.apkshow.R;
import com.xianv.apkshow.view.ExpandableTextView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
public class PlugInfoActivity extends Activity{
	private FrameworkInstance frame=null;
	private TextView sourceurl=null;
	private ExpandableTextView appinfo=null;
	private Button plugBtn=null;
	private appModel appModel=null;
	private org.osgi.framework.Bundle plug=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pluginfo);
		appinfo=(ExpandableTextView) this.findViewById(R.id.appinfo);
		sourceurl=(TextView) this.findViewById(R.id.sourceurl);
		sourceurl.setAutoLinkMask(Linkify.ALL); 
		sourceurl.setMovementMethod(LinkMovementMethod.getInstance()); 
		frame=((ProxyApplication)this.getApplication()).getFrame();
		plugBtn=(Button) this.findViewById(R.id.plugBtn);
		plugBtn.setText("下 载");
		plugBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				if(plugBtn.getText().equals("下 载") ){
					if(appModel!=null){
						try {
							download(frame.getSystemBundleContext(),appModel);
						} catch (IOException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}
					}
				}else if(plugBtn.getText().equals("运 行") ){
					plug=gatBundle(frame.getSystemBundleContext(),appModel.getSymbolicName());
					if(plug!=null){
						if(plug.getBundleActivity()!=null){
							Intent i=new Intent();
							i.setClassName(PlugInfoActivity.this, plug.getBundleActivity().split(",")[0]);
							i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							PlugInfoActivity.this.startActivity(i);
						}else{
							try {
								plug.start();
							} catch (BundleException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			}
		});
		
		Bundle bundleFromMainActivity = this.getIntent().getExtras();  
        appModel = (appModel) bundleFromMainActivity.getSerializable("appModel");
        if(appModel!=null){
        	plug=gatBundle(frame.getSystemBundleContext(),appModel.getSymbolicName());
            if(plug!=null){
            	plugBtn.setText("运 行");
            }
	        ApkplugCloudAgent.getInstance(null).getAppPlugVar().queryGroupVar(appModel.getAppid(), new AppPlugVarCallBack(){
	
	    			@Override
	    			public void onSuccess(final Map var) {
	    				Log.d("AppPlug", "成功");
	    				PlugInfoActivity.this.runOnUiThread(new Runnable(){
	
							@Override
							public void run() {
								appinfo.setText("DEMO介绍",""+var.get("appinfo"));
			    				sourceurl.setText("源码下载地址:"+var.get("sourceurl"));
							}
	    					
	    				});
	    				
	    			}
	
	    			@Override
	    			public void onFailure(int statusCode, final String log) {
	    				Log.d("AppPlug", "查询失败:"+log);
	    				PlugInfoActivity.this.runOnUiThread(new Runnable(){
	
							@Override
							public void run() {
								appinfo.setText("查询失败",log);
							}
	    					
	    				});
	    				
	    			}			
	    		});
        }
	}
	public org.osgi.framework.Bundle gatBundle(BundleContext context,String SymbolicName) {
		// TODO Auto-generated method stub
    	 org.osgi.framework.Bundle[] bs=context.getBundles();
    	 for(int i=0;i<bs.length;i++)
 		{
 			if(bs[i].getSymbolicName().equals(SymbolicName)){
 				return bs[i];
 			}    	        
 		}
 		return null;
	}
	/**
	 * AppDownload 插件下载服务
	 * @param context
	 * @param ab
	 * @param viewHolder
	 * @throws IOException
	 */
    public void download(BundleContext context,final appModel ab) throws IOException{
    	AppDownload service=ApkplugCloudAgent.getInstance(null).getAppDownload();
    	if(service!=null){	
				  service.download(ab,this, 
						  new AppDownloadCallBack(){
						public void onDownLoadSuccess(final String info) {
							//插件apk文件下载成功
							
						}
						public void onFailure(int arg1, final String arg2) {
							//插件下载失败
							
						}
						public void onInstallSuccess(final int stutas,org.osgi.framework.Bundle arg0) {
							//插件安装成功
							plugBtn.post(new Runnable(){
				        		public void run(){
				        			plugBtn.setText("运 行") ;
				        		}
				        	});
						}
						public void onProgress(final int bytesWritten, final int totalSize,final String Speed) {
							//插件文件下载进度
							plugBtn.post(new Runnable(){
				        		public void run(){
				        			plugBtn.setText(String.format("当前下载进度:(%d%%)", (totalSize > 0) ? (int)(((float)bytesWritten / (float)totalSize) * 100) : -1)) ;
				        		}
				        	});
						}
					});
    	}
    }
}
