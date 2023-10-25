package UNFPApp;

import java.io.IOException;

import UFPLib.FileData;
import UFPLib.IFormat;
import UFPLib.PSI3;

public class ScanFormatPSI3 implements IScanFormat{
    static private byte[] header = new byte[]{0x33, 0x49, 0x53, 0x50};

    public IFormat scanFormat(IFormat f) throws IOException {
        if(FileData.compareNextBytes(f.getData(), 4, header, f.getStartOffset(), f.getEndOffset()))
        {
            return new PSI3(f.getPath());
        }
        return f;
    }

    public IFormat scanFormat(IFormat f, IFormat parent) throws IOException
    {
        if(FileData.compareNextBytes(f.getData(), 4, header, f.getStartOffset(), f.getEndOffset()))
        {
            return new PSI3(f.getName(), parent, f.getStartOffset(), f.getEndOffset());
        }
        return f;
    }
}
