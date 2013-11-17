package ac.jp.tokai.is132304;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.text.util.Linkify;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import edu.dhbw.andar.ARToolkit;
import edu.dhbw.andar.ARObject;
import edu.dhbw.andar.AndARActivity;
import edu.dhbw.andar.exceptions.AndARException;
import edu.dhbw.andopenglcam.R;
import edu.dhbw.andopenglcam.R.id;
/**
 * Example of an application that makes use of the AndAR toolkit.
 * @author Tobi
 *
 */
public class CustomActivity extends AndARActivity {

	private static final int CUSTOM_DIALOG = 0;
	private static final int ABOUT_DIALOG  = 101;
	double[] Ratio = new double[2];

	ARToolkit artoolkit;
	/* Add own Boxes. */
	
	CustomObject ID_1;
	CustomObject ID_2;
	CustomObject ID_3;
	CustomObject ID_4;
	CustomObject ID_5;
	CustomObject ID_6;
	CustomObject ID_7;
	CustomObject ID_8;
	CustomObject ID_9;
	
	CustomObject Concent_Test;


	float xW = 1;
	float yH = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i("ARapp","super.onCreate()");
		super.onCreate(savedInstanceState);
		Log.i("ARapp","onCreate()");
		CustomRenderer renderer = new CustomRenderer();//optional, may be set to null
		super.setNonARRenderer(renderer);//or might be omited
		// �ｽr�ｽ�ｽ�ｽ[�ｽ�ｽ�ｽd�ｽﾋゑｿｽ
		View view1 =  getLayoutInflater().inflate(R.layout.layout_main, null);
		addContentView(view1, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
		// about...
		TextView myAbout = (TextView)findViewById(R.id.aboutArea);
	    //show about Dialog
		/******************************************************************/
		myAbout.setOnClickListener( new OnClickListener() {
	        public void onClick(View v) {
	            showDialog(ABOUT_DIALOG);
	        }
	    });
		try {
		  //register a object for each marker type
			artoolkit = super.getArtoolkit();
			Log.i("ARapp","getARkit");
			/* default size is 1x1x1 */
			ID_1 = new CustomObject
					("ID_1", "A.pat", 30.0, new double[]{0,0},new float[]{1.0f,0.0f,0.0f,0.8f},new float[]{1.2f,1.2f,1.2f});
			ID_2 = new CustomObject
					("ID_2", "B.pat", 30.0, new double[]{0,0},new float[]{1.0f,0.0f,0.0f,0.8f},new float[]{1.2f,1.2f,1.2f});
			ID_3 = new CustomObject
					("ID_3", "C.pat", 30.0, new double[]{0,0},new float[]{0.0f,1.0f,1.0f,0.8f},new float[]{1.0f,1.0f,1.0f});
			ID_4 = new CustomObject
					("ID_4", "D.pat", 30.0, new double[]{0,0},new float[]{1.0f,1.0f,0.0f,0.8f},new float[]{1.0f,1.0f,1.0f});
			ID_5 = new CustomObject
					("ID_5", "E.pat", 30.0, new double[]{0,0},new float[]{1.0f,0.0f,1.0f,0.8f},new float[]{1.0f,1.0f,1.0f});
			ID_6 = new CustomObject
					("ID_6", "F.pat", 30.0, new double[]{0,0},new float[]{0.5f,1.0f,1.0f,0.8f},new float[]{1.0f,1.0f,1.0f});
			ID_7 = new CustomObject
					("ID_7", "G.pat", 30.0, new double[]{0,0},new float[]{0.1f,0.8f,1.0f,0.8f},new float[]{1.0f,1.0f,1.0f});
			ID_8 = new CustomObject
					("ID_8", "H.pat", 30.0, new double[]{0,0},new float[]{0.5f,0.2f,0.2f,0.8f},new float[]{1.0f,1.0f,1.0f});
			ID_9 = new CustomObject
					("ID_9", "I.pat", 30.0, new double[]{0,0},new float[]{0.5f,0.2f,0.8f,0.8f},new float[]{1.0f,1.0f,1.0f});
			//Concent_Test = new CustomObject
			//		("CTest", "test.pt", 30.0, new double[]{0,0},new float[]{0.1f,0.9f,0.1f,0.8f},new float[]{0.8f,1.5f,1.0f});
			//
			

			// register objs.
			
			artoolkit.registerARObject(ID_1);
			artoolkit.registerARObject(ID_2);
			artoolkit.registerARObject(ID_3);
			artoolkit.registerARObject(ID_4);
			artoolkit.registerARObject(ID_5);
			artoolkit.registerARObject(ID_6);
			artoolkit.registerARObject(ID_7);
			artoolkit.registerARObject(ID_8);
			artoolkit.registerARObject(ID_9);
			
			//artoolkit.registerARObject(Concent_Test);


		//------------------------------------------------------------------------------------------------
		
	    //------------------------------------------------------------------------------------------------
	        
		} catch (AndARException ex){
			//handle the exception, that means: show the user what happened
			System.out.println("");
		}
		startPreview();

	}
	public static boolean collideBox(double x1,double y1,double w1,double h1, double x2,double y2,double w2,double h2 ){
	    /* 矩形Ａ */
	    double ax =  x1;        // x 座標
	    double ay =  y1;        // y 座標
	    double aw =  50;        // 幅
	    double ah =  80;        // 高さ

	    /* 矩形Ｂ */
	    double bx = x2;			// x 座標
	    double by = y2;			// y 座標
	    double bw = w2;			// 幅
	    double bh = h2;			// 高さ
	    
	    /* 矩形Ａ */
	    double a_x_min = ax;            // x 座標最小
	    double a_y_min = ay;            // y 座標最小
	    double a_x_max = a_x_min + aw;  // x 座標最大
	    double a_y_max = a_y_min + ah;  // y 座標最大

	    /* 矩形Ｂ */
	    double b_x_min = bx;            // x 座標最小
	    double b_y_min = by;            // y 座標最小
	    double b_x_max = b_x_min + bw;  // x 座標最大
	    double b_y_max = b_y_min + bh;  // y 座標最大
	    
	    if( a_x_max < b_x_min){
	    }else if( b_x_max < a_x_min){
	    }else if( a_y_max < b_y_min){
	    }else if( b_y_max < a_y_min){
	    }else{
	        // 当たりあり
	    	return true;
	    }
		return false;
	}
	public static boolean collideCircle(double Ax,double Ay,double Bx,double By,double Rad){
		if( Math.pow(Ax-Bx, 2)+Math.pow(Ay-By,2) < Math.pow(Rad,2)){
					return true;
		}
		return false;
	}
	
