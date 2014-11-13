package com.xianv.apkshow.SeachManager;

import java.util.ArrayList;

import com.apkplug.CloudService.model.appModel;

public interface refreshListener {
	/**
	 * 查询插件列表成功的回调函数
	 * @param upordown    是上啦还是下拉
	 * @param refreshNum  本次查询到的item数目
	 * @param apps        完整的item数目
	 */
	public void onSuccess(int upordown,int refreshNum,ArrayList<appModel> apps);
	public void onFailure(int upordown,int arg0, final String arg1);
}
