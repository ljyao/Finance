package org.weixvn.finance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;


public class ImagePreProcess {
	private static Bitmap trainBitmap[] = new Bitmap[10];
	
	public static int isWhite(int color) {

		if (Color.red(color) + Color.green(color) + Color.blue(color) > 100) {
			return 1;
		}
		return 0;
	}

	public static int isBlack(int color) {
		if (Color.red(color) + Color.green(color) + Color.blue(color) <= 100) {
			return 1;
		}
		return 0;
	}

	public static Bitmap removeBackgroud(Bitmap bitmap) throws Exception {
		Bitmap targetBitmap = null;
		try {
			targetBitmap = bitmap.copy(bitmap.getConfig(), true);
		} catch (Exception e) {
			System.out.println(e);
		}

		int width = targetBitmap.getWidth();
		int height = targetBitmap.getHeight();
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				if (isWhite(targetBitmap.getPixel(x, y)) == 1) {
					targetBitmap.setPixel(x, y, Color.WHITE);
				} else {
					targetBitmap.setPixel(x, y, Color.BLACK);
				}
			}
		}
		return targetBitmap;
	}

	public static List<Bitmap> splitImage(Bitmap img) throws Exception {
		List<Bitmap> subImgs = new ArrayList<Bitmap>();

		subImgs.add(Bitmap.createBitmap(img, 6, 6, 9, 18));
		subImgs.add(Bitmap.createBitmap(img, 15, 6, 9, 18));
		subImgs.add(Bitmap.createBitmap(img, 24, 6, 9, 18));
		subImgs.add(Bitmap.createBitmap(img, 33, 6, 9, 18));

		return subImgs;
	}

	public static Map<Bitmap, String> loadTrainData() throws Exception {
		Map<Bitmap, String> map = new HashMap<Bitmap, String>();
		Resources res = MainActivity.context.getResources();
		trainBitmap[0] = BitmapFactory.decodeStream(res.openRawResource(R.drawable.zero));
		trainBitmap[1] = BitmapFactory.decodeStream(res.openRawResource(R.drawable.one));
		trainBitmap[2] = BitmapFactory.decodeStream(res.openRawResource(R.drawable.two));
		trainBitmap[3] = BitmapFactory.decodeStream(res.openRawResource(R.drawable.three));
		trainBitmap[4] = BitmapFactory.decodeStream(res.openRawResource(R.drawable.four));
		trainBitmap[5] = BitmapFactory.decodeStream(res.openRawResource(R.drawable.five));
		trainBitmap[6] = BitmapFactory.decodeStream(res.openRawResource(R.drawable.six));
		trainBitmap[7] = BitmapFactory.decodeStream(res.openRawResource(R.drawable.seven));
		trainBitmap[8] = BitmapFactory.decodeStream(res.openRawResource(R.drawable.eight));
		trainBitmap[9] = BitmapFactory.decodeStream(res.openRawResource(R.drawable.nine));

		for (int i = 0; i < 10; i++) {
			map.put(trainBitmap[i], i + "");
		}

		return map;

	}

	public static String getSingleCharOcr(Bitmap img, Map<Bitmap, String> map) {
		String result = "";
		int width = img.getWidth();
		int height = img.getHeight();
		int min = width * height;
		for (Bitmap bi : map.keySet()) {
			int count = 0;
			Label1: for (int x = 0; x < width; ++x) {
				for (int y = 0; y < height; ++y) {
					if (isWhite(img.getPixel(x, y)) != isWhite(bi
							.getPixel(x, y))) {
						count++;
						if (count >= min)
							break Label1;
					}
				}
			}
			if (count < min) {
				min = count;
				result = map.get(bi);
			}
		}
		return result;
	}

	public static String getAllOcr(Bitmap bimap) throws Exception {
		Bitmap img = removeBackgroud(bimap);
		List<Bitmap> listImg = splitImage(img);
		Map<Bitmap, String> map = loadTrainData();
		String result = "";
		int i = 0;
		for (Bitmap bi : listImg) {
			result += getSingleCharOcr(bi, map);
		}
		return result;
	}
}
