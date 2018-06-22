package com.otoclash.pelapor;

import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.content.*;
import android.graphics.*;
import android.media.*;
import android.net.*;
import android.text.*;
import android.util.*;
import android.webkit.*;
import android.animation.*;
import android.view.animation.*;
import java.util.*;
import java.text.*;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import java.util.HashMap;
import java.util.ArrayList;
import android.widget.LinearLayout;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Button;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.storage.OnProgressListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;
import android.net.Uri;
import java.io.File;
import android.content.Intent;
import android.provider.MediaStore;
import android.content.ClipData;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import android.app.Activity;
import android.content.SharedPreferences;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Context;
import android.os.Vibrator;
import android.view.View;
import android.graphics.Typeface;

public class KontribusiActivity extends AppCompatActivity {
	
	public final int REQ_CD_CAMERA = 101;
	public final int REQ_CD_FILEPICK = 102;
	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
	private FirebaseStorage _firebase_storage = FirebaseStorage.getInstance();
	
	private Toolbar _toolbar;
	private double path = 0;
	private String PATHFOTO = "";
	private double nilaiprogressupload = 0;
	private HashMap<String, Object> map = new HashMap<>();
	
	private ArrayList<String> listGambar = new ArrayList<>();
	
	private LinearLayout linearpembagi;
	private LinearLayout linear1;
	private LinearLayout linear2;
	private LinearLayout linear10;
	private LinearLayout linear4;
	private EditText judul;
	private TextView tgl;
	private LinearLayout linear3;
	private LinearLayout linear9;
	private LinearLayout linear6;
	private LinearLayout linear11;
	private LinearLayout linearprogressupload;
	private ImageView imageview1;
	private TextView textview6;
	private TextView progressupload;
	private TextView textview4;
	private TextView textview5;
	private Button tombol_camera;
	private Button tombol_ambilfile;
	private EditText deskripsi;
	private Button clear_btn;
	private Button send_btn;
	
