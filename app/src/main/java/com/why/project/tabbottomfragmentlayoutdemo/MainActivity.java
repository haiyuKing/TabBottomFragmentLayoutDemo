package com.why.project.tabbottomfragmentlayoutdemo;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.why.project.tabbottomfragmentlayoutdemo.fragment.HomeFragment;
import com.why.project.tabbottomfragmentlayoutdemo.fragment.MessageFragment;
import com.why.project.tabbottomfragmentlayoutdemo.fragment.ContactFragment;
import com.why.project.tabbottomfragmentlayoutdemo.views.tab.TabBottomFragmentLayout;

public class MainActivity extends FragmentActivity {

	private static final String TAG = "MainActivity";

	//自定义底部选项卡
	private TabBottomFragmentLayout mBottomTabLayout;

	private FragmentManager fragmentManager;//碎片管理器

	/**碎片声明*/
	private HomeFragment homeFragment;//首页
	private MessageFragment messageFragment;//消息
	private ContactFragment contactFragment;//我的

	/**首页fragment索引值--需要和TabBottomLayout中的数组的下标值对应*/
	public static final int Home_Fragment_Index = 0;
	/**消息fragment索引值*/
	public static final int Message_Fragment_Index = 1;
	/**我的fragment索引值*/
	public static final int Contact_Fragment_Index = 2;

	/**保存的选项卡的下标值*/
	private int savdCheckedIndex = Home_Fragment_Index;
	/**当前的选项卡的下标值*/
	private int mCurrentIndex = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//初始化控件
		initView();
		//初始化数据
		initData();
		//初始化控件的点击事件
		initEvent();

		//初始化碎片管理器
		fragmentManager = getSupportFragmentManager();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.w(TAG, "{onResume}");
		//设置保存的或者初始的选项卡标红显示
		SwitchTab(savdCheckedIndex);

		mCurrentIndex = -1;//解决按home键后长时间不用，再次打开显示空白的问题
		//设置保存的或者初始的选项卡展现对应的fragment
		ShowFragment(savdCheckedIndex);
	}

	/**
	 * 初始化控件
	 * */
	private void initView(){
		mBottomTabLayout = (TabBottomFragmentLayout) findViewById(R.id.bottomtab_Layout);
	}

	/**初始化数据*/
	private void initData() {
	}

	/**
	 * 初始化点击事件
	 * */
	private void initEvent(){
		//每一个选项卡的点击事件
		mBottomTabLayout.setOnBottomTabSelectedListener(new TabBottomFragmentLayout.OnBottomTabSelectListener() {
			@Override
			public void onBottomTabSelected(int index) {
				ShowFragment(index);//独立出来，用于OnResume的时候初始化展现相应的Fragment
			}
		});
	}

	/**控制切换选项卡*/
	public void SwitchTab(int checkedIndex){
		if(mBottomTabLayout != null){
			mBottomTabLayout.setTabsDisplay(checkedIndex);
		}
	}

	/**
	 * 显示选项卡对应的Fragment*/
	public void ShowFragment(int checkedIndex){
		if(mCurrentIndex == checkedIndex) {
			return;
		}
		//开启一个事务
		FragmentTransaction transcation = fragmentManager.beginTransaction();
		//隐藏全部碎片
		hideFragments(transcation);
		switch (checkedIndex) {
			case Home_Fragment_Index:
				if(homeFragment == null){
					homeFragment = HomeFragment.getInstance("这个是传递参数的写法");
					transcation.add(R.id.center_layout, homeFragment);
				}else{
					transcation.show(homeFragment);
				}
				break;
			case Message_Fragment_Index:
				if(messageFragment == null){
					messageFragment = new MessageFragment();
					transcation.add(R.id.center_layout, messageFragment);
				}else{
					transcation.show(messageFragment);
				}
				break;
			case Contact_Fragment_Index:
				if(contactFragment == null){
					contactFragment = new ContactFragment();
					transcation.add(R.id.center_layout, contactFragment);
				}else{
					transcation.show(contactFragment);
				}
				break;
			default:
				break;
		}
		savdCheckedIndex = checkedIndex;
		mCurrentIndex = checkedIndex;
		transcation.commitAllowingStateLoss();
	}

	/**隐藏全部碎片
	 * 需要注意：不要在OnResume方法中实例化碎片，因为先添加、显示，才可以隐藏。否则会出现碎片无法显示的问题*/
	private void hideFragments(FragmentTransaction transaction) {
		if (null != homeFragment) {
			transaction.hide(homeFragment);
		}
		if (null != messageFragment) {
			transaction.hide(messageFragment);
		}
		if (null != contactFragment) {
			transaction.hide(contactFragment);
		}
	}

	/**
	 * http://blog.csdn.net/caesardadi/article/details/20382815
	 * */
	// 自己记录fragment的位置,防止activity被系统回收时，fragment错乱的问题【按home键返回到桌面一段时间，然后在进程里面重新打开，会发现RadioButton的图片选中状态在第二个，但是文字和背景颜色的选中状态在第一个】
	//onSaveInstanceState()只适合用于保存一些临时性的状态，而onPause()适合用于数据的持久化保存。
	protected void onSaveInstanceState(Bundle outState) {
		//http://www.cnblogs.com/chuanstone/p/4672096.html?utm_source=tuicool&utm_medium=referral
		//总是执行这句代码来调用父类去保存视图层的状态”。其实到这里大家也就明白了，就是因为这句话导致了重影的出现
		//super.onSaveInstanceState(outState);
		outState.putInt("selectedCheckedIndex", savdCheckedIndex);
		outState.putInt("mCurrentIndex", mCurrentIndex);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		savdCheckedIndex = savedInstanceState.getInt("selectedCheckedIndex");
		mCurrentIndex = savedInstanceState.getInt("mCurrentIndex");
		super.onRestoreInstanceState(savedInstanceState);
	}

}
