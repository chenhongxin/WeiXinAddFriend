package com.xingqiba.weixinaddfriend.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public class ChxListView extends ListView implements OnScrollListener{

	//定义最大滚动高度
	int mContentMaxMoveHeight = 300;
	
	OnListScrollListener onListScrollListener;
	
	public ChxListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOnScrollListener(this);
	}

	/**
     * 阻尼系数,越小阻力就越大.
     */
    public static final float SCROLL_RATIO = 0.95f;
    private Rect mHeadInitRect = new Rect();
    private View mTopView;

    public void setTopView(View view) {
        mTopView = view;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
        	if(mTopView != null)
            mHeadInitRect.set(mTopView.getLeft(), mTopView.getTop(), mTopView.getRight(), mTopView.getBottom());
        }
        return super.onInterceptTouchEvent(ev);
    }

    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        //监听是否到底，如果到底就将maxOverScrollY设为0
        int newScrollY = scrollY + deltaY;
        final int bottom = maxOverScrollY + scrollRangeY;
        final int top = -maxOverScrollY;
        if (newScrollY > bottom) {
            maxOverScrollY = 0;
        } else if (newScrollY < top) {
            maxOverScrollY = mContentMaxMoveHeight;
        }
        //在向下移动时，scrollY是负值，所以scrollY + deltaY应该是当前应当所在位置。而由于scrollY + deltaY是负值，所以外层要包一个Math.abs（）来取绝对值
        int headerMoveHeight = (int) Math.abs((scrollY + deltaY) * SCROLL_RATIO);
        int mHeaderCurTop = (int) (mHeadInitRect.top + headerMoveHeight);
        if(mTopView != null)
        mTopView.layout(mHeadInitRect.left, mHeaderCurTop, mHeadInitRect.right, (int) (mHeadInitRect.bottom + headerMoveHeight));
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {  
            if (view.getLastVisiblePosition() == view.getCount() - 1) { 
            	Log.i("json", "onScrollStateChanged");
            	if(onListScrollListener != null){
            		onListScrollListener.onScrolLRefresh();
            	}
            }
        }
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
	}

	public void setOnListScrollListener(
			OnListScrollListener onListScrollListener) {
		this.onListScrollListener = onListScrollListener;
	}
	
	public interface OnListScrollListener{
		void onScrolLRefresh();
	}

}