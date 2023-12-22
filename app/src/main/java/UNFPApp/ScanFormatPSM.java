package UNFPApp;

import java.io.IOException;

import UFPLib.FileData;
import UFPLib.IFormat;
import UFPLib.PSM;

public class ScanFormatPSM implements IScanFormat{
    static private byte[] header = new byte[]{0x31, 0x4D, 0x53, 0x50}; //1MSP

    /**
     * Checks if IFormat object is a valid PSM file.
     * If so returns a new PSM object made from the given IFormat.
     * Otherwise, returns the IFormat.
     * @param f The IFormat object to scan
     * @return a PSM object or the same IFormat
     * @throws IOException
     */
    public IFormat scanFormat(IFormat f) throws IOException
    {
        if(FileData.compareNextBytes(f.getData(), 4, header, f.getStartOffset(), f.getEndOffset()))
        {
            return new PSM(f.getPath());
        }
        return f;
    }

    public IFormat scanFormat(IFormat f, IFormat parent) throws IOException
    {
        if(FileData.compareNextBytes(f.getData(), 4, header, f.getStartOffset(), f.getEndOffset()))
        {
            return new PSM(f.getName(), parent, f.getStartOffset(), f.getEndOffset());
        }
        return f;
    }
}
