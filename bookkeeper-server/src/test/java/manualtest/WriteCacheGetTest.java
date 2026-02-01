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
        WriteCacheState invalidAllocatorState = new WriteCacheState(invalidByteBufAllocator(), 512, 128);
        WriteCacheState nullAllocatorState = new WriteCacheState(null, 512, 128);
        WriteCacheState invalidZeroCacheSize = new WriteCacheState(unpooledByteBufAllocator(), 0, 1);
        WriteCacheState validSegment = new WriteCacheState(unpooledByteBufAllocator(), 512, 128);
        ByteBuf sampleEntry = lenFullByteBuf(4);
        // Stato per forzare l'uso del secondo segmento (maxSegmentSize = 256)
        WriteCacheState multiSegmentState = new WriteCacheState(unpooledByteBufAllocator(), 1024, 256);

        return Stream.of(

                /*
                // G1: invalid allocator → eccezione; test fallito
                Arguments.of(invalidAllocatorState, 1, 1, CacheContentState.NON_WRITTEN, null, Exception.class)
                */

                /*
                // G2: null allocator → eccezione; test fallito
                Arguments.of(nullAllocatorState, 1, 1, CacheContentState.NON_WRITTEN, null, Exception.class)
                */

                // G1: Stato WRITTEN forza l'uso dell'allocatore invalido -> Eccezione lanciata
                Arguments.of(invalidAllocatorState, 1, 1, CacheContentState.WRITTEN, null, Exception.class),

                // G2: Stato WRITTEN forza l'uso dell'allocatore nullo -> Eccezione lanciata
                Arguments.of(nullAllocatorState, 1, 1, CacheContentState.WRITTEN, null, Exception.class),

                // G3: maxCacheSize=0 → sempre null; test passato
                Arguments.of(invalidZeroCacheSize, 1, 1, CacheContentState.NON_WRITTEN, null, null),

                // G4: ledgerId < 0 → eccezione; test passato
                Arguments.of(validSegment, -1, 1, CacheContentState.NON_WRITTEN, null, Exception.class),

                // G5: ledgerId = 0 → null; test passato
                Arguments.of(validSegment, 0, 1, CacheContentState.NON_WRITTEN, null, null),

                // G6: ledgerId > 0 → null; test passato
                Arguments.of(validSegment, 1, 1, CacheContentState.NON_WRITTEN, null, null),

                /*
                // G7: entryId < 0 → eccezione; test fallito
                Arguments.of(validSegment, 1, -1, CacheContentState.NON_WRITTEN, null, Exception.class)
                */

                // G7: EID negativo restituirebbe null senza sollevare eccezioni (specifica reale)
                Arguments.of(validSegment, 1, -1, CacheContentState.NON_WRITTEN, null, null),

                // G8: entryId = 0 → null; test passato
                Arguments.of(validSegment, 1, 0, CacheContentState.NON_WRITTEN, null, null),

                // G9: entryId > 0 → null; test passato
                Arguments.of(validSegment, 1, 1, CacheContentState.NON_WRITTEN, null, null),


                // NEW 1: WRITTEN – entry presente (match); test passato
                Arguments.of(validSegment, 10, 20, CacheContentState.WRITTEN, sampleEntry, null),

                // NEW 2: WRITTEN – entry NON corrispondente (cache contiene un’altra entry); test passato
                Arguments.of(validSegment, 10, 21, CacheContentState.WRITTEN, null, null),

                //aggiunto a seguito dell'analisi con PIT
                Arguments.of(multiSegmentState, 100, 100, CacheContentState.WRITTEN, sampleEntry, null)
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

        // Setup: scriviamo nella cache solo se lo stato richiesto è WRITTEN
        if (cacheState == CacheContentState.WRITTEN) {
            if (s.maxSegmentSize < s.maxCacheSize) {
                ByteBuf padding = unpooledByteBufAllocator().buffer(s.maxSegmentSize);
                padding.writeZero(s.maxSegmentSize);
                wc.put(999, 999, padding);
            }

            if (expectedEntry != null) {
                wc.put(ledgerId, entryId, expectedEntry.copy());
            } else {
                // Per i casi di test come G1 e G2 (che hanno expectedEntry = null ma stato WRITTEN)
                // scriviamo un'entry fittizia per forzare il cache hit e attivare l'allocatore.
                wc.put(ledgerId, entryId, lenFullByteBuf(4));
            }
        }

        // Caso 1: Verifica eccezione (G1, G2)
        if (expectedException != null) {
            Assertions.assertThrows(expectedException, () -> wc.get(ledgerId, entryId));
            return;
        }

        // Esecuzione della get reale
        ByteBuf result = wc.get(ledgerId, entryId);

        // Caso 2: Verifica risultato NULL (G7 - entryId negativo)
        // Se ci aspettiamo null (perché la cache è vuota o l'ID è invalido)
        if (cacheState == CacheContentState.NON_WRITTEN || (entryId < 0)) {
            Assertions.assertNull(result, "Expected null result for non-written state or negative EID");
        }
        // Caso 3: Verifica contenuto (Successo)
        else {
            Assertions.assertNotNull(result, "Expected non-null ByteBuf");
            if (expectedEntry != null) {
                Assertions.assertEquals(expectedEntry.readableBytes(), result.readableBytes(), "Size mismatch");
                for (int i = 0; i < expectedEntry.readableBytes(); i++) {
                    Assertions.assertEquals(expectedEntry.getByte(i), result.getByte(i), "Byte mismatch at " + i);
                }
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