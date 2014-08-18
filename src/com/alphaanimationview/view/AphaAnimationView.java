package com.alphaanimationview.view;

import java.util.ArrayList;
import java.util.List;
import com.alphaanimationview.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler.Callback;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.FrameLayout;


public class AphaAnimationView extends FrameLayout implements Animation.AnimationListener, Callback{

	View firstView, seconView;
	CustomAlphaAnimation alphaInAnimation, alphaOutAnimation;
	ViewAdapter adapter;
	Context context;
	
	private int currentPosition = -1, totalItemCount, animEndCount;
	private boolean animationStoped = false;
	private List<View> viewScapes = new ArrayList<View>();
	Handler handler = new Handler(Looper.getMainLooper(), this);
	
	private static final int defalutDuration = 3000;
	private static final int defalutInterval = 3000;
	private static final int defaultInterpolator = android.R.anim.accelerate_interpolator;
	public static final int VIEW_ALPHA_OUT = 0;	
	public static final int VIEW_ALPHA_IN = 1;	
	
	private int interpolator = defaultInterpolator;
	private int duration = defalutDuration;
	private int interval = defalutInterval;
	
	public AphaAnimationView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
		this.context = context;
		
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AlphaAnimationView, defStyle, 0);
		this.interpolator = a.getInt(R.styleable.AlphaAnimationView_interpolator, defaultInterpolator);
		this.duration = a.getInt(R.styleable.AlphaAnimationView_duration, defalutDuration);
		this.interval = a.getInt(R.styleable.AlphaAnimationView_interval, defalutInterval);
		a.recycle();
		initAnimationView();
	}

	public AphaAnimationView(Context context) {
		super(context);
		this.context = context;
		initAnimationView();
	}
	
	public AphaAnimationView setInterpolator(int interpolator) {
		this.interpolator = (interpolator == 0 ? defaultInterpolator : interpolator);
		return this;
	}
	
	public AphaAnimationView setDuration(int duration) {
		this.duration = (duration == 0 ? defalutDuration : duration);
		return this;
	}
	
	public AphaAnimationView setInterval(int interval) {
		this.interval = (interval == 0 ? defalutInterval : interval);
		return this;
	}
	
	private void initAnimationView() {
		alphaInAnimation = new CustomAlphaAnimation(CustomAlphaAnimation.TYPE_AlphaInAnimation);
		alphaOutAnimation = new CustomAlphaAnimation(CustomAlphaAnimation.TYPE_AlphaOutAnimation);
	}
	
	public void setAdapter(ViewAdapter adapter) {
		this.adapter = adapter;
		initChildViews();
	}
	
	private void initChildViews() {
		totalItemCount = adapter.getCount();
		firstView = adapter.getView(getCurrentPosition(), null);
		seconView = adapter.getView(getCurrentPosition(), null);
		firstView.setVisibility(View.VISIBLE);
		seconView.setVisibility(View.GONE);
		addView(firstView);
		addView(seconView);
		viewScapes.add(firstView);
		viewScapes.add(seconView);		
	}
	
	private int getCurrentPosition() {
		return currentPosition == (totalItemCount -1) ? (currentPosition = 0) : ++currentPosition;
	}
	
	public void startAnimation() {
		if (totalItemCount > 1) {
			animationStoped = false;
			handler.removeMessages(VIEW_ALPHA_OUT);
			handler.sendEmptyMessageDelayed(VIEW_ALPHA_OUT, interval);			
		}
	}
	
	public void stopAnimation() {
		if (totalItemCount > 1) {
			animationStoped = true;
			handler.removeMessages(VIEW_ALPHA_OUT);
		}
	}
	
	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case VIEW_ALPHA_OUT:
			viewScapes.get(VIEW_ALPHA_OUT).startAnimation(alphaOutAnimation);
			break;
		case VIEW_ALPHA_IN:
			viewScapes.get(VIEW_ALPHA_IN).startAnimation(alphaInAnimation);
		default:
			break;
		}
		return true;
	}
	
	@Override
	public void onAnimationEnd(Animation animation) {
		if (animation == alphaOutAnimation) {
			View convertView = viewScapes.remove(VIEW_ALPHA_OUT);
			convertView.setVisibility(View.GONE);
			viewScapes.add(adapter.getView(getCurrentPosition(), convertView));
			animEndCount++;
		} else if (animation == alphaInAnimation) {
			animEndCount++;
		}
		// confirm the new animation begins before both previous two animations are finished
		if (animEndCount == 2) {
			if (!animationStoped) {
				handler.sendEmptyMessageDelayed(VIEW_ALPHA_OUT, interval);
			}
			System.out.println(viewScapes.size());
			animEndCount = 0;
		}
	}

	@Override
	public void onAnimationRepeat(Animation animation) {	
	}

	@Override
	public void onAnimationStart(Animation animation) {	
		if (animation == alphaOutAnimation) {
			View view = viewScapes.get(1);
			view.setVisibility(View.VISIBLE);
			handler.sendMessageAtFrontOfQueue(handler.obtainMessage(VIEW_ALPHA_IN));
		}
	}
	
	class CustomAlphaAnimation extends Animation {
		
	    private float mFromAlpha;
	    private float mToAlpha;
	    
	    private static final int TYPE_AlphaInAnimation = 0;
	    private static final int TYPE_AlphaOutAnimation = 1;
	    
	    public CustomAlphaAnimation(int animationType) {
	    	initConfig(animationType);
	    	
	    }
	    
	    private void initConfig(int animationType) {
	    	mFromAlpha = animationType == TYPE_AlphaInAnimation ? 0.0f : 1.0f;
	    	mToAlpha = animationType == TYPE_AlphaOutAnimation ? 0.0f : 1.0f;
	    	setDuration(duration);
	    	setInterpolator(AnimationUtils.loadInterpolator(context, interpolator));
	    	setAnimationListener(AphaAnimationView.this);
	    }
	    
	    @Override
	    protected void applyTransformation(float interpolatedTime, Transformation t) {
	        final float alpha = mFromAlpha;
	        t.setAlpha(alpha + ((mToAlpha - alpha) * interpolatedTime));
	    }
	}
	
	public static abstract class ViewAdapter {
		abstract public int getCount();
		abstract public Object getItem(int position);
		abstract public View getView(int postion, View convertView);
	}
}
