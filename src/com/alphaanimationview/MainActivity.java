package com.alphaanimationview;

import java.util.ArrayList;
import java.util.List;

import com.alphaanimationview.view.AphaAnimationView;
import com.alphaanimationview.view.AphaAnimationView.ViewAdapter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class MainActivity extends ActionBarActivity {
	
	List<HornDetail> hornList = new ArrayList<HornDetail>();
	boolean animaionOn = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		for (int i = 0; i < 10; i++) {
			HornDetail hornDetail = new HornDetail();
			hornDetail.content = "µÚ" + i + "¸öÀ®°È";
			hornDetail.nickName = "êÇ³Æ" + i;
			hornDetail.status = i % 2 == 0 ? 1 : 2;
			hornList.add(hornDetail);
		}
		RelativeLayout root = (RelativeLayout)findViewById(R.id.root);
		final AphaAnimationView view = new AphaAnimationView(MainActivity.this);
		view.setDuration(3000).setInterpolator(3000).setInterpolator(android.R.anim.accelerate_interpolator);
		view.setAdapter(new MyViewAdapter());
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)getResources().getDisplayMetrics().density * 74);
		root.addView(view, lp);
		view.startAnimation();
		
		final Button button = new Button(MainActivity.this);
		button.setText("Í£Ö¹¶¯»­");
		RelativeLayout.LayoutParams buttonParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		buttonParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		root.addView(button, buttonParam);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (animaionOn) {
					animaionOn = false;
					view.stopAnimation();
					button.setText("¿ªÊ¼¶¯»­");
				} else {
					animaionOn = true;
					view.startAnimation();
					button.setText("Í£Ö¹¶¯»­");
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	class MyViewAdapter extends ViewAdapter {

		@Override
		public int getCount() {
			return hornList.size();
		}

		@Override
		public Object getItem(int position) {
			return hornList.get(position);
		}

		@Override
		public View getView(int position, View convertView) {
			Holder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.qvip_nearby_horn_animation_item, null);
				holder = new Holder();
				holder.headerView = (ImageView)convertView.findViewById(R.id.icon);
				holder.businessView = (ImageView)convertView.findViewById(R.id.vip_image);
				holder.nickView = (TextView)convertView.findViewById(R.id.nick);
				holder.contentView = (TextView)convertView.findViewById(R.id.horn_content);
				convertView.setTag(holder);
			} else {
				holder = (Holder)convertView.getTag(); 
			}
			HornDetail hornDetail = (HornDetail)getItem(position);
			holder.headerView.setImageResource(R.drawable.h001);
			holder.nickView.setText(hornDetail.nickName);
			holder.contentView.setText(hornDetail.content);
			holder.businessView.setImageResource(hornDetail.status == 2 ? R.drawable.svip : R.drawable.vip);
			return convertView;
		}
		
	}
	static class Holder {
		ImageView headerView;
		ImageView businessView;
		TextView nickView;
		TextView contentView;
	}
	
	static class HornDetail {
		String content;
		String nickName;
		int status;
	}
}
