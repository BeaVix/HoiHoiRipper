package UFPLib;


public class Hasher {
    public static long byteHash(byte[] bytes)
    {
        long h = 0;
        for(int i = 0; i < bytes.length; i++)
        {
            int p = (bytes[i] & 0xFF);
            h = (255*h + p)%457;
        }
        return h;
    }

    public static long byteReHash(byte[] oldBytes, long hash, byte newByte, long pow)
    {
        hash = (255*(hash-(oldBytes[0] & 0xFF)*pow) + (newByte & 0xFF))%457;
        if (hash < 0){
            hash += 457;
        }
        return hash;
    }
}
