package org.morph.bukget.data;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author Morphesus
 */
public class DataOutputHelper {
    public static void writeSmallString(final DataOutputStream dos, String str) throws IOException {
        if (dos != null && str != null && !str.isEmpty()) {
            dos.writeByte(str.length());
            dos.writeBytes(str);
        }
    }
    
    public static void writeMediumString(final DataOutputStream dos, String str) throws IOException {
        if (dos != null && str != null && !str.isEmpty()) {
            dos.writeShort(str.length());
            dos.writeBytes(str);
        }
    }
    
    public static void writeBigString(final DataOutputStream dos, String str) throws IOException {
        if (dos != null && str != null && !str.isEmpty()) {
            dos.writeInt(str.length());
            dos.writeBytes(str);
        }
    }
}