	@Override 
	public boolean onTouchEvent( MotionEvent event ) {
		  // �ｽ�ｽ�ｽ�ｽ�ｽ�ｽ�ｽu�ｽﾔのゑｿｽ
		 String[] mystr = (String[])artoolkit.getObjInfostr(); // Convert to StringArray
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		DisplayMetrics displayMetrics = new DisplayMetrics();
		display.getMetrics(displayMetrics);
		TextView myTv = (TextView)findViewById(R.id.MyText1);
		  if( event.getAction() == MotionEvent.ACTION_DOWN ) {
			  String[] columns;
			  
			 
			  float ratioX = (float) ( artoolkit.getImageSize()[0] / display.getWidth() );
			  float ratioY = (float) ( artoolkit.getImageSize()[1] / display.getHeight());
			  float x = (event.getX() *ratioX);
			  float y = (event.getY() *ratioY);
			  /* ヌルポならここで死ぬ */
		      TextView tv = (TextView)findViewById(R.id.tvTrace);
		      myTv.setText("dispW:"+display.getWidth()+" dispH:"+display.getHeight()+" art:W:"+artoolkit.getImageSize()[0]+" art:H:"+artoolkit.getImageSize()[1] +
		    		  		" ratioX:"+ratioX + "ratioY:"+ratioY);
		      tv.setText("M:"+mystr[0]+":pos "+x+"/"+y);
		      System.out.println("Display::Touch::pos "+x+"/"+y);
			  for(int ii=1;ii<mystr.length;ii++){
				  if(mystr[ii] == null)continue;
				  
				  columns = mystr[ii].split(",");
				  
				  
				  //String Name = columns[0];
				  //System.out.println(Name);
				  
				  //if(id < 0)continue;
				  try{
					  int id=Integer.parseInt(columns[0]);// get id
				  	  float x1=Float.parseFloat(columns[1]);// get x*ratioX
					  float y1=Float.parseFloat(columns[2]);// get y*ratioY
					  //if(CustomActivity.collideBox(x1,y1,50.0,50.0,x,y,20.0,20.0)){
					  if(collideCircle(x1,y1,x,y,40)){
					  		Toast ts = Toast.makeText(this, "HIT! ("+id+")"+mystr[ii]+"touch:"+x+"/"+y+":", Toast.LENGTH_SHORT);
					        ts.show();
					        System.out.println("Display::Touch::pos "+x+"/"+y);
					        myTv.setText("次の物体がタッチされた:"+mystr[ii]+"//");
					        ARObject.setObjID(id);
					        showDialog(id);
					        boolean touch_on = true;
					        return super.onTouchEvent( event );
					  }
				  }catch(Exception e){
						  continue;			// 例外来たらやりなおす
				  }
				  
				  System.out.println("Display::Object::Str "+mystr[ii]);
				  columns[0] = "";
				  columns[1] = "";
				  columns[2] = "";
				  
			  }
		    boolean touch_on = true;
		  }

		  return super.onTouchEvent( event );
		}
	/**
	 * Inform the user about exceptions that occurred in background threads.
	 * This exception is rather severe and can not be recovered from.
	 * TODO Inform the user and shut down the application.
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		Log.e("AndAR EXCEPTION", ex.getMessage());
		finish();
	}

	/*
	 * 
	 * TODO: ARtoolkitの方から名前を貰って、変更したらその名前でサイズを紐づける
	 *       Tapしたときに名前を拾って名前が主キー、サイズと色がついてくるデータ。
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreateDialog(int)
	 */
	@Override
	protected Dialog onCreateDialog(final int id) {
	    if(id>=CUSTOM_DIALOG && id <= 100){
	    	int   markerID           = (id)-CUSTOM_DIALOG;
	        LayoutInflater factory   = LayoutInflater.from(this);
	        final View inputView     = factory.inflate(R.layout.input_dialog, null);
	        final EditText etDepth   = (EditText) inputView.findViewById(R.id.dialog_Depth);
	        final EditText etHeight  = (EditText) inputView.findViewById(R.id.dialog_Height);
	        final EditText etWidth   = (EditText) inputView.findViewById(R.id.dialog_Width);
	        String[] str = artoolkit.getObjInfostr();
	        return new AlertDialog.Builder(CustomActivity.this)
	        	//.setTitle(str[markerID])
	            .setView(inputView)
	            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                	double D,H,W;
	                	D=H=W=0;
	                	try{
		                	D=Double.parseDouble(etDepth.getText().toString());
		                	H=Double.parseDouble(etHeight.getText().toString());
		                	W=Double.parseDouble(etWidth.getText().toString());
	                	}catch(NumberFormatException e){
	                		D=H=W=1.0;
	                	}
	                	/* サイズを反映 */
	                	ARObject.setObjSize(id,new double[] {D,W,H});
	                }
	            })
	            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                }
	            })
	            .create();
	    }
	    else if(id==ABOUT_DIALOG){
	        LayoutInflater afactory = LayoutInflater.from(this);
	        final View ainputView = afactory.inflate(R.layout.about_dialog, null);
	        
	        return new AlertDialog.Builder(CustomActivity.this)
	            .setView(ainputView)
	            .create();
	    }else{
	    	return null;
	    }
	}
	
}


