package com.infteh.comboseekbar;

import java.util.ArrayList;

import java.util.List;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.SeekBar;


public class ComboSeekBar extends SeekBar {
	private boolean isMandatory = false;
	private String rootSector;
	private CustomThumbDrawable mThumb;
	private int mColor;
	private List<Dot> mDots = new ArrayList<Dot>();

	private OnItemClickListener mItemClickListener;
	private Dot prevSelected = null;
	private boolean isSelected = false;
	private int mTextSize;
	private List<String> texts;
	private boolean mIsMultiline;

	public ComboSeekBar(Context context, int a){
		super(context);
	}
	public ComboSeekBar(Context context, List<String> texts) {
		super(context);
		//this.setScrollBarSize(100);

		List<String> seekBarStep = texts;
		this.texts = texts;
		int overalSize = 0;
		Paint p = new Paint();
		for (int i = 0; i < seekBarStep.size(); i++) {
			final Rect textBounds = new Rect();
			String text = seekBarStep.get(i);
//			int bounds =  text.length();
			p.getTextBounds(text, 0, text.length(), textBounds);
			overalSize = overalSize + textBounds.width()+40;
		}
		LayoutParams linLayoutParam = new LayoutParams(overalSize+72, 158);
		this.setLayoutParams(linLayoutParam);

		//mColor = a.getColor(R.styleable.ComboSeekBar_myColor, Color.WHITE);
		//mTextSize = a.getDimensionPixelSize(R.styleable.ComboSeekBar_textSize, 5);
		//mIsMultiline = a.getBoolean(R.styleable.ComboSeekBar_multiline, false);

		mThumb = new CustomThumbDrawable(context, Color.parseColor("#3498db"));
		setThumb(mThumb);
		mThumb.setVisible(false, true);
		this.mColor = Color.parseColor("#3498db");
		//setProgressDrawable(new CustomDrawable(this.getProgressDrawable(), this, 0, mDots, mColor, 20, false));



		this.setAdapter(seekBarStep);


		final SeekBar self = this;
		final List<String> finalTexts = texts;
		this.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				int total = self.getMax()/finalTexts.size();
				progress = progress / total;
				progress = progress * total;
				self.setProgress(progress);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}
		});
	}

	public String getValue(){
		int progress = 0;
		int total = getMax()/texts.size();
		progress = getProgress() / total;
		if(progress >= texts.size()){
			progress = texts.size()-1;
		}
		return texts.get(progress);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		isSelected = false;
		return super.onTouchEvent(event);
	}

	/**
	 * @param color
	 *            color.
	 */
	public void setColor(int color) {
		mColor = color;
		mThumb.setColor(color);
		//setProgressDrawable(new CustomDrawable((CustomDrawable) this.getProgressDrawable(), this, mThumb.getRadius(), mDots, color, mTextSize, mIsMultiline));
	}

	public synchronized void setSelection(int position) {
		if ((position < 0) || (position >= mDots.size())) {
			throw new IllegalArgumentException("Position is out of bounds:" + position);
		}
		for (Dot dot : mDots) {
			if (dot.id == position) {
				dot.isSelected = true;
			} else {
				dot.isSelected = false;
			}
		}

		isSelected = true;
		invalidate();
	}

	public void setAdapter(List<String> dots) {
		mDots.clear();
		int index = 0;
		for (String dotName : dots) {
			Dot dot = new Dot();
			dot.text = dotName;
			dot.id = index++;
			mDots.add(dot);
		}
		initDotsCoordinates();
	}

	@Override
	public void setThumb(Drawable thumb) {
		if (thumb instanceof CustomThumbDrawable) {
			mThumb = (CustomThumbDrawable) thumb;
		}
		super.setThumb(thumb);
	}

	@Override
	protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		CustomDrawable d = (CustomDrawable) getProgressDrawable();

		int thumbHeight = mThumb == null ? 0 : mThumb.getIntrinsicHeight();
		int dw = 0;
		int dh = 0;
		if (d != null) {
			dw = d.getIntrinsicWidth();
			dh = Math.max(thumbHeight, d.getIntrinsicHeight());
		}

		dw += getPaddingLeft() + getPaddingRight();
		dh += getPaddingTop() + getPaddingBottom();

		setMeasuredDimension(resolveSize(dw, widthMeasureSpec), resolveSize(dh, heightMeasureSpec));
	}

	/**
	 * dot coordinates.
	 */
	private void initDotsCoordinates() {
//		float thumbRadius = mThumb.getRadius()*2;
		mDots.size();
		float intervalWidth = (getWidth()) / (mDots.size()-1) -23;
		for (Dot dot : mDots) {
			if(dot.id > 0){
				dot.mX = (int) (intervalWidth * (dot.id));
			}else{
				dot.mX = (int) (intervalWidth * (dot.id));
			}
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		initDotsCoordinates();
	}


	@Override
	protected synchronized void onDraw(Canvas canvas) {
		if ((mThumb != null) && (mDots.size() > 1)) {
			if (isSelected) {
				for (Dot dot : mDots) {
					if (dot.isSelected) {
						Rect bounds = mThumb.copyBounds();
						bounds.right = dot.mX;
						bounds.left = dot.mX;
						mThumb.setBounds(bounds);
						break;
					}
				}
			} else {
				int intervalWidth = mDots.get(1).mX - mDots.get(0).mX;
				Rect bounds = mThumb.copyBounds();
				// find nearest dot
				if ((mDots.get(mDots.size() - 1).mX - bounds.centerX()) < 0) {
					bounds.right = mDots.get(mDots.size() - 1).mX;
					bounds.left = mDots.get(mDots.size() - 1).mX;
					mThumb.setBounds(bounds);

					for (Dot dot : mDots) {
						dot.isSelected = false;
					}
					mDots.get(mDots.size() - 1).isSelected = true;
					handleClick(mDots.get(mDots.size() - 1));
				} else {
					for (int i = 0; i < mDots.size(); i++) {
						if (Math.abs(mDots.get(i).mX - bounds.centerX()) <= (intervalWidth / 2)) {
							bounds.right = mDots.get(i).mX;
							bounds.left = mDots.get(i).mX;
							mThumb.setBounds(bounds);
							mDots.get(i).isSelected = true;
							handleClick(mDots.get(i));
						} else {
							mDots.get(i).isSelected = false;
						}
					}
				}
			}
		}
		super.onDraw(canvas);
	}

	private void handleClick(Dot selected) {
		if ((prevSelected == null) || (prevSelected.equals(selected) == false)) {
			if (mItemClickListener != null) {
				mItemClickListener.onItemClick(null, this, selected.id, selected.id);
			}
			prevSelected = selected;
		}
	}


	public static class Dot {
		public int id;
		public int mX;
		public String text;
		public boolean isSelected = false;

		@Override
		public boolean equals(Object o) {
			return ((Dot) o).id == id;
		}
	}


}
