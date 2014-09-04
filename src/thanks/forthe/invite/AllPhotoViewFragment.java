package thanks.forthe.invite;

import java.util.ArrayList;

import thanks.forthe.text.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AllPhotoViewFragment extends Fragment{
	
	private ArrayAdapter<Bitmap> adapterOne;
	private ArrayAdapter<Bitmap> adapterTwo;
	
	public void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
			
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){

		View rootView = inflater.inflate(R.layout.photo_album_fragment, container,
				false);
		ListView firstList = (ListView) rootView.findViewById(R.id.first_list_view);
		ListView secondList = (ListView) rootView.findViewById(R.id.second_list_view);
		
		ArrayList<Bitmap> listOfPics = new ArrayList<Bitmap>();
		Bitmap wood = BitmapFactory.decodeResource(this.getResources(),R.drawable.wooden_background);
		Bitmap city = BitmapFactory.decodeResource(this.getResources(),R.drawable.time_square_background);
		Bitmap people = BitmapFactory.decodeResource(this.getResources(),R.drawable.running_at_the_beach_background);
		listOfPics.add(wood);
		listOfPics.add(city);
		listOfPics.add(people);
		
		adapterOne = new PhotoImageArrayAdapter(this.getActivity(), listOfPics);
		adapterTwo = new PhotoImageArrayAdapter(this.getActivity(), listOfPics);
		
		firstList.setAdapter(adapterOne);
		secondList.setAdapter(adapterTwo);
		
		return rootView;
		
	}

}
