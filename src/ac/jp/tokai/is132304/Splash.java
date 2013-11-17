package ac.jp.tokai.is132304;

import edu.dhbw.andopenglcam.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class Splash extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// �^�C�g�����\���ɂ��܂��B
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// splashmain.xml��View�Ɏw�肵�܂��B
		setContentView(R.layout.splashmain);
		Handler hdl = new Handler();
		// 2000ms�x��������splashHandler�����s���܂��B
		hdl.postDelayed(new splashHandler(), 2000);
	}
	class splashHandler implements Runnable {
		public void run() {
			// �X�v���b�V��������Ɏ��s����Activity���w�肵�܂��B
			Intent intent = new Intent(getApplication(), CustomActivity.class);
			startActivity(intent);
			// SplashActivity���I�������܂��B
			Splash.this.finish();
		}
	}
}
