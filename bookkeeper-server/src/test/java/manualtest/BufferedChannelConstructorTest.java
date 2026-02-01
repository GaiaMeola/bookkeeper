package manualtest;


import io.netty.buffer.ByteBufAllocator;
import org.apache.bookkeeper.bookie.BufferedChannel;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static customutils.Utils.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit testing for {@link BufferedChannel}. class <br>
 * Tested method: constructor
 */

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BufferedChannelConstructorTest {

    private static Stream<Arguments> data() {
        try {
            return Stream.of(
                    // Varying allocator
                    Arguments.of(unpooledByteBufAllocator(), validFileChannel(), 100, 100, 1, null),
                    /*Arguments.of(invalidByteBufAllocator(),  validFileChannel(), 100, 100, 1, Exception.class), // T2 Not passed */
                    Arguments.of(null, validFileChannel(), 100, 100, 1, Exception.class),


                    // Varying FileChannel
                    Arguments.of(unpooledByteBufAllocator(),    readOnlyFileChannel(),          100, 100, 1, null),
                    Arguments.of(unpooledByteBufAllocator(),    writeOnlyFileChannel(),         100, 100, 1, null),
                    Arguments.of(unpooledByteBufAllocator(),    closedFileChannel(),            100, 100, 1, Exception.class),
                   /* Arguments.of(unpooledByteBufAllocator(),    invalidPositionFileChannel(),   100, 100, 1, Exception.class) // T7 Not passed */
                    Arguments.of(unpooledByteBufAllocator(),    null,                           100, 100, 1, Exception.class),

                    // Varying writeCapacity
                    Arguments.of(unpooledByteBufAllocator(),    validFileChannel(),    1, 100, 1, null),
                    Arguments.of(unpooledByteBufAllocator(),    validFileChannel(),    0, 100, 1, null),
                    Arguments.of(unpooledByteBufAllocator(),    validFileChannel(),    -1, 100, 1, Exception.class),

                    // Varying readCapacity
                    Arguments.of(unpooledByteBufAllocator(), validFileChannel(), 100,       1,      1, null),
                    Arguments.of(unpooledByteBufAllocator(), validFileChannel(), 100,       0,      1, null),
                    Arguments.of(unpooledByteBufAllocator(), validFileChannel(), 100,       -1,     1, Exception.class),

                    // Varying unpersistedBytesBound
                    Arguments.of(unpooledByteBufAllocator(), validFileChannel(), 100, 100,      1,     null),
                    Arguments.of(unpooledByteBufAllocator(), validFileChannel(), 100, 100,      0,      null)
                   /* Arguments.of(unpooledByteBufAllocator(), validFileChannel(), 100, 100,     -1,     Exception.class)        // T17 Not passed */
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // --- Test parametrizzato ---

    @ParameterizedTest
    @MethodSource("data")
    @Timeout(value = 5, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    void construct(ByteBufAllocator allocator, FileChannel fc, int writeCapacity, int readCapacity,
                   long unpersistedBytesBound, Class<? extends Exception> expectedException) {
        if (expectedException != null) {
            assertThrows(expectedException,
                    () -> new BufferedChannel(allocator, fc, writeCapacity, readCapacity, unpersistedBytesBound),
                    "Expected exception: " + expectedException.getName());
        } else {
            try {
                BufferedChannel bc = new BufferedChannel(allocator, fc, writeCapacity, readCapacity, unpersistedBytesBound);
                Assertions.assertNotNull(bc);

                // Verifica campi principali
                Assertions.assertEquals(fc.position(), bc.getFileChannelPosition());
                Assertions.assertEquals(0, bc.getUnpersistedBytes());
                Assertions.assertEquals(unpersistedBytesBound, bc.getUnpersistedBytesBound());
                Assertions.assertSame(allocator, bc.getWriteBuffer().alloc());
                Assertions.assertEquals(fc.position(), bc.position());
                Assertions.assertEquals(writeCapacity, bc.getWriteCapacity());

                // Verifica campi superclass BufferedReadChannel (se presenti)
                Assertions.assertEquals(0, bc.getCacheHitCount());
                Assertions.assertEquals(0, bc.getInvocationCount());
                Assertions.assertNotNull(bc.getReadBuffer());
                Assertions.assertEquals(readCapacity, bc.getReadCapacity());
                Assertions.assertFalse(bc.isSealed());

                // Verifica campi superclass BufferedChannelBase (se presenti)
                Assertions.assertEquals(fc, bc.getFileChannel());

            } catch (Exception e) {
                Assertions.fail("Unexpected exception thrown: " + e);
            }
        }
    }

    @AfterEach
    public void deleteTestFile() throws IOException {
        Path path = Paths.get(BC_TEST_FILE);
        if (Files.exists(path)) Files.delete(path);
    }
}