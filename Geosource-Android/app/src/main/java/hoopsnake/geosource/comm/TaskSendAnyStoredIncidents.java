package hoopsnake.geosource.comm;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;

import hoopsnake.geosource.FileIO;
import hoopsnake.geosource.IncidentActivity;
import hoopsnake.geosource.data.AppIncident;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by wsv759 on 29/03/15.
 */
public class TaskSendAnyStoredIncidents extends AsyncTask<Void, Void, Void> {
    private static final String LOG_TAG = "geosource comm";
    Activity activity;
    public TaskSendAnyStoredIncidents(Activity activity)
    {
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(Void... params) {
        File savedIncidentsDir = activity.getDir(IncidentActivity.DIRNAME_INCIDENTS_YET_TO_SEND, Context.MODE_PRIVATE);
        File[] savedIncidentFiles = savedIncidentsDir.listFiles();

        Log.v(LOG_TAG, Integer.toString(savedIncidentFiles.length) + " incidents to send.");

        assertNotNull(savedIncidentFiles);
        if (savedIncidentFiles.length == 0)
            return null;
//
//        String activityBaseFilesDirPath;
//        try {
//            activityBaseFilesDirPath = activity.getFilesDir().getCanonicalPath();
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.e(LOG_TAG, "unable to send files. Could not retrieve canonical path.");
//            return null;
//        }

        //Send each of the individual files.
        for (File incidentFileToSend : savedIncidentFiles) {
            String incidentFilePath = incidentFileToSend.getAbsolutePath();
//            try {
//                incidentFilePath = incidentFileToSend.getCanonicalPath();
//            } catch (IOException e) {
//                e.printStackTrace();
//                Log.e(LOG_TAG, "unable to send files. Could not retrieve canonical path.");
//                return null;
//            }

//            String incidentRelativePath;
//            if (incidentFilePath.startsWith(activityBaseFilesDirPath)) {
//                incidentRelativePath = incidentFilePath.substring(activityBaseFilesDirPath.length() + 1);
//            } else {
//                Log.e(LOG_TAG, "unable to send files. Canonical paths " + incidentFilePath + ", " + activityBaseFilesDirPath + " did not match up.");
//                return null;
//            }

            AppIncident incidentToSend = (AppIncident) FileIO.readObjectFromFileNoContext(incidentFilePath);

            if (incidentToSend == null)
                Log.e(LOG_TAG, "unable to send file " + incidentFilePath +". File IO failed.");
            else
                new TaskSendIncident(activity).execute(incidentToSend);
        }

        return null;
    }
}