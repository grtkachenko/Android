package ru.ifmo.rain.tkachenko.picdecrease;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class Life extends SurfaceView implements Runnable {
	static SurfaceHolder holder;
	static Bitmap bitmapFirst, smallBitmap, fastPic = null, qualPic = null;
	static Thread thread;
	static Canvas canvas;
	final int FINAL_WIDTH = 434, FINAL_HEIGHT = 405;
	static int[] pixels, newPixels, decodePixels;
	static int width, height, widthFirst, heightFirst;
	volatile boolean needDraw = false, running = false;
	static final double PIXEL_KOEF = 1.73;
	static int pictureState = -1; // 0 - full, 1 - Fast, 2- Good Quality

	public Life(Context context) {
		super(context);
		holder = getHolder();
	}

	public Life(Context context, AttributeSet attrs) {
		super(context, attrs);
		holder = getHolder();
	}

	public Life(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		holder = getHolder();
	}

	public void resume() {
		if (pictureState == -1) {
			pictureState = 0;
			bitmapFirst = BitmapFactory.decodeResource(getResources(),
					R.drawable.source);
			width = widthFirst = bitmapFirst.getWidth();
			height = heightFirst = bitmapFirst.getHeight();
			pixels = new int[width * height];
			newPixels = new int[width * height];
			decodePixels = new int[FINAL_WIDTH * FINAL_HEIGHT];
		}
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

	@Override
	public void onSizeChanged(int w, int h, int oldW, int oldH) {
	}

	private void draw() {
		while (!holder.getSurface().isValid()) {
			try {
				Thread.sleep(16);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		Canvas canvas = holder.lockCanvas();
		canvas.drawColor(Color.BLACK);
		switch (pictureState) {
		case 0:
			canvas.drawBitmap(bitmapFirst, 0, 0, null);
			break;
		case 1:
			canvas.drawBitmap(fastPic, 0, 0, null);
			break;
		case 2:
			canvas.drawBitmap(qualPic, 0, 0, null);
			break;

		}
		holder.unlockCanvasAndPost(canvas);
	}

	public void drawPicture() {
		needDraw = true;
	}

	public void run() {
		while (running) {
			// TODO Auto-generated method stub
			if (needDraw) {
				draw();
				needDraw = false;
			}
			Thread.yield();
		}
	}

	Random random = new Random();;

	private int[] getFunction(Bitmap bitmap) {
		int width = bitmap.getWidth(), height = bitmap.getHeight();
		bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
		int curx, cury;
		int[] ans = new int[width * height];
		for (int i = 0; i < pixels.length; i++) {
			int x = i % width;
			int sum = 0, num = 0;
			int y = i / width;

			for (int dx = -1; dx < 2; dx++) {
				for (int dy = -1; dy < 2; dy++) {
					curx = x + dx;
					cury = y + dy;
					if (curx < 0 || curx >= width || cury < 0 || cury >= height) {
						continue;
					}
					final int tmp = getDist(pixels[i], pixels[cury * width
							+ curx]);
					sum += tmp;
					num++;
					if (tmp > sum) {
						sum = tmp;
					}
				}
			}
			ans[i] = sum / num;
		}
		return ans;
	}

	static int R1, G1, B1, R2, G2, B2;

	private int getDist(int x, int y) {
		R2 = (x >> 16) & 0xff;
		G2 = (x >> 8) & 0xff;
		B2 = x & 0xff;

		R1 = (y >> 16) & 0xff;
		G1 = (y >> 8) & 0xff;
		B1 = y & 0xff;

		// int ans = (int) Math.round(Math.sqrt((R2 - R1) * (R2 - R1) + (G2 -
		// G1)
		// * (G2 - G1) + (B1 - B2) * (B1 - B2)));
		int ans = Math.abs(R2 - R1) + Math.abs(G2 - G1) + Math.abs(B2 - B1);
		return ans;
	}

	private void rotateFast() {
		if (fastPic == null) {
			int width = widthFirst, height = heightFirst;
			fastPic = Bitmap.createBitmap(FINAL_WIDTH, FINAL_HEIGHT,
					Bitmap.Config.ARGB_8888);
			bitmapFirst.getPixels(pixels, 0, width, 0, 0, width, height);
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					newPixels[i * height + height - 1 - j] = pixels[i + j
							* width];
				}
			}
			pixels = newPixels;
			int tmp = width;
			width = height;
			height = tmp;
			int[] row = new int[width];
			int numRow = 0;
			double curSum = 0;
			while (curSum < height) {
				int i = (int) Math.round(curSum);
				curSum += PIXEL_KOEF;
				double cur = 0;
				int num = 0, index = 0;
				for (int j = 0; j < width; j++) {
					row[num++] = pixels[i * width + j];
				}
				num = 0;
				while (cur < width) {
					index = (int) Math.round(cur);
					decodePixels[numRow * FINAL_WIDTH + num] = row[index];
					num++;
					cur += PIXEL_KOEF;
				}
				numRow++;
			}
			brightUp(decodePixels);
			fastPic.setPixels(decodePixels, 0, FINAL_WIDTH, 0, 0, FINAL_WIDTH,
					FINAL_HEIGHT);

		}
		drawPicture();
		// drawX2(decodePixels);
	}

	void drawX2(int[] arr) {
		int scale = 4;
		int[] pixels = new int[FINAL_HEIGHT * FINAL_WIDTH * scale * scale];
		int num = 0;
		for (int i = 0; i < FINAL_HEIGHT; i++) {
			for (int j = 0; j < FINAL_WIDTH; j++) {
				for (int kk = 0; kk < scale; kk++) {
					pixels[num++] = arr[i * FINAL_WIDTH + j];
				}
			}
			for (int kk = 0; kk < scale - 1; kk++) {
				for (int j = 0; j < scale * FINAL_WIDTH; j++) {
					pixels[num] = pixels[num - scale * FINAL_WIDTH];
					num++;
				}
			}
		}
		if (pictureState == 1) {
			fastPic = Bitmap.createBitmap(FINAL_WIDTH * scale, FINAL_HEIGHT
					* scale, Bitmap.Config.ARGB_8888);
			fastPic.setPixels(pixels, 0, FINAL_WIDTH * scale, 0, 0, FINAL_WIDTH
					* scale, FINAL_HEIGHT * scale);
		} else {
			qualPic = Bitmap.createBitmap(FINAL_WIDTH * scale, FINAL_HEIGHT
					* scale, Bitmap.Config.ARGB_8888);
			qualPic.setPixels(pixels, 0, FINAL_WIDTH * scale, 0, 0, FINAL_WIDTH
					* scale, FINAL_HEIGHT * scale);
		}
		drawPicture();
	}

	private void rotateQuality() {
		if (qualPic == null) {
			int width = widthFirst, height = heightFirst;
			qualPic = Bitmap.createBitmap(FINAL_WIDTH, FINAL_HEIGHT,
					Bitmap.Config.ARGB_8888);
			bitmapFirst.getPixels(pixels, 0, width, 0, 0, width, height);
			int[] f = getFunction(bitmapFirst);
			int[] rowf = new int[height];
			ArrayList<Integer> rem = new ArrayList<Integer>();
			ArrayList<Integer> weGetRow = new ArrayList<Integer>();

			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					rowf[i] += f[j + i * width];
				}
			}

			for (int i = 0; i < height; i += 2) {
				if (rowf[i] > rowf[i + 1]) {
					weGetRow.add(i);
					rem.add(i + 1);
				} else {
					weGetRow.add(i + 1);
					rem.add(i);
				}
			}
			for (int i = 0; i < 38 * 6; i += 6) {
				int max = -1, num = -1;
				for (int j = i; j < i + 6; j++) {
					if (rowf[rem.get(j)] > max) {
						max = rowf[rem.get(j)];
						num = rem.get(j);
					}
				}
				weGetRow.add(num);
			}
			for (int i = 38 * 6; i < rem.size(); i += 7) {
				int max = -1, num = 0;
				for (int j = i; j < i + 7; j++) {
					if (rowf[rem.get(j)] > max) {
						max = rowf[rem.get(j)];
						num = rem.get(j);
					}
				}
				weGetRow.add(num);
			}
			Collections.sort(weGetRow);
			rem.clear();
			int numRow = 0, max = -1, num = -1;
			int[] weGetCol = new int[FINAL_HEIGHT];
			int[] remCol = new int[FINAL_HEIGHT];

			for (int index = 0; index < FINAL_WIDTH; index++) {
				int i = weGetRow.get(index), curRem = 0;
				for (int j = 0; j < width; j += 2) {
					if ((f[i * width + j] > f[i * width + j + 1] && pixels[i
							* width + j + 1] != 0xff000000)
							|| pixels[i * width + j] == 0xff000000) {
						weGetCol[j / 2] = j;
						remCol[j / 2] = j + 1;
					} else {
						weGetCol[j / 2] = j + 1;
						remCol[j / 2] = j;
					}
				}

				for (int j = 0; j < 6 * 35; j += 6) {
					max = -1;
					num = -1;
					for (int jj = j; jj < j + 6; jj++) {
						if (f[i * width + remCol[jj]] > max) {
							max = f[i * width + remCol[jj]];
							num = remCol[jj];
						}
					}
					weGetCol[width / 2 + curRem++] = num;

				}
				for (int j = 6 * 35; j < width / 2; j += 7) {
					max = -1;
					num = -1;
					for (int jj = j; jj < j + 7; jj++) {
						if (f[i * width + remCol[jj]] > max) {
							max = f[i * width + remCol[jj]];
							num = remCol[jj];
						}
					}
					weGetCol[width / 2 + curRem++] = num;
				}
				Arrays.sort(weGetCol);

				for (int j = 0; j < FINAL_HEIGHT; j++) {
					decodePixels[j * FINAL_WIDTH + FINAL_WIDTH - 1 - numRow] = pixels[i
							* width + weGetCol[j]];
				}
				numRow++;
			}
			brightUp(decodePixels);
			qualPic.setPixels(decodePixels, 0, FINAL_WIDTH, 0, 0, FINAL_WIDTH,
					FINAL_HEIGHT);
		}

		drawPicture();
		// drawX2(decodePixels);

	}

	private void brightUp(int[] arr) {
		int R, G, B;
		for (int i = 0; i < arr.length; i++) {
			R = Math.min(255, ((arr[i] >> 16) & 0xff) * 2);
			G = Math.min(255, ((arr[i] >> 8) & 0xff) * 2);
			B = Math.min(255, (arr[i] & 0xff) * 2);

			arr[i] = 0xff000000 | (R << 16) | (G << 8) | B;
		}
	}

	Thread tr;
	ProgressDialog pg;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			if (pictureState == 0) {
				pictureState = 1;
				rotateFast();
				Toast.makeText(getContext(), "Fast edition", Toast.LENGTH_SHORT)
						.show();

			} else {
				tr = new Thread(new Runnable() {
					public void run() {
						// TODO Auto-generated method stub

					}
				});
				tr.start();
				if (pictureState == 1) {
					pictureState = 2;
					if (qualPic == null) {
						pg = ProgressDialog.show(getContext(), "",
								"Loading. Please wait.", true);
						Thread th = new Thread(new Runnable() {

							public void run() {
								// TODO Auto-generated method stub
								rotateQuality();
								handler.sendEmptyMessage(0);
							}
						});
						th.start();

					} else {
						rotateQuality();
						Toast.makeText(getContext(), "Quality edition",
								Toast.LENGTH_SHORT).show();
					}

				} else {
					pictureState = 1;
					rotateFast();
					Toast.makeText(getContext(), "Fast edition",
							Toast.LENGTH_SHORT).show();
				}
				try {
					tr.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			pg.dismiss();
			Toast.makeText(getContext(), "Quality edition", Toast.LENGTH_SHORT)
					.show();
		};
	};
}