package hoopsnake.geosource.data;

import android.content.Context;
import android.net.Uri;

/**
 * Created by wsv759 on 07/03/15.
 */
public class ImageField extends AbstractAppFieldWithContentAndFile {
    public ImageField(FieldWithContent fieldToWrap) {
        super(fieldToWrap);
    }

    @Override
    public boolean isCorrectFileType(Uri contentFileUri) {
        //TODO implement this.
        return true;
    }

    @Override
    public void populateUi(Context context) {
        //TODO implement this.
    }
}