package ru.tkachenko.exam_helper.Cropper;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.*;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import ru.tkachenko.exam_helper.R;

import java.io.*;


public class CropImage extends MonitoredActivity {
	private static final String TAG = "CropImage";
    private final int BITMAP_IMAGE_MAX_SIZE = 2048;


    // These are various options can be specified in the intent.
	private Bitmap.CompressFormat mOutputFormat =
		Bitmap.CompressFormat.JPEG; // only used with mSaveUri
	private Uri mSaveUri = null;


	// These options specifiy the output image size and whether we should
	// scale the output to fit it (or just crop it).
	boolean mSaving;  // Whether the "save" button is already clicked.

	private CropImageView mImageView;
	private ContentResolver mContentResolver;

    private Bitmap mBitmap;
    private Bitmap defaultBitmap;

    private final BitmapManager.ThreadSet mDecodingThreads =
		new BitmapManager.ThreadSet();
	HighlightView mCrop;

	private String mImagePath;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		mContentResolver = getContentResolver();

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.cropimage);

		mImageView = (CropImageView) findViewById(R.id.image);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			mImagePath = extras.getString("image-path");
			mSaveUri = getImageUri(mImagePath);
            defaultBitmap = mBitmap = getBitmap(mImagePath);
		}

		if (mBitmap == null) {
			Log.d(TAG, "Have null bitmap");
			finish();
			return;
		}

		// Make UI fullscreen.
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });

        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveClicked();
            }
        });

		startTaskCropping();
	}

	private Uri getImageUri(String path) {
		return Uri.fromFile(new File(path));
	}

    //
    private Bitmap getBitmap(String path) {
		Uri uri = getImageUri(path);
		InputStream in = null;
		try {
			in = mContentResolver.openInputStream(uri);

			//Decode image size
	        BitmapFactory.Options o = new BitmapFactory.Options();
	        o.inJustDecodeBounds = true;

	        BitmapFactory.decodeStream(in, null, o);
	        in.close();

	        int scale = 1;
	        if (o.outHeight > BITMAP_IMAGE_MAX_SIZE || o.outWidth > BITMAP_IMAGE_MAX_SIZE) {
	            scale = (int) Math.pow(2, (int) Math.round(Math.log(BITMAP_IMAGE_MAX_SIZE / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
	        }

	        BitmapFactory.Options o2 = new BitmapFactory.Options();
	        o2.inSampleSize = scale;
	        in = mContentResolver.openInputStream(uri);
	        Bitmap b = BitmapFactory.decodeStream(in, null, o2);
	        in.close();
			return b.copy(
                    Bitmap.Config.ARGB_8888 ,true);
		} catch (FileNotFoundException e) {
			Log.e(TAG, "file " + path + " not found");
		} catch (IOException e) {
			Log.e(TAG, "file " + path + " not found");
		}
		return null;
	}


	private void startTaskCropping() {
		if (isFinishing()) {
			return;
		}
		mImageView.setImageBitmapResetBase(mBitmap, true);
        mTaskCropping.run();
	}

	private void onSaveClicked() {
		// TODO this code needs to change to use the decode/crop/encode single
		// step api so that we don't require that the whole (possibly large)
		// bitmap doesn't have to be read into memory
		if (mSaving) {
            return;
        }

		if (mCrop == null) {
			return;
		}
		mSaving = true;
		Rect rect = mCrop.getCropRect();

		Bitmap croppedImage = Bitmap.createBitmap(rect.width(), rect.height(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(croppedImage);
        Rect dstRect = new Rect(0, 0, rect.width(), rect.height());
        canvas.drawBitmap(defaultBitmap, rect, dstRect, null);

        mSaveUri = Uri.fromFile(FileStorageHelper.createTempFile());
        saveOutput(croppedImage);
        mCrop.drawCroppedHighligth();
        mImageView.invalidate();
        mSaving = false;
	}

	private void saveOutput(Bitmap croppedImage) {
		if (mSaveUri != null) {
			OutputStream outputStream = null;
			try {
				outputStream = mContentResolver.openOutputStream(mSaveUri);
				if (outputStream != null) {
					croppedImage.compress(mOutputFormat, 75, outputStream);
				}
			} catch (IOException ex) {
				// TODO: report error to caller
				Log.e(TAG, "Cannot open file: " + mSaveUri, ex);
			} finally {
				Util.closeSilently(outputStream);
			}
			Bundle extras = new Bundle();
			setResult(RESULT_OK, new Intent(mSaveUri.toString())
			.putExtras(extras));
		} else {
			Log.e(TAG, "not defined image url");
		}
		croppedImage.recycle();
	}

	@Override
	protected void onPause() {
		super.onPause();
		BitmapManager.instance().cancelThreadDecoding(mDecodingThreads);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mBitmap.recycle();
	}


	Runnable mTaskCropping = new Runnable() {
		@SuppressWarnings("hiding")
		float mScale = 1F;
		Matrix mImageMatrix;

		private void makeHiglithedView() {
            mScale = 1.0F / mScale;
            mImageMatrix = mImageView.getImageMatrix();
			HighlightView hv = new HighlightView(mImageView, mBitmap);

			int width = mBitmap.getWidth();
			int height = mBitmap.getHeight();

			Rect imageRect = new Rect(0, 0, width, height);

			// make the default size about 4/5 of the width or height
			int cropWidth = Math.min(width, height) * 4 / 5;
			int cropHeight = cropWidth;

			int x = (width - cropWidth) / 2;
			int y = (height - cropHeight) / 2;

			RectF cropRect = new RectF(x, y, x + cropWidth, y + cropHeight);
			hv.setup(mImageMatrix, imageRect, cropRect,
					false);

			mImageView.mHighlightView = hv;
		}

		public void run() {
            makeHiglithedView();
            mImageView.invalidate();
            mCrop = mImageView.mHighlightView;
		}
	};
}


class CropImageView extends ImageViewTouchBase {
	HighlightView mHighlightView = null;
	HighlightView mMotionHighlightView = null;
	float mLastX, mLastY;
	int mMotionEdge;

	private Context mContext;

	@Override
	protected void onLayout(boolean changed, int left, int top,
			int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		if (mBitmapDisplayed.getBitmap() != null) {
            mHighlightView.mMatrix.set(getImageMatrix());
            mHighlightView.invalidate();
            centerBasedOnHighlightView(mHighlightView);
		}
	}

    public CropImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }


    @Override
	protected void zoomTo(float scale, float centerX, float centerY) {
		super.zoomTo(scale, centerX, centerY);
        mHighlightView.mMatrix.set(getImageMatrix());
        mHighlightView.invalidate();
	}

	@Override
	protected void zoomIn() {
		super.zoomIn();
        mHighlightView.mMatrix.set(getImageMatrix());
        mHighlightView.invalidate();
	}

	@Override
	protected void zoomOut() {
		super.zoomOut();
        mHighlightView.mMatrix.set(getImageMatrix());
        mHighlightView.invalidate();
	}

	@Override
	protected void postTranslate(float deltaX, float deltaY) {
		super.postTranslate(deltaX, deltaY);
        mHighlightView.mMatrix.postTranslate(deltaX, deltaY);
        mHighlightView.invalidate();
	}

	// According to the event's position, change the focus to the first
	// hitting cropping rectangle.
	private void recomputeFocus(MotionEvent event) {
        mHighlightView.invalidate();

        mHighlightView.invalidate();
		invalidate();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		CropImage cropImage = (CropImage) mContext;
		if (cropImage.mSaving) {
			return false;
		}

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:

                int edge = mHighlightView.getHit(event.getX(), event.getY());
                if (edge != HighlightView.GROW_NONE) {
                    mMotionEdge = edge;
                    mMotionHighlightView = mHighlightView;
                    mLastX = event.getX();
                    mLastY = event.getY();
                    mMotionHighlightView.setMode(
                            (edge == HighlightView.MOVE)
                            ? HighlightView.ModifyMode.Move
                                    : HighlightView.ModifyMode.Grow);
                    break;
				}
			break;
		case MotionEvent.ACTION_UP:
			 if (mMotionHighlightView != null) {
				centerBasedOnHighlightView(mMotionHighlightView);
				mMotionHighlightView.setMode(
						HighlightView.ModifyMode.None);
			}
			mMotionHighlightView = null;
			break;
		case MotionEvent.ACTION_MOVE:
			if (mMotionHighlightView != null) {
				mMotionHighlightView.handleMotion(mMotionEdge,
                        event.getX() - mLastX,
                        event.getY() - mLastY);
				mLastX = event.getX();
				mLastY = event.getY();

				if (true) {
					// This section of code is optional. It has some user
					// benefit in that moving the crop rectangle against
					// the edge of the screen causes scrolling but it means
					// that the crop rectangle is no longer fixed under
					// the user's finger.
					ensureVisible(mMotionHighlightView);
				}
			}
			break;
		}

		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			center(true, true);
			break;
		case MotionEvent.ACTION_MOVE:
			// if we're not zoomed then there's no point in even allowing
			// the user to move the image around.  This call to center puts
			// it back to the normalized location (with false meaning don't
			// animate).
			if (getScale() == 1F) {
				center(true, true);
			}
			break;
		}

		return true;
	}

	// Pan the displayed image to make sure the cropping rectangle is visible.
	private void ensureVisible(HighlightView hv) {
		Rect r = hv.mDrawRect;

		int panDeltaX1 = Math.max(0, mLeft - r.left);
		int panDeltaX2 = Math.min(0, mRight - r.right);

		int panDeltaY1 = Math.max(0, mTop - r.top);
		int panDeltaY2 = Math.min(0, mBottom - r.bottom);

		int panDeltaX = panDeltaX1 != 0 ? panDeltaX1 : panDeltaX2;
		int panDeltaY = panDeltaY1 != 0 ? panDeltaY1 : panDeltaY2;

		if (panDeltaX != 0 || panDeltaY != 0) {
			panBy(panDeltaX, panDeltaY);
		}
	}

	// If the cropping rectangle's size changed significantly, change the
	// view's center and scale according to the cropping rectangle.
	private void centerBasedOnHighlightView(HighlightView hv) {
		Rect drawRect = hv.mDrawRect;

		float width = drawRect.width();
		float height = drawRect.height();

		float thisWidth = getWidth();
		float thisHeight = getHeight();

		float z1 = thisWidth / width * .6F;
		float z2 = thisHeight / height * .6F;

		float zoom = Math.min(z1, z2);
		zoom = zoom * this.getScale();
		zoom = Math.max(1F, zoom);
		if ((Math.abs(zoom - getScale()) / zoom) > .1) {
			float [] coordinates = new float[] {hv.mCropRect.centerX(),
					hv.mCropRect.centerY()};
			getImageMatrix().mapPoints(coordinates);
			zoomTo(zoom, coordinates[0], coordinates[1], 300F);
		}

		ensureVisible(hv);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
        mHighlightView.draw(canvas);
	}
}