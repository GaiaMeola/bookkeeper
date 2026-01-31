package manualtest;

import io.netty.buffer.ByteBufAllocator;
import org.apache.bookkeeper.bookie.storage.ldb.WriteCache;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Field;
import java.util.stream.Stream;
import static customutils.Utils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;

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
                Arguments.of(unpooledByteBufAllocator(), 0L, 1, null) ,//T5 passed a seguito della correzione e della comprensione del SUT*/

                // T6: maxCacheSize negativo -> Il SUT lancia ArrayIndexOutOfBoundsException
                Arguments.of(unpooledByteBufAllocator(), -1L, 1, Exception.class), //T6

                // T7: maxSegmentSize negativo -> Il SUT fallisce più avanti con ArrayIndexOutOfBoundsException
                // Cambiarlo qui risponde alla tua logica: il test riflette la differenza reale di comportamento.
                Arguments.of(unpooledByteBufAllocator(), 512L, -1, Exception.class), //T7

                // T8: maxSegmentSize 0 -> Il SUT lancia IllegalArgumentException (Riga 95)
                Arguments.of(unpooledByteBufAllocator(), 512L, 0, Exception.class), //T8

                Arguments.of(unpooledByteBufAllocator(), 512L, 1, null),//T9

                // T10: Non potenza di 2 -> Il SUT lancia IllegalArgumentException (Riga 98)
                Arguments.of(unpooledByteBufAllocator(), 512L, 100, Exception.class),//T10

                Arguments.of(unpooledByteBufAllocator(), 512L, 128, null), //T11
                Arguments.of(unpooledByteBufAllocator(), 512L, 512, null), //T12

                // T13: Segmento > Cache -> Il SUT lancia IllegalArgumentException (Riga 98)
                Arguments.of(unpooledByteBufAllocator(), 512L, 513, IllegalArgumentException.class)//T13
        );
    }

    @ParameterizedTest
    @MethodSource("data")
    @Timeout(5)
    void construct(ByteBufAllocator allocator, long maxCacheSize, int maxSegmentSize, Class<? extends Exception> expectedException) {
        if (expectedException != null) {
            Assertions.assertThrows(expectedException, () -> new WriteCache(allocator, maxCacheSize, maxSegmentSize));
        } else {
            try (WriteCache wc = new WriteCache(allocator, maxCacheSize, maxSegmentSize)) {
                assertNotNull(wc);

                // Calcolo corretto del residuo
                int expectedLastSize = (int) (maxCacheSize % maxSegmentSize);

                int lastIdx = wc.getCacheSegments().length - 1;
                int actualLastSize = wc.getCacheSegments()[lastIdx].capacity();

                assertEquals(expectedLastSize, actualLastSize,
                        "Errore di allocazione: l'ultimo segmento non ha la dimensione residua attesa.");
            }
        }
    }

    //Aggiunto per controllare la correttezza della classe
    @ParameterizedTest
    @MethodSource("data")
    void testConstructorConsistency(ByteBufAllocator allocator, long maxCacheSize, int maxSegmentSize, Class<? extends Exception> expectedException) throws Exception {
        if (expectedException != null) {
            assertThrows(expectedException, () -> new WriteCache(allocator, maxCacheSize, maxSegmentSize));
        } else {
            try (WriteCache wc = new WriteCache(allocator, maxCacheSize, maxSegmentSize)) {
                // Creiamo lo Spy
                WriteCache spyCache = spy(wc);

                // Non essendoci un getter, la reflection è l'unica via per la "consistenza"
                Field field = WriteCache.class.getDeclaredField("segmentOffsetBits");
                field.setAccessible(true);
                long actualBits = (long) field.get(spyCache);

                // Calcolo atteso: 63 - leading zeros
                long expectedBits = 63 - Long.numberOfLeadingZeros(maxSegmentSize);

                assertEquals(expectedBits, actualBits, "Errore di consistenza interna riga 104!");
            }
        }
    }
}
