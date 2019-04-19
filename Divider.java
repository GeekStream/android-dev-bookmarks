public class Divider extends RecyclerView.ItemDecoration {
	private boolean mRemoveBottomSpace;
	private boolean mRemoveTopSpace;
	private Drawable mDivider;
	private boolean mIsHorizontalList;
	@Px
	private int mSpace;
	@Px
	private int mLeftOffset;
	@Px
	private int mTopOffset;
	@Px
	private int mRightOffset;
	@Px
	private int mBottomOffset; 

	/**
	 *
	 * @param space
	 * @param color
	 * @param isHorizontalList
	 * @param leftOffset
	 * @param topOffset
	 * @param rightOffset
	 * @param bottomOffset
	 * @param removeTopSpace 如果有添加refreshHeader,解决recycleview顶部设置有space的问题
	 */
	public Divider(@Px int space, @ColorInt int color, boolean isHorizontalList
			, @Px int leftOffset, @Px int topOffset, @Px int rightOffset, @Px int bottomOffset, boolean removeTopSpace) {
		mSpace = space;
		mDivider = new ColorDrawable(color);
		mIsHorizontalList = isHorizontalList;
		mLeftOffset = leftOffset;
		mTopOffset = topOffset;
		mRightOffset = rightOffset;
		mBottomOffset = bottomOffset;
		mRemoveTopSpace = removeTopSpace;
	}

	/**
	 * @param space
	 * @param color
	 * @param isHorizontalList
	 * @param leftOffset
	 * @param topOffset
	 * @param rightOffset
	 * @param bottomOffset
	 * @param removeTopSpace 如果有添加refreshHeader,解决recycleview顶部设置有space的问题
	 * @param removeBottomSpace 如果有添加footerLayout,解决底部设置有space的问题

	 */
	public Divider(@Px int space, @ColorInt int color, boolean isHorizontalList
			, @Px int leftOffset, @Px int topOffset, @Px int rightOffset, @Px int bottomOffset, boolean removeTopSpace,boolean removeBottomSpace) {
		mSpace = space;
		mDivider = new ColorDrawable(color);
		mIsHorizontalList = isHorizontalList;
		mLeftOffset = leftOffset;
		mTopOffset = topOffset;
		mRightOffset = rightOffset;
		mBottomOffset = bottomOffset;
		mRemoveTopSpace = removeTopSpace;
		mRemoveBottomSpace = removeBottomSpace;
	}

	public Divider(@Px int space, @ColorInt int color, boolean isHorizontalList
			, @Px int leftOffset, @Px int topOffset, @Px int rightOffset, @Px int bottomOffset) {
		mSpace = space;
		mDivider = new ColorDrawable(color);
		mIsHorizontalList = isHorizontalList;
		mLeftOffset = leftOffset;
		mTopOffset = topOffset;
		mRightOffset = rightOffset;
		mBottomOffset = bottomOffset;
	}

	@Override
	public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
		if (mIsHorizontalList) {
			drawHorizontal(c, parent);
		} else {
			drawVertical(c, parent);
		}
	}

	private void drawVertical(Canvas c, RecyclerView parent) {
		final int left = parent.getPaddingLeft();
		final int right = parent.getWidth() - parent.getPaddingRight();
		int childCount = parent.getChildCount() - 1;
		int i = 0;
		if(mRemoveTopSpace){//第一个layout不显示divider
			int position = parent.getChildAdapterPosition(parent.getChildAt(0));
			if(position == 0) {
				i++;
			}
		}
		if(mRemoveBottomSpace) {//footerLayout的上一个layout不显示divider
			int itemCount = parent.getAdapter().getItemCount();
			int position = parent.getChildAdapterPosition(parent.getChildAt(childCount));
			if (position == itemCount - 1) {
				childCount -= 1;
			}
		}

		for (; i < childCount; i++) {
			final View child = parent.getChildAt(i);
			final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
			final int top = child.getBottom() + params.bottomMargin;
			final int bottom = top + mSpace;
//			CLog.i("Divider", "drawVertical(): top=" + top + ", mTopOffset=" + mTopOffset + ", bottom=" + bottom + ", mBottomOffset=" + mBottomOffset);
			mDivider.setBounds(left + mLeftOffset, top + mTopOffset, right + mRightOffset, bottom + mBottomOffset);
			mDivider.draw(c);
		}
	}

	private void drawHorizontal(Canvas c, RecyclerView parent) {
		final int top = parent.getPaddingTop();
		final int bottom = parent.getHeight() - parent.getPaddingBottom();
		int childCount = parent.getChildCount() - 1;
		int i = 0;
		if(mRemoveTopSpace){//第一个layout不显示divider
			int position = parent.getChildAdapterPosition(parent.getChildAt(0));
			if(position == 0) {
				i++;
			}
		}
		if(mRemoveBottomSpace) {//footerLayout的上一个layout不显示divider
			int itemCount = parent.getAdapter().getItemCount();
			int position = parent.getChildAdapterPosition(parent.getChildAt(childCount));
			if(position == itemCount-1) {
				childCount -= 1;
			}
		}
		for (; i < childCount; i++) {
			final View child = parent.getChildAt(i);
			final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
			final int left = child.getRight() + params.rightMargin;
			final int right = left + mSpace;
			mDivider.setBounds(left + mLeftOffset, top + mTopOffset, right + mRightOffset, bottom + mBottomOffset);
			mDivider.draw(c);
		}
	}

	@SuppressWarnings("SuspiciousNameCombination")
	@Override
	public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
		int itemCount = parent.getAdapter().getItemCount();
		int position = parent.getChildAdapterPosition(view);
		if (mIsHorizontalList) {
			outRect.right =  position == itemCount - 1 ? 0 : mSpace;
		} else {
			if(position == itemCount-1 //最后一个layout不显示space
					||(mRemoveTopSpace && position == 0) //去掉第一个layout的space
					||(mRemoveBottomSpace && position == itemCount-2)){ //去掉footerLayout的上一个layout的space
				outRect.bottom = 0;
			}else {
				outRect.bottom = mSpace;

			}
//			outRect.set(0, 0, 0, mSpace);
		}
	}

	public static int dp2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
}
