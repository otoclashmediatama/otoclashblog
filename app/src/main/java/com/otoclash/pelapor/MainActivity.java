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
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.HashMap;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.Button;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.Intent;
import android.net.Uri;
import android.content.Context;
import android.os.Vibrator;
import android.widget.AdapterView;
import android.view.View;
import android.text.Editable;
import android.text.TextWatcher;
import com.bumptech.glide.Glide;
import android.graphics.Typeface;

public class MainActivity extends AppCompatActivity {
	
	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
	
	private Toolbar _toolbar;
	private FloatingActionButton _fab;
	private DrawerLayout _drawer;
	private String emailUser = "";
	private String passwordUser = "";
	private double klikback = 0;
	private String uid = "";
	
	private ArrayList<HashMap<String, Object>> listuploadtan = new ArrayList<>();
	
	private LinearLayout linear1;
	private LinearLayout linear2;
	private ListView listview1;
	private LinearLayout tampilanbelumlogin;
	private LinearLayout linear3;
	private LinearLayout linear4;
	private TextView textview1;
	private TextView emailakun;
	private TextView textview2;
	private TextView uid_akun;
	private LinearLayout linear6;
	private ImageView imageview1;
	private TextView textview3;
	private LinearLayout _drawer_linear1;
	private LinearLayout _drawer_linear4;
	private LinearLayout _drawer_linear8;
	private ImageView _drawer_imageview1;
	private LinearLayout _drawer_linear5;
	private LinearLayout _drawer_linear6;
	private LinearLayout _drawer_linear7;
	private LinearLayout _drawer_linearexit;
	private EditText _drawer_edittext_email;
	private EditText _drawer_edittext_password;
	private Button _drawer_button_daftar;
	private Button _drawer_button_masuk;
	private Button _drawer_button_reset;
	private Button _drawer_exit;
	
