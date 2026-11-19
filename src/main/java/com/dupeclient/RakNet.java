package com.dupeclient;

import java.net.*;
import java.nio.ByteBuffer;

public class RakNet {
    // Magic RakNet (16 bytes fijos)
    private static final byte[] MAGIC = {
        0x00,(byte)0xff,(byte)0xff,0x00,(byte)0xfe,(byte)0xfe,(byte)0xfe,(byte)0xfe,
        (byte)0xfd,(byte)0xfd,(byte)0xfd,(byte)0xfd,0x12,0x34,0x56,0x78
    };

    public static byte[] connectionRequest(long clientGuid, long timeMs) {
        // 0x09 = ID_CONNECTION_REQUEST, + time + magic + guid
        ByteBuffer b = ByteBuffer.allocate(1 + 8 + 16 + 8);
        b.put((byte)0x09);
        b.putLong(timeMs);
        b.put(MAGIC);
        b.putLong(clientGuid);
        return b.array();
    }
}
