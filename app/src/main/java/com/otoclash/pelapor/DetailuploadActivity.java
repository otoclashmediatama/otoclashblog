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
import android.widget.ScrollView;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.Intent;
import android.net.Uri;
import android.content.Context;
import android.os.Vibrator;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.storage.OnProgressListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;
import java.io.File;
import android.app.AlertDialog;
import android.content.DialogInterface;
import java.util.Timer;
import java.util.TimerTask;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ChildEventListener;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import android.view.View;
import com.bumptech.glide.Glide;
import android.graphics.Typeface;

public class DetailuploadActivity extends AppCompatActivity {
	
	private Timer _timer = new Timer();
	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
	private FirebaseStorage _firebase_storage = FirebaseStorage.getInstance();
	
	private Toolbar _toolbar;
	private String urlDownloadGambar = "";
	private String FilePathDownload = "";
	private HashMap<String, Object> mapkomen = new HashMap<>();
	
	private ArrayList<HashMap<String, Object>> listKomen = new ArrayList<>();
	
	private LinearLayout linearutama;
	private LinearLayout tampilandetail;
	private LinearLayout tampilangambarpenuh;
	private LinearLayout linear6;
	private LinearLayout linear2;
	private ScrollView vscroll1;
	private ImageView gambarunggulan;
	private LinearLayout linear7;
	private LinearLayout linear8;
	private Button lihatpenuh;
	private Button downloadgambar;
	private Button modemalam;
	private Button modepagi;
	private TextView judul;
	private LinearLayout linear5;
	private LinearLayout linear4;
	private LinearLayout linear3;
	private TextView deskripsi;
	private LinearLayout linear10;
	private ListView listview1;
	private TextView textview3;
	private TextView author;
	private TextView textview2;
	private TextView tgl;
	private LinearLayout linear11;
	private LinearLayout linear14;
	private LinearLayout linear12;
	private EditText usernamekomen;
	private TextView tglkomen;
	private LinearLayout linear13;
	private Button btnkirimkomen;
	private EditText isikomen;
	private LinearLayout linear9;
	private ImageView imageview1;
	private LinearLayout close;
	private Button download_langsung;
	private LinearLayout linearprosesdownload;
	private TextView textview4;
	private Button buttonclose;
	
