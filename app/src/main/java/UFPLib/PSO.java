package UFPLib;

import java.io.IOException;
import java.nio.file.Path;

public class PSO extends Format{
    public PSO(Path path) throws IOException {
        super(path);
    }

    public PSO(String name, IFormat parent, Long startOffset, Long endOffset) throws IOException
    {
        super(name, parent, startOffset, endOffset);
    }
}
