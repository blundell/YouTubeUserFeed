package com.blundell.tut.ui.phone;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.blundell.tut.R;
import com.blundell.tut.domain.Library;
import com.blundell.tut.service.task.GetYouTubeUserVideosTask;
import com.blundell.tut.ui.widget.VideosListView;

/**
 * The Activity can retrieve Videos for a specific username from YouTube</br>
 * It then displays them into a list including the Thumbnail preview and the title</br>
 * There is a reference to each video on YouTube as well but this isn't used in this tutorial</br>
 * </br>
 * <b>Note<b/> orientation change isn't covered in this tutorial, you will want to override
 * onSaveInstanceState() and onRestoreInstanceState() when you come to this
 * </br>
 * @author paul.blundell
 */
public class MainActivity extends Activity {
    // A reference to our list that will hold the video details
	private VideosListView listView;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        listView = (VideosListView) findViewById(R.id.videosListView);
    }

    // This is the XML onClick listener to retreive a users video feed
    public void getUserYouTubeFeed(View v){
    	// We start a new task that does its work on its own thread
    	// We pass in a handler that will be called when the task has finished
    	// We also pass in the name of the user we are searching YouTube for
    	new GetYouTubeUserVideosTask(responseHandler, "blundellp").run();
    }
   
    // This is the handler that receives the response when the YouTube task has finished
	Handler responseHandler = new Handler() {
		public void handleMessage(Message msg) {
			populateListWithVideos(msg);
		};
	};

	/**
	 * This method retrieves the Library of videos from the task and passes them to our ListView
	 * @param msg
	 */
	private void populateListWithVideos(Message msg) {
		// Retreive the videos are task found from the data bundle sent back
		Library lib = (Library) msg.getData().get(GetYouTubeUserVideosTask.LIBRARY);
		// Because we have created a custom ListView we don't have to worry about setting the adapter in the activity
		// we can just call our custom method with the list of items we want to display
		listView.setVideos(lib.getVideos());
	}
	
	@Override
	protected void onStop() {
		// Make sure we null our handler when the activity has stopped
		// because who cares if we get a callback once the activity has stopped? not me!
		responseHandler = null;
		super.onStop();
	}
}