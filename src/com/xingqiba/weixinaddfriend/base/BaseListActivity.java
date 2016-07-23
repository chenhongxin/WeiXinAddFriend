package com.xingqiba.weixinaddfriend.base;

import android.os.Bundle;

import com.xingqiba.weixinaddfriend.R;
import com.xingqiba.weixinaddfriend.widget.ChxListView;
import com.xingqiba.weixinaddfriend.widget.ChxListView.OnListScrollListener;

public abstract class BaseListActivity extends BaseActivity {

	protected ChxListView clv_listview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		clv_listview = (ChxListView) findViewById(R.id.clv_listview);
        if(clv_listview != null){
	        clv_listview.setOnListScrollListener(new OnListScrollListener() {
				
				@Override
				public void onScrolLRefresh() {
					onListScrolLRefresh();
				}
			});
        }
	}

	public abstract void onListScrolLRefresh();
	
}
