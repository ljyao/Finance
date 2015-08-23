package org.weixvn.finance;
import org.apache.http.Header;
import org.jsoup.nodes.Document;
import org.weixvn.finance.webpages.loginwebpage;
import org.weixvn.http.AsyncWaeHttpClient;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.mobstat.StatService;

public class LoginActivity extends Activity {

	public static AsyncWaeHttpClient httpClient ;
	static String YES = "yes";
	static String NO = "no";
	public Button cw_login_btn, back;
	private EditText ed_userName;
	private EditText ed_pwd;
	public  String name, password;
	// isMemory变量用来判断SharedPreferences有没有数据，包括上面的YES和NO
	private String isMemory = "";
	// 用于保存SharedPreferences的文件
	private String FILE = "saveUserNamePwd";
	// 声明一个SharedPreferences
	private SharedPreferences sp = null;
	Common common;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
	
		common = new Common();
		back = (Button) findViewById(R.id.backmain);
		cw_login_btn = (Button) findViewById(R.id.cw_login);
		ed_userName = (EditText) findViewById(R.id.cw_userid);
		ed_pwd = (EditText) findViewById(R.id.cw_password);
		sp = getSharedPreferences(FILE, MODE_PRIVATE);
		isMemory = sp.getString("isMemory", NO);
		
		// 保存账号密码
		if (isMemory.equals(YES)) {

			name = sp.getString("name", "");
			ed_userName.setText(name);
			password = sp.getString("password", "");
			ed_pwd.setText(password);
			load();
		}


		cw_login_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 得到用户名与密码
				name = ed_userName.getText().toString();
				password = ed_pwd.getText().toString();
				// 判断用户名以及密码
				if (name.length() == 0) {
					Toast.makeText(LoginActivity.this,
							getResources().getString(R.string.input_user),
							Toast.LENGTH_SHORT).show();
					return;
				} else if (password.length() == 0) {
					Toast.makeText(LoginActivity.this,
							getResources().getString(R.string.input_pwd),
							Toast.LENGTH_SHORT).show();
					return;
				}
				load();

			}
		});
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(android.R.anim.slide_in_left,
						android.R.anim.slide_out_right);
			}
		});
	}
	
	/**
	 * 发送登陆请求
	 */
	private void load() {
		// 检查连网
		if (Utils.checkNetwork(this) == false) {
			Toast.makeText(this,
					getResources().getString(R.string.network_error),
					Toast.LENGTH_SHORT).show();
		} else {
			httpClient=new AsyncWaeHttpClient();
			common.showProgressDialog(LoginActivity.this);
			//代理
			httpClient.setProxy("liconglei.oicp.net", 11265);
			httpClient.putCache("username", name);
			httpClient.putCache("password", password);
			httpClient.setTimeout(2000);
			httpClient.execute(new loginwebpage()
			{

				@Override
				public void onSuccess(int statusCode, Header[] headers,
						Document doc) {
					boolean flag=analyse(doc);
					if (flag) {
						Toast.makeText(LoginActivity.this,
								getResources().getString(R.string.login_success),
								Toast.LENGTH_SHORT).show();
						remenber();
						common.cancelProgressDialog();
						Intent intent = new Intent();
						intent.setClass(LoginActivity.this, MainActivity.class);
						LoginActivity.this.startActivity(intent);
						finish();
					} else {
						common.cancelProgressDialog();
						Toast.makeText(LoginActivity.this,
								getResources().getString(R.string.login_error),
								Toast.LENGTH_SHORT).show();
					}
					super.onSuccess(statusCode, headers, doc);
				}
				@Override
				public void onFailure(int statusCode, Header[] headers,
						Document doc, Throwable throwable) {
					common.cancelProgressDialog();
					Toast.makeText(LoginActivity.this,
							getResources().getString(R.string.login_system_error),
							Toast.LENGTH_SHORT).show();
					super.onFailure(statusCode, headers, doc, throwable);
				}
				
			});
	}
	}

	/**
	 * 保存账号密码
	 */
	public void remenber() {
		if (sp == null) {
			sp = getSharedPreferences(FILE, MODE_PRIVATE);
		}
		Editor edit = sp.edit();
		edit.putString("name", ed_userName.getText().toString());
		edit.putString("password", ed_pwd.getText().toString());
		edit.putString("isMemory", YES);
		edit.commit();
	}

	public void onResume() {
		super.onResume();
		/**
		 * 页面起始（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
		 * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
		 */
		// 自定义事件
		StatService.onEvent(LoginActivity.this, "1", "登录", 1);
		// 自定义事件开始
		StatService.onEventStart(LoginActivity.this, "1", "登录");
		StatService.onResume(this);
	}

	public void onPause() {
		super.onPause();
		/**
		 * 页面结束（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
		 * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
		 */
		// 自定义事件结束
		StatService.onEventEnd(LoginActivity.this, "1", "登录");
		StatService.onPause(this);
	}

}
