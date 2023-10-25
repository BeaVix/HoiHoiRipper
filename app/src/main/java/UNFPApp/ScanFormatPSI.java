package UNFPApp;

import java.io.IOException;

import UFPLib.FileData;
import UFPLib.IFormat;
import UFPLib.PSI;

public class ScanFormatPSI implements IScanFormat{
    static private byte[] header = new byte[]{0x31, 0x49, 0x53, 0x50};
    
    public IFormat scanFormat(IFormat f) throws IOException {
        if(FileData.compareNextBytes(f.getData(), 4, header, f.getStartOffset(), f.getEndOffset()))
        {
            return new PSI(f.getPath());
        }
        return f;
    }

    public IFormat scanFormat(IFormat f, IFormat parent) throws IOException
    {
        if(FileData.compareNextBytes(f.getData(), 4, header, f.getStartOffset(), f.getEndOffset()))
        {
            return new PSI(f.getName(), parent, f.getStartOffset(), f.getEndOffset());
        }
        return f;
    }
}
