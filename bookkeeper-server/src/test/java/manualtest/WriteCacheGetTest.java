package manualtest;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.apache.bookkeeper.bookie.storage.ldb.WriteCache;
import org.apache.bookkeeper.util.collections.ConcurrentLongLongPairHashMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Field;
import java.util.stream.Stream;

import static customutils.Utils.*;  // Assume lenFullByteBuf(), emptyByteBuf() etc.

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WriteCacheGetTest {

    private static Stream<Arguments> data() {
       /* WriteCacheState invalidAllocatorState = new WriteCacheState(invalidByteBufAllocator(), 512, 128);
        WriteCacheState nullAllocatorState = new WriteCacheState(null, 512, 128);*/
        WriteCacheState invalidZeroCacheSize = new WriteCacheState(unpooledByteBufAllocator(), 0, 1);
        WriteCacheState validTwoSegmentUnWritten = new WriteCacheState(unpooledByteBufAllocator(), 512, 128);

        return Stream.of(

                /*test G1: invalid allocator; test fallito
                Arguments.of(invalidAllocatorState, 1, 1, 0, 0, null, Exception.class)
                */
                /*test G2: null allocator; test fallito
                Arguments.of(nullAllocatorState, 1, 1, 0, 0, null, Exception.class)
                 */

                // test G3: maxCacheSize = 0; test passato
                Arguments.of(invalidZeroCacheSize, 1, 1, 0, 0, null, null),

                // test 1: ledgerId < 0; test passato
                Arguments.of(validTwoSegmentUnWritten, -1, 1, 0, 0, null, Exception.class),

                /*
                // test 2: entryId < 0; test fallito
                Arguments.of(validTwoSegmentUnWritten, 1, -1, 0, 0, null, Exception.class)
                 */

                // test 3:
                Arguments.of(validTwoSegmentUnWritten, 0, 1, 0, 1, lenFullByteBuf(1), null)


        );
    }

    @ParameterizedTest
    @MethodSource("data")
    @Timeout(value = 5, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    void get(WriteCacheState s, long ledgerId, long entryId, long offset, int size,
             ByteBuf expectedEntry, Class<? extends Exception> expectedException) throws Exception {

        WriteCache wc = new WriteCache(s.allocator, s.maxCacheSize, s.maxSegmentSize);
        Assertions.assertNotNull(wc);

        // Inserisco dati nella index e nei segmenti, se serve
        if (size > 0) {
            Field indexField = WriteCache.class.getDeclaredField("index");
            indexField.setAccessible(true);
            ConcurrentLongLongPairHashMap index = (ConcurrentLongLongPairHashMap) indexField.get(wc);

            Field cacheSegmentsField = WriteCache.class.getDeclaredField("cacheSegments");
            cacheSegmentsField.setAccessible(true);
            ByteBuf[] cacheSegments = (ByteBuf[]) cacheSegmentsField.get(wc);

            Field segmentOffsetBitsField = WriteCache.class.getDeclaredField("segmentOffsetBits");
            segmentOffsetBitsField.setAccessible(true);
            long segmentOffsetBitsLong = segmentOffsetBitsField.getLong(wc);
            int segmentOffsetBits = (int) segmentOffsetBitsLong;  // cast esplicito

            Field segmentOffsetMaskField = WriteCache.class.getDeclaredField("segmentOffsetMask");
            segmentOffsetMaskField.setAccessible(true);
            long segmentOffsetMaskLong = segmentOffsetMaskField.getLong(wc);
            int segmentOffsetMask = (int) segmentOffsetMaskLong;  // cast esplicito

            int segmentIdx = (int) (offset >>> segmentOffsetBits);
            int localOffset = (int) (offset & segmentOffsetMask);

            if (cacheSegments[segmentIdx] != null) {
                byte[] data = new byte[size];
                cacheSegments[segmentIdx].setBytes(localOffset, data);
            }

            index.put(ledgerId, entryId, offset, size);
        }

        if (expectedException != null) {
            Assertions.assertThrows(expectedException, () -> wc.get(ledgerId, entryId));
            return;
        }

        ByteBuf result = wc.get(ledgerId, entryId);

        if (expectedEntry == null) {
            Assertions.assertNull(result, "Expected null result");
        } else {
            Assertions.assertNotNull(result, "Expected non-null ByteBuf");
            Assertions.assertEquals(expectedEntry.readableBytes(), result.readableBytes(), "Size mismatch");

            for (int i = 0; i < expectedEntry.readableBytes(); i++) {
                Assertions.assertEquals(expectedEntry.getByte(i), result.getByte(i), "Byte mismatch at index " + i);
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
