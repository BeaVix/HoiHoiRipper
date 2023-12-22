package UNFPApp;

import java.io.IOException;

import UFPLib.Format;
import UFPLib.IFormat;

public class GetFormatService {
    private GetFormat getF;
    private static IScanFormat[] formats = {new ScanFormatUFP(), new ScanFormatPSI(), new ScanFormatPSI3(), new ScanFormatPSP()};

    public GetFormatService()
    {
        this.getF = new GetFormat();
    }

    public IFormat scanFormat(IFormat f) throws IOException
    {
        for(IScanFormat scan : formats)
        {
            this.getF.setScan(scan);
            f = this.getF.getFormat(f);
            if(f.getClass() != Format.class)
            {
                break;
            }
        }
        return f;
    }

    public IFormat scanFormat(IFormat f, IFormat parent) throws IOException
    {
        for(IScanFormat scan : formats)
        {
            this.getF.setScan(scan);
            f = this.getF.getFormat(f, parent);
            if(f.getClass() != Format.class)
            {
                break;
            }
        }
        return f;
    }
}
