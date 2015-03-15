package hoopsnake.geosource.data;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import ServerClientShared.VideoFieldWithContent;
import hoopsnake.geosource.IncidentActivity;
import hoopsnake.geosource.R;
import hoopsnake.geosource.media.MediaManagement;

/**
 * Created by wsv759 on 07/03/15.
 *
 * Implementation of an app field with type Video.
 */
public class AppVideoField extends AbstractAppFieldWithContentAndFile {

    public AppVideoField(VideoFieldWithContent fieldToWrap, IncidentActivity activity) {
        super(fieldToWrap, activity);
    }

    @Override
    public boolean usesFilesOfType(Uri contentFileUri) {
        //TODO implement this.
        return true;
    }

    @Override
    public View getContentViewRepresentation(final int requestCodeForIntent) {
        ImageView iv = (ImageView) activity.getLayoutInflater().inflate(R.layout.field_image_view, null);


        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                activity.doIfClickable(new Runnable() {
                    @Override
                    public void run() {
                        //Make sure the activity knows which view was clicked.
                        activity.setCurFieldIdx((int) v.getTag());

                        Uri fileUriForNewVideo = MediaManagement.getOutputVideoFileUri();
                        if (fileUriForNewVideo == null)
                        {
                            Toast.makeText(activity, "Cannot take video; new image file could not be created on external storage device.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        MediaManagement.startCameraActivityForVideo(activity, requestCodeForIntent, fileUriForNewVideo);
                    }
                });
            }
        });

        return iv;
    }

    @Override
    public void onResultFromSelection(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            // Video captured and saved to fileUri specified in the Intent
            Toast.makeText(activity, "Video saved to:\n" + getContentFileUri(), Toast.LENGTH_LONG).show();

            //TODO display the video in its field! This means notifying the UI.
            //TODO set the content of this field appropriately. Probably in a background task?


        } else if (resultCode == Activity.RESULT_CANCELED) {
            // User cancelled the video capture
        } else {
            // Video capture failed, advise user
            Toast.makeText(activity, "Failed to capture video.", Toast.LENGTH_LONG).show();
        }
    }
}
