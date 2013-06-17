package com.ifmo.colloc;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Arrays;

/**
 * User: svasilinets Date: 31.10.12 Time: 15:21
 */
public class Mandelbrot extends SurfaceView implements Runnable {
	volatile boolean running = false;
	Thread thread;
	SurfaceHolder holder;
	Complex[][] z, start;
	int[] colorBitmap;
	volatile static int n = 0;
	Bitmap bitmap;
	int width, height;
	static final int FRAMES_PER_COLOR = 16;

	private class Complex {
		double x, y;

		Complex(double x, double y) {
			this.x = x;
			this.y = y;
		}

		double value() {
			return Math.sqrt(x * x + y * y);
		}

		void makeSqr() {
			double tmpx = this.x, tmpy = this.y;
			this.x = this.x * this.x - this.y * this.y;
			this.y = 2 * tmpx * tmpy;
		}

		void add(Complex c) {
			this.x += c.x;
			this.y += c.y;
		}

	}

	public Mandelbrot(Context context, Point p) {
		super(context);
		holder = getHolder();
		// First Fix: move 2/3 to height
		this.width = p.x;
		this.height = p.x * 2 / 3;

		bitmap = Bitmap.createBitmap(this.width, this.height,
				Bitmap.Config.RGB_565);
		bitmap.eraseColor(Color.BLACK);

		colorBitmap = new int[this.width * this.height];
		Arrays.fill(colorBitmap, Color.BLACK);

		z = new Complex[this.height][this.width];
		for (int i = 0; i < this.height; i++) {
			for (int j = 0; j < this.width; j++) {
				z[i][j] = new Complex(0, 0);
			}
		}

		start = new Complex[this.height][this.width];
		for (int i = 0; i < this.height; i++) {
			for (int j = 0; j < this.width; j++) {
				start[i][j] = new Complex((j - this.width * 2 / 3)
						/ (this.width / 3 + 0.0), (-i + this.height / 2 + 0.0)
						/ (this.height / 2 + 0.0));
			}
		}
		int k = 0;
	}

	Integer curx = null, cury = null;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			int x = (int) event.getX();
			int y = (int) event.getY();
			if (curx == null) {
				curx = x;
				cury = y;
			} else {
				pause();

				Point p1 = new Point(Math.min(x, curx), Math.min(y, cury));
				Point p2 = new Point(Math.max(x, curx), Math.max(y, cury));

				// Second Fix: dy *= 2, dx *= 3;
				double dy = (p2.y - p1.y + 0.0) / (this.height + 0.0) * 2
						/ this.height, dx = (p2.x - p1.x + 0.0)
						/ (0.0 + this.width) * 3 / this.width;
				double curx = (p1.x - this.width * 2 / 3)
						/ (this.width / 3 + 0.0), cury = (-p1.y + this.height
						/ 2 + 0.0)
						/ (this.height / 2 + 0.0);

				System.out.println("1a" + dx + " " + dy + " " + curx);
				for (int i = 0; i < this.height; i++) {
					for (int j = 0; j < this.width; j++) {
						start[i][j] = new Complex(curx, cury);
						curx += dx;
					}
					cury -= dy;
					curx = (p1.x - this.width * 2 / 3) / (this.width / 3 + 0.0);
				}
				System.out.println("1a" + start[0][0].x + " " + start[0][0].y);
				resume();
				System.out.println("1a resumed");
				this.curx = this.cury = null;
			}
			return true;
		}
		return false;
	}

	public void run() {
		initField();

		while (running) {
			if (holder.getSurface().isValid()) {
				updateField();
				Canvas canvas = holder.lockCanvas();
				onDraw(canvas);
				holder.unlockCanvasAndPost(canvas);

			}
			// System.out.println("runnibg " + );
			try {
				Thread.sleep(16);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	public void resume() {
		running = true;

		thread = new Thread(this);
		thread.start();
	}

	public void pause() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException ignore) {
		}
	}

	void initField() {

		n = 0;
		running = true;
		bitmap.eraseColor(Color.BLACK);
		for (int i = 0; i < this.height; i++) {
			for (int j = 0; j < this.width; j++) {
				z[i][j] = new Complex(0, 0);
			}
		}
		Arrays.fill(colorBitmap, Color.BLACK);
	}

	void updateField() {

		n++;
		int colorN = color(n);

		for (int i = 0; i < this.height; i++) {
			for (int j = 0; j < this.width; j++) {
				if (z[i][j].value() > 2) {
					continue;
				}
				z[i][j].makeSqr();
				z[i][j].add(start[i][j]);
				if (z[i][j].value() >= 2) {
					colorBitmap[j + i * this.width] = colorN;
				}
			}
		}
		bitmap.setPixels(colorBitmap, 0, this.width, 0, 0, this.width,
				this.height);
	}

	private static int color(int n) {
		int d = n / FRAMES_PER_COLOR;
		int color = (n % FRAMES_PER_COLOR) * (256 / FRAMES_PER_COLOR);
		switch (d) {
		case 0:
			return Color.rgb(color, 0, 0);
		case 1:
			return Color.rgb(255, color, 0);
		case 2:
			return Color.rgb(255, 255, color);
		default:
			return Color.rgb(255, 255, 255);

		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(bitmap, 0, 0, null);
	}
}
