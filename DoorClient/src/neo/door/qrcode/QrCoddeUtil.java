package neo.door.qrcode;

import java.io.IOException;
import java.util.Hashtable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import android.graphics.Bitmap;

public class QrCoddeUtil {
	private static final int BLACK = 0xFF000000;
	private static final int WHITE = 0xFFFFFFFF;

	private static Bitmap toBitmap(BitMatrix matrix) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				bitmap.setPixel(x, y, matrix.get(x, y) ? BLACK : WHITE);
			}
		}
		return bitmap;
	}

	// public static void addLogo(BufferedImage qrcodeImg, BufferedImage
	// logoImg) {
	// int qrcodeImgWidth = qrcodeImg.getWidth();
	// int suitableWidth = qrcodeImgWidth/5;
	// qrcodeImg.getGraphics().drawImage(logoImg, (qrcodeImgWidth -
	// suitableWidth) / 2,
	// (qrcodeImgWidth - suitableWidth) / 2, suitableWidth, suitableWidth,
	// null);
	//
	// }
	/**
	 * ���ɶ�ά��
	 * 
	 * @param content
	 *            ��ά������
	 * @param width
	 *            ͼƬ�ߴ�
	 * @param dest
	 *            ��ŵĶ�ά��������
	 * @param logo
	 *            �Ƿ��ڶ�ά���м��logo
	 * @throws WriterException
	 * @throws IOException
	 */
	public static Bitmap createQRBitmap(String content, Integer width)
			throws WriterException, IOException {
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		// ָ������ȼ�
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		// ָ�������ʽ
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, width, hints);
		Bitmap qrcode = toBitmap(bitMatrix);
		return qrcode;
	}
}
