package thanks.forthe.invite;

import android.graphics.drawable.BitmapDrawable;

public class AsyncTaskBitmapEvent {
	
	private BitmapDrawable image;
	
	public AsyncTaskBitmapEvent(BitmapDrawable image){
		this.image = image;
	}
	
	public BitmapDrawable getResult(){
		return image;
	}

}