	private DatabaseReference db = _firebase.getReference("db");
	private ChildEventListener _db_child_listener;
	private StorageReference simpanan = _firebase_storage.getReference("simpanan");
	private OnSuccessListener<UploadTask.TaskSnapshot> _simpanan_upload_success_listener;
	private OnSuccessListener<FileDownloadTask.TaskSnapshot> _simpanan_download_success_listener;
	private OnSuccessListener _simpanan_delete_success_listener;
	private OnProgressListener _simpanan_upload_progress_listener;
	private OnProgressListener _simpanan_download_progress_listener;
	private OnFailureListener _simpanan_failure_listener;
	private Intent iMain = new Intent();
	private Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	private Uri _uri_camera;
	private Intent filepick = new Intent(Intent.ACTION_GET_CONTENT);
	private Calendar calender = Calendar.getInstance();
	private SharedPreferences identifikasiLogin;
	private AlertDialog.Builder dialogSuksesUpload;
	private AlertDialog.Builder dialogFailedUpload;
	private Vibrator gtr;
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.kontribusi);
		initialize();
		initializeLogic();
	}
	
	private void initialize() {
		
		_toolbar = (Toolbar) findViewById(R.id._toolbar);
		setSupportActionBar(_toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _v) {
				onBackPressed();
			}
		});
		linearpembagi = (LinearLayout) findViewById(R.id.linearpembagi);
		linear1 = (LinearLayout) findViewById(R.id.linear1);
		linear2 = (LinearLayout) findViewById(R.id.linear2);
		linear10 = (LinearLayout) findViewById(R.id.linear10);
		linear4 = (LinearLayout) findViewById(R.id.linear4);
		judul = (EditText) findViewById(R.id.judul);
		tgl = (TextView) findViewById(R.id.tgl);
		linear3 = (LinearLayout) findViewById(R.id.linear3);
		linear9 = (LinearLayout) findViewById(R.id.linear9);
		linear6 = (LinearLayout) findViewById(R.id.linear6);
		linear11 = (LinearLayout) findViewById(R.id.linear11);
		linearprogressupload = (LinearLayout) findViewById(R.id.linearprogressupload);
		imageview1 = (ImageView) findViewById(R.id.imageview1);
		textview6 = (TextView) findViewById(R.id.textview6);
		progressupload = (TextView) findViewById(R.id.progressupload);
		textview4 = (TextView) findViewById(R.id.textview4);
		textview5 = (TextView) findViewById(R.id.textview5);
		tombol_camera = (Button) findViewById(R.id.tombol_camera);
		tombol_ambilfile = (Button) findViewById(R.id.tombol_ambilfile);
		deskripsi = (EditText) findViewById(R.id.deskripsi);
		clear_btn = (Button) findViewById(R.id.clear_btn);
		send_btn = (Button) findViewById(R.id.send_btn);
		_uri_camera = FileUtil.getNewPictureUri(getApplicationContext());
		camera.putExtra(MediaStore.EXTRA_OUTPUT, _uri_camera);
		filepick.setType("image/*");
		filepick.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
		identifikasiLogin = getSharedPreferences("identifikasiLogin", Activity.MODE_PRIVATE);
		dialogSuksesUpload = new AlertDialog.Builder(this);
		dialogFailedUpload = new AlertDialog.Builder(this);
		gtr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		
		tombol_camera.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				gtr.vibrate((long)(50));
				startActivityForResult(camera, REQ_CD_CAMERA);
			}
		});
		
		tombol_ambilfile.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				gtr.vibrate((long)(50));
				startActivityForResult(filepick, REQ_CD_FILEPICK);
				path = 0;
			}
		});
		
		clear_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				gtr.vibrate((long)(50));
				judul.setText("");
				deskripsi.setText("");
				imageview1.setImageResource(R.drawable.image_icon);
				textview6.setText("image");
			}
		});
		
		send_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				gtr.vibrate((long)(50));
				if (judul.getText().toString().equals("") && deskripsi.getText().toString().equals("")) {
					SketchwareUtil.showMessage(getApplicationContext(), "Judul dan deskripsi tidak boleh kosong");
				}
				else {
					if (deskripsi.getText().toString().length() < 50) {
						SketchwareUtil.showMessage(getApplicationContext(), "Deskripsi harus lebih dari 50 karakter");
					}
					else {
						linearprogressupload.setVisibility(View.VISIBLE);
						simpanan.child(judul.getText().toString().concat(tgl.getText().toString())).putFile(Uri.fromFile(new File(PATHFOTO))).addOnSuccessListener(_simpanan_upload_success_listener).addOnFailureListener(_simpanan_failure_listener).addOnProgressListener(_simpanan_upload_progress_listener);
					}
				}
			}
		});
		
		_db_child_listener = new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				
			}
			
			@Override
			public void onChildChanged(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				
			}
			
			@Override
			public void onChildMoved(DataSnapshot _param1, String _param2) {
				
			}
			
			@Override
			public void onChildRemoved(DataSnapshot _param1) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				
			}
			
			@Override
			public void onCancelled(DatabaseError _param1) {
				final int _errorCode = _param1.getCode();
				final String _errorMessage = _param1.getMessage();
				SketchwareUtil.showMessage(getApplicationContext(), _errorMessage.concat(" kode eror : ".concat(String.valueOf((long)(_errorCode)))));
			}
		};
		db.addChildEventListener(_db_child_listener);
		
		_simpanan_upload_progress_listener = new OnProgressListener<UploadTask.TaskSnapshot>() {
			@Override
			public void onProgress(UploadTask.TaskSnapshot _param1) {
				double _progressValue = (100.0 * _param1.getBytesTransferred()) / _param1.getTotalByteCount();
				progressupload.setText(String.valueOf((long)(_progressValue)));
			}
		};
		
		_simpanan_download_progress_listener = new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
			@Override
			public void onProgress(FileDownloadTask.TaskSnapshot _param1) {
				double _progressValue = (100.0 * _param1.getBytesTransferred()) / _param1.getTotalByteCount();
				
			}
		};
		
		_simpanan_upload_success_listener = new OnSuccessListener<UploadTask.TaskSnapshot>() {
			@Override
			public void onSuccess(UploadTask.TaskSnapshot _param1) {
				final String _downloadUrl = _param1.getDownloadUrl().toString();
				gtr.vibrate((long)(50));
				linearprogressupload.setVisibility(View.INVISIBLE);
				map = new HashMap<>();
				map.put("urlGambar", _downloadUrl);
				map.put("judul", judul.getText().toString());
				map.put("tgl", tgl.getText().toString());
				map.put("author", identifikasiLogin.getString("emailauth", ""));
				map.put("deskripsi", deskripsi.getText().toString());
				db.push().updateChildren(map);
				dialogSuksesUpload.setTitle("Konfirmasi");
				dialogSuksesUpload.setMessage("Sukses! postinganmu berhasil terkirim");
				dialogSuksesUpload.setPositiveButton("Tutup", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						judul.setText("");
						deskripsi.setText("");
						imageview1.setImageResource(R.drawable.image_icon);
						textview6.setText("image");
					}
				});
				dialogSuksesUpload.create().show();
			}
		};
		
		_simpanan_download_success_listener = new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
			@Override
			public void onSuccess(FileDownloadTask.TaskSnapshot _param1) {
				final long _totalByteCount = _param1.getTotalByteCount();
				
			}
		};
		
		_simpanan_delete_success_listener = new OnSuccessListener() {
			@Override
			public void onSuccess(Object _param1) {
				
			}
		};
		
		_simpanan_failure_listener = new OnFailureListener() {
			@Override
			public void onFailure(Exception _param1) {
				final String _message = _param1.getMessage();
				linearprogressupload.setVisibility(View.INVISIBLE);
				dialogFailedUpload.setTitle("Failed");
				dialogFailedUpload.setMessage(_message);
				dialogFailedUpload.setPositiveButton("Close", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						
					}
				});
				dialogFailedUpload.create().show();
			}
		};
	}
	private void initializeLogic() {
		setTitle("Buat Postingan");
		_FONT_TYPE();
		linearprogressupload.setVisibility(View.INVISIBLE);
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
		
		switch (_requestCode) {
			case REQ_CD_CAMERA:
			if (_resultCode == Activity.RESULT_OK) {
				 String _filePath = "";
				if (_uri_camera != null) {
					_filePath = FileUtil.convertUriToFilePath(getApplicationContext(), _uri_camera);
				}
				imageview1.setImageBitmap(FileUtil.decodeSampleBitmapFromPath(_filePath, 1024, 1024));
				PATHFOTO = _filePath;
				textview6.setText(Uri.parse(PATHFOTO).getLastPathSegment());
			}
			else {
				
			}
			break;
			
			case REQ_CD_FILEPICK:
			if (_resultCode == Activity.RESULT_OK) {
				ArrayList<String> _filePath = new ArrayList<>();
				if (_data != null) {
					if (_data.getClipData() != null) {
						for (int _index = 0; _index < _data.getClipData().getItemCount(); _index++) {
							ClipData.Item _item = _data.getClipData().getItemAt(_index);
							_filePath.add(FileUtil.convertUriToFilePath(getApplicationContext(), _item.getUri()));
						}
					}
					else {
						_filePath.add(FileUtil.convertUriToFilePath(getApplicationContext(), _data.getData()));
					}
				}
				imageview1.setImageBitmap(FileUtil.decodeSampleBitmapFromPath(_filePath.get((int)(path)), 1024, 1024));
				PATHFOTO = _filePath.get((int)(path));
				textview6.setText(Uri.parse(PATHFOTO).getLastPathSegment());
			}
			else {
				
			}
			break;
			default:
			break;
		}
	}
	
	@Override
	public void onBackPressed() {
		iMain.setClass(getApplicationContext(), MainActivity.class);
		iMain.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(iMain);
		finish();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		calender = Calendar.getInstance();
		tgl.setText(new SimpleDateFormat("dd-MM-yyyy").format(calender.getTime()));
	}
	private void _FONT_TYPE () {
		judul.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/oswald_medium.ttf"), 0);
		tgl.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/oswald_medium.ttf"), 0);
		textview6.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/oswald_medium.ttf"), 0);
		progressupload.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/oswald_medium.ttf"), 0);
		textview4.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/oswald_medium.ttf"), 0);
		textview5.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/oswald_medium.ttf"), 0);
		tombol_camera.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/oswald_medium.ttf"), 0);
		tombol_ambilfile.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/oswald_medium.ttf"), 0);
		deskripsi.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/oswald_medium.ttf"), 0);
		clear_btn.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/oswald_medium.ttf"), 0);
		send_btn.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/oswald_medium.ttf"), 0);
	}
	
	
	@Deprecated
	public void showMessage(String _s) {
		Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
	}
	
	@Deprecated
	public int getLocationX(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[0];
	}
	
	@Deprecated
	public int getLocationY(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[1];
	}
	
	@Deprecated
	public int getRandom(int _min, int _max) {
		Random random = new Random();
		return random.nextInt(_max - _min + 1) + _min;
	}
	
	@Deprecated
	public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
		ArrayList<Double> _result = new ArrayList<Double>();
		SparseBooleanArray _arr = _list.getCheckedItemPositions();
		for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
			if (_arr.valueAt(_iIdx))
			_result.add((double)_arr.keyAt(_iIdx));
		}
		return _result;
	}
	
	@Deprecated
	public float getDip(int _input){
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
	}
	
	@Deprecated
	public int getDisplayWidthPixels(){
		return getResources().getDisplayMetrics().widthPixels;
	}
	
	@Deprecated
	public int getDisplayHeightPixels(){
		return getResources().getDisplayMetrics().heightPixels;
	}
	
}
