package com.blundell.tut.ui.widget;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import java.io.IOException;
import java.net.MalformedURLException;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class UrlImageView extends LinearLayout {

	private Context mContext;
	private Drawable mDrawable;
	private ProgressBar mSpinner;
	private ImageView mImage;

	public UrlImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public UrlImageView(Context context) {
		super(context);
		init(context);
	}

	/**
	 * First time loading of the LoaderImageView
	 * Sets up the LayoutParams of the view, you can change these to
	 * get the required effects you want
	 */
	private void init(final Context context) {
		mContext = context;

		mImage = new ImageView(mContext);
		mImage.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		mImage.setVisibility(View.GONE);
		
		mSpinner = new ProgressBar(mContext);
		mSpinner.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		mSpinner.setIndeterminate(true);

		addView(mSpinner);
		addView(mImage);
	}

	/**
	 * Set's the view's drawable, this uses the internet to retrieve the image
	 * don't forget to add the correct permissions to your manifest
	 * 
	 * @param imageUrl the url of the image you wish to load
	 */
	public void setImageDrawable(final String imageUrl) {
		mDrawable = null;
		mSpinner.setVisibility(View.VISIBLE);
		mImage.setVisibility(View.GONE);
		new Thread() {
			public void run() {
				try {
					mDrawable = getDrawableFromUrl(imageUrl);
					imageLoadedHandler.sendEmptyMessage(RESULT_OK);
				} catch (MalformedURLException e) {
					imageLoadedHandler.sendEmptyMessage(RESULT_CANCELED);
				} catch (IOException e) {
					imageLoadedHandler.sendEmptyMessage(RESULT_CANCELED);
				}
			};
		}.start();
	}

	/**
	 * Callback that is received once the image has been downloaded
	 */
	private final Handler imageLoadedHandler = new Handler(new Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case RESULT_OK:
				mImage.setImageDrawable(mDrawable);
				mImage.setVisibility(View.VISIBLE);
				mSpinner.setVisibility(View.GONE);
				break;
			case RESULT_CANCELED:
			default:
				// Could change image here to a 'failed' image
				// otherwise will just keep on spinning
				break;
			}
			return true;
		}
	});

	/**
	 * Pass in an image url to get a drawable object
	 * 
	 * @return a drawable object
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	private static Drawable getDrawableFromUrl(final String url) throws IOException, MalformedURLException {
		return Drawable.createFromStream(((java.io.InputStream) new java.net.URL(url).getContent()), "name");
	}

}
