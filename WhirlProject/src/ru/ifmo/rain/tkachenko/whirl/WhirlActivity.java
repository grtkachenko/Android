package ru.ifmo.rain.tkachenko.whirl;

import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

class WhirlView extends SurfaceView implements Runnable {
	static boolean allUsed = false, isCalc = false, was = false;
	volatile boolean running = false, runningLeft = false,
			runningRight = false;
	static int[][][] field = null;
	static int curNumLoop = 0, width = 0, height = 0, scale = 4, timer = 0;

	static final int MAX_COLOR = 10;
	static final int[] palette = { 0xFFFF0000, 0xFF800000, 0xFF808000,
			0xFF008000, 0xFF00FF00, 0xFF008080, 0xFF0000FF, 0xFF000080,
			0xFF800080, 0xFFFFFFFF, 0xFCFF0000, 0xFC800000, 0xFF811100,
			0xFF232000, 0xFF00FFDD, 0xFF003100, 0xF3108080, 0xFF3100FF,
			0xFF000310, 0x31800080, 0xFF31FFFF, 0xF31F0000, 0xFC831000 };

	static Bitmap bitmap;
	static SurfaceHolder holder;
	static Thread thread = null, calcLeftThread = null, calcRightThread = null;
	volatile static int[] colorsBitmap;

	public WhirlView(Context context) {
		super(context);
		holder = getHolder();
	}

	public void resume() {
		initField();
		timer = 0;
		allUsed = false;
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

	public void run() {
		calcLeftThread = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (true) {
					while (runningLeft) {
						final int index1 = 1 - timer & 1, index2 = timer & 1;
						for (int x = width / 2; x < width; x++) {
							for (int y = 0; y < height; y++) {
								was = false;
								field[index1][x][y] = field[index2][x][y];

								loop: for (int dx = -1; dx <= 1; dx++) {
									for (int dy = -1; dy <= 1; dy++) {
										int x2 = x + dx;
										int y2 = y + dy;
										if (x2 < 0)
											x2 += width;
										if (y2 < 0)
											y2 += height;
										if (x2 >= width)
											x2 -= width;
										if (y2 >= height)
											y2 -= height;
										if ((field[index2][x][y] + 1)
												% MAX_COLOR == field[index2][x2][y2]) {
											field[index1][x][y] = field[index2][x2][y2];
											was = true;
											break loop;
										}
									}
								}
								if (!was) {
									continue;
								}
								final int tmpConst = width * scale * y * scale
										+ x * scale, color = palette[field[timer & 1][x][y]];
								for (int j = 0; j < scale; j++) {
									for (int i = 0; i < scale; i++) {
										colorsBitmap[tmpConst + i + j * scale
												* width] = color;
									}
								}
							}
						}
						runningLeft = false;
					}

					try {
						Thread.sleep(16);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		calcRightThread = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (true) {
					while (runningRight) {
						final int index1 = 1 - timer & 1, index2 = timer & 1;
						for (int x = 0; x < width / 2; x++) {
							for (int y = 0; y < height; y++) {
								was = false;
								field[index1][x][y] = field[index2][x][y];

								loop: for (int dx = -1; dx <= 1; dx++) {
									for (int dy = -1; dy <= 1; dy++) {
										int x2 = x + dx;
										int y2 = y + dy;
										if (x2 < 0)
											x2 += width;
										if (y2 < 0)
											y2 += height;
										if (x2 >= width)
											x2 -= width;
										if (y2 >= height)
											y2 -= height;
										if ((field[index2][x][y] + 1)
												% MAX_COLOR == field[index2][x2][y2]) {
											field[index1][x][y] = field[index2][x2][y2];
											was = true;
											break loop;
										}
									}
								}
								if (!was) {
									continue;
								}
								final int tmpConst = width * scale * y * scale
										+ x * scale, color = palette[field[timer & 1][x][y]];
								for (int j = 0; j < scale; j++) {
									for (int i = 0; i < scale; i++) {
										colorsBitmap[tmpConst + i + j * scale
												* width] = color;
									}
								}

							}
						}
						runningRight = false;
						try {
							Thread.sleep(16);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}

			}
		});
		calcRightThread.start();
		calcLeftThread.start();
		while (running) {
			if (holder.getSurface().isValid()) {
				long startTime = System.nanoTime();
				Canvas canvas = holder.lockCanvas();
				updateField();
				while (runningRight || runningLeft) {
					Thread.yield();

				}
				;
				onDraw(canvas);
				holder.unlockCanvasAndPost(canvas);
				long finishTime = System.nanoTime();
				Log.i("TIME", "Circle: " + ((finishTime - startTime) / 1000000)
						+ " allUsed: " + allUsed);
				try {
					Thread.sleep(16);
				} catch (InterruptedException ignore) {
				}
			}
		}
	}

	@Override
	public void onSizeChanged(int w, int h, int oldW, int oldH) {
		width = w / scale;
		height = h / scale;
		colorsBitmap = new int[width * height * scale * scale];
		field = new int[2][width][height];
		bitmap = Bitmap.createBitmap(width * scale, height * scale,
				Bitmap.Config.ARGB_8888);
		initField();
	}

	void initField() {

		Random rand = new Random();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				field[0][x][y] = rand.nextInt(MAX_COLOR);
				for (int j = 0; j < scale; j++) {
					for (int i = 0; i < scale; i++) {
						colorsBitmap[(y * scale + j) * width * scale
								+ (x * scale + i)] = palette[field[0][x][y]];
					}
				}
			}
		}

	}

	void updateField() {
		runningLeft = runningRight = true;
		timer++;

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			pause();
			resume();
		}
		return true;
	}

	@Override
	public void onDraw(Canvas canvas) {
		bitmap.setPixels(colorsBitmap, 0, width * scale, 0, 0, width * scale,
				height * scale);
		canvas.drawBitmap(bitmap, 0, 0, null);
	}
}

public class WhirlActivity extends Activity {
	WhirlView whirlView;

	@Override
	public void onResume() {
		super.onResume();
		whirlView.resume();
	}

	@Override
	public void onPause() {
		super.onPause();
		whirlView.pause();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		whirlView = new WhirlView(this);
		setContentView(whirlView);

		// setContentView(R.layout.activity_whirl);
	}

}