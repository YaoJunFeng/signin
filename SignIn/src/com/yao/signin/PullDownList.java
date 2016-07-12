package com.yao.signin;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * <p>
 * ����:QYC
 * <p>
 * �ʼ�:68452263@qq.com
 * <p>
 * */
public class PullDownList extends ListView {

	public PullDownList(Context context) {
		super(context);
		if (isInEditMode()) {
			inEditMode();
		} else {
			init(null);
		}
	}

	public PullDownList(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (isInEditMode()) {
			inEditMode();
		} else {
			init(attrs);
		}
	}

	public PullDownList(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		if (isInEditMode()) {
			inEditMode();
		} else {
			init(attrs);
		}
	}

	// �ڿ��ӻ��༭��������ʾ������.��Щ����ֻ��XML�༭������ʾ,��Ӱ�����
	// �����ѿؼ���װ��������,Ҳ���������ַ�ʽչʾ��������Ա.
	// ��EditMode�����е���Դ�ļ���������,ȫ��Ҫ�ô��붯̬�γ�.
	// ������̳е���listView,��������adapter����ʾ����.�����Ŀؼ�������һ��layout
	// �Ϳ����Լ���ʼ��һЩView�ӵ�����ȥ.
	private void inEditMode() {
		if (isInEditMode()) {
			setBackgroundColor(Color.rgb(134, 154, 190));
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);
			adapter.add("PullDownList");
			adapter.add("����:QYC");
			adapter.add("����:68452263@qq.com");
			setAdapter(adapter);
			return;
		}
	}

	/*
	 * ==================�б?���Ŀؼ�����==================
	 */
	private ProgressBar progressBar_top = null;
	private ImageView imageView_top = null;
	private TextView txtTime_top, txtRefresh_top;
	/** �б?������ͼ */
	private View topViewItem = null;
	/** �б?����ͼ�ĸ� */
	private int topViewItemHeight = 0;
	/** ˢ�����¼�ͷ */
	private int imgRefreshDown = R.drawable.png_pulldownlist_refreshdown;
	/** ˢ�����ϼ�ͷ */
	private int imgRefreshUp = R.drawable.png_pulldownlist_refreshup;
	/*
	 * =====================================================
	 */

	/*
	 * ==================�б�ײ��Ŀؼ�����==================
	 */
	private View bottomViewItem = null;
	private ProgressBar progressBar_bottom = null;
	private TextView txtBottom = null;
	/*
	 * =====================================================
	 */

	/*
	 * ==================�����߼��жϵ�4������==================
	 */
	/** ��һ���Ƿ�����Ļ�� */
	private boolean isFirstItemShow = false;// ��һ�м�������,ֻҪ���б��������,���ܼ����б�ѹ���˻��Ǳ�������,������Ϊ��ʾ������Ļ��
	/** �Ƿ��м�����ͼ,�����ص�һ�е���ͼ */
	private boolean isWantToExtrude = true;
	/** �Ƿ��Ѿ����� */
	private boolean isExtruded = false;
	/** �Ƿ��Ѿ��ڼ��� */
	private boolean isLoading = false;
	/*
	 * =====================================================
	 */

	/** ʱ���ʽ�� */
	private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);

	/*
	 * =====================================================
	 */

	/** ��ʼ�� */
	private void init(AttributeSet attrs) {
		setOnScrollListener(onScrollListener);

		// ��ʼ���б�ײ�������
		LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		bottomViewItem = layoutInflater.inflate(R.layout.pulldownlist_bottom, this, false);

		progressBar_bottom = (ProgressBar) bottomViewItem.findViewById(R.id.progressBar1);
		txtBottom = (TextView) bottomViewItem.findViewById(R.id.textView1);
		progressBar_bottom.setVisibility(View.INVISIBLE);// ���ؽ����
		txtBottom.setText("������ظ��");// ��������
		bottomViewItem.setOnClickListener(onClickListener);// ���õ������

		// ��ʼ���б?��������
		topViewItem = layoutInflater.inflate(R.layout.pulldownlist_top, this, false);
		topViewItem.setOnClickListener(null);// ʹ������Ϊlist��һ��item�����
		progressBar_top = (ProgressBar) topViewItem.findViewById(R.id.progressBar1);
		progressBar_top.setVisibility(View.INVISIBLE);
		imageView_top = (ImageView) topViewItem.findViewById(R.id.imageView1);
		txtTime_top = (TextView) topViewItem.findViewById(R.id.textView2);
		txtTime_top.setText("��һ�θ���ʱ��:��");
		txtRefresh_top = (TextView) topViewItem.findViewById(R.id.textView1);

		// ����ȶ�������,�Կؼ���ʽ���е���
		setResValue(attrs);

		/*
		 * 3�����ظ�topViewItem: 1.����������Ļ��ĳ��Ϳ�,��Ϊ������û������Ļ,ֱ�ӻ�ȡ�䳤������Ч�� 2.�õ�����ĸ߶�
		 * 3.���ϱ߾�����Ϊ���߶�,�൱�ڰѸ߶ȱ����0,�ﵽ���ص�Ч��(˳��һ��:topViewItem��ʹͨ��padding������߶�Ϊ0
		 * ,���б���,����Ȼ��ռ��һ�е�����,�б���Ȼ��Ϊ���ǵ�0��)
		 */
		// ��һ��
		measureView(topViewItem);
		// �ڶ���
		topViewItemHeight = topViewItem.getMeasuredHeight();
		// ����
		topViewItem.setPadding(0, 0 - topViewItemHeight, 0, 0);
		// ���뵽�б���
		addHeaderView(topViewItem);
	}

	// �����Զ������Ե�����,���Բο�����,����DP��PX��ת��
	/** ����Զ�������,�Կؼ���ʽ���е��� */
	private void setResValue(AttributeSet attrs) {
		// ��ȡ�ؼ��Զ�������ֵ
		TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.PullDownList);
		int N = a.getIndexCount();// �Զ������Ա�ʹ�õ�����
		for (int i = 0; i < N; i++) {
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.PullDownList_loadingMoreBKColor :
				// �ײ�����������ɫ
				int loadingMoreBKColorID = a.getResourceId(attr, 0);
				if (loadingMoreBKColorID != 0) {
					bottomViewItem.setBackgroundResource(loadingMoreBKColorID);
				} else {
					int loadingMoreBKColor = a.getColor(attr, 0);
					if (loadingMoreBKColor != 0) {
						bottomViewItem.setBackgroundColor(loadingMoreBKColor);
					}
				}
			break;
			case R.styleable.PullDownList_loadingMoreTextColor :
				// �ײ�������������ɫ
				int loadingMoreTextColor = a.getColor(attr, 0);
				if (loadingMoreTextColor != 0) {
					txtBottom.setTextColor(loadingMoreTextColor);
				}
			break;
			case R.styleable.PullDownList_loadingMoreTextSize :
				// �ײ����������ִ�С
				float loadingMoreTextSize = a.getDimension(attr, 0);
				if (loadingMoreTextSize != 0) {
					txtBottom.setTextSize(TypedValue.COMPLEX_UNIT_PX, loadingMoreTextSize);
				}
			break;
			case R.styleable.PullDownList_refreshTextColor :
				// ������������ʾ������ɫ
				int refreshTextColor = a.getColor(attr, 0);
				if (refreshTextColor != 0) {
					txtRefresh_top.setTextColor(refreshTextColor);
				}
			break;
			case R.styleable.PullDownList_refreshTextSize :
				// ������������ʾ���ִ�С
				float refreshTextSize = a.getDimension(attr, 0);
				if (refreshTextSize != 0) {
					txtRefresh_top.setTextSize(TypedValue.COMPLEX_UNIT_PX, refreshTextSize);
				}
			break;
			case R.styleable.PullDownList_refreshTimeColor :
				// ����������ʱ��������ɫ
				int refreshTimeColor = a.getColor(attr, 0);
				if (refreshTimeColor != 0) {
					txtTime_top.setTextColor(refreshTimeColor);
				}
			break;
			case R.styleable.PullDownList_refreshTimeSize :
				// ����������ʱ�����ִ�С
				float refreshTimeSize = a.getDimension(attr, 0);
				if (refreshTimeSize != 0) {
					txtTime_top.setTextSize(TypedValue.COMPLEX_UNIT_PX, refreshTimeSize);
				}
			break;
			case R.styleable.PullDownList_refreshArrowUp :
				// �������������ϼ�ͷ
				int refreshArrowUp = a.getResourceId(attr, 0);
				if (refreshArrowUp != 0) {
					this.imgRefreshUp = refreshArrowUp;
				}
			break;
			case R.styleable.PullDownList_refreshArrowDown :
				// �������������¼�ͷ
				int refreshArrowDown = a.getResourceId(attr, 0);
				if (refreshArrowDown != 0) {
					this.imgRefreshDown = refreshArrowDown;
				}
			break;
			default :
			break;
			}
		}
		a.recycle();
	}

	// �б�����¼�
	private android.widget.AbsListView.OnScrollListener onScrollListener = new OnScrollListener() {
		/*
		 * ���б����κ���ʽ(�Զ������������Ź�)ֹͣ������,����һ���Ѿ�������,��ô��Ϊ�Ѿ����˼��ص���ͼ
		 * ����һ��û�г���,��ô��Ϊû�м��ص���ͼ
		 */
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (isLoading)// �Ѿ��ڼ��������κδ���
			{
				return;
			}
			// ���б����κ���ʽ(�Զ������������Ź�)ֹͣ����ʱ
			if (scrollState == SCROLL_STATE_IDLE) {
				if (isFirstItemShow) {
					isWantToExtrude = true;
					// System.out.println("��ͼ��onScrollStateChanged��Ϊtrue");
				} else {
					isWantToExtrude = false;
					// System.out.println("��ͼ��onScrollStateChanged��Ϊfalse");
				}
			}
		}

		/*
		 * ���б���������,�����Ļ����ǲ��ǵ�0��,���ж�:��0���Ƿ��������Ļ��,�Ƿ���ڼ��ص���ͼ
		 */
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			if (isLoading)// �Ѿ��ڼ��������κδ���
			{
				return;
			}
			if (firstVisibleItem == 0) {
				isFirstItemShow = true;
				// System.out.println("isFirstItemShow��onScroll��Ϊtrue");
			} else {
				isFirstItemShow = false;
				isWantToExtrude = false;
				// System.out.println("��ͼ��onScroll��Ϊfalse");
			}
		}
	};

	/** ��һ����ָ������Ļ��Y����� */
	private float lastActionDown_Y = 0;

	// �����¼�
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (isLoading)// �Ѿ��ڼ��������κδ���
		{
			return super.onTouchEvent(ev);
		}

		int action = ev.getAction();
		if (action == MotionEvent.ACTION_DOWN) {
			// ������ָʱ,��ð���y��λ��,�������϶�ʱ������ָ�϶�����
			lastActionDown_Y = ev.getY();
		} else if (action == MotionEvent.ACTION_MOVE) {
			/*
			 * 1.����м��ص���ͼ,���ݴ�����Y����ƶ����ж�.
			 * 2.Y���ƶ�ֵΪ��˵������������ͼ�������,�����߽���������,����Ӧ�ø������ķ���������һ��,������������ؼ��Ĵ������¼���
			 * 3.Y���ƶ�ֵ��ɸ�,˵���������Ѿ�����ȥ,��һ��Ҳ�Ѿ�����û..��ʱ�������δ������¼�,ֱ��ListView��������ͼ��Ϊ��ֵ��
			 * ��������ٴ�����. 4.�������м��ص���ͼ���������ֿ��ܡ�һ����ƽ���Ĵ������������κδ��?
			 * ��һ���ǵ�ʱ�Ѿ������ˣ��û��ֲ������ˣ��������������˻�ȥ �ƻ�ȥ��,������ͼ��onScroll������Ϊfalse��
			 * ����������£�Ӧ�ðѵ�һ������ѹ����ԭ״��
			 */
			// ��ָ�����ľ���
			float gapValue_y = ev.getY() - lastActionDown_Y;
			gapValue_y = gapValue_y * 2 / 3;// ���������ٶ�,����ʵ����ָ�����ٶ�����
			if (isWantToExtrude) {
				if (gapValue_y >= 0) {
					isExtruded = true;
					// �µ�PaddingTopӦ���ǻ����ľ����ȥ�ܸ�,�Ż�ʹ�����������������Ч��
					int newPaddingTop = (int) (gapValue_y - topViewItemHeight);
					topViewItem.setPadding(0, newPaddingTop, 0, 0);
					// ����µ�PaddingTop���ڵ���20,˵���Ѿ�����ʾ������»�������20����,����㹻��,��ʾ�û��ɿ��Ļ��ǿ��Ը��µ�
					if (newPaddingTop >= 20) {
						setTopViewControl(2);
					} else {
						setTopViewControl(1);
					}
					// ����
					return false;
				}
			} else {
				// �û��˿��Ѿ�������ظ��
				// ���ܶ����������Ѿ���ʧ����,Ҳ�����ϵ�һ��
				if (isExtruded) {// �ϵ�һ��,��ԭ
					isExtruded = false;
					topViewItem.setPadding(0, 0 - topViewItemHeight, 0, 0);
				}
			}
		} else if (action == MotionEvent.ACTION_UP) {
			if (isExtruded) {
				if (topViewItem.getPaddingTop() >= 20) {
					if (this.onPDListListener != null) {
						isLoading = true;
						topViewItem.setPadding(0, 0, 0, 0);
						setTopViewControl(3);
						this.onPDListListener.onRefresh();
					} else {
						topViewItem.setPadding(0, 0 - topViewItemHeight, 0, 0);
						isExtruded = false;
					}
				} else {
					topViewItem.setPadding(0, 0 - topViewItemHeight, 0, 0);
					isExtruded = false;
				}
			}
		}
		return super.onTouchEvent(ev);
	}

	// ��������,��ֹ��������ظ���ֵ
	private int controlState = -1;

	/**
	 * �û��Ƿ�����������
	 * 
	 * @return
	 */
	public boolean isExtruded() {
		return isExtruded;
	}

	/*
	 * ==================�������������ֵ�˽�к���=================
	 */
	/**
	 * ���ö����������ؼ�����ʾ����
	 * 
	 * @param state
	 *            0��ʾ���û�����,1��ʾ����ˢ��,2��ʾ�ɿ�ˢ��,3��ʾ����ˢ��
	 */
	private void setTopViewControl(int state) {
		if (controlState != state) {
			System.out.println("state=" + state);
			controlState = state;
			if (state == 0) {
				imageView_top.setVisibility(View.VISIBLE);
				progressBar_top.setVisibility(View.INVISIBLE);
			} else if (state == 1) {
				txtRefresh_top.setText("��������");
				imageView_top.setImageResource(imgRefreshDown);
			} else if (state == 2) {
				txtRefresh_top.setText("�ͷ���������");
				imageView_top.setImageResource(imgRefreshUp);
			} else if (state == 3) {
				imageView_top.setVisibility(View.INVISIBLE);
				progressBar_top.setVisibility(View.VISIBLE);
				txtRefresh_top.setText("���ڸ���...");
			}
		}
	}

	/*
	 * ==============�ײ����������ֵ�˽�к���=================
	 */

	// View�ĵ���¼�
	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v.equals(bottomViewItem)) {
				if (onPDListListener != null) {
					boolean result = onPDListListener.onloadMore();
					if (result) {
						loadingMoreView_State(true);
					}
				}
			}
		}
	};

	private void loadingMoreView_State(boolean isLoading) {
		if (!isLoading) {
			progressBar_bottom.setVisibility(View.INVISIBLE);
			txtBottom.setText("������ظ��");
			bottomViewItem.setClickable(true);
		} else {
			progressBar_bottom.setVisibility(View.VISIBLE);
			txtBottom.setText("����Ŭ������...");
			bottomViewItem.setClickable(false);
		}
	}

	/* =======================����ֵĹ�������====================== */
	/** ����ˢ�¼��� */
	public void setOnPDListen(OnPDListListener onPDListListener) {
		this.onPDListListener = onPDListListener;
	}

	/** �Ƴ�ˢ�¼��� */
	public void removePDListen() {
		this.onPDListListener = null;
	}

	/* ===============�ײ����������ֵĹ�������============= */

	/** �Ƿ����ü��ظ�� */
	public void loadingMoreView_IsEnabled(boolean isEnabled) {
		if (isEnabled) {
			addFooterView(bottomViewItem);
		} else {
			removeFooterView(bottomViewItem);
		}
	}

	/** ��ɼ��ظ�� */
	public void loadingMoreView_loadfinish() {
		loadingMoreView_State(false);
	}

	/* ====================�������������ֵĹ�������==================== */
	/** ����ˢ���� ������onRefresh�ӿ� */
	public void startRefresh() {
		if (!isLoading && this.onPDListListener != null) {
			isLoading = true;
			topViewItem.setPadding(0, 0, 0, 0);
			setTopViewControl(3);
			setSelection(0);// ѡ������
			this.onPDListListener.onRefresh();
		}
	}

	/**
	 * ����ˢ��
	 * 
	 * @param isUpdateTime
	 *            �Ƿ����ʱ��,ˢ��ʧ�ܿ��Բ�����
	 */
	public void stopRefresh(boolean isUpdateTime) {
		// ��ԭ���б���,�������ûس�ʼֵ,ѡ������
		if (isLoading) {
			// �������б���
			isFirstItemShow = false;
			isWantToExtrude = true;
			isExtruded = false;
			isLoading = false;
			// ���������пؼ�������
			setTopViewControl(0);
			// ����ʱ��
			if (isUpdateTime) {
				txtTime_top.setText("��һ�θ���ʱ��:" + sdf.format(Calendar.getInstance().getTime()));
			}
			topViewItem.setPadding(0, 0 - topViewItemHeight, 0, 0);
			setSelection(0);
		}
	}

	/* ================��������========================== */
	/** ΪView�������� ��ΪView�ڼ��뵽����֮ǰ,ֱ��ȥ��ȡ��������Ч��,����Activity��onCreatʱ�޷���ȡ�ؼ�����һ������ */
	private static void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	private OnPDListListener onPDListListener = null;

	public interface OnPDListListener {
		public void onRefresh();

		/**
		 * ����˼��ظ��
		 * 
		 * @return �����,�����ǲ������ȥ����ȥ����?
		 */
		public boolean onloadMore();
	}

}
