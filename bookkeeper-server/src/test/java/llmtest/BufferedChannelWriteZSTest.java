package llmtest;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import org.apache.bookkeeper.bookie.BufferedChannel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BufferedChannelWriteZSTest {

    @Mock
    private FileChannel mockFileChannel;
    @Mock
    private ByteBufAllocator mockAllocator;

    private BufferedChannel bufferedChannel;

    @BeforeEach
    void setUp() throws IOException {
        lenient().when(mockFileChannel.position()).thenReturn(0L);
        lenient().when(mockAllocator.directBuffer(anyInt())).thenAnswer(i -> Unpooled.buffer(i.getArgument(0)));
        int CAPACITY = 10;
        long UNPERSISTED_BOUND = 20L;
        bufferedChannel = new BufferedChannel(mockAllocator, mockFileChannel, CAPACITY, UNPERSISTED_BOUND);
    }

    /**
     * Answer personalizzata per evitare il loop infinito nel metodo flush().
     * Simula il FileChannel avanzando la posizione del ByteBuffer di input.
     */
    private Answer<Integer> consumeBuffer() {
        return invocation -> {
            ByteBuffer buffer = invocation.getArgument(0);
            int remaining = buffer.remaining();
            buffer.position(buffer.limit()); // Simula il consumo totale dei byte
            return remaining;
        };
    }

    @Test
    @DisplayName("TC-W1: Scrittura che eccede la capacità - Risoluzione Loop Infinito")
    void testWriteTriggersFlushFixed() throws IOException {
        // Istruiamo il mock a consumare i byte per far terminare il ciclo do-while in flush()
        when(mockFileChannel.write(any(ByteBuffer.class))).thenAnswer(consumeBuffer());
        when(mockFileChannel.position()).thenReturn(10L);

        // 12 byte > 10 di capacità -> Innesca flush()
        ByteBuf src = Unpooled.copiedBuffer(new byte[12]);
        bufferedChannel.write(src);

        assertEquals(12, bufferedChannel.position());
        assertEquals(2, bufferedChannel.getNumOfBytesInWriteBuffer());
        verify(mockFileChannel, atLeastOnce()).write(any(ByteBuffer.class));
    }

    @Test
    @DisplayName("TC-W2: Scrittura valida entro i limiti")
    void testWriteValidDataUnderCapacity() throws IOException {
        ByteBuf src = Unpooled.copiedBuffer(new byte[5]);
        bufferedChannel.write(src);

        assertEquals(5, bufferedChannel.position());
        assertEquals(5, bufferedChannel.getNumOfBytesInWriteBuffer());
        verify(mockFileChannel, never()).write(any(ByteBuffer.class));
    }

    @Test
    @DisplayName("TC-W3: Superamento Bound Persistenza (ForceWrite)")
    void testWriteTriggersForceWrite() throws IOException {
        when(mockFileChannel.write(any(ByteBuffer.class))).thenAnswer(consumeBuffer());

        // Bound fissato a 5
        BufferedChannel lowBoundChannel = new BufferedChannel(mockAllocator, mockFileChannel, 100, 5);
        ByteBuf src = Unpooled.copiedBuffer(new byte[10]);

        lowBoundChannel.write(src);

        verify(mockFileChannel).force(false);
    }

    @Test
    @DisplayName("TC-W4: Test Buffer Vuoto")
    void testWriteEmptyBuffer() throws IOException {
        ByteBuf src = Unpooled.EMPTY_BUFFER;
        bufferedChannel.write(src);
        assertEquals(0, bufferedChannel.getNumOfBytesInWriteBuffer());
        verify(mockFileChannel, never()).write(any(ByteBuffer.class));
    }

    @Test
    @DisplayName("TC-W5: Test Null Pointer")
    void testWriteNullBuffer() {
        assertThrows(NullPointerException.class, () -> bufferedChannel.write(null));
    }
}