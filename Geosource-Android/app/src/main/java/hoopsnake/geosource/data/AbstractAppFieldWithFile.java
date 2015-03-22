package hoopsnake.geosource.data;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

import ServerClientShared.FieldWithContent;
import hoopsnake.geosource.IncidentActivity;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by wsv759 on 07/03/15.
 *
 * Abstract implementation of AppField, intended to be extended by all content types
 * whose content is stored in a file on android (e.g. image, video, audio.)
 */
public abstract class AbstractAppFieldWithFile extends AbstractAppField {

    public AbstractAppFieldWithFile(FieldWithContent fieldToWrap, IncidentActivity activity)
    {
        super(fieldToWrap, activity);
    }

    /**
     * The Uri representing the file which will be converted into the Serializable content object
     * at some point. If this is non-null, the content field is assumed to be filled.
     *
     * If this is null, the content field may still be non-null!
     *
     * This should only ever be non-null if the content actually corresponds to a file
     * (i.e. Image, Video, or Audio content.)
     */
    private Uri contentFileUri;

    Uri getContentFileUri() {
        return contentFileUri;
    }

    @Override
    public boolean contentIsFilled()
    {
        return contentFileUri != null || wrappedField.getContent() != null;
    }

    @Override
    public String getContentStringRepresentation()
    {
        if (!contentIsFilled())
            return "this is an empty file";

        return contentFileUri.toString();
    }

    @Override
    public View getContentViewRepresentation(final int requestCodeForIntent)
    {
        if (contentIsFilled())
            return getFilledContentViewRepresentation();
        else
            return getEmptyContentViewRepresentation(requestCodeForIntent);
    }

    /**
     *
     * @return a content view representation for if the content is filled.
     */
    abstract View getFilledContentViewRepresentation();

    /**
     *
     * @return a content view representation for if the content is empty.
     */
    abstract View getEmptyContentViewRepresentation(final int requestCodeForIntent);

    /**
     *
     * @param contentFileUri the Uri referring to a file on the device. This file should currently
     *                       store the content associated with this field.
     * @precond none. contentFileUri can be null.
     * @postcond sets the fileUri of this field to be contentFileUri.
     */
    public void setContentFileUri(Uri contentFileUri)
    {
        if (contentFileUri == null || usesFilesOfType(contentFileUri))
            this.contentFileUri = contentFileUri;
        else
            throw new RuntimeException(contentFileUri.toString() + "has incorrect file type.");
    }

    /**
     * //TODO This doesn't work yet, at least not on emulator?
     * //TODO move this somewhere more suitable.
     * Draw an icon from a svg file.
     * @param svgFileResourceCode the R.raw.* resource code defining the svg file to draw.
     * @return the icon to draw, in Drawable format.
     */
    Drawable getDrawableIconFromSVGResource(int svgFileResourceCode)
    {
        Resources resources = activity.getResources();
        assertNotNull(resources);
        SVG svg = SVGParser.getSVGFromResource(resources, svgFileResourceCode);
        return svg.createPictureDrawable();
    }

    /**
     * @param contentFileUri the Uri representing the content file to check. Does it contain the correct
     *                       filetype for this field?
     * @precond contentFileUri is not null.
     * @postcond see return.
     * @return return true if this field uses this type of file for its content, or false otherwise.
     */
    abstract boolean usesFilesOfType(Uri contentFileUri);
}