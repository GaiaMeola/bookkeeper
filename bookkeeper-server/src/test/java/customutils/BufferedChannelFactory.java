package customutils;

import io.netty.buffer.ByteBufAllocator;
import randoop.TestValue;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;
import java.nio.file.Paths;

public class BufferedChannelFactory {

    @TestValue
    public static ByteBufAllocator allocator = Utils.unpooledByteBufAllocator();

    @TestValue
    public static FileChannel channel;

    static {
        try {
            channel = FileChannel.open(Paths.get("tempfile.tmp"),
                    StandardOpenOption.READ,
                    StandardOpenOption.WRITE,
                    StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
