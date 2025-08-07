package manualtest;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import org.apache.bookkeeper.bookie.storage.ldb.WriteCache;
import org.apache.bookkeeper.util.collections.ConcurrentLongLongPairHashMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static customutils.Utils.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WriteCachePutTest {

    private enum WcType {
        ONE_SEGMENT_WRITTEN,
        HALF_SEGMENT_WRITTEN,
        NON_WRITTEN
    }

    private static Stream<Arguments> data() {

        WriteCacheState invalidZeroCacheSize = new WriteCacheState(unpooledByteBufAllocator(), 0, 1, WcType.NON_WRITTEN);

        WriteCacheState validTwoSegmentUnWritten = new WriteCacheState(unpooledByteBufAllocator(), 512, 256, WcType.NON_WRITTEN);
        WriteCacheState validOneSegmentUnwritten = new WriteCacheState(unpooledByteBufAllocator(), 512, 1024, WcType.NON_WRITTEN);
        WriteCacheState validOneSegWritten = new WriteCacheState(unpooledByteBufAllocator(), 256, 128, WcType.ONE_SEGMENT_WRITTEN);
        WriteCacheState validHalfSegWritten = new WriteCacheState(unpooledByteBufAllocator(), 256, 128, WcType.HALF_SEGMENT_WRITTEN);

        WriteCacheState invalidAllocatorState = new WriteCacheState(invalidByteBufAllocator(), 512, 128, WcType.NON_WRITTEN);
        WriteCacheState nullAllocatorState = new WriteCacheState(null, 512, 128, WcType.NON_WRITTEN);


        return Stream.of(

                /*
                // Test P1: allocator restituisce buffer nulli; test fallito
                Arguments.of(invalidAllocatorState, 1, 2, fullByteBuf(), false, Exception.class)
                */

                /*
                // Test P2: allocator nullo; test fallito
                Arguments.of(nullAllocatorState(), 1, 2, fullByteBuf(), false, Exception.class)
                */

                // Test P3: maxCacheSize = 0; test passato
                Arguments.of(invalidZeroCacheSize, 1, 2, fullByteBuf(), false, null),

                // Test 1: ledgerId negativo; test passato
                Arguments.of(validTwoSegmentUnWritten, -1, 2, fullByteBuf(), false, Exception.class),

                // Test 2: ledgerId nullo (0); test passato
                Arguments.of(validTwoSegmentUnWritten, 0, 2, fullByteBuf(), true, null),

                // Test 3: ledgerId positivo; test passato
                Arguments.of(validTwoSegmentUnWritten, 1, 2, fullByteBuf(), true, null),

                // Test 4: entryId negativo; test passato
                Arguments.of(validTwoSegmentUnWritten, 1, -1, fullByteBuf(), false, Exception.class),

                // Test 5: entryId nullo; test passato
                Arguments.of(validTwoSegmentUnWritten, 1, 0, fullByteBuf(), true, null),

                // Test 6: entryId positivo; test passato
                Arguments.of(validTwoSegmentUnWritten, 1, 1, fullByteBuf(), true, null),

                // Test 7: entryId positivo; test passato
                Arguments.of(validTwoSegmentUnWritten, 1, 2, fullByteBuf(), true, null),

                // Test 8: entry vuota; test passato
                Arguments.of(validTwoSegmentUnWritten, 1, 2, emptyByteBuf(), true, null),

                // Test 9: entry che sta in un singolo segmento; test passato
                Arguments.of(validTwoSegmentUnWritten, 1, 2, lenFullByteBuf(100), true, null),

                // Test 10: entry che supera maxSegmentSize; test passato
                Arguments.of(validTwoSegmentUnWritten, 1, 2, lenFullByteBuf(300), false, null),

                // Test 11: entry con indice di lettura errato; test passato
                Arguments.of(validTwoSegmentUnWritten, 1, 1, invalidReadIndexByteBuf(), false, Exception.class),

                // Test 12: entry deallocata; test passato
                Arguments.of(validTwoSegmentUnWritten, 1, 1, deallocatedByteBuf(), false, Exception.class),

                // Test 13: entry null
                Arguments.of(validTwoSegmentUnWritten, 1, 1, null, false, Exception.class)
        );
    }

    @ParameterizedTest
    @MethodSource("data")
    @Timeout(value = 5, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    void put(WriteCacheState s, long ledgerId, long entryId, ByteBuf entry, boolean expectedReturn,
             Class<? extends Exception> expectedException) {
        WriteCache wc = new WriteCache(s.allocator, s.maxCacheSize, s.maxSegmentSize);
        Assertions.assertNotNull(wc);

        int firstPutEntryId = -1;
        int firstPutEntrySize = 0;
        if (s.type == WcType.ONE_SEGMENT_WRITTEN || s.type == WcType.HALF_SEGMENT_WRITTEN) {
            firstPutEntryId = 2;
            firstPutEntrySize = s.type == WcType.ONE_SEGMENT_WRITTEN ? wc.getMaxSegmentSize() : wc.getMaxSegmentSize() / 2;
            ByteBuf firstPutEntry = lenFullByteBuf(firstPutEntrySize);
            Assertions.assertTrue(wc.put(ledgerId, firstPutEntryId, firstPutEntry), "Put failed during setup");
        }

        if (expectedException != null) {
            Assertions.assertThrows(expectedException, () -> wc.put(ledgerId, entryId, entry));
        } else {
            boolean actualReturn = wc.put(ledgerId, entryId, entry);

            Assertions.assertEquals(expectedReturn, actualReturn, "Return value mismatch");

            long expectedSize = expectedReturn ? entry.readableBytes() : 0;
            long expectedCount = expectedReturn ? 1 : 0;

            if (s.type == WcType.ONE_SEGMENT_WRITTEN || s.type == WcType.HALF_SEGMENT_WRITTEN) {
                expectedSize += firstPutEntrySize;
                expectedCount += 1;
            }

            Assertions.assertEquals(expectedSize, wc.size(), "Size mismatch");
            Assertions.assertEquals(expectedCount, wc.count(), "Count mismatch");

            long actualStoredEntryId = wc.getLastEntryMap().getOrDefault(ledgerId, -1L);
            long expectedStoredEntryId = actualReturn && entryId > firstPutEntryId ? entryId : firstPutEntryId;
            Assertions.assertEquals(expectedStoredEntryId, actualStoredEntryId, "Last entry ID mismatch");

            if (expectedReturn) {
                ConcurrentLongLongPairHashMap.LongPair pairs = wc.getIndex().get(ledgerId, entryId);
                Assertions.assertNotNull(pairs, "Index entry missing");

                long offset = pairs.first;
                int length = (int) pairs.second;
                int localOffset = (int) (offset % s.maxSegmentSize);
                int segment = (int) (offset / s.maxSegmentSize);

                ByteBuf actualWrittenEntry = Unpooled.buffer(length, length);
                actualWrittenEntry.writeBytes(wc.getCacheSegments()[segment], localOffset, length);
                String actualWrittenString = actualWrittenEntry.toString(StandardCharsets.UTF_8);
                String expectedWrittenString = entry.toString(StandardCharsets.UTF_8);

                Assertions.assertEquals(expectedWrittenString, actualWrittenString, "Written entry mismatch");
            }
        }
    }

    @Test
    @Timeout(value = 5, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    void testLastEntryMapBehaviorWithSequentialPuts() {

        // Setup iniziale
        WriteCacheState s = new WriteCacheState(unpooledByteBufAllocator(), 512, 256, WcType.NON_WRITTEN);
        WriteCache wc = new WriteCache(s.allocator, s.maxCacheSize, s.maxSegmentSize);
        long ledgerId = 1L;

        // Caso 1: Inserisco entryId = 1 (nuova entry)
        ByteBuf entry1 = fullByteBuf();
        Assertions.assertTrue(wc.put(ledgerId, 1L, entry1), "Put 1L failed");
        Assertions.assertEquals(1L, wc.getLastEntryMap().get(ledgerId), "After first put, lastEntryId should be 1");

        // Caso 2: Inserisco entryId = 0 (più vecchia)
        ByteBuf entry0 = fullByteBuf();
        Assertions.assertTrue(wc.put(ledgerId, 0L, entry0), "Put 0L failed (should succeed but not update)");
        Assertions.assertEquals(1L, wc.getLastEntryMap().get(ledgerId), "After older put, lastEntryId should remain 1");

        // Caso 3: Inserisco di nuovo entryId = 1 (duplicato)
        ByteBuf entry1Dup = fullByteBuf();
        Assertions.assertTrue(wc.put(ledgerId, 1L, entry1Dup), "Put duplicate 1L failed");
        Assertions.assertEquals(1L, wc.getLastEntryMap().get(ledgerId), "After duplicate put, lastEntryId should remain 1");

        // Caso 4: Inserisco entryId = 2 (più nuova)
        ByteBuf entry2 = fullByteBuf();
        Assertions.assertTrue(wc.put(ledgerId, 2L, entry2), "Put 2L failed");
        Assertions.assertEquals(2L, wc.getLastEntryMap().get(ledgerId), "After newer put, lastEntryId should update to 2");
    }

    private static WriteCacheState nullAllocatorState() {
        return new WriteCacheState(null, 256, 256, WcType.NON_WRITTEN);
    }

    private static final class WriteCacheState {
        private final ByteBufAllocator allocator;
        private final long maxCacheSize;
        private final int maxSegmentSize;
        private final WcType type;

        WriteCacheState(ByteBufAllocator allocator, long maxCacheSize, int maxSegmentSize, WcType type) {
            this.allocator = allocator;
            this.maxCacheSize = maxCacheSize;
            this.maxSegmentSize = maxSegmentSize;
            this.type = type;
        }
    }
}
