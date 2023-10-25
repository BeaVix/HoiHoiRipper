package UNFPApp;

import java.io.IOException;

import UFPLib.IFormat;

public class GetFormat {
    private IScanFormat scan;
    public IFormat getFormat(IFormat f) throws IOException
    {
        return scan.scanFormat(f);
    }

    public IFormat getFormat(IFormat f, IFormat parent) throws IOException
    {
        return scan.scanFormat(f, parent);
    }

    public void setScan(IScanFormat scan)
    {
        this.scan = scan;
    }
}
