package ServerClientShared;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;

/**
 * Created by wsv759 on 12/03/15.
 */
public class ImageFieldWithoutContent extends FieldWithoutContent {
    //change this if and only if a new implementation is incompatible with an old one
    private static final long serialVersionUID = 1L;

    public static final String TYPE = "image";

    public ImageFieldWithoutContent(String title, boolean isRequired) {
        super(title, TYPE, isRequired);
    }

    private void writeObject(ObjectOutputStream out) throws IOException
    {
        super.writeObjectHelper(out);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        super.readObjectHelper(in);
    }

    private void readObjectNoData() throws ObjectStreamException
    {
        super.readObjectNoDataHelper();
    }
}
