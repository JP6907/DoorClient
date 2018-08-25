package neo.door.qrcode;

import java.io.IOException;

import com.google.zxing.WriterException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import neo.door.usermanager.UserManager;
import neo.door.utils.TimeUtil;

/**
 * ���ɶ�ά��
 * @author Administrator
 *
 */

public class CreateQrCodeTask extends AsyncTask {

	private Activity mContext;
	private ProgressDialog progressDialog;
	private ImageView imageView;
	private TextView tvTip;
	
	private String password;
	
	/**
	 * ���캯��
	 * @param context Activity����
	 * @param imageView	��ʾ��ά���ImageView
	 * @param tvTip ��ʾ��ʾ��Ϣ��TextView
	 */
	public CreateQrCodeTask(Activity context, ImageView imageView, TextView tvTip) {
		this.mContext = context;
		this.imageView = imageView;
		this.tvTip = tvTip;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressDialog = new ProgressDialog(mContext);
		progressDialog.show();
		
	}

	@Override
	protected Object doInBackground(Object... params) {
		password = (String) params[0];
		String information = UserManager.getPhone() + "," + password + "," + TimeUtil.getTime();
		Bitmap bitmap = null; 
		try {
			bitmap = QrCoddeUtil.createQRBitmap(information, imageView.getWidth());
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("QrCode", "WriterException:" + e.toString());
			bitmap = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("QrCode", "IOException:" + e.toString());
			bitmap = null;
		}
		return bitmap;
	}

	@Override
	protected void onPostExecute(Object result) {
		progressDialog.cancel();
		if (result != null) {
			imageView.setImageBitmap((Bitmap)result);
			Toast.makeText(mContext, "���ɶ�ά��ɹ�!", Toast.LENGTH_SHORT).show();
			tvTip.setText("�뽫��ά���׼����ͷ");
		} else{
			Toast.makeText(mContext, "���ɶ�ά��ʧ��!", Toast.LENGTH_LONG).show();
		}
			
	}

}
