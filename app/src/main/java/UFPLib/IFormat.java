package UFPLib;

import java.nio.channels.SeekableByteChannel;
import java.nio.file.Path;

public interface IFormat {
    public void setName(Path s);
    public void setName(String s);
    public void setPath(Path s);
    public void setData(SeekableByteChannel s);
    public void setStartOffset(long startOffset);
    public void setEndOffset(long endOffset);
    public String getName();
    public Path getPath();
    public SeekableByteChannel getData();
    public long getStartOffset();
    public long getEndOffset();
}
