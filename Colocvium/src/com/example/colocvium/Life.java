package com.example.colocvium;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Arrays;

/**
 * User: svasilinets Date: 31.10.12 Time: 15:21
 */
public class Life extends SurfaceView implements Runnable {
	volatile boolean running = false;
	Thread thread;

	SurfaceHolder holder;
	// 0 - black, 1 - white
	int[][] z, znew;
	int[] colorBitmap;
	static boolean wasInit = false, initBitmap = false;
	volatile static int n = 0;
	Bitmap bitmap;
	int width, height;
	static final int FRAMES_PER_COLOR = 16;

	public Life(Context context, AttributeSet attrs) {
		super(context, attrs);
		holder = getHolder();
		initConstructor();
	}

	public Life(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		holder = getHolder();
		initConstructor();
	}

	public Life(Context context) {
		super(context);
		holder = getHolder();
		initConstructor();

	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		if (initBitmap) {
			return;
		} else {
			initBitmap = true;
		}
		this.width = w - w % 30;
		this.height = h - h % 30;

		bitmap = Bitmap.createBitmap(this.width, this.height,
				Bitmap.Config.RGB_565);
		bitmap.eraseColor(Color.BLACK);

		colorBitmap = new int[this.width * this.height];
		Arrays.fill(colorBitmap, Color.BLACK);

		z = new int[this.height / 30][this.width / 30];
		znew = new int[this.height / 30][this.width / 30];

		z[10][10] = 1;
		z[10][11] = 1;
		z[10][12] = 1;
		z[11][12] = 1;
		z[12][11] = 1;
		thread.start();
	}

	private void initConstructor() {

	}

	Integer curx = null, cury = null;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			int x = (int) event.getX();
			int y = (int) event.getY();
			x /= 30;
			y /= 30;
			fillByCoordinate(y, x, color(1 - z[y][x]));
			z[y][x] = 1 - z[y][x];
			bitmap.setPixels(colorBitmap, 0, this.width, 0, 0, this.width,
					this.height);
			Canvas canvas = holder.lockCanvas();
			onDraw(canvas);
			holder.unlockCanvasAndPost(canvas);
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
		if (initBitmap) {
			thread.start();
		}
	}

	public void pause() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException ignore) {
		}
	}

	void initField() {
		if (!wasInit) {
			wasInit = true;
		} else {
			return;
		}
		n = 0;
		running = true;
		bitmap.eraseColor(Color.BLACK);
		for (int i = 0; i < this.height / 30; i++) {
			for (int j = 0; j < this.width / 30; j++) {
				fillByCoordinate(i, j, color(z[i][j]));
			}
		}
		bitmap.setPixels(colorBitmap, 0, this.width, 0, 0, this.width,
				this.height);
	}

	private void fillByCoordinate(int x, int y, int color) {
		for (int i = x * 30; i < x * 30 + 30; i++) {
			for (int j = y * 30; j < y * 30 + 30; j++) {
				colorBitmap[i * this.width + j] = color;
			}
		}
	}

	void updateField() {

		for (int i = 0; i < this.height / 30; i++) {
			for (int j = 0; j < this.width / 30; j++) {
				int countAlive = 0;
				for (int ii = -1; ii <= 1; ii++) {
					for (int jj = -1; jj <= 1; jj++) {
						if (ii != 0 || jj != 0) {
							if (z[(i + ii + (this.height / 30))
									% (this.height / 30)][(j + jj + (this.width / 30))
									% (this.width / 30)] == 1) {
								countAlive++;
							}
						}
					}
				}
				if (countAlive < 2 || countAlive > 3) {
					znew[i][j] = 0;
				} else if (countAlive == 3) {
					znew[i][j] = 1;
				} else if (countAlive == 2) {
					znew[i][j] = z[i][j];
				}
			}
		}
		for (int i = 0; i < this.height / 30; i++) {
			for (int j = 0; j < this.width / 30; j++) {
				z[i][j] = znew[i][j];
			}
		}

		for (int i = 0; i < this.height / 30; i++) {
			for (int j = 0; j < this.width / 30; j++) {
				fillByCoordinate(i, j, color(z[i][j]));
			}
		}
		bitmap.setPixels(colorBitmap, 0, this.width, 0, 0, this.width,
				this.height);
	}

	private static int color(int n) {
		if (n == 0) {
			return Color.BLACK;
		} else {
			return Color.WHITE;
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(bitmap, 0, 0, null);
	}
}
