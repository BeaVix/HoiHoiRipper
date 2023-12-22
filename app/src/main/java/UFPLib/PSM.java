package UFPLib;

import java.io.IOException;
import java.nio.file.Path;

public class PSM extends Format{
    public PSM(Path path) throws IOException
    {
        super(path);
    }

    public PSM(String name, IFormat parent, Long startOffset, Long endOffset) throws IOException
    {
        super(name, parent, startOffset, endOffset);
    }
}
