package manualtest;

import io.netty.buffer.ByteBufAllocator;
import org.apache.bookkeeper.bookie.storage.ldb.WriteCache;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;
import static customutils.Utils.*;

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

    @ParameterizedTest
    @MethodSource("data")
    @Timeout(5)
    void construct(ByteBufAllocator allocator, long maxCacheSize, int maxSegmentSize, Class<? extends Exception> expectedException) {
        if (expectedException != null) {
            Assertions.assertThrows(expectedException, () -> new WriteCache(allocator, maxCacheSize, maxSegmentSize));
        } else {
            WriteCache wc = new WriteCache(allocator, maxCacheSize, maxSegmentSize);
            Assertions.assertNotNull(wc);
        }
    }
}