	private SharedPreferences detailkonten;
	private Intent iHome = new Intent();
	private Intent iDownloadGambar = new Intent();
	private Vibrator gtr;
	private StorageReference simpanan = _firebase_storage.getReference("simpanan");
	private OnSuccessListener<UploadTask.TaskSnapshot> _simpanan_upload_success_listener;
	private OnSuccessListener<FileDownloadTask.TaskSnapshot> _simpanan_download_success_listener;
	private OnSuccessListener _simpanan_delete_success_listener;
	private OnProgressListener _simpanan_upload_progress_listener;
	private OnProgressListener _simpanan_download_progress_listener;
	private OnFailureListener _simpanan_failure_listener;
	private AlertDialog.Builder errorDownload;
	private TimerTask TimerHilang;
	private DatabaseReference komen = _firebase.getReference("komen");
	private ChildEventListener _komen_child_listener;
	private Calendar tglnow = Calendar.getInstance();
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.detailupload);
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
		linearutama = (LinearLayout) findViewById(R.id.linearutama);
		tampilandetail = (LinearLayout) findViewById(R.id.tampilandetail);
		tampilangambarpenuh = (LinearLayout) findViewById(R.id.tampilangambarpenuh);
		linear6 = (LinearLayout) findViewById(R.id.linear6);
		linear2 = (LinearLayout) findViewById(R.id.linear2);
		vscroll1 = (ScrollView) findViewById(R.id.vscroll1);
		gambarunggulan = (ImageView) findViewById(R.id.gambarunggulan);
		linear7 = (LinearLayout) findViewById(R.id.linear7);
		linear8 = (LinearLayout) findViewById(R.id.linear8);
		lihatpenuh = (Button) findViewById(R.id.lihatpenuh);
		downloadgambar = (Button) findViewById(R.id.downloadgambar);
		modemalam = (Button) findViewById(R.id.modemalam);
		modepagi = (Button) findViewById(R.id.modepagi);
		judul = (TextView) findViewById(R.id.judul);
		linear5 = (LinearLayout) findViewById(R.id.linear5);
		linear4 = (LinearLayout) findViewById(R.id.linear4);
		linear3 = (LinearLayout) findViewById(R.id.linear3);
		deskripsi = (TextView) findViewById(R.id.deskripsi);
		linear10 = (LinearLayout) findViewById(R.id.linear10);
		listview1 = (ListView) findViewById(R.id.listview1);
		textview3 = (TextView) findViewById(R.id.textview3);
		author = (TextView) findViewById(R.id.author);
		textview2 = (TextView) findViewById(R.id.textview2);
		tgl = (TextView) findViewById(R.id.tgl);
		linear11 = (LinearLayout) findViewById(R.id.linear11);
		linear14 = (LinearLayout) findViewById(R.id.linear14);
		linear12 = (LinearLayout) findViewById(R.id.linear12);
		usernamekomen = (EditText) findViewById(R.id.usernamekomen);
		tglkomen = (TextView) findViewById(R.id.tglkomen);
		linear13 = (LinearLayout) findViewById(R.id.linear13);
		btnkirimkomen = (Button) findViewById(R.id.btnkirimkomen);
		isikomen = (EditText) findViewById(R.id.isikomen);
		linear9 = (LinearLayout) findViewById(R.id.linear9);
		imageview1 = (ImageView) findViewById(R.id.imageview1);
		close = (LinearLayout) findViewById(R.id.close);
		download_langsung = (Button) findViewById(R.id.download_langsung);
		linearprosesdownload = (LinearLayout) findViewById(R.id.linearprosesdownload);
		textview4 = (TextView) findViewById(R.id.textview4);
		buttonclose = (Button) findViewById(R.id.buttonclose);
		detailkonten = getSharedPreferences("detailkonten", Activity.MODE_PRIVATE);
		gtr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		errorDownload = new AlertDialog.Builder(this);
		
		lihatpenuh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				gtr.vibrate((long)(50));
				tampilandetail.setVisibility(View.GONE);
				tampilangambarpenuh.setVisibility(View.VISIBLE);
				setTitle("Tampilan Gambar Penuh");
			}
		});
		
		downloadgambar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				gtr.vibrate((long)(50));
				iDownloadGambar.setAction(Intent.ACTION_VIEW);
				iDownloadGambar.setData(Uri.parse(detailkonten.getString("gbr", "")));
				startActivity(iDownloadGambar);
			}
		});
		
		modemalam.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				gtr.vibrate((long)(50));
				tampilandetail.setBackgroundColor(0xFF263238);
				judul.setTextColor(0xFFFFFFFF);
				deskripsi.setTextColor(0xFFFFFFFF);
				author.setTextColor(0xFFFFFFFF);
				tgl.setTextColor(0xFFFFFFFF);
				textview3.setTextColor(0xFFFFFFFF);
				textview2.setTextColor(0xFFFFFFFF);
				SketchwareUtil.showMessage(getApplicationContext(), "Berpindah mode malam");
			}
		});
		
		modepagi.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				gtr.vibrate((long)(50));
				tampilandetail.setBackgroundColor(0xFFFFFFFF);
				judul.setTextColor(0xFF3F51B5);
				deskripsi.setTextColor(0xFF000000);
				author.setTextColor(0xFFFF5722);
				tgl.setTextColor(0xFF795548);
				textview3.setTextColor(0xFFFF5722);
				textview2.setTextColor(0xFF795548);
				SketchwareUtil.showMessage(getApplicationContext(), "Berpindah mode pagi");
			}
		});
		
		btnkirimkomen.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (usernamekomen.getText().toString().equals("") && isikomen.getText().toString().equals("")) {
					SketchwareUtil.showMessage(getApplicationContext(), "Pastikan data terisi semua");
				}
				else {
					mapkomen = new HashMap<>();
					mapkomen.put("judul", judul.getText().toString());
					mapkomen.put("username", usernamekomen.getText().toString());
					mapkomen.put("isikomen", isikomen.getText().toString());
					mapkomen.put("tglkomen", tglkomen.getText().toString());
					komen.push().updateChildren(mapkomen);
				}
			}
		});
		
		close.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				gtr.vibrate((long)(50));
				tampilandetail.setVisibility(View.VISIBLE);
				tampilangambarpenuh.setVisibility(View.GONE);
				setTitle("Detail Artikel");
			}
		});
		
		download_langsung.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				_firebase_storage.getReferenceFromUrl(detailkonten.getString("gbr", "")).getFile(new File("/storage/emulated/0/pelapor/download/image".concat("/".concat(judul.getText().toString().concat(tgl.getText().toString().concat(".jpg")))))).addOnSuccessListener(_simpanan_download_success_listener).addOnFailureListener(_simpanan_failure_listener).addOnProgressListener(_simpanan_download_progress_listener);
			}
		});
		
		buttonclose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				gtr.vibrate((long)(50));
				tampilandetail.setVisibility(View.VISIBLE);
				tampilangambarpenuh.setVisibility(View.GONE);
				setTitle("Detail Artikel");
			}
		});
		
		_simpanan_upload_progress_listener = new OnProgressListener<UploadTask.TaskSnapshot>() {
			@Override
			public void onProgress(UploadTask.TaskSnapshot _param1) {
				double _progressValue = (100.0 * _param1.getBytesTransferred()) / _param1.getTotalByteCount();
				
			}
		};
		
		_simpanan_download_progress_listener = new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
			@Override
			public void onProgress(FileDownloadTask.TaskSnapshot _param1) {
				double _progressValue = (100.0 * _param1.getBytesTransferred()) / _param1.getTotalByteCount();
				linearprosesdownload.setVisibility(View.VISIBLE);
				download_langsung.setVisibility(View.GONE);
				textview4.setText(String.valueOf((long)(_progressValue)).concat("%"));
			}
		};
		
		_simpanan_upload_success_listener = new OnSuccessListener<UploadTask.TaskSnapshot>() {
			@Override
			public void onSuccess(UploadTask.TaskSnapshot _param1) {
				final String _downloadUrl = _param1.getDownloadUrl().toString();
				
			}
		};
		
		_simpanan_download_success_listener = new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
			@Override
			public void onSuccess(FileDownloadTask.TaskSnapshot _param1) {
				final long _totalByteCount = _param1.getTotalByteCount();
				textview4.setText("✓ Download sukses, ukuran : ".concat(String.valueOf((long)(_totalByteCount)).concat(" Byte".concat(". Tersimpan di : ".concat("/storage/emulated/0/pelapor/download/image")))));
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
				errorDownload.setTitle("Error");
				errorDownload.setMessage(_message);
				errorDownload.setPositiveButton("Close", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						
					}
				});
				errorDownload.create().show();
			}
		};
		
		_komen_child_listener = new ChildEventListener() {
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
				
			}
		};
		komen.addChildEventListener(_komen_child_listener);
	}
	private void initializeLogic() {
		_FONT_TYPE();
		setTitle("Detail Artikel");
		if (FileUtil.isExistFile("/storage/emulated/0/pelapor/download/image")) {
			
		}
		else {
			FileUtil.makeDir("/storage/emulated/0/pelapor/download/image");
		}
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
		
		switch (_requestCode) {
			
			default:
			break;
		}
	}
	
	@Override
	protected void onPostCreate(Bundle _savedInstanceState) {
		super.onPostCreate(_savedInstanceState);
		tglnow = Calendar.getInstance();
		tglkomen.setText(new SimpleDateFormat("dd-MM-yyyy").format(tglnow.getTime()));
		Glide.with(getApplicationContext()).load(Uri.parse(detailkonten.getString("gbr", ""))).into(gambarunggulan);
		Glide.with(getApplicationContext()).load(Uri.parse(detailkonten.getString("gbr", ""))).into(imageview1);
		judul.setText(detailkonten.getString("jdl", ""));
		tgl.setText(detailkonten.getString("tgl", ""));
		author.setText(detailkonten.getString("auth", ""));
		deskripsi.setText(detailkonten.getString("des", ""));
		linearprosesdownload.setVisibility(View.GONE);
		komen.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot _dataSnapshot) {
				listKomen = new ArrayList<>();
				try {
					GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
					for (DataSnapshot _data : _dataSnapshot.getChildren()) {
						HashMap<String, Object> _map = _data.getValue(_ind);
						listKomen.add(_map);
					}
				}
				catch (Exception _e) {
					_e.printStackTrace();
				}
				listview1.setAdapter(new Listview1Adapter(listKomen));
				((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
			}
			@Override
			public void onCancelled(DatabaseError _databaseError) {
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		iHome.setClass(getApplicationContext(), MainActivity.class);
		iHome.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(iHome);
		finish();
	}
	private void _FONT_TYPE () {
		lihatpenuh.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/oswald_medium.ttf"), 0);
		downloadgambar.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/oswald_medium.ttf"), 0);
		judul.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/oswald_medium.ttf"), 0);
		deskripsi.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/oswald_medium.ttf"), 0);
		textview3.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/oswald_medium.ttf"), 0);
		author.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/oswald_medium.ttf"), 0);
		textview2.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/oswald_medium.ttf"), 0);
		tgl.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/oswald_medium.ttf"), 0);
		buttonclose.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/oswald_medium.ttf"), 0);
	}
	
	
	public class Listview1Adapter extends BaseAdapter {
		ArrayList<HashMap<String, Object>> _data;
		public Listview1Adapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}
		
		@Override
		public int getCount() {
			return _data.size();
		}
		
		@Override
		public HashMap<String, Object> getItem(int _index) {
			return _data.get(_index);
		}
		
		@Override
		public long getItemId(int _index) {
			return _index;
		}
		@Override
		public View getView(final int _position, View _view, ViewGroup _viewGroup) {
			LayoutInflater _inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View _v = _view;
			if (_v == null) {
				_v = _inflater.inflate(R.layout.listkomen, null);
			}
			
			final LinearLayout linear1 = (LinearLayout) _v.findViewById(R.id.linear1);
			final LinearLayout linear2 = (LinearLayout) _v.findViewById(R.id.linear2);
			final LinearLayout linear4 = (LinearLayout) _v.findViewById(R.id.linear4);
			final TextView tglkirimkomen = (TextView) _v.findViewById(R.id.tglkirimkomen);
			final ImageView imageview1 = (ImageView) _v.findViewById(R.id.imageview1);
			final TextView userkomentar = (TextView) _v.findViewById(R.id.userkomentar);
			final ImageView imageview2 = (ImageView) _v.findViewById(R.id.imageview2);
			final LinearLayout linear5 = (LinearLayout) _v.findViewById(R.id.linear5);
			final TextView isikomentar = (TextView) _v.findViewById(R.id.isikomentar);
			
			if (listKomen.get((int)_position).get("judul").toString().equals(detailkonten.getString("jdl", ""))) {
				userkomentar.setText(listKomen.get((int)_position).get("username").toString());
				isikomentar.setText(listKomen.get((int)_position).get("isikomen").toString());
				tglkirimkomen.setText(listKomen.get((int)_position).get("tglkomen").toString());
			}
			else {
				linear1.setVisibility(View.GONE);
			}
			
			return _v;
		}
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
