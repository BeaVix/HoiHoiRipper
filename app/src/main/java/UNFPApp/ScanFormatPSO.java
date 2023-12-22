package UNFPApp;

import UFPLib.FileData;
import UFPLib.IFormat;
import UFPLib.PSO;

import java.io.IOException;

public class ScanFormatPSO implements IScanFormat{
    static private byte[] header = new byte[]{0x31, 0x4F, 0x53, 0x50}; //1OSP

    public IFormat scanFormat(IFormat f) throws IOException
    {
        if(FileData.compareNextBytes(f.getData(), 4, header, f.getStartOffset(), f.getEndOffset()))
        {
            return new PSO(f.getPath());
        }
        return f;
    }

    public IFormat scanFormat(IFormat f, IFormat parent) throws IOException
    {
        if(FileData.compareNextBytes(f.getData(), 4, header, f.getStartOffset(), f.getEndOffset()))
        {
            return new PSO(f.getName(), parent, f.getStartOffset(), f.getEndOffset());
        }
        return f;
    }
}
