package base.widget.scrollview;

import android.animation.AnimatorSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import base.ACT_Base;

import com.example.demoxswidget.R;

public class ACT_HoverView extends ACT_Base {

	protected static final String TAG = ACT_HoverView.class.getSimpleName();
	private ScrollView scrollView;
	private TextView tvShare,tvShare2,tvDownload,tvDownload2;
	private LinearLayout vgAction;
	boolean isUp=false;//是否是向上滑
	boolean isExeUp=false;
	boolean isExeDown=true;
	private AnimatorSet animationUp;
	private AnimatorSet animationDown;
	private int preScrollY;
	private TextView tvHello;
	@Override
	protected void initViews() {
		setContentView(R.layout.act_hoverview);
		scrollView=(ScrollView) findViewById(R.id.scrollView);
		tvShare=(TextView) findViewById(R.id.tvShare);
		tvShare2=(TextView) findViewById(R.id.tvShare2);
		tvDownload=(TextView) findViewById(R.id.tvDownload);
		tvDownload2=(TextView) findViewById(R.id.tvDownload2);
		vgAction=(LinearLayout) findViewById(R.id.vgAction);
	}
	int currentScrollY;
	int oldScrollY;
	int deltaScrollY;
	@Override
	protected void bindEvents() {
		scrollView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				currentScrollY= v.getScrollY();
				deltaScrollY=currentScrollY-oldScrollY;
				oldScrollY=currentScrollY;
				Log.i(TAG, "scrollY:"+currentScrollY);
				if(Math.abs(deltaScrollY)>80&&deltaScrollY<0){
					//向下移动过快时
					vgAction.setTranslationY(0);
				}else{
					vgAction.setTranslationY(Math.max(-currentScrollY, -170));
				}
				return false;
			}
		});
	}
	
    public void startAnimationUp() {
//        createAnimationUp();
//        animationUp.start();
    }
    public void startAnimationDown() {
//    	createAnimationDown();
//    	animationDown.start();
    }
	private void createAnimationUp() {
		/*if(animationUp==null){
			ObjectAnimator anim1 = ObjectAnimator.ofFloat(vgAction, "y", 100);
			ObjectAnimator anim2 = ObjectAnimator.ofFloat(vgAction, "x", 400);
			animationUp = new AnimatorSet();
			animationUp.playTogether(anim1,anim2);
		}*/
	}
	private void createAnimationDown() {
		/*if(animationDown==null){
			ObjectAnimator anim1 = ObjectAnimator.ofFloat(vgAction, "y", 300);
			ObjectAnimator anim2 = ObjectAnimator.ofFloat(vgAction, "x", 300);
			animationDown = new AnimatorSet();
			animationDown.playTogether(anim1,anim2);
		}*/
	}

	@Override
	protected void initDatas() {
		
	}

}
