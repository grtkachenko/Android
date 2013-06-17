package ru.ifmo.rain.tkachenko.whirl;

import android.content.Context;

import android.graphics.Canvas;

import android.graphics.Color;

import android.graphics.Paint;

import android.util.AttributeSet;

import android.view.View;

public class CustomView extends View {

	final int MIN_WIDTH = 200;

	final int MIN_HEIGHT = 50;

	final int DEFAULT_COLOR = Color.WHITE;
	Paint paint = new Paint();
	int _color;

	final int STROKE_WIDTH = 2;
	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
	}
	public CustomView(Context context) {

		super(context);

		// TODO Auto-generated constructor stub

		init();

	}

	public CustomView(Context context, AttributeSet attrs) {

		super(context, attrs);

		// TODO Auto-generated constructor stub

		init();

	}

	public CustomView(Context context, AttributeSet attrs, int defStyle) {

		super(context, attrs, defStyle);

		// TODO Auto-generated constructor stub

		init();

	}

	private void init() {

		setMinimumWidth(MIN_WIDTH);

		setMinimumHeight(MIN_HEIGHT);

		_color = DEFAULT_COLOR;

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		// TODO Auto-generated method stub

		super.onMeasure(getSuggestedMinimumWidth(), getSuggestedMinimumHeight());

	}

	@Override
	protected void onDraw(Canvas canvas) {

		// TODO Auto-generated method stub

		paint.setColor(_color);

		paint.setStrokeWidth(STROKE_WIDTH);

		canvas.drawRect(5, 5, getWidth() - 5, getHeight() - 5, paint);

	}

	public void setColor(int color) {

		_color = color;

	}

}
