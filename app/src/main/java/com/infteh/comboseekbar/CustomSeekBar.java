/**
 * Created by chico on 6/16/15.
 */
package com.infteh.comboseekbar;

import java.util.ArrayList;

import java.util.List;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SeekBar;


public class CustomSeekBar extends SeekBar {

    private CustomThumbDrawable mThumb;
    private int mColor;
    private List<Dot> mDots = new ArrayList<Dot>();

    private OnItemClickListener mItemClickListener;
    private Dot prevSelected = null;
    private boolean isSelected = false;
    private int mTextSize;
    private List<String> texts;
    private boolean mIsMultiline;

    public CustomSeekBar(Context context, List<String> texts) {
        super(context);

        mThumb = new CustomThumbDrawable(context, Color.parseColor("#3498db"));
        setThumb(mThumb);
        mThumb.setVisible(true, true);
        this.mColor = Color.parseColor("#3498db");
    }

    public CustomSeekBar(Context context)
    {
        super(context);

        mThumb = new CustomThumbDrawable(context, Color.parseColor("#3498db"));
        setThumb(mThumb);
        mThumb.setVisible(true, true);
        this.mColor = Color.parseColor("#3498db");

    }
    public CustomSeekBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        mThumb = new CustomThumbDrawable(context, Color.parseColor("#3498db"));
        setThumb(mThumb);
        mThumb.setVisible(true, true);
        this.mColor = Color.parseColor("#3498db");
    }

    public CustomSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mThumb = new CustomThumbDrawable(context, Color.parseColor("#3498db"));
        setThumb(mThumb);
        mThumb.setVisible(true, true);
        this.mColor = Color.parseColor("#3498db");
    }

    public void init(){

        super.invalidate();
        List<String> seekBarStep = this.texts;
        int overalSize = 0;
        Paint p = new Paint();
        for (int i = 0; i < seekBarStep.size(); i++) {
            final Rect textBounds = new Rect();
            String text = seekBarStep.get(i);

            p.getTextBounds(text, 0, text.length(), textBounds);
            overalSize = overalSize + textBounds.width();
        }

        CustomDrawable ct = new CustomDrawable(this.getProgressDrawable(), this, 0, mDots, mColor, 20, false);
        setProgressDrawable(ct);

    }

    public String getValue(){
        int position = this.getNormalizedPosition(getProgress());
        if(position>= texts.size()){
            position = position-1;
        }
        return texts.get(position);
    }

    public int getNormalizedPosition(int progress){
        int total = getMax()/texts.size();
        progress = progress / total;

        if(progress >= texts.size()){
            progress = texts.size();
        }
        return progress;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        isSelected = false;

        // normalization of the value to always stay in the middle of the intervals
        int amountTotal = texts.size();
        int progress = this.getProgress();
        if(progress>0){
            int realProgress = progress;
            int slots = this.getMax()/amountTotal;
            float bar = ((float)progress/(float)slots);
            float bar_int = (float) Math.floor(bar);

            realProgress = ((int)bar_int+1)*(slots);

            this.setSelection(getNormalizedPosition(realProgress));
        }else{
            this.setSelection(0);
        }

        return super.onTouchEvent(event);
    }


    /**
     * @param color
     *            color.
     */
    public void setColor(int color) {
        mColor = color;
        mThumb.setColor(color);
        setProgressDrawable(new CustomDrawable((CustomDrawable) this.getProgressDrawable(), this, mThumb.getRadius(), mDots, color, mTextSize, mIsMultiline));
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
        this.texts = dots;
        mDots.clear();
        int index = 0;

        Dot first = new Dot();
        first.text = "";
        first.id = index++;
        mDots.add(first);

        for (String dotName : dots) {
            Dot dot = new Dot();
            dot.text = dotName;
            dot.id = index++;
            mDots.add(dot);
        }
        initDotsCoordinates();
        init();
    }

    @Override
    public void setThumb(Drawable thumb) {
        if (thumb instanceof CustomThumbDrawable) {
            mThumb = (CustomThumbDrawable) thumb;
        }
        super.setThumb(thumb);
    }

    /**
     * dot coordinates.
     */

    private void initDotsCoordinates() {

        float intervalWidth = (this.getProgressDrawable().getBounds().width()) / (mDots.size()-1);
        for (Dot dot : mDots) {
            dot.mX = (int) (intervalWidth * (dot.id));
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initDotsCoordinates();
    }


    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.invalidate();
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