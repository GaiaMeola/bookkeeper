package manualtest;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.apache.bookkeeper.bookie.storage.ldb.WriteCache;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static customutils.Utils.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WriteCacheGetTest {

    private enum CacheContentState {
        WRITTEN, NON_WRITTEN
    }

    private static Stream<Arguments> data() {
        /*WriteCacheState invalidAllocatorState = new WriteCacheState(invalidByteBufAllocator(), 512, 128);
        WriteCacheState nullAllocatorState = new WriteCacheState(null, 512, 128);*/
        WriteCacheState invalidZeroCacheSize = new WriteCacheState(unpooledByteBufAllocator(), 0, 1);
        WriteCacheState validSegment = new WriteCacheState(unpooledByteBufAllocator(), 512, 128);
        ByteBuf sampleEntry = lenFullByteBuf(4);

        return Stream.of(

                /*
                // G1: invalid allocator → eccezione; test fallito
                Arguments.of(invalidAllocatorState, 1, 1, CacheContentState.NON_WRITTEN, null, Exception.class)
                */

                /*
                // G2: null allocator → eccezione; test fallito
                Arguments.of(nullAllocatorState, 1, 1, CacheContentState.NON_WRITTEN, null, Exception.class)
                */

                // G3: maxCacheSize=0 → sempre null; test passato
                Arguments.of(invalidZeroCacheSize, 1, 1, CacheContentState.NON_WRITTEN, null, null),

                // T1: ledgerId < 0 → eccezione; test passato
                Arguments.of(validSegment, -1, 1, CacheContentState.NON_WRITTEN, null, Exception.class),

                // T2: ledgerId = 0 → null; test passato
                Arguments.of(validSegment, 0, 1, CacheContentState.NON_WRITTEN, null, null),

                // T3: ledgerId > 0 → null; test passato
                Arguments.of(validSegment, 1, 1, CacheContentState.NON_WRITTEN, null, null),

                /*
                // T4: entryId < 0 → eccezione; test fallito
                Arguments.of(validSegment, 1, -1, CacheContentState.NON_WRITTEN, null, Exception.class)
                */

                // T5: entryId = 0 → null; test passato
                Arguments.of(validSegment, 1, 0, CacheContentState.NON_WRITTEN, null, null),

                // T6: entryId > 0 → null; test passato
                Arguments.of(validSegment, 1, 1, CacheContentState.NON_WRITTEN, null, null),


                // NEW 1: WRITTEN – entry presente (match); test passato
                Arguments.of(validSegment, 10, 20, CacheContentState.WRITTEN, sampleEntry, null),

                // NEW 2: WRITTEN – entry NON corrispondente (cache contiene un’altra entry); test passato
                Arguments.of(validSegment, 10, 21, CacheContentState.WRITTEN, null, null)
        );
    }

    @ParameterizedTest
    @MethodSource("data")
    @Timeout(value = 5, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    void get(WriteCacheState s,
             long ledgerId,
             long entryId,
             CacheContentState cacheState,
             ByteBuf expectedEntry,
             Class<? extends Exception> expectedException) {

        WriteCache wc = new WriteCache(s.allocator, s.maxCacheSize, s.maxSegmentSize);
        Assertions.assertNotNull(wc);

        // Se lo stato è WRITTEN, inseriamo direttamente l'entry
        if (cacheState == CacheContentState.WRITTEN && expectedException == null) {
            if (expectedEntry != null) {
                // Caso WRITTEN con match: scriviamo esattamente la entry cercata
                wc.put(ledgerId, entryId, expectedEntry.copy());
            } else {
                // Caso WRITTEN con mismatch: scriviamo un'altra entry (ledgerId o entryId diverso)
                wc.put(ledgerId, entryId + 1, lenFullByteBuf(4));
            }
        }

        // Controllo eccezione attesa
        if (expectedException != null) {
            Assertions.assertThrows(expectedException, () -> wc.get(ledgerId, entryId));
            return;
        }

        // Recupero e confronto
        ByteBuf result = wc.get(ledgerId, entryId);

        if (expectedEntry == null) {
            Assertions.assertNull(result, "Expected null result");
        } else {
            Assertions.assertNotNull(result, "Expected non-null ByteBuf");
            Assertions.assertEquals(expectedEntry.readableBytes(), result.readableBytes(), "Size mismatch");

            for (int i = 0; i < expectedEntry.readableBytes(); i++) {
                Assertions.assertEquals(expectedEntry.getByte(i), result.getByte(i),
                        "Byte mismatch at index " + i);
            }
        }
    }

    private static final class WriteCacheState {
        private final ByteBufAllocator allocator;
        private final long maxCacheSize;
        private final int maxSegmentSize;

        WriteCacheState(ByteBufAllocator allocator, long maxCacheSize, int maxSegmentSize) {
            this.allocator = allocator;
            this.maxCacheSize = maxCacheSize;
            this.maxSegmentSize = maxSegmentSize;
        }
    }
}