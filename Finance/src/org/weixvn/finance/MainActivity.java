package org.weixvn.finance;

import org.apache.http.Header;
import org.jsoup.nodes.Document;
import org.weixvn.finance.webpages.GetUserName;
import org.weixvn.finance.webpages.QueueStausMessage;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.mobstat.StatService;

public class MainActivity extends Activity {

	public boolean loadsuccess = false;
	public static MainActivity main = null;
	public static Context context;
	private String[] array = new String[9];
	private Button btn_set;
	private Button btn_tomain;
	private TextView btn_today, btn_tomorrow, view[] = new TextView[9],
			idTextView;
	Common common;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		StatService.setSessionTimeOut(10);
		StatService.setAppKey("34ac64f942");
		context = this;
		main = this;
		btn_today = (TextView) findViewById(R.id.gettoday);
		btn_tomorrow = (TextView) findViewById(R.id.gettomorrow);
		btn_set = (Button) findViewById(R.id.set);
		idTextView = (TextView) findViewById(R.id.user);
		btn_tomain = (Button) findViewById(R.id.back);

		btn_today.setOnClickListener(new blistener());
		btn_tomorrow.setOnClickListener(new blistener());
		btn_set.setOnClickListener(new blistener());
		btn_tomain.setOnClickListener(new blistener());

		common = new Common();
		GetorUpMessage();
	}

	/**
	 * 更新信息
	 */
	private void GetorUpMessage() {
		common.showProgressDialog(this);
		LoginActivity.httpClient.execute(new QueueStausMessage() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, Document doc) {
				array = analyze(doc);
				showview();
				super.onSuccess(statusCode, headers, doc);
			}

		});
		LoginActivity.httpClient.execute(new GetUserName() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, Document doc) {
				String array = analyse(doc);
				idTextView.setText(array);
				super.onSuccess(statusCode, headers, doc);
			}

		});
	}

	/**
	 * 主界面显示的信息
	 */
	private void showview() {

		view[0] = (TextView) findViewById(R.id.content1);
		view[1] = (TextView) findViewById(R.id.content2);
		view[2] = (TextView) findViewById(R.id.content3);
		view[3] = (TextView) findViewById(R.id.content4);
		view[4] = (TextView) findViewById(R.id.content5);
		view[5] = (TextView) findViewById(R.id.content6);
		for (int i = 0; i < 6; i++) {
			view[i].setText(array[i]);
		}
		common.cancelProgressDialog();
	}

	public class blistener implements OnClickListener {
		@Override
		public void onClick(View v) {

			Intent intent = new Intent();

			switch (v.getId()) {
			case R.id.gettoday:
				intent.setClass(MainActivity.this, ServiceTypeActivity.class);
				LoginActivity.httpClient.putCache("timeType", true);
				MainActivity.this.startActivity(intent);
				break;
			case R.id.gettomorrow:
				intent.setClass(MainActivity.this, ServiceTypeActivity.class);
				LoginActivity.httpClient.putCache("timeType", false);
				MainActivity.this.startActivity(intent);
				break;
			case R.id.set:
				intent.setClass(MainActivity.this, SetActivity.class);
				MainActivity.this.startActivity(intent);
				break;
			case R.id.back:
				MainActivity.this.finish();
				break;
			}
		}
	}

	public void onResume() {
		super.onResume();
		/**
		 * 页面起始（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
		 * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
		 */
		// 自定义事件
		StatService.onEvent(MainActivity.this, "ID", "Name", 1);
		// 自定义事件开始
		StatService.onEventStart(MainActivity.this, "ID", "Name");
		StatService.onResume(this);
	}

	public void onPause() {
		super.onPause();
		/**
		 * 页面结束（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
		 * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
		 */
		// 自定义事件结束
		StatService.onEventEnd(MainActivity.this, "ID", "Name");
		StatService.onPause(this);
	}

}
