package llmtest;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.apache.bookkeeper.bookie.BufferedChannel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.FileChannel;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BufferedChannelConstructorZSTest {

    @Mock
    private ByteBufAllocator mockAllocator;

    @Mock
    private FileChannel mockFileChannel;

    @Mock
    private ByteBuf mockByteBuf;

    private static final int VALID_CAPACITY = 1024;
    private static final long VALID_BOUND = 512L;
    private static final long INITIAL_POSITION = 100L;

    @BeforeEach
    void setUp() throws IOException {
        // Setup comune per i casi validi
        lenient().when(mockFileChannel.position()).thenReturn(INITIAL_POSITION);
        lenient().when(mockAllocator.directBuffer(anyInt())).thenReturn(mockByteBuf);
    }

    @Test
    @DisplayName("TC1: Costruttore con parametri validi (Classe di Equivalenza: Istanze valide, > 0)")
    void testConstructorValidParameters() throws IOException {
        BufferedChannel channel = new BufferedChannel(mockAllocator, mockFileChannel, VALID_CAPACITY, VALID_BOUND);

        assertAll("Verifica inizializzazione parametri",
                () -> assertEquals(VALID_CAPACITY, channel.getWriteCapacity()),
                () -> assertEquals(INITIAL_POSITION, channel.getPosition()),
                () -> assertEquals(VALID_BOUND, channel.getUnpersistedBytesBound()),
                () -> assertNotNull(channel.getWriteBuffer()),
                () -> assertEquals(0, channel.getUnpersistedBytes())
        );

        verify(mockFileChannel).position();
        verify(mockAllocator).directBuffer(VALID_CAPACITY);
    }

    @Test
    @DisplayName("TC2: Costruttore con FileChannel nullo (Classe di Equivalenza: fc istanza nulla)")
    void testConstructorNullFileChannel() {
        assertThrows(NullPointerException.class, () -> new BufferedChannel(mockAllocator, null, VALID_CAPACITY, VALID_BOUND));
    }

    @Test
    @DisplayName("TC3: Costruttore con Allocator nullo (Classe di Equivalenza: allocator istanza nulla)")
    void testConstructorNullAllocator() {
        assertThrows(NullPointerException.class, () -> new BufferedChannel(null, mockFileChannel, VALID_CAPACITY, VALID_BOUND));
    }

    @Test
    @DisplayName("TC4: Capacità negativa o nulla (Classe di Equivalenza: writeCapacity <= 0)")
    void testConstructorInvalidCapacity() {
        // Nota: Netty allocator solitamente lancia IllegalArgumentException per capacità <= 0
        lenient().when(mockAllocator.directBuffer(-1)).thenThrow(new IllegalArgumentException());

        assertThrows(IllegalArgumentException.class, () -> new BufferedChannel(mockAllocator, mockFileChannel, -1, VALID_BOUND));
    }

    @Test
    @DisplayName("TC5: UnpersistedBound zero o negativo (Classe di Equivalenza: unpersistedBound <= 0)")
    void testConstructorZeroOrNegativeBound() throws IOException {
        BufferedChannel channelZero = new BufferedChannel(mockAllocator, mockFileChannel, VALID_CAPACITY, 0L);
        assertEquals(0L, channelZero.getUnpersistedBytesBound());

        BufferedChannel channelNeg = new BufferedChannel(mockAllocator, mockFileChannel, VALID_CAPACITY, -1L);
        assertEquals(-1L, channelNeg.getUnpersistedBytesBound());
    }

    @Test
    @DisplayName("TC6: FileChannel con posizione non valida o chiuso (Classe di Equivalenza: fc istanza chiusa/non valida)")
    void testConstructorFileChannelException() throws IOException {
        when(mockFileChannel.position()).thenThrow(new ClosedChannelException());

        assertThrows(ClosedChannelException.class, () -> new BufferedChannel(mockAllocator, mockFileChannel, VALID_CAPACITY, VALID_BOUND));
    }

    @Test
    @DisplayName("TC7: Verifica costruttore delegato a 3 parametri")
    void testThreeParamConstructor() throws IOException {
        BufferedChannel channel = new BufferedChannel(mockAllocator, mockFileChannel, VALID_CAPACITY);

        assertEquals(0L, channel.getUnpersistedBytesBound(), "Il bound dovrebbe essere 0 di default");
        assertEquals(VALID_CAPACITY, channel.getWriteCapacity());
    }

    @Test
    @DisplayName("TC8: Verifica costruttore completo (5 parametri) con diverse capacità read/write")
    void testFiveParamConstructor() throws IOException {
        int writeCap = 2048;
        int readCap = 1024;

        BufferedChannel channel = new BufferedChannel(mockAllocator, mockFileChannel, writeCap, readCap, VALID_BOUND);

        assertEquals(writeCap, channel.getWriteCapacity());
        // La verifica di readCapacity avviene implicitamente tramite super(fc, readCapacity)
        // che in un test unitario completo richiederebbe l'accesso ai campi della superclasse
        verify(mockAllocator).directBuffer(writeCap);
    }
}