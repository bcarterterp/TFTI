package thanks.forthe.invite;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

import com.parse.ParseUser;

import thanks.forthe.text.R;
import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Bitmap.CompressFormat;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

public class FirstCreateEventFragment extends Fragment implements OnClickListener{
	
	private NewEventCallback onCallback;
	private ImageView select_image;
	private Bitmap image;
	private TextView event_date,event_time;
	private EditText event_name,event_location,event_desc;
	private static final int SELECT_PHOTO = 100;
	private int year,month,day;
	private int hours,minutes;
	private String ampm,name,location,description;
	private boolean secret;
	
	
	public void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
		if(savedInstanceState == null){
			year = 0000;
			hours = 0000;
			secret = false;
			
		}else{
			recreate(savedInstanceState);
		}
		
	}
	
	private void recreate(Bundle savedInstanceState) {
		
		
			name = savedInstanceState.getString("event_name");
			description = savedInstanceState.getString("event_desc");
			location = savedInstanceState.getString("event_location");
			year = savedInstanceState.getInt("year", 0);
			month = savedInstanceState.getInt("month", 0);
			day = savedInstanceState.getInt("day", 0);

			hours = savedInstanceState.getInt("hours", 0);
			minutes = savedInstanceState.getInt("minutes", 0);
			
			byte[] data = savedInstanceState.getByteArray("image");
			if(data != null){
				image = BitmapFactory.decodeByteArray(data, 0, data.length);
			}
		
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){

		View rootView = inflater.inflate(R.layout.first_new_event_fragment, container,
				false);
		Button next_button = (Button) rootView.findViewById(R.id.next_button);
		select_image = (ImageView) rootView.findViewById(R.id.user_image);
		event_date = (TextView)rootView.findViewById(R.id.text_date);
		event_time = (TextView)rootView.findViewById(R.id.text_time);
		event_name = (EditText)rootView.findViewById(R.id.event_name_text);
		event_location = (EditText)rootView.findViewById(R.id.event_location_text);
		event_desc = (EditText)rootView.findViewById(R.id.event_desc);
		ToggleButton privacy = (ToggleButton)rootView.findViewById(R.id.privacy);
		event_time.setOnClickListener(this);
		event_date.setOnClickListener(this);
		select_image.setOnClickListener(this);
		next_button.setOnClickListener(this);
		
		privacy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				if(isChecked){
					secret = true;
				}else{
					secret = false;
				}
				
			}
		});
	
		return rootView;
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.next_button:
			if(event_name.getText().toString().length() > 1){
				if(year == 0 || hours == 0){
					Toast.makeText(getActivity(), "Set a date AND time!!", Toast.LENGTH_LONG).show();
				}else{
					if(event_location.getText().toString().length() > 1){
						sendInfo();
						onCallback.next_screen(2);
					}else{
						Toast.makeText(getActivity(), "Enter a location!", Toast.LENGTH_LONG).show();
					}
				}
			}else{
				Toast.makeText(getActivity(), "Name your event!", Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.user_image:
			selectImage();
			break;
		case R.id.text_date:
			showDatePicker();
			break;
		case R.id.text_time:
			showTimePicker();
			break;
		default:
			break;
		}
		
	}
	
	private void sendInfo() {
		
		description = event_desc.getText().toString();
		name = event_name.getText().toString();
		location = event_location.getText().toString();
		onCallback.screenOneInfo(year, month, day, hours, minutes, 
				ampm, name, location, description, image,secret);
		
	}

	private void selectImage(){
		
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(photoPickerIntent, SELECT_PHOTO);
		
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent){
		super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
		
		switch(requestCode){
		
		case SELECT_PHOTO:
			if(resultCode == this.getActivity().RESULT_OK){
		
				Uri selectedImage = imageReturnedIntent.getData();
				String UriPath = getRealPathFromURI(getActivity(), selectedImage);
				Matrix matrix = new Matrix();
				matrix.postRotate(getImageRotation(UriPath));
				InputStream imageStream;
				try {
					imageStream = this.getActivity().getContentResolver().openInputStream(selectedImage);
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inSampleSize = 2;
					image = BitmapFactory.decodeStream(imageStream,null, options);
					image = Bitmap.createBitmap(image,0,0,image.getWidth(),image.getHeight(),matrix,true);
					select_image.setImageBitmap(image);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
				}
				
			}else{
			}
			break;
		}
		
	}
	
	private String getRealPathFromURI(Context context,Uri contentUri){
		Cursor cursor = null;
		try{
			String[] proj = {MediaStore.Images.Media.DATA};
			cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		}finally{
			if(cursor !=null){
				cursor.close();
			}
		}
	}
	
	private int getImageRotation(String path) {
		int rotate = 0;
		try{
			ExifInterface exif = new ExifInterface(path);
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch(orientation){
			case ExifInterface.ORIENTATION_ROTATE_90:
				rotate = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				rotate = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				rotate = 270;
				break;
			}
		}catch(IOException e){
			
		}
		return rotate;
	}
	
	private void showDatePicker(){
		
		DatePickerFragment date = new DatePickerFragment();
		Calendar calendar = Calendar.getInstance();
		Bundle args = new Bundle();
		args.putInt("year", calendar.get(Calendar.YEAR));
		args.putInt("month", calendar.get(Calendar.MONTH));
		args.putInt("day", calendar.get(Calendar.DAY_OF_MONTH));
		date.setArguments(args);
		date.setCallBack(onDate);
		date.show(getActivity().getSupportFragmentManager(), "Date Picker");
		
		
	}
	
	private void showTimePicker(){
		
		TimePickerFragment time = new TimePickerFragment();
		Calendar calendar = Calendar.getInstance();
		Bundle args = new Bundle();
		args.putInt("hour", calendar.get(Calendar.HOUR));
		args.putInt("minute", calendar.get(Calendar.MINUTE));
		time.setArguments(args);
		time.setCallBack(onTime);
		time.show(getActivity().getSupportFragmentManager(), "Time Picker");
		
		
	}
	
	public void updateDate(int year,int month,int day){
		event_date.setText((month+1)+"/"+day+"/"+year);
		this.year = year;
		this.month = month;
		this.day = day;
	}
	
	public void updateTime(int hour,int minute){
		
		String sAMPM = "AM";
		this.hours = hour;
		this.minutes = minute;
		
		if(hour >= 12){
			sAMPM = "PM";
			if(hour > 12){
				hour = hour-12;
			}
		}
		if(hour == 0){
			hour = 12;
		}
		
		this.hours = hour;
		this.minutes = minute;
		
		if(minute < 10){
			event_time.setText(hour+":0"+minute+sAMPM);
			
		}else{
			event_time.setText(hour+":"+minute+sAMPM);
		}

		ampm = sAMPM;
		
	}

	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			onCallback = (NewEventCallback) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(
					"Activity must implement NewEventCallback.");
		}
	}
	
	private OnDateSetListener onDate = new OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			updateDate(year, monthOfYear, dayOfMonth);
			
		}
	};
	
	private OnTimeSetListener onTime = new OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			
			updateTime(hourOfDay, minute);
			
		}
		
	};
	
	public interface NewEventCallback{
		
		public void next_screen(int step);
		public void screenOneInfo(int year,int month,int day,
				int hour,int minute,String ampm,String name,
				String loc,String desc,Bitmap img,boolean secret);
		public void screenTwoInfo(ArrayList<ParseUser>names);
		public Bitmap passImage();
		public ArrayList<ParseUser> passUsers();
		
	}
	
	public void onSaveInstanceState(Bundle outState){
		
		super.onSaveInstanceState(outState);
		
		if(event_name.getText().toString().length() > 0){
			outState.putString("event_name", event_name.getText().toString());
		}
		if(event_desc.getText().toString().length() > 0){
			outState.putString("event_desc", event_desc.getText().toString());
		}
		if(event_location.getText().toString().length() > 0){
			outState.putString("event_location", event_location.getText().toString());
		}
		if(year != 0){
			outState.putInt("year", year);
			outState.putInt("month", month);
			outState.putInt("day", day);
		}
		if(hours != 0){
			outState.putInt("hours", hours);
			outState.putInt("minutes", minutes);
		}
		if(image != null){
			ByteArrayOutputStream used_image = new ByteArrayOutputStream();
			image.compress(CompressFormat.PNG, 0, used_image);
			byte[] bitmapdata = used_image.toByteArray();
			outState.putByteArray("image", bitmapdata);
		}
		
	}
	
	@Override
	public void onPause() {
		
		super.onPause();
		
	}
	
	public void onResume(){
		
		super.onResume();

		if(name != null){
			event_name.setText(name);
		}
		if(location != null){
			event_location.setText(location);
		}
		if(description != null){
			event_desc.setText(description);
		}
		
		if(year != 0){
			event_date.setText(month+"/"+day+"/"+year);
		}
		if(hours != 0){
			event_time.setText(hours+":"+minutes+ampm);
		}
		if(image != null){
			select_image.setImageBitmap(image);
		}
	}


}
