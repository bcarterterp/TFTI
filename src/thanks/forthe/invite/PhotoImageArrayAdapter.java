package thanks.forthe.invite;

import java.util.ArrayList;

import thanks.forthe.text.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class PhotoImageArrayAdapter extends ArrayAdapter<Bitmap>{
	
	private Context context;
	private ArrayList<Bitmap> pictures;
	
	public PhotoImageArrayAdapter(Context context,ArrayList<Bitmap> pictures){
		
		super(context,R.layout.photo_list_row,pictures);
		this.context = context;
		this.pictures = pictures;
		
	}
	
	public View getView(final int position,View convertView,ViewGroup parent){
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.photo_list_row, parent, false);
		
		ImageView picture = (ImageView) rowView.findViewById(R.id.photo_picture);
		picture.setImageBitmap(pictures.get(position));
		
		return rowView;
		
	}

}