	private DatabaseReference db = _firebase.getReference("db");
	private ChildEventListener _db_child_listener;
	private FirebaseAuth auth;
	private OnCompleteListener<AuthResult> _auth_create_user_listener;
	private OnCompleteListener<AuthResult> _auth_sign_in_listener;
	private OnCompleteListener<Void> _auth_reset_password_listener;
	private SharedPreferences identifikasiLogin;
	private Intent iKontribusi = new Intent();
	private Intent iDetail = new Intent();
	private SharedPreferences detailkonten;
	private Vibrator gtr;
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.main);
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
		_fab = (FloatingActionButton) findViewById(R.id._fab);
		
		_drawer = (DrawerLayout) findViewById(R.id._drawer);ActionBarDrawerToggle _toggle = new ActionBarDrawerToggle(MainActivity.this, _drawer, _toolbar, R.string.app_name, R.string.app_name);
		_drawer.addDrawerListener(_toggle);
		_toggle.syncState();
		
		LinearLayout _nav_view = (LinearLayout) findViewById(R.id._nav_view);
		
		linear1 = (LinearLayout) findViewById(R.id.linear1);
		linear2 = (LinearLayout) findViewById(R.id.linear2);
		listview1 = (ListView) findViewById(R.id.listview1);
		tampilanbelumlogin = (LinearLayout) findViewById(R.id.tampilanbelumlogin);
		linear3 = (LinearLayout) findViewById(R.id.linear3);
		linear4 = (LinearLayout) findViewById(R.id.linear4);
		textview1 = (TextView) findViewById(R.id.textview1);
		emailakun = (TextView) findViewById(R.id.emailakun);
		textview2 = (TextView) findViewById(R.id.textview2);
		uid_akun = (TextView) findViewById(R.id.uid_akun);
		linear6 = (LinearLayout) findViewById(R.id.linear6);
		imageview1 = (ImageView) findViewById(R.id.imageview1);
		textview3 = (TextView) findViewById(R.id.textview3);
		_drawer_linear1 = (LinearLayout) _nav_view.findViewById(R.id.linear1);
		_drawer_linear4 = (LinearLayout) _nav_view.findViewById(R.id.linear4);
		_drawer_linear8 = (LinearLayout) _nav_view.findViewById(R.id.linear8);
		_drawer_imageview1 = (ImageView) _nav_view.findViewById(R.id.imageview1);
		_drawer_linear5 = (LinearLayout) _nav_view.findViewById(R.id.linear5);
		_drawer_linear6 = (LinearLayout) _nav_view.findViewById(R.id.linear6);
		_drawer_linear7 = (LinearLayout) _nav_view.findViewById(R.id.linear7);
		_drawer_linearexit = (LinearLayout) _nav_view.findViewById(R.id.linearexit);
		_drawer_edittext_email = (EditText) _nav_view.findViewById(R.id.edittext_email);
		_drawer_edittext_password = (EditText) _nav_view.findViewById(R.id.edittext_password);
		_drawer_button_daftar = (Button) _nav_view.findViewById(R.id.button_daftar);
		_drawer_button_masuk = (Button) _nav_view.findViewById(R.id.button_masuk);
		_drawer_button_reset = (Button) _nav_view.findViewById(R.id.button_reset);
		_drawer_exit = (Button) _nav_view.findViewById(R.id.exit);
		auth = FirebaseAuth.getInstance();
		identifikasiLogin = getSharedPreferences("identifikasiLogin", Activity.MODE_PRIVATE);
		detailkonten = getSharedPreferences("detailkonten", Activity.MODE_PRIVATE);
		gtr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		
		listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				gtr.vibrate((long)(50));
				detailkonten.edit().putString("gbr", listuploadtan.get((int)_position).get("urlGambar").toString()).commit();
				detailkonten.edit().putString("jdl", listuploadtan.get((int)_position).get("judul").toString()).commit();
				detailkonten.edit().putString("tgl", listuploadtan.get((int)_position).get("tgl").toString()).commit();
				detailkonten.edit().putString("auth", listuploadtan.get((int)_position).get("author").toString()).commit();
				detailkonten.edit().putString("des", listuploadtan.get((int)_position).get("deskripsi").toString()).commit();
				iDetail.setClass(getApplicationContext(), DetailuploadActivity.class);
				iDetail.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(iDetail);
				finish();
			}
		});
		
		_fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				gtr.vibrate((long)(50));
				if (identifikasiLogin.getString("akses", "").equals("ok")) {
					iKontribusi.setClass(getApplicationContext(), KontribusiActivity.class);
					iKontribusi.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					startActivity(iKontribusi);
					finish();
				}
				else {
					SketchwareUtil.showMessage(getApplicationContext(), "Anda harus masuk dulu");
					_drawer.openDrawer(GravityCompat.START);
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
				
			}
		};
		db.addChildEventListener(_db_child_listener);
		
		_drawer_linearexit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if ((FirebaseAuth.getInstance().getCurrentUser() != null)) {
					gtr.vibrate((long)(50));
					identifikasiLogin.edit().remove("akses").commit();
					FirebaseAuth.getInstance().signOut();
					finish();
				}
				else {
					SketchwareUtil.showMessage(getApplicationContext(), "Anda Belum Login Sebelumnya, tekan saja kembali untuk keluar");
				}
			}
		});
		
		_drawer_edittext_email.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				final String _charSeq = _param1.toString();
				emailUser = _charSeq;
			}
			
			@Override
			public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				
			}
			
			@Override
			public void afterTextChanged(Editable _param1) {
				
			}
		});
		
		_drawer_edittext_password.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				final String _charSeq = _param1.toString();
				passwordUser = _charSeq;
			}
			
			@Override
			public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				
			}
			
			@Override
			public void afterTextChanged(Editable _param1) {
				
			}
		});
		
		_drawer_button_daftar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				gtr.vibrate((long)(50));
				if (emailUser.equals("") && passwordUser.equals("")) {
					SketchwareUtil.showMessage(getApplicationContext(), "Email atau password tidak boleh kosong");
				}
				else {
					auth.createUserWithEmailAndPassword(emailUser, passwordUser).addOnCompleteListener(MainActivity.this, _auth_create_user_listener);
				}
			}
		});
		
		_drawer_button_masuk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				gtr.vibrate((long)(50));
				if (emailUser.equals("") && passwordUser.equals("")) {
					SketchwareUtil.showMessage(getApplicationContext(), "Email atau password tidak boleh kosong");
				}
				else {
					auth.signInWithEmailAndPassword(emailUser, passwordUser).addOnCompleteListener(MainActivity.this, _auth_sign_in_listener);
				}
			}
		});
		
		_drawer_button_reset.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				gtr.vibrate((long)(50));
				if (emailUser.equals("")) {
					SketchwareUtil.showMessage(getApplicationContext(), "Harap isi email anda");
				}
				else {
					auth.sendPasswordResetEmail(emailUser).addOnCompleteListener(_auth_reset_password_listener);
				}
			}
		});
		
		_drawer_exit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if ((FirebaseAuth.getInstance().getCurrentUser() != null)) {
					gtr.vibrate((long)(50));
					identifikasiLogin.edit().remove("akses").commit();
					FirebaseAuth.getInstance().signOut();
					finish();
				}
				else {
					SketchwareUtil.showMessage(getApplicationContext(), "Anda Belum Login Sebelumnya, tekan saja kembali untuk keluar");
				}
			}
		});
		
		_auth_create_user_listener = new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(Task<AuthResult> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				if (_success) {
					SketchwareUtil.showMessage(getApplicationContext(), "Lanjut masuk");
				}
				else {
					SketchwareUtil.showMessage(getApplicationContext(), _errorMessage);
				}
			}
		};
		
		_auth_sign_in_listener = new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(Task<AuthResult> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				if (_success) {
					db.addChildEventListener(_db_child_listener);
					uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
					db.addListenerForSingleValueEvent(new ValueEventListener() {
						@Override
						public void onDataChange(DataSnapshot _dataSnapshot) {
							listuploadtan = new ArrayList<>();
							try {
								GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
								for (DataSnapshot _data : _dataSnapshot.getChildren()) {
									HashMap<String, Object> _map = _data.getValue(_ind);
									listuploadtan.add(_map);
								}
							}
							catch (Exception _e) {
								_e.printStackTrace();
							}
							listview1.setAdapter(new Listview1Adapter(listuploadtan));
							((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
						}
						@Override
						public void onCancelled(DatabaseError _databaseError) {
						}
					});
					identifikasiLogin.edit().putString("akses", "ok").commit();
					identifikasiLogin.edit().putString("emailauth", FirebaseAuth.getInstance().getCurrentUser().getEmail()).commit();
					identifikasiLogin.edit().putString("uid", FirebaseAuth.getInstance().getCurrentUser().getUid()).commit();
					emailakun.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
					uid_akun.setText(FirebaseAuth.getInstance().getCurrentUser().getUid());
					SketchwareUtil.showMessage(getApplicationContext(), "Berhasil Masuk");
					_drawer.closeDrawer(GravityCompat.START);
					listview1.setVisibility(View.VISIBLE);
					tampilanbelumlogin.setVisibility(View.GONE);
				}
				else {
					SketchwareUtil.showMessage(getApplicationContext(), _errorMessage);
					identifikasiLogin.edit().putString("akses", "ng").commit();
					listview1.setVisibility(View.GONE);
					tampilanbelumlogin.setVisibility(View.VISIBLE);
				}
			}
		};
		
		_auth_reset_password_listener = new OnCompleteListener<Void>() {
			@Override
			public void onComplete(Task<Void> _param1) {
				final boolean _success = _param1.isSuccessful();
				if (_success) {
					SketchwareUtil.showMessage(getApplicationContext(), "Link reset password berhasil dikirimkan, cek pesan masuk email anda");
					finish();
				}
				else {
					SketchwareUtil.showMessage(getApplicationContext(), "Ada yang salah");
				}
			}
		};
	}
	private void initializeLogic() {
		setTitle("Beranda");
		_FONT_TYPE();
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
	public void onBackPressed() {
		klikback++;
		if (klikback == 1) {
			SketchwareUtil.showMessage(getApplicationContext(), "Tekan sekali lagi");
		}
		else {
			if (klikback == 2) {
				finish();
			}
		}
	}
	
	@Override
	public void onStart() {
		super.onStart();
		if ((FirebaseAuth.getInstance().getCurrentUser() != null)) {
			db.addChildEventListener(_db_child_listener);
			emailakun.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
			uid_akun.setText(FirebaseAuth.getInstance().getCurrentUser().getUid());
			uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
			db.addListenerForSingleValueEvent(new ValueEventListener() {
				@Override
				public void onDataChange(DataSnapshot _dataSnapshot) {
					listuploadtan = new ArrayList<>();
					try {
						GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
						for (DataSnapshot _data : _dataSnapshot.getChildren()) {
							HashMap<String, Object> _map = _data.getValue(_ind);
							listuploadtan.add(_map);
						}
					}
					catch (Exception _e) {
						_e.printStackTrace();
					}
					listview1.setAdapter(new Listview1Adapter(listuploadtan));
					((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
				}
				@Override
				public void onCancelled(DatabaseError _databaseError) {
				}
			});
			listview1.setVisibility(View.VISIBLE);
			tampilanbelumlogin.setVisibility(View.GONE);
		}
		else {
			emailakun.setText("Anonim");
			uid_akun.setText("-");
			listview1.setVisibility(View.GONE);
			tampilanbelumlogin.setVisibility(View.VISIBLE);
		}
	}
	private void _FONT_TYPE () {
		textview1.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/oswald_medium.ttf"), 0);
		emailakun.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/oswald_medium.ttf"), 0);
		textview2.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/oswald_medium.ttf"), 0);
		uid_akun.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/oswald_medium.ttf"), 0);
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
				_v = _inflater.inflate(R.layout.listview_c, null);
			}
			
			final LinearLayout linear7 = (LinearLayout) _v.findViewById(R.id.linear7);
			final LinearLayout linear3 = (LinearLayout) _v.findViewById(R.id.linear3);
			final LinearLayout linear8 = (LinearLayout) _v.findViewById(R.id.linear8);
			final LinearLayout linear10 = (LinearLayout) _v.findViewById(R.id.linear10);
			final LinearLayout linear11 = (LinearLayout) _v.findViewById(R.id.linear11);
			final TextView tgl_upload = (TextView) _v.findViewById(R.id.tgl_upload);
			final TextView textview1 = (TextView) _v.findViewById(R.id.textview1);
			final TextView author = (TextView) _v.findViewById(R.id.author);
			final LinearLayout linear9 = (LinearLayout) _v.findViewById(R.id.linear9);
			final TextView judul = (TextView) _v.findViewById(R.id.judul);
			final TextView textview3 = (TextView) _v.findViewById(R.id.textview3);
			final ImageView gambarutama = (ImageView) _v.findViewById(R.id.gambarutama);
			final TextView deskripsi = (TextView) _v.findViewById(R.id.deskripsi);
			final TextView textview4 = (TextView) _v.findViewById(R.id.textview4);
			
			Glide.with(getApplicationContext()).load(Uri.parse(listuploadtan.get((int)_position).get("urlGambar").toString())).into(gambarutama);
			judul.setText(listuploadtan.get((int)_position).get("judul").toString().toUpperCase());
			tgl_upload.setText(listuploadtan.get((int)_position).get("tgl").toString());
			author.setText(listuploadtan.get((int)_position).get("author").toString());
			deskripsi.setText(listuploadtan.get((int)_position).get("deskripsi").toString().substring((int)(0), (int)(50)).concat("......"));
			
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
