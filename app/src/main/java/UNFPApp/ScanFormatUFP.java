package UNFPApp;

import java.io.IOException;

import UFPLib.FileData;
import UFPLib.IFormat;
import UFPLib.UFP;

public class ScanFormatUFP implements IScanFormat{
    static private byte[] header = new byte[]{0x55, 0x46, 0x56, 0x31};

    @Override
    public IFormat scanFormat(IFormat f) throws IOException 
    {
        boolean c = FileData.compareNextBytes(f.getData(), 4, header, f.getStartOffset(), f.getEndOffset());
        if(c)
        {
            return new UFP(f.getPath());
        }
        return f;
    }

    public IFormat scanFormat(IFormat f, IFormat parent) throws IOException
    {
        if(FileData.compareNextBytes(f.getData(), 4, header, f.getStartOffset(), f.getEndOffset()))
        {
            return new UFP(f.getName(), parent, f.getStartOffset(), f.getEndOffset());
        }
        return f;
    }
}
