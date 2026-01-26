package manualtest;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import org.apache.bookkeeper.bookie.storage.ldb.WriteCache;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.stream.Stream;
import static customutils.Utils.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WriteCacheConstructorTest {

    private static Stream<Arguments> data() {
        return Stream.of(
                // Varying allocator
                Arguments.of(unpooledByteBufAllocator(), 512L, 128, null), //T1
               /* Arguments.of(invalidByteBufAllocator(), 512L, 128, Exception.class) //T2 not passed
                Arguments.of(null, 512L, 128, Exception.class) //T3 not passed  */


                Arguments.of(unpooledByteBufAllocator(), 1L, 1, null), //T4
                /*Arguments.of(unpooledByteBufAllocator(), 0L, 1, Exception.class) //T5 not passed */
                Arguments.of(unpooledByteBufAllocator(), -1L, 1, Exception.class), //T6
                Arguments.of(unpooledByteBufAllocator(), 512L, -1, Exception.class), //T7
                Arguments.of(unpooledByteBufAllocator(), 512L, 0, Exception.class), //T8
                Arguments.of(unpooledByteBufAllocator(), 512L, 1, null),//T9
                Arguments.of(unpooledByteBufAllocator(), 512L, 100, Exception.class),//T10
                Arguments.of(unpooledByteBufAllocator(), 512L, 128, null), //T11
                Arguments.of(unpooledByteBufAllocator(), 512L, 512, null), //T12
                Arguments.of(unpooledByteBufAllocator(), 512L, 600, Exception.class) //T13
        );
    }

    //aggiunto dopo PIT per uccidere la mutazione sopravvissuta a LOC 104
    @Test
    void testSegmentOffsetBitsMutationDetectedByOverwrite() {
        int maxSegmentSize = 64;      // >= alignamento 64
        long maxCacheSize = 256;      // 4 segmenti da 64

        try (WriteCache wc = new WriteCache(unpooledByteBufAllocator(), maxCacheSize, maxSegmentSize)) {
            // Inseriamo 4 entry, ciascuna di esattamente maxSegmentSize bytes,
            // con contenuto distintivo: byte[0] == (byte)(i+1)
            for (int i = 0; i < 4; i++) {
                byte[] payload = new byte[maxSegmentSize];
                byte marker = (byte) (i + 1);
                Arrays.fill(payload, marker);
                ByteBuf entry = Unpooled.buffer(maxSegmentSize);
                entry.writeBytes(payload);
                assertTrue(wc.put(1L, i, entry), "put() dovrebbe riuscire per entry " + i);
            }

            // Recuperiamo e verifichiamo il marker in posizione 0 per ciascuna entry
            for (int i = 0; i < 4; i++) {
                ByteBuf got = wc.get(1L, i);
                assertNotNull(got, "get() ha restituito null per entry " + i);
                assertEquals(maxSegmentSize, got.readableBytes(), "dimensione sbagliata per entry " + i);
                byte b0 = got.getByte(0);
                assertEquals((byte) (i + 1), b0,
                        "Il primo byte di entry " + i + " dovrebbe essere " + (i + 1) + " ma era " + b0);
            }
        }
        // libera i direct buffers
    }

    @ParameterizedTest
    @MethodSource("data")
    @Timeout(5)
    void construct(ByteBufAllocator allocator, long maxCacheSize, int maxSegmentSize, Class<? extends Exception> expectedException) {
        if (expectedException != null) {
            Assertions.assertThrows(expectedException, () -> new WriteCache(allocator, maxCacheSize, maxSegmentSize));
        } else {
            WriteCache wc = new WriteCache(allocator, maxCacheSize, maxSegmentSize);
            assertNotNull(wc);
        }
    }
}
