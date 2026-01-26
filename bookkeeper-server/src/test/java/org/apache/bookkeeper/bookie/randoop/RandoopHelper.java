package org.apache.bookkeeper.bookie.randoop;
import java.io.*;
import java.nio.channels.FileChannel;
import io.netty.buffer.Unpooled;
import io.netty.buffer.ByteBufAllocator;

public class RandoopHelper {
    public static FileChannel getValidChannel() throws IOException {
        File temp = File.createTempFile("randoop", ".test");
        return new RandomAccessFile(temp, "rw").getChannel();
    }
    public static ByteBufAllocator getAllocator() {
        return Unpooled.buffer().alloc();
    }
}