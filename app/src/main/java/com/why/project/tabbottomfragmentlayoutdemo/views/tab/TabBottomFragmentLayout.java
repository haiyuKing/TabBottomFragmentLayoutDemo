package com.why.project.tabbottomfragmentlayoutdemo.views.tab;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import com.why.project.tabbottomfragmentlayoutdemo.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Create By HaiyuKing
 * @Used 底部选项卡布局类（注意：这个是tab_bottom_item的父布局）
 */
public class TabBottomFragmentLayout extends LinearLayout{

	private Context mContext;
	
	//选项卡的CheckedTextView控件的android:drawableTop属性的数组【是一系列selector选择器xml文件】,所以类型是int
	//底部选项卡对应的图标
	private int[] bottomtab_IconIds = {R.drawable.home_tab_home_selector,R.drawable.home_tab_message_selector,R.drawable.home_tab_contact_selector};
	//底部选项卡对应的文字
	//CharSequence与String都能用于定义字符串，但CharSequence的值是可读可写序列，而String的值是只读序列。
	private CharSequence[] bottomtab_Titles = {getResources().getString(R.string.home_function_home),getResources().getString(R.string.home_function_message),getResources().getString(R.string.home_function_contact)};
	
	//选项卡的各个选项的view的集合：用于更改背景颜色
	private List<View> bottomtab_Items = new ArrayList<View>();
	//选项卡的各个选项的CheckedTextView的集合：用于切换时改变图标和文字颜色
	private List<CheckedTextView> bottomTab_checkeds = new ArrayList<CheckedTextView>();
	

	public TabBottomFragmentLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mContext = context;
		
		List<CharSequence> tab_titleList = new ArrayList<CharSequence>();
		tab_titleList = Arrays.asList(bottomtab_Titles);
		//初始化view：创建多个view对象（引用tab_bottom_item文件），设置图片和文字，然后添加到这个自定义类的布局中
		initAddBottomTabItemView(tab_titleList);
	}
	
	//初始化控件
	private void initAddBottomTabItemView(List<CharSequence> tabTitleList){
		
		int countChild = this.getChildCount();
		if(countChild > 0){
			this.removeAllViewsInLayout();//清空控件
			//将各个选项的view添加到集合中
			bottomtab_Items.clear();
			//将各个选项卡的各个选项的标题添加到集合中
			bottomTab_checkeds.clear();
		}
		
		
		//设置要添加的子布局view的参数
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		params.weight = 1;//在tab_bottom_item文件的根节点RelativeLayout中是无法添加的，而这个是必须要写上的，否则只会展现一个view
		params.gravity = Gravity.CENTER;
		
		for(int index=0;index<tabTitleList.size();index++){
			
			final int finalIndex = index;
			
			//============引用选项卡的各个选项的布局文件=================
			View bottomtabitemView = LayoutInflater.from(mContext).inflate(R.layout.tab_bottom_item, this, false);
			
			//===========设置CheckedTextView控件的图片和文字==========
			final CheckedTextView bottomtab_checkedTextView = (CheckedTextView) bottomtabitemView.findViewById(R.id.bottomtab_checkedTextView);
			
			//设置CheckedTextView控件的android:drawableTop属性值
			Drawable drawable = ContextCompat.getDrawable(mContext,bottomtab_IconIds[index]);
			//setCompoundDrawables 画的drawable的宽高是按drawable.setBound()设置的宽高
			//而setCompoundDrawablesWithIntrinsicBounds是画的drawable的宽高是按drawable固定的宽高，即通过getIntrinsicWidth()与getIntrinsicHeight()自动获得
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
			bottomtab_checkedTextView.setCompoundDrawables(null, drawable, null, null);
			//bottomtab_checkedTextView.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
			
			//设置CheckedTextView的文字
			bottomtab_checkedTextView.setText(tabTitleList.get(index).toString());
			
			//===========设置CheckedTextView控件的Tag(索引)==========用于后续的切换更改图片和文字
			bottomtab_checkedTextView.setTag("tag"+index);
			
			//添加选项卡各个选项的触发事件监听
			bottomtabitemView.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					//设置CheckedTextView状态为选中状态
					//修改View的背景颜色
					setTabsDisplay(finalIndex);
					//添加点击事件
					if(bottomTabSelectedListener != null){
						//执行activity主类中的onBottomTabSelected方法
						bottomTabSelectedListener.onBottomTabSelected(finalIndex);
					}
				}
			});
			
			//把这个view添加到自定义的MyBottomTab布局里面
			this.addView(bottomtabitemView,params);
			
			//将各个选项的view添加到集合中
			bottomtab_Items.add(bottomtabitemView);
			//将各个选项卡的各个选项的CheckedTextView添加到集合中
			bottomTab_checkeds.add(bottomtab_checkedTextView);
		}
	}
	
	/**
	 * 设置底部导航中图片显示状态和字体颜色
	 */
	public void setTabsDisplay(int checkedIndex) {
		
		int size = bottomTab_checkeds.size();
		
		for(int i=0;i<size;i++){
			CheckedTextView checkedTextView = bottomTab_checkeds.get(i);
			//设置CheckedTextView状态为选中状态
			if(checkedTextView.getTag().equals("tag"+checkedIndex)){
				checkedTextView.setChecked(true);
				//修改文字颜色
				checkedTextView.setTextColor(getResources().getColor(R.color.tab_text_selected));
				//修改view的背景颜色
				bottomtab_Items.get(i).setBackgroundColor(getResources().getColor(R.color.tab_bg_selected));
				
			}else{
				checkedTextView.setChecked(false);
				checkedTextView.setTextColor(getResources().getColor(R.color.tab_text_normal));
				bottomtab_Items.get(i).setBackgroundColor(getResources().getColor(R.color.tab_bg_normal));
			}
		}
	}
	
	private OnBottomTabSelectListener bottomTabSelectedListener;
	
	//自定义一个内部接口，用于监听选项卡选中的事件,用于获取选中的选项卡的下标值
	public interface OnBottomTabSelectListener{
		void onBottomTabSelected(int index);
	}
	
	public void setOnBottomTabSelectedListener(OnBottomTabSelectListener bottomTabSelectedListener){
		this.bottomTabSelectedListener = bottomTabSelectedListener;
	}
}
