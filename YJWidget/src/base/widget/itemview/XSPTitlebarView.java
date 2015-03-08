package base.widget.itemview;


import com.example.demoxswidget.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class XSPTitlebarView extends RelativeLayout {
	
	Drawable leftButtonDrawable, titleDrawable, rightButtonDrawable;
	String leftButtonStr, titleStr, rightButtonStr;
	
	TextView leftButtonTv, titleTv, rightButtonTv;
	
	OnTitleBarClickEvent mTitleBarOnClickEvent;
	
	public void setOnTitleBarClickEvent(OnTitleBarClickEvent titleBarOnClickEvent){
		this.mTitleBarOnClickEvent = titleBarOnClickEvent;
	}
	
	public static final String TAG_LEFT_BUTTON = "tag_left_button";
	public static final String TAG_TITLE_TEXTVIEW = "tag_title_textview";
	public static final String TAG_RIGHT_BUTTON = "tag_right_button";
	
	
	public XSPTitlebarView(Context context){
		this(context, null);
	}
	
	public XSPTitlebarView(Context context, AttributeSet attrs){
		this(context, attrs, -1);
	}

	public XSPTitlebarView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.title, defStyle, 0);
		
		leftButtonDrawable = a.getDrawable(R.styleable.title_leftIcon);
		titleDrawable = a.getDrawable(R.styleable.title_titleIcon);
		rightButtonDrawable = a.getDrawable(R.styleable.title_rightIcon);
		
		leftButtonStr = a.getString(R.styleable.title_leftLabel);
		titleStr = a.getString(R.styleable.title_titleLabel);
		rightButtonStr = a.getString(R.styleable.title_rightLabel);
		
		LayoutInflater.from(context).inflate(R.layout.base_xsp_view_003_title_bar, this, true);
		a.recycle();
	}
	
	private void setBounds(Drawable icon){
		icon.setBounds(0, 0, icon.getMinimumWidth(), icon.getMinimumHeight());
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		initLeftButton();
		
		initTitle();
		
		initRightButton();
		
	}
	
	private void initLeftButton(){
		leftButtonTv = (TextView) findViewById(R.id.tv_left_button);
		leftButtonTv.setTag(TAG_LEFT_BUTTON);
		
		if(null != leftButtonDrawable){
			setIcon(leftButtonTv, leftButtonDrawable);
		}else if(!TextUtils.isEmpty(leftButtonStr)){
			leftButtonTv.setText(leftButtonStr);
		}else{
			leftButtonTv.setVisibility(View.GONE);
		}
		
		setOnClickEvent(leftButtonTv);
	}
	
	private void initTitle(){
		titleTv = (TextView) findViewById(R.id.tv_title);
		titleTv.setTag(TAG_TITLE_TEXTVIEW);
		
		if(null != titleDrawable){
			setIcon(titleTv, titleDrawable);
			setOnClickEvent(titleTv);
		}
		
		if(!TextUtils.isEmpty(titleStr)){
			titleTv.setText(titleStr);
		}
	}
	
	public void setTitleLongClick(OnLongClickListener onLongClickListener) {
		if (null == onLongClickListener) {
			return;
		}
		if (null == titleTv) {
			return;
		}
		titleTv.setLongClickable(true);
		titleTv.setOnLongClickListener(onLongClickListener);
	}
	
	public void setRightIcon(int rsid){
		if(null == rightButtonTv){
			return;
		}
		rightButtonTv.setVisibility(View.VISIBLE);
		Drawable drawable = getResources().getDrawable(rsid);
		setIcon(rightButtonTv, drawable);
	}
	
	public void setRightLabel(String label){
		if(null == rightButtonTv){
			return;
		}
		rightButtonTv.setVisibility(View.VISIBLE);
		rightButtonTv.setText(label);
	}
	
	public void setRightEnable(boolean isEnable){
		if(null == rightButtonTv){
			return;
		}
		rightButtonTv.setEnabled(isEnable);
	}
	
	public void setRightInvisible() {
		rightButtonTv.setVisibility(View.INVISIBLE);
	}
	
	public void setTitleIcon(int rsid){
        if(null == titleTv){
            return;
        }
        Drawable drawable = getResources().getDrawable(rsid);
        setIcon(titleTv, drawable);
        setOnClickEvent(titleTv);
    }
	
	public void setTitle(String titleStr){
		if(null == titleTv){
			return;
		}
		titleTv.setText(titleStr);
	}
	
	public void setTitle(CharSequence titleChar){
		if(null == titleChar){
			return;
		}
		titleTv.setText(titleChar);
	}
	
	public void setTitleEllipsize(TruncateAt where){
		if(null == titleTv){
			return;
		}
		titleTv.setEllipsize(where);
	}
	
	private void initRightButton(){
		rightButtonTv = (TextView) findViewById(R.id.tv_right_button);
		rightButtonTv.setTag(TAG_RIGHT_BUTTON);
		
		if(null != rightButtonDrawable){
			setIcon(rightButtonTv, rightButtonDrawable);
		}else if(!TextUtils.isEmpty(rightButtonStr)){
			rightButtonTv.setText(rightButtonStr);
		}else{
			rightButtonTv.setVisibility(View.GONE);
		}
		
		setOnClickEvent(rightButtonTv);
	}
	
	private void setIcon(TextView tv,Drawable drawable){
		setBounds(drawable);
		tv.setCompoundDrawables(null, null, drawable, null);
	}
	
	private void setOnClickEvent(TextView tv){
		tv.setClickable(true);
		tv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(null == mTitleBarOnClickEvent){
					return;
				}
				
				String tag = (String) v.getTag();
				if(TAG_LEFT_BUTTON.equals(tag)){//左侧按钮
					mTitleBarOnClickEvent.onLeftClick(v);
					
				}else if(TAG_TITLE_TEXTVIEW.equals(tag)){//中间title
					mTitleBarOnClickEvent.onTitleClick(v);
					
				}else{//右侧按钮
					mTitleBarOnClickEvent.onRightClick(v);
					
				}
			}
		});
	}
	
	public interface OnTitleBarClickEvent{
		public void onLeftClick(View view);
		public void onTitleClick(View view);
		public void onRightClick(View view);
	}

	public TextView getRightButtonTv() {
		return rightButtonTv;
	}

}
