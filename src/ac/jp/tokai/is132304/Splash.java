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
		// タイトルを非表示にします。
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// splashmain.xmlをViewに指定します。
		setContentView(R.layout.splashmain);
		Handler hdl = new Handler();
		// 2000ms遅延させてsplashHandlerを実行します。
		hdl.postDelayed(new splashHandler(), 2000);
	}
	class splashHandler implements Runnable {
		public void run() {
			// スプラッシュ完了後に実行するActivityを指定します。
			Intent intent = new Intent(getApplication(), CustomActivity.class);
			startActivity(intent);
			// SplashActivityを終了させます。
			Splash.this.finish();
		}
	}
}
