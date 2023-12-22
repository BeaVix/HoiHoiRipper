package UNFPApp;

import java.io.IOException;

import UFPLib.FileData;
import UFPLib.IFormat;
import UFPLib.PSP;

public class ScanFormatPSP implements IScanFormat{
    static private byte[] header = new byte[]{0x32, 0x50, 0x53, 0x50}; //2PSP

    public IFormat scanFormat(IFormat f) throws IOException
    {
        if(FileData.compareNextBytes(f.getData(), 4, header, f.getStartOffset(), f.getEndOffset()))
        {
            return new PSP(f.getPath());
        }
        return f;
    }

    public IFormat scanFormat(IFormat f, IFormat parent) throws IOException
    {
        if(FileData.compareNextBytes(f.getData(), 4, header, f.getStartOffset(), f.getEndOffset()))
        {
            return new PSP(f.getName(), parent, f.getStartOffset(), f.getEndOffset());
        }
        return f;
    }
}
