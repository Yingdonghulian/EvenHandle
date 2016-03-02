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
 * ����ˢ��
 * @author Liu-
 *
 */
public class RefreshListView extends ListView implements OnScrollListener ,
android.widget.AdapterView.OnItemClickListener{
	private static final int STATE_PULL_REFRESH = 0;// ����ˢ��
	private static final int STATE_RELEASE_REFRESH = 1;// �ɿ�ˢ��
	private static final int STATE_REFRESHING = 2;// ����ˢ��
	private int startY = -1;// ��������y����
	private int mCurrrentState = STATE_PULL_REFRESH;// ��ǰ״̬
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
	 * ��ʼ������ˢ��
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
		 mRefreshHead.setPadding(0, -mRefreshHeadViewHeight, 0, 0);//��������ͷ����
		 initArrowAnim();
	     tvTime.setText("���ˢ��ʱ��:" + getCurrentTime());
	 }
	 /*
		 * ��ʼ���Ų���
		 */
		private void initFooterView() {
			mFooterView = View.inflate(getContext(),
					R.layout.refresh_listview_footer, null);
			this.addFooterView(mFooterView);

			mFooterView.measure(0, 0);
			mFooterViewHeight = mFooterView.getMeasuredHeight();

			mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);// ����

			this.setOnScrollListener(this);
		}
	 @Override
		public boolean onTouchEvent(MotionEvent ev) {
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				startY = (int) ev.getRawY();
				break;
			case MotionEvent.ACTION_MOVE:
				if (startY == -1) {// ȷ��startY��Ч
					startY = (int) ev.getRawY();
				}

				if (mCurrrentState == STATE_REFRESHING) {// ����ˢ��ʱ��������
					break;
				}

				int endY = (int) ev.getRawY();
				int dy = endY - startY;// �ƶ�������

				if (dy > 0 && getFirstVisiblePosition() == 0) {// ֻ���������ҵ�ǰ�ǵ�һ��item,����������
					int padding = dy - mRefreshHeadViewHeight;// ����padding
					mRefreshHead.setPadding(0, padding, 0, 0);// ���õ�ǰpadding

					if (padding > 0 && mCurrrentState != STATE_RELEASE_REFRESH) {// ״̬��Ϊ�ɿ�ˢ��
						mCurrrentState = STATE_RELEASE_REFRESH;
						refreshState();
					} else if (padding < 0 && mCurrrentState != STATE_PULL_REFRESH) {// ��Ϊ����ˢ��״̬
						mCurrrentState = STATE_PULL_REFRESH;
						refreshState();
					}

					return true;
				}

				break;
			case MotionEvent.ACTION_UP:
				startY = -1;// ����

				if (mCurrrentState == STATE_RELEASE_REFRESH) {
					mCurrrentState = STATE_REFRESHING;// ����ˢ��
					mRefreshHead.setPadding(0, 0, 0, 0);// ��ʾ
					refreshState();
				} else if (mCurrrentState == STATE_PULL_REFRESH) {
					mRefreshHead.setPadding(0, -mRefreshHeadViewHeight, 0, 0);// ����
				}

				break;

			default:
				break;
			}
			return super.onTouchEvent(ev);
		}
	 /**
		 * ˢ�������ؼ��Ĳ���
		 */
		private void refreshState() {
			switch (mCurrrentState) {
			case STATE_PULL_REFRESH:
				tvTitle.setText("����ˢ��");
				ivArrow.setVisibility(View.VISIBLE);
				pbProgress.setVisibility(View.INVISIBLE);
				ivArrow.startAnimation(animDown);
				break;
			case STATE_RELEASE_REFRESH:
				tvTitle.setText("�ɿ�ˢ��");
				ivArrow.setVisibility(View.VISIBLE);
				pbProgress.setVisibility(View.INVISIBLE);
		     	ivArrow.startAnimation(animUp);
				break;
			case STATE_REFRESHING:
				tvTitle.setText("����ˢ��...");
				ivArrow.clearAnimation();// �������������,��������
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
		 * ��ʼ����ͷ����
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
	        public void onLoadMore();// ������һҳ����
		}
		/*
		 * ��������ˢ�µĿؼ�
		 */
		public void onRefreshComplete(boolean success) {
			if (isLoadingMore) {// ���ڼ��ظ���...
				mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);// ���ؽŲ���
				isLoadingMore = false;
			} else {
				mCurrrentState = STATE_PULL_REFRESH;
				tvTitle.setText("����ˢ��");
				ivArrow.setVisibility(View.VISIBLE);
				pbProgress.setVisibility(View.INVISIBLE);

				mRefreshHead.setPadding(0, -mRefreshHeadViewHeight, 0, 0);// ����

   		   if (success) {
				tvTime.setText("���ˢ��ʱ��:" + getCurrentTime());
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

			if (getLastVisiblePosition() == getCount() - 1 && !isLoadingMore) {// ���������
				mFooterView.setPadding(0, 0, 0, 0);// ��ʾ
				setSelection(getCount() - 1);// �ı�listview��ʾλ��
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
