package com.changshagaosu.roadtools.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.changshagaosu.roadtools.R;
import com.changshagaosu.roadtools.service.PatrolService;
import com.changshagaosu.roadtools.utils.GPSUtil;
import com.nobcdz.upload.UploadImageService;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 日常巡查
 */
public class PatrolingActivity extends Activity {
	private TextView gpsTextView;
	private String CheckDailyRecordID;
	private ImageView imageView;
	private ImageButton startImageButton;
	private boolean isRunning = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_patroling);
		CheckDailyRecordID = getIntent().getStringExtra("CheckDailyRecordID");

		gpsTextView = (TextView) findViewById(R.id.gps_info_tv);
		imageView = (ImageView) findViewById(R.id.img);
		imageView.setOnTouchListener(new TounchListener());
		startImageButton = (ImageButton) findViewById(R.id.start_ibtn);

		if (GPSUtil.getInstance().isProviderEnabled()) {
			startService();
		} else {
			gpsTextView.setText("GPS未打开");
		}

		startImageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (isRunning) {
					isRunning = false;

				/*	Intent service = new Intent(getApplicationContext(),
							PatrolService.class);
					stopService(service);*/
					stopGPS();

					startImageButton
							.setImageResource(R.drawable.playing_button);
					//			startGif(isRunning);
				} else {
					if (GPSUtil.getInstance().isProviderEnabled()) {
						startService();
					} else {
						Toast.makeText(getApplicationContext(), "GPS未打开",
								Toast.LENGTH_SHORT).show();
					}
				}
			}

		});
		this.registerReceiver(new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				double longitude = intent.getDoubleExtra("Longitude", 0);
				double latitude = intent.getDoubleExtra("Latitude", 0);
				double speed = intent.getFloatExtra("Speed", 0);
				gpsTextView.setText("经度：" + longitude + "\n纬度：" + latitude
						+ "\n速度：" + speed);
			}
		}, new IntentFilter("com.changshagaosu.roadtools.utils.GPSUtil"));
	}

	private void startService() {
		isRunning = true;

		Intent service = new Intent(getApplicationContext(),
				PatrolService.class);
		service.putExtra("CheckDailyRecordID", CheckDailyRecordID);
		startService(service);

		startImageButton.setImageResource(R.drawable.pause_button);
		//	startGif(isRunning);
	}

	private void startGif(boolean isplay) {
		Intent intent = new Intent();
		intent.setAction("com.nobcdz.trainingsystem.ui.LineActivity");
		intent.putExtra("isRunning", isplay);
		PatrolingActivity.this.sendBroadcast(intent);
	}

	public void mOnClick(View view) {
		switch (view.getId()) {
			case R.id.add_btn:
				Intent addIntent = new Intent(getApplicationContext(),
						PatrolContentActivity.class);
				addIntent.putExtra("Key", CheckDailyRecordID);
				startActivity(addIntent);
				break;
			case R.id.stop_btn:
				dialog();
				break;
			case R.id.disease_btn:
				Intent intent = new Intent(getApplicationContext(),
						DiseaseActivity.class);
				intent.putExtra("CheckDailyRecordID", CheckDailyRecordID);
				startActivity(intent);
				break;
			case R.id.take_btn:
				takePic();
				break;
			default:
				break;
		}
	}

	public void dialog() {
		AlertDialog.Builder builder = new Builder(PatrolingActivity.this);
		builder.setMessage("确认结束吗？");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new android.content.DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent service = new Intent(getApplicationContext(),
						PatrolService.class);
				stopService(service);
				finish();
			}
		});
		builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	@Override
	public void onBackPressed() {
		Toast.makeText(this, "请点击结束巡查...", Toast.LENGTH_SHORT).show();
	}

	private class TounchListener implements OnTouchListener {

		private PointF startPoint = new PointF();
		private Matrix matrix = new Matrix();
		private Matrix currentMaritx = new Matrix();

		private int mode = 0;// 用于标记模式
		private static final int DRAG = 1;// 拖动
		private static final int ZOOM = 2;// 放大
		private float startDis = 0;
		private PointF midPoint;// 中心点

		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
				case MotionEvent.ACTION_DOWN:
					mode = DRAG;
					currentMaritx.set(imageView.getImageMatrix());// 记录ImageView当期的移动位置
					startPoint.set(event.getX(), event.getY());// 开始点
					break;

				case MotionEvent.ACTION_MOVE:// 移动事件
					if (mode == DRAG) {// 图片拖动事件
						float dx = event.getX() - startPoint.x;// x轴移动距离
						float dy = event.getY() - startPoint.y;
						matrix.set(currentMaritx);// 在当前的位置基础上移动
						matrix.postTranslate(dx, dy);

					} else if (mode == ZOOM) {// 图片放大事件
						float endDis = distance(event);// 结束距离
						if (endDis > 10f) {
							float scale = endDis / startDis;// 放大倍数
							matrix.set(currentMaritx);
							matrix.postScale(scale, scale, midPoint.x, midPoint.y);
						}

					}

					break;

				case MotionEvent.ACTION_UP:
					mode = 0;
					break;
				// 有手指离开屏幕，但屏幕还有触点(手指)
				case MotionEvent.ACTION_POINTER_UP:
					mode = 0;
					break;
				// 当屏幕上已经有触点（手指）,再有一个手指压下屏幕
				case MotionEvent.ACTION_POINTER_DOWN:
					mode = ZOOM;
					startDis = distance(event);

					if (startDis > 10f) {// 避免手指上有两个茧
						midPoint = mid(event);
						currentMaritx.set(imageView.getImageMatrix());// 记录当前的缩放倍数
					}

					break;

			}
			imageView.setImageMatrix(matrix);
			return true;
		}

	}

	/**
	 * 两点之间的距离
	 *
	 * @param event
	 * @return
	 */
	private static float distance(MotionEvent event) {
		// 两根线的距离
		float dx = event.getX(1) - event.getX(0);
		float dy = event.getY(1) - event.getY(0);
		return (float)(dx * dx + dy * dy);
	}

	/**
	 * 计算两点之间中心点的距离
	 *
	 * @param event
	 * @return
	 */
	private static PointF mid(MotionEvent event) {
		float midx = event.getX(1) + event.getX(0);
		float midy = event.getY(1) - event.getY(0);

		return new PointF(midx / 2, midy / 2);
	}

	public void stopGPS() {
		GPSUtil.getInstance().stop();
	}

	public void startGPS() {
		GPSUtil.getInstance().start();
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case 1:
					try {
						Bitmap bitmap = null;
						Options op = new Options();
						op.inSampleSize = 8;
						op.inJustDecodeBounds = false;
						bitmap = BitmapFactory.decodeFile(imageFile.getPath(), op);
						createThumbnail(bitmap);

						Intent intent = new Intent(getApplicationContext(),
								UploadImageService.class);
						startService(intent);
					} catch (Exception e) {
						e.printStackTrace();
					}
				default:
					break;
			}
		}
	}

	public String createThumbnail(Bitmap source) {
		Bitmap imgThumb = ThumbnailUtils.extractThumbnail(source, 220, 145);
		return saveBitmap(imgThumb);
	}

	public String saveBitmap(Bitmap bm) {
		File f = new File(imagePath, imageName_s);
		try {
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	private File imageFile;
	private File imagePath;
	private String imageName_s;

	private void takePic() {
		long time = System.currentTimeMillis();
		String imageName = time + ".jpg";
		imageName_s = time + "_s.jpg";
		imagePath = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ "/RoadTools/UploadPicture/InspectLogUser/"
				+ CheckDailyRecordID + "/");
		if (!imagePath.exists()) {
			imagePath.mkdirs();
		}
		imageFile = new File(imagePath + "/" + imageName);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
		startActivityForResult(intent, 1);
	}
}
