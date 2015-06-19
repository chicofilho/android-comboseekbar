package com.infteh.comboseekbar;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import com.infteh.comboseekbar.CustomSeekBar.Dot;


/**
 * seekbar background with text on it.
 *
 * @author Francisco Pimentel
 *
 */
public class CustomDrawable extends Drawable {
	private final CustomSeekBar mySlider;
	private final Drawable myBase;
	private final Paint textUnselected;
	private float mThumbRadius;
	/**
	 * paints.
	 */
	private final Paint unselectLinePaint;
	private List<Dot> mDots;
	private Paint selectLinePaint;
	private Paint circleLinePaint;
	private float mDotRadius;
	private Paint textSelected;
	private int mTextSize;
	private float mTextMargin;
	private int mTextHeight;
	private boolean mHasCancel;

	public CustomDrawable(Drawable base, CustomSeekBar slider, float thumbRadius, List<Dot> dots, int color, int textSize, boolean hasCancel) {
		mHasCancel = hasCancel;
		mySlider = slider;
		myBase = base;
		mDots = dots;
		mTextSize = textSize;
		textUnselected = new Paint(Paint.ANTI_ALIAS_FLAG);
		textUnselected.setColor(color);
		textUnselected.setAlpha(255);

		textSelected = new Paint(Paint.ANTI_ALIAS_FLAG);
		textSelected.setTypeface(Typeface.DEFAULT_BOLD);
		textSelected.setColor(Color.WHITE);
		textSelected.setAlpha(255);

		mThumbRadius = 56;//thumbRadius;

		unselectLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		unselectLinePaint.setColor(color);

		unselectLinePaint.setStrokeWidth(toPix(1));

		selectLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		selectLinePaint.setColor(color);
		selectLinePaint.setStrokeWidth(toPix(3));

		circleLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		circleLinePaint.setColor(color);

		Rect textBounds = new Rect();
		textSelected.setTextSize((int) (mTextSize * 2));
		textSelected.getTextBounds("M", 0, 1, textBounds);

		textUnselected.setTextSize(mTextSize);
		textSelected.setTextSize(mTextSize);

		mTextHeight = textBounds.height();
		mDotRadius = toPix(5);
		mTextMargin = toPix(3);
	}

