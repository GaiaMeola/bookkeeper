package customutils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledByteBufAllocator;
import org.apache.bookkeeper.bookie.storage.ldb.WriteCache;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class Utils {

    public static final String BC_TEST_FILE = "bc_test_file.txt";
    public static final String BC_FC_CONTENT = "File Channel!";
    public static final String BC_BB_CONTENT = "Byte Buffer?!";

    public static ByteBufAllocator unpooledByteBufAllocator() {
        return UnpooledByteBufAllocator.DEFAULT;
    } /* restituzione di un'istanza di allocator valida */

    public static ByteBufAllocator invalidByteBufAllocator() {
        ByteBufAllocator bba = mock(ByteBufAllocator.class);
        /*oggetto finto di tipo ByteBufAllocator */
        when(bba.buffer()).thenReturn(null);
        when(bba.buffer(anyInt())).thenReturn(null);
        when(bba.buffer(anyInt(), anyInt())).thenReturn(null);

        when(bba.directBuffer()).thenReturn(null);
        /*ci assicura che venga ignorato qualsiasi tipo di input */
        when(bba.directBuffer(anyInt())).thenReturn(null);
        when(bba.directBuffer(anyInt(), anyInt())).thenReturn(null);
        /*ogni volta che qualcuno tenta di allocare un ByteBuFAllocator, riceverà sempre un valore nullo */
        return bba;
    }

    public static FileChannel validFileChannel() throws IOException {
        Path path = Paths.get(BC_TEST_FILE);
        if (Files.exists(path)) Files.delete(path);
        Files.createFile(path);
        Files.write(path, BC_FC_CONTENT.getBytes());

        FileChannel fc = FileChannel.open(path, StandardOpenOption.READ, StandardOpenOption.WRITE);
        fc.position(BC_FC_CONTENT.length());
        return fc;
    }
    public static FileChannel closedFileChannel() throws IOException {
        FileChannel fileChannel = validFileChannel();
        fileChannel.close();
        return fileChannel;
    }
    public static FileChannel readOnlyFileChannel() throws IOException {
        Path path = Paths.get(BC_TEST_FILE);
        if (Files.exists(path)) Files.delete(path);
        Files.createFile(path);
        Files.write(path, BC_FC_CONTENT.getBytes());

        FileChannel fc = FileChannel.open(path, StandardOpenOption.READ);
        fc.position(BC_FC_CONTENT.length());
        return fc;
    }
    public static FileChannel writeOnlyFileChannel() throws IOException {
        Path path = Paths.get(BC_TEST_FILE);
        if (Files.exists(path)) Files.delete(path);
        Files.createFile(path);
        Files.write(path, BC_FC_CONTENT.getBytes());

        FileChannel fc = FileChannel.open(path, StandardOpenOption.WRITE);
        fc.position(BC_FC_CONTENT.length());
        return fc;
    }
    public static FileChannel invalidPositionFileChannel() throws IOException {
        FileChannel fc = spy(validFileChannel());
        when(fc.position()).thenReturn(-1L);
        return fc;
    }

    public static ByteBuf emptyByteBuf() {
        return Unpooled.buffer(BC_BB_CONTENT.length()+BC_FC_CONTENT.length(), BC_BB_CONTENT.length()+BC_FC_CONTENT.length());
    }

    public static ByteBuf semiFullByteBuf() {
        int length = BC_FC_CONTENT.length() / 2;
        int capacity = length * 2; // esattamente il doppio di quanto vuoi scrivere
        ByteBuf buffer = Unpooled.buffer(capacity, capacity);
        buffer.writeBytes(new byte[length]); // metà pieno
        return buffer;
    }

    public static ByteBuf fullByteBuf() {
        ByteBuf buffer = Unpooled.buffer(BC_BB_CONTENT.length(), BC_BB_CONTENT.length());
        buffer.writeBytes(BC_BB_CONTENT.getBytes());
        return buffer;
    }

    @SuppressWarnings("unused") public static ByteBuf invalidWriteIndexByteBuf() {
        ByteBuf buffer = spy(fullByteBuf());
        when(buffer.writerIndex()).thenReturn(BC_BB_CONTENT.length());
        when(buffer.readerIndex()).thenReturn(BC_BB_CONTENT.length() + 1);
        return buffer;
    } /* For read testing */

    public static ByteBuf invalidReadIndexByteBuf() {
        ByteBuf buffer = spy(fullByteBuf());
        when(buffer.readerIndex()).thenReturn(-1);
        return buffer;
    } /* For write testing */

    public static ByteBuf deallocatedByteBuf() {
        ByteBuf buffer = Unpooled.buffer(BC_BB_CONTENT.length());
        buffer.writeBytes(BC_BB_CONTENT.getBytes());
        // buffer reference count starts from 1, release decrements it by 1 so deallocating the buffer
        buffer.release();
        return buffer;
    }

    public static void clearReadBuffer(Object bc) {
        try {
            Class<?> clazz = bc.getClass();
            Field readBufferField = null, startPosField = null;

            while (clazz != null) {
                try {
                    readBufferField = clazz.getDeclaredField("readBuffer");
                    readBufferField.setAccessible(true);
                    startPosField = clazz.getDeclaredField("readBufferStartPosition");
                    startPosField.setAccessible(true);
                    break;
                } catch (NoSuchFieldException e) {
                    clazz = clazz.getSuperclass();
                }
            }
            if (readBufferField == null || startPosField == null) {
                throw new RuntimeException("Fields not found");
            }

            ByteBuf readBuffer = (ByteBuf) readBufferField.get(bc);
            if (readBuffer != null) readBuffer.clear();
            startPosField.setLong(bc, Long.MAX_VALUE);

        } catch (Exception e) {
            throw new RuntimeException("Error resetting readBuffer: " + e.getMessage(), e);
        }
    }

    public static ByteBuf lenFullByteBuf(int size) {
        ByteBuf buffer = Unpooled.buffer(size, size);
        buffer.writeBytes(new byte[size]); // Scrive 'size' byte (tutti a 0)
        return buffer;
    }

    /**
     * Restituisce un mock di EntryConsumer che lancia IOException ad ogni chiamata di accept.
     */
    public static WriteCache.EntryConsumer exceptionThrowingConsumer() throws IOException {
        WriteCache.EntryConsumer mockConsumer = mock(WriteCache.EntryConsumer.class);
        doThrow(new IOException("forced exception from mock consumer")).when(mockConsumer)
                .accept(anyLong(), anyLong(), any(ByteBuf.class));
        return mockConsumer;
    }

}
