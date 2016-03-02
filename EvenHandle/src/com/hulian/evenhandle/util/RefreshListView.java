package com.hulian.evenhandle.util;




import java.text.SimpleDateFormat;
import java.util.Date;

import com.hulian.evenhandle.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
/**
 * 下拉刷新
 * @author Liu-
 *
 */
public class RefreshListView extends ListView implements OnScrollListener ,
android.widget.AdapterView.OnItemClickListener{
	private static final int STATE_PULL_REFRESH = 0;// 下拉刷新
	private static final int STATE_RELEASE_REFRESH = 1;// 松开刷新
	private static final int STATE_REFRESHING = 2;// 正在刷新
	private int startY = -1;// 滑动起点的y坐标
	private int mCurrrentState = STATE_PULL_REFRESH;// 当前状态
	private View mRefreshHead;
	private int mRefreshHeadViewHeight;
	private TextView tvTitle;
	private TextView tvTime;
	private ImageView ivArrow;
	private ProgressBar pbProgress;
	private RotateAnimation animUp;
	private RotateAnimation animDown;

	public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initRefreshHead();
		initFooterView();
	}

	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initRefreshHead();
		initFooterView();
	}

	public RefreshListView(Context context) {
		super(context);
		initRefreshHead();
		initFooterView();
	}
	/**
	 * 初始化下拉刷新
	 * @return 
	 */
	 public  void  initRefreshHead(){
		 mRefreshHead = View.inflate(getContext(),R.layout.refresh_header,null);
		 this.addHeaderView(mRefreshHead);
		 
		 tvTitle = (TextView) mRefreshHead.findViewById(R.id.tv_title);
		 tvTime = (TextView) mRefreshHead.findViewById(R.id.tv_time);
		 ivArrow = (ImageView) mRefreshHead.findViewById(R.id.iv_arr);
		 pbProgress = (ProgressBar) mRefreshHead.findViewById(R.id.pb_progress);
		 mRefreshHead.measure(0, 0);
		 mRefreshHeadViewHeight = mRefreshHead.getMeasuredHeight();
		 mRefreshHead.setPadding(0, -mRefreshHeadViewHeight, 0, 0);//隐藏下拉头布局
		 initArrowAnim();
	     tvTime.setText("最后刷新时间:" + getCurrentTime());
	 }
	 /*
		 * 初始化脚布局
		 */
		private void initFooterView() {
			mFooterView = View.inflate(getContext(),
					R.layout.refresh_listview_footer, null);
			this.addFooterView(mFooterView);

			mFooterView.measure(0, 0);
			mFooterViewHeight = mFooterView.getMeasuredHeight();

			mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);// 隐藏

			this.setOnScrollListener(this);
		}
	 @Override
		public boolean onTouchEvent(MotionEvent ev) {
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				startY = (int) ev.getRawY();
				break;
			case MotionEvent.ACTION_MOVE:
				if (startY == -1) {// 确保startY有效
					startY = (int) ev.getRawY();
				}

				if (mCurrrentState == STATE_REFRESHING) {// 正在刷新时不做处理
					break;
				}

				int endY = (int) ev.getRawY();
				int dy = endY - startY;// 移动便宜量

				if (dy > 0 && getFirstVisiblePosition() == 0) {// 只有下拉并且当前是第一个item,才允许下拉
					int padding = dy - mRefreshHeadViewHeight;// 计算padding
					mRefreshHead.setPadding(0, padding, 0, 0);// 设置当前padding

					if (padding > 0 && mCurrrentState != STATE_RELEASE_REFRESH) {// 状态改为松开刷新
						mCurrrentState = STATE_RELEASE_REFRESH;
						refreshState();
					} else if (padding < 0 && mCurrrentState != STATE_PULL_REFRESH) {// 改为下拉刷新状态
						mCurrrentState = STATE_PULL_REFRESH;
						refreshState();
					}

					return true;
				}

				break;
			case MotionEvent.ACTION_UP:
				startY = -1;// 重置

				if (mCurrrentState == STATE_RELEASE_REFRESH) {
					mCurrrentState = STATE_REFRESHING;// 正在刷新
					mRefreshHead.setPadding(0, 0, 0, 0);// 显示
					refreshState();
				} else if (mCurrrentState == STATE_PULL_REFRESH) {
					mRefreshHead.setPadding(0, -mRefreshHeadViewHeight, 0, 0);// 隐藏
				}

				break;

			default:
				break;
			}
			return super.onTouchEvent(ev);
		}
	 /**
		 * 刷新下拉控件的布局
		 */
		private void refreshState() {
			switch (mCurrrentState) {
			case STATE_PULL_REFRESH:
				tvTitle.setText("下拉刷新");
				ivArrow.setVisibility(View.VISIBLE);
				pbProgress.setVisibility(View.INVISIBLE);
				ivArrow.startAnimation(animDown);
				break;
			case STATE_RELEASE_REFRESH:
				tvTitle.setText("松开刷新");
				ivArrow.setVisibility(View.VISIBLE);
				pbProgress.setVisibility(View.INVISIBLE);
		     	ivArrow.startAnimation(animUp);
				break;
			case STATE_REFRESHING:
				tvTitle.setText("正在刷新...");
				ivArrow.clearAnimation();// 必须先清除动画,才能隐藏
				ivArrow.setVisibility(View.INVISIBLE);
				pbProgress.setVisibility(View.VISIBLE);
				if (mListener != null) {
					mListener.onRefresh();
				}
				break;
				
			   default:
				break;
			}
		}
		/**
		 * 初始化箭头动画
		 */
		private void initArrowAnim() {
			animUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			animUp.setDuration(200);
			animUp.setFillAfter(true);

			animDown = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF,
					0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
			animDown.setDuration(200);
			animDown.setFillAfter(true);
		}
		
		OnRefreshListener mListener;
		private View mFooterView;
		private int mFooterViewHeight;

		public void setOnRefreshListener(OnRefreshListener listener) {
			mListener = listener;
		}

		public interface OnRefreshListener {
			public void onRefresh();
	        public void onLoadMore();// 加载下一页数据
		}
		/*
		 * 收起下拉刷新的控件
		 */
		public void onRefreshComplete(boolean success) {
			if (isLoadingMore) {// 正在加载更多...
				mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);// 隐藏脚布局
				isLoadingMore = false;
			} else {
				mCurrrentState = STATE_PULL_REFRESH;
				tvTitle.setText("下拉刷新");
				ivArrow.setVisibility(View.VISIBLE);
				pbProgress.setVisibility(View.INVISIBLE);

				mRefreshHead.setPadding(0, -mRefreshHeadViewHeight, 0, 0);// 隐藏

   		   if (success) {
				tvTime.setText("最后刷新时间:" + getCurrentTime());
				}
			}
		}
		public String getCurrentTime(){
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return format.format(new Date());
		}

	@Override
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
            		
	}
	private boolean isLoadingMore;
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == SCROLL_STATE_IDLE
				|| scrollState == SCROLL_STATE_FLING) {

			if (getLastVisiblePosition() == getCount() - 1 && !isLoadingMore) {// 滑动到最后
				mFooterView.setPadding(0, 0, 0, 0);// 显示
				setSelection(getCount() - 1);// 改变listview显示位置
				isLoadingMore = true;
				if (mListener != null) {
					mListener.onLoadMore();
				}
			}
		}		
	}
	
	OnItemClickListener mItemClickListener;

	@Override
	public void setOnItemClickListener(
			android.widget.AdapterView.OnItemClickListener listener) {
		super.setOnItemClickListener(this);

		mItemClickListener = listener;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (mItemClickListener != null) {
			mItemClickListener.onItemClick(parent, view, position
					- getHeaderViewsCount(), id);
		}
	}
}
