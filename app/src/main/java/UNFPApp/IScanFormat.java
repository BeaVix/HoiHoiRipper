package UNFPApp;

import java.io.IOException;

import UFPLib.IFormat;

public interface IScanFormat {
    public IFormat scanFormat(IFormat f) throws IOException;
    public IFormat scanFormat(IFormat f, IFormat parent) throws IOException;
}
