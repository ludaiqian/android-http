package com.vimc.ahttp;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

import com.vimc.ahttp.Response.ResponseListener;

/**
 * Bitmap Request
 * @author CharLiu
 *
 */
public class BitmapRequest extends Request<Bitmap> {

	private static final byte[] sDecodeLock = new byte[1]; // 一次只decode一张图片

	private Config mConfig;

	public BitmapRequest(String url) {
		super(url, null, null, null);
	}

	public BitmapRequest(String url, ResponseListener<Bitmap> listener) {
		super(url, null, null, listener);
	}

	public Bitmap mResponse;

	@Override
	public Response<Bitmap> parseResponse(NetworkResponse response) {
		Options options = new Options();
		options.inPreferredConfig = mConfig;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		synchronized (sDecodeLock) {
			try {
				byte[] data = response.data;
				Bitmap bitmap = BitmapFactory.decodeByteArray(response.data, 0, data.length, options);
				if (bitmap != null) {
					return Response.success(bitmap);
				} else {
					HLog.e("Decode bitmap fail, URL:" + mUrl);
					return Response.error(new HError(HError.PARSE_ERROR, "Decode bitmap fail"));
				}
			} catch (OutOfMemoryError e) {
				HLog.e("OutOfMemoryError:" + e.getMessage());
				return Response.error(new HError(e, HError.PARSE_ERROR, "Parse OutOfMemoryError"));
			}
		}
	}

}