	private float toPix(int size) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, mySlider.getContext().getResources().getDisplayMetrics());
	}

	@Override
	protected final void onBoundsChange(Rect bounds) {
		myBase.setBounds(bounds);
	}

	@Override
	protected final boolean onStateChange(int[] state) {
		invalidateSelf();
		return false;
	}

	@Override
	public final boolean isStateful() {
		return true;
	}

	@Override
	public final void draw(Canvas canvas) {
		// Log.d("--- draw:" + (getBounds().right - getBounds().left));
		int height = this.getIntrinsicHeight()/ 2;
		if (mDots.size() == 0) {
			canvas.drawLine(0, height, getBounds().right, height, unselectLinePaint);
			return;
		}
		for (Dot dot : mDots) {

			int pos = mDots.indexOf(dot);
			int round = 15;
//			int semiRound = round/2;
			int borderConfig = 0;;
			if(mDots.indexOf(dot)==1)
				borderConfig = 1;
			else if(mDots.indexOf(dot)==mDots.size()-1){
				borderConfig = 2;
			}
			if (dot.isSelected) {
				//===================
				//System.out.println("-------------->"+dot.id);
				Paint pa = new Paint(Paint.ANTI_ALIAS_FLAG);
				pa.setStyle(Style.FILL);
				pa.setColor(Color.parseColor("#3498db"));

				if(pos>0){
//					RectF rect = new RectF(mDots.get(pos-1).mX, 0, dot.mX-1, height);
//					canvas.drawRoundRect(rect, 6, 6, pa);
					drawRoundedPath(canvas, dot, round, height, borderConfig, pa);
					//drawRoundedRectangle(canvas, dot, round, height, borderConfig, pa);

				}
				if(mHasCancel){
					Paint p3 = new Paint(Paint.ANTI_ALIAS_FLAG);
					p3.setStyle(Style.STROKE);
					p3.setStrokeWidth(2);
					p3.setColor(Color.parseColor("#bbbbbb"));
					canvas.drawCircle(8, height/2, 15, p3);

					int x1 = 0;
					int y1 = height/3-1;
					int x2 = x1+16;
					int y2 = y1+16;
					canvas.drawLine(x1, y1, x2, y2, p3);
					canvas.drawLine(x1, y2, x2, y1, p3);
				}



			}else{
				Paint p2 = new Paint(Paint.ANTI_ALIAS_FLAG);
				p2.setStyle(Style.STROKE);
				p2.setStrokeWidth(2);
				p2.setColor(Color.parseColor("#3498db"));

				pos = mDots.indexOf(dot);
				if(pos>0){
					drawRoundedRectangle(canvas, dot, round, height, borderConfig, p2);
				}
			}
			if(pos>0){
				drawText(canvas, dot, mDots.get(pos-1).mX, height);
			}
			//canvas.drawCircle(dot.mX, height, mDotRadius, circleLinePaint);
		}
	}

	private void drawRoundedPath(Canvas canvas, Dot dot, int round, int height,
								 int borderConfig, Paint pa) {

		int semiRound = round/2;
		int pos = mDots.indexOf(dot);

		int offsetLeft = semiRound-1;
		int offsetRight = semiRound-1;
		if(borderConfig ==0){
			offsetLeft = 0;
			offsetRight = 0;
		}else if(borderConfig==1){
			offsetRight = 0;
		}else if(borderConfig==2){
			offsetLeft = 0;
		}

		int x1 = mDots.get(pos-1).mX+this.getXWidth();
		int x2 = dot.mX+this.getXWidth();

		Path p = new Path();

		p.lineTo(x2-offsetLeft, 0);
		p.lineTo(x1+offsetLeft, 0);
		p.lineTo(x1, 0+offsetLeft);
		p.lineTo(x1, height-offsetLeft);
		p.lineTo(x1+offsetLeft, height+1);
		p.lineTo(x2-offsetRight, height+1);
		p.lineTo(x2-1, height-offsetRight);
		p.lineTo(x2-1, 0+offsetRight);
		p.lineTo(x2-offsetRight, 0);
		p.close();
		canvas.drawPath(p, pa);

		final RectF oval = new RectF();
		//borders:
		//border leftTop
		oval.set(x1, 0, x1+round, 0+round);
		canvas.drawArc(oval, 180, 90, false, pa);
		//borderLeftBottom
		oval.set(x1, height-round, x1+round, height+1);
		canvas.drawArc(oval, 90, 90, false, pa);
		//border RighBottom
		oval.set(x2-round, height-round, x2-1, height+1);
		canvas.drawArc(oval, 1, 90, false, pa);
		//border rightTop
		oval.set(x2-round, 0, x2-1, 0+round);
		canvas.drawArc(oval, 270, 90, false, pa);
	}

	private void drawRoundedRectangle(Canvas canvas, Dot dot, int round, int height, int borderConfig, Paint pa){

		int semiRound = round/2;
		int pos = mDots.indexOf(dot);

		final RectF oval = new RectF();

		int xLastDot = mDots.get(pos-1).mX+this.getXWidth();
		if(dot.id>1){
			xLastDot = xLastDot-1; //o valor para apenas uma linha ser colocada entre o elemento de boolean eh definido aqui, na m�o
		}

		int x2 = dot.mX+this.getXWidth();

		int offsetLeft = semiRound;
		int offsetRight = semiRound;

		if(borderConfig ==0){
			offsetLeft = 0;
			offsetRight = 0;
		}else if(borderConfig==1){
			offsetRight = 0;
		}else if(borderConfig==2){
			offsetLeft = 0;
		}


		//line top
		canvas.drawLine(xLastDot+offsetLeft, 1, x2-offsetRight-1, 1, pa);

		if(borderConfig==1 || borderConfig>2){
			//border leftTop
			oval.set(xLastDot, 1, xLastDot+round, 0+round);
			canvas.drawArc(oval, 180, 90, false, pa);
			//borderLeftBottom
			oval.set(xLastDot, height-round, xLastDot+round, height);
			canvas.drawArc(oval, 90, 90, false, pa);
		}

		//line left
		canvas.drawLine(xLastDot, 1+offsetLeft, xLastDot, height-offsetLeft, pa);

		//line bottom
		canvas.drawLine(xLastDot+offsetLeft, height, x2-offsetRight, height, pa);
		if(borderConfig==2 || borderConfig>2){
			//border RighBottom
			oval.set(x2-round, height-round, x2-1, height);
			canvas.drawArc(oval, 1, 90, false, pa);
			//border rightTop
			oval.set(x2-round, 1, x2-1, 0+round);
			canvas.drawArc(oval, 270, 90, false, pa);
		}


		//line right
		canvas.drawLine(x2-1, 0+offsetRight, x2-1, height-offsetRight, pa);


	}

	/**
	 * @param canvas
	 *            canvas.
	 * @param dot
	 *            current dot.
	 * @param x
	 *            x cor.
	 * @param y
	 *            y cor.
	 */
	private void drawText(Canvas canvas, Dot dot, float x, float y) {
		final Rect textBounds = new Rect();
		textSelected.getTextBounds(dot.text, 0, dot.text.length(), textBounds);
		float xres;
//		if (dot.id == (mDots.size() - 1) && false) {
//			//xres = getBounds().width() - textBounds.width();
//		} else 
		if (dot.id == 0) {
			xres = 0;
		} else {

			int pos = mDots.indexOf(dot);
			int x1 = mDots.get(pos-1).mX;
			int x2 = dot.mX;
			float size = x2-x1;//getBounds().width()/(mDots.size());
			int offsetText = (int) Math.ceil((size-textBounds.width())/2);
			xres = x1 + this.getXWidth() + offsetText;//-textBounds.width();
		}
		mTextMargin = 0;
		float yres;
//		if (mIsMultiline) {
//			if ((dot.id % 2) == 0) {
//				yres = y - mTextMargin - mDotRadius;
//			} else {
//				yres = y + mTextHeight;
//			}
//		} else {
			//float offsetY =  (float) (Math.ceil((y)/2) + textBounds.height());
			yres = y/2+(mDotRadius*3)/2;// y - (mDotRadius * 2) + mTextMargin;
//		}

		if (dot.isSelected) {
			canvas.drawText(dot.text, xres, yres, textSelected);
		} else {
			canvas.drawText(dot.text, xres, yres, textUnselected);
		}
	}

	@Override
	public final int getIntrinsicHeight() {
//		if (mIsMultiline) {
//			return (int) (selectLinePaint.getStrokeWidth() + mDotRadius + (mTextHeight) * 2  + mTextMargin);
//		} else {
			return (int) (mThumbRadius + mTextMargin + mTextHeight + mDotRadius);
//		}
	}

	@Override
	public final int getOpacity() {
		return PixelFormat.TRANSLUCENT;
	}

	@Override
	public void setAlpha(int alpha) {
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
	}

	public int getXWidth(){
		return 0;
	}
	public int getRectangleWidth(){
		return 0;
	}
}
