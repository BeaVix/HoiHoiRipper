package UFPLib;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Path;

public class Format implements IFormat{
    protected String name;
    protected Path path;
    protected SeekableByteChannel data;
    protected long startOffset;
    protected long endOffset;

    public Format(){}

    public Format(Path filename) throws IOException
    {
        setName(filename);
        setPath(filename);
        setData(FileHandler.readFile(filename));
        setStartOffset(0);
        setEndOffset(data.size());
    }

    public Format(String name, IFormat file, long startOffset, long endOffset) throws IOException
    {
        setName(name);
        setData(file.getData());
        setStartOffset(startOffset);
        setEndOffset(endOffset);
    }

    public void setName(Path s)
    {
        this.name = s.getFileName().toString();
    }

    public void setName(String s)
    {
        this.name = s;
    }

    public void setPath(Path s)
    {
        this.path = s;
    }

    public void setData(SeekableByteChannel s)
    {
        this.data = s;
    }

    public void setStartOffset(long startOffset)
    {
        this.startOffset = startOffset;
    }

    public void setEndOffset(long endOffset)
    {
        this.endOffset = endOffset;
    }

    public String getName()
    {
        return this.name;
    }

    public Path getPath()
    {
        return this.path;
    }

    public SeekableByteChannel getData()
    {
        return this.data;
    }

    public long getStartOffset()
    {
        return this.startOffset;
    }

    public long getEndOffset()
    {
        return this.endOffset;
    }
}
