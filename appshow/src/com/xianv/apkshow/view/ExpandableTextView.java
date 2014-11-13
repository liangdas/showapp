package com.xianv.apkshow.view;

import com.xianv.apkshow.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ExpandableTextView extends RelativeLayout implements OnClickListener{
	private Button bt_more;
	private FrameLayout fl_desc;
	private TextView tv_desc_short;
	private TextView tv_desc_long;
	private TextView tv_title;
	private boolean isInit = false;
	private boolean isShowShortText = true;
	private ImageView iv_more_line;
	private String content="",title="tilte";
	public ExpandableTextView(Context context) {
		super(context);
		LayoutInflater mInflater=LayoutInflater.from(context);
		mInflater.inflate(R.layout.expandoble_textview, this, true);
		findView();
		initView();
		setListener();
	}
	public ExpandableTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater mInflater=LayoutInflater.from(context);
		mInflater.inflate(R.layout.expandoble_textview, this, true);
		findView();
		initView();
		setListener();
	}

	private void setListener() {
		bt_more.setOnClickListener(this);
	}
	public void setText(String title,String content){
		this.title=title;
		this.content=content;
		initView();
	}
	private void initView() {
		tv_title.setText(title);
		tv_desc_short.setText(content);
		tv_desc_long.setText(content);
		isInit=false;
		ViewTreeObserver vto = fl_desc.getViewTreeObserver();
		vto.addOnPreDrawListener(new OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				if (isInit)
					return true;
				if (mesureDescription(tv_desc_short, tv_desc_long)) {
					iv_more_line.setVisibility(View.VISIBLE);
					bt_more.setVisibility(View.VISIBLE);
				}
				isInit = true;
				return true;
			}
		});
	}

	/**
	 * 计算描述信息是否过长
	 */
	private boolean mesureDescription(TextView shortView, TextView longView) {
		final int shortHeight = shortView.getHeight();
		final int longHeight = longView.getHeight();
		if (longHeight > shortHeight) {
			shortView.setVisibility(View.VISIBLE);
			longView.setVisibility(View.GONE);
			return true;
		}
		shortView.setVisibility(View.GONE);
		longView.setVisibility(View.VISIBLE);
		return false;
	}

	private void findView() {
		fl_desc = (FrameLayout) findViewById(R.id.fl_desc);
		tv_desc_short = (TextView) findViewById(R.id.tv_desc_short);
		tv_desc_long = (TextView) findViewById(R.id.tv_desc_long);
		tv_title = (TextView) findViewById(R.id.tv_title);
		bt_more = (Button) findViewById(R.id.bt_more);
		iv_more_line = (ImageView) findViewById(R.id.iv_more_line);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_more:
			if (isShowShortText) {

				tv_desc_short.setVisibility(View.GONE);
				tv_desc_long.setVisibility(View.VISIBLE);
			} else {
				tv_desc_short.setVisibility(View.VISIBLE);
				tv_desc_long.setVisibility(View.GONE);
			}
			toogleMoreButton(bt_more);
			isShowShortText = !isShowShortText;
			break;

		default:
			break;
		}
	}

	/**
	 * 更改按钮【更多】的文本
	 */
	private void toogleMoreButton(Button btn) {

		String text = (String) btn.getText();
		String moreText = "收起";
		String lessText = "更多";
		if (moreText.equals(text)) {
			btn.setText(lessText);
		} else {
			btn.setText(moreText);
		}
	}
}
