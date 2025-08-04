package manualtest;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import org.apache.bookkeeper.bookie.BufferedChannel;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static customutils.Utils.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BufferedChannelWriteTest {

   private static Stream<Arguments> data() {
        try {
            return Stream.of(


                    // W1 – Allocator invalido (mock) → eccezione; test passato
                    Arguments.of(invalidByteBufAllocator(), validFileChannel(), 100, 100, 1, fullByteBuf(), Exception.class),

                    /* test fallito
                    // W2 – FileChannel con posizione non valida → eccezione su flush
                    Arguments.of(unpooledByteBufAllocator(), invalidPositionFileChannel(), 100, 100, 1, fullByteBuf(), Exception.class)
                    */

                    /*test fallito
                    // W3 – unpersistedBytesBound negativo
                    Arguments.of(unpooledByteBufAllocator(), validFileChannel(), 100, 100, -1, fullByteBuf(), Exception.class)
                    */

                    // W4 – FileChannel in sola lettura, ovviamente non può essere scritto; test passato
                    Arguments.of(unpooledByteBufAllocator(), readOnlyFileChannel(), 100, 100, 1, fullByteBuf(), Exception.class),


                    /*
                    // W5 – writeCapacity = 0 → eccezione; test in errore a causa del time-out
                    Arguments.of(unpooledByteBufAllocator(), validFileChannel(), 0, 100, 1, fullByteBuf(), Exception.class)
                     */


                    // T1.1 – Scrittura nel solo buffer, no flush; test passato
                    Arguments.of(unpooledByteBufAllocator(), validFileChannel(), 100, 100, BC_BB_CONTENT.length() + 1, fullByteBuf(), null),


                    // T1.2 – Scrittura che è uguale ad unpersistedBytesBound, quindi flush; test passato
                    Arguments.of(unpooledByteBufAllocator(), validFileChannel(), 100, 100, BC_BB_CONTENT.length(), fullByteBuf(), null),


                    // T1.3 – Scrittura che supera l’unpersistedBytesBound, quindi flush; test passato
                    Arguments.of(unpooledByteBufAllocator(), validFileChannel(), 100, 100, BC_BB_CONTENT.length() - 1,  fullByteBuf(), null),

                     /*
                     // T1.4 – Scrittura che supera l’unpersistedBytesBound, poiché è nullo; test fallito
                    Arguments.of(unpooledByteBufAllocator(), validFileChannel(), 100, 100, 0, fullByteBuf(), null)
                    */

                    // T2 - ByteBuf con index invalido
                    Arguments.of(unpooledByteBufAllocator(), validFileChannel(), 100, 100, 1, invalidWriteIndexByteBuf(), Exception.class),

                    // T3 – ByteBuf deallocato; test passato
                    Arguments.of(unpooledByteBufAllocator(), validFileChannel(), 100, 100, 1, deallocatedByteBuf(), Exception.class),

                    // T4 – ByteBuf vuoto; test passato
                    Arguments.of(unpooledByteBufAllocator(), validFileChannel(), 100, 100, 1, emptyByteBuf(), null),

                    // T5 – ByteBuf nullo; test passato
                    Arguments.of(unpooledByteBufAllocator(), validFileChannel(), 100, 100, 1, null, Exception.class)

                    /*
                    // T6 – ByteBuf write-only (nessun byte leggibile); test fallito
                    Arguments.of(unpooledByteBufAllocator(), validFileChannel(), 100, 100, 1, writeOnlyByteBuf(), Exception.class)
                     */
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @ParameterizedTest
    @MethodSource("data")
    @Timeout(value = 5, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    void write(ByteBufAllocator allocator, FileChannel fc, int writeCapacity, int readCapacity,
                      long unpersistedBytesBound, ByteBuf src, Class<Exception> expectedException) {

        BufferedChannel bc;
        String expectedWrittenContent;
        int expectedWrittenContentLength;
        long initialFileChannelPosition;
        long expectedPosition, expectedUnpersistedBytes, expectedWriteBufferStartPosition;

        try {
            bc = new BufferedChannel(allocator, fc, writeCapacity, readCapacity, unpersistedBytesBound);
            Assertions.assertNotNull(bc);
            initialFileChannelPosition = fc.position();
        } catch (Exception e) { throw new RuntimeException(e); }

        if (expectedException != null) {
            Assertions.assertThrows(expectedException, () -> bc.write(src));
        } else {
            try {
                bc.write(src);

                expectedWrittenContent = src.toString(StandardCharsets.UTF_8);
                expectedWrittenContentLength = expectedWrittenContent.length();

                int expectedFcWrittenBytesLength;
                int expectedWbWrittenBytesLength;
                String expectedFcWrittenContent;
                String expectedWbWrittenContent;
                String actualFcWrittenContent;
                String actualWbWrittenContent;

                if (unpersistedBytesBound < 1) {
                    expectedFcWrittenContent = expectedWrittenContent;
                    expectedWbWrittenContent = "";
                } else if (expectedWrittenContentLength < writeCapacity) {
                    if (expectedWrittenContentLength < unpersistedBytesBound) {
                        expectedFcWrittenContent = "";
                        expectedWbWrittenContent = expectedWrittenContent;
                    } else {
                        expectedFcWrittenContent = expectedWrittenContent;
                        expectedWbWrittenContent = "";
                    }
                } else {
                    int lastBytesWrittenOnWbLength = expectedWrittenContentLength % writeCapacity;
                    int bytesWrittenOnFcLength = expectedWrittenContentLength - lastBytesWrittenOnWbLength;
                    if (lastBytesWrittenOnWbLength == 0) {
                        expectedFcWrittenContent = expectedWrittenContent;
                        expectedWbWrittenContent = "";
                    } else {
                        if (lastBytesWrittenOnWbLength > unpersistedBytesBound) {
                            expectedFcWrittenContent = expectedWrittenContent;
                            expectedWbWrittenContent = "";
                        } else {
                            expectedFcWrittenContent = expectedWrittenContent.substring(0, bytesWrittenOnFcLength);
                            expectedWbWrittenContent = expectedWrittenContent.substring(bytesWrittenOnFcLength);
                        }
                    }
                }

                expectedFcWrittenBytesLength = expectedFcWrittenContent.length();
                expectedWbWrittenBytesLength = expectedWbWrittenContent.length();

                ByteBuffer bb = ByteBuffer.allocate(expectedWrittenContentLength);
                fc.read(bb, initialFileChannelPosition);
                bb.flip();
                actualFcWrittenContent = new String(bb.array(), 0, bb.limit());

                ByteBuf actualWrittenBuffer = Unpooled.buffer(expectedWrittenContentLength);
                bc.getWriteBuffer().getBytes(0, actualWrittenBuffer, expectedWbWrittenBytesLength);
                actualWbWrittenContent = actualWrittenBuffer.toString(StandardCharsets.UTF_8);

                Assertions.assertEquals(expectedFcWrittenContent, actualFcWrittenContent);
                Assertions.assertEquals(expectedWbWrittenContent, actualWbWrittenContent);

                expectedPosition = initialFileChannelPosition + expectedWrittenContentLength;
                expectedUnpersistedBytes = expectedWbWrittenBytesLength;
                expectedWriteBufferStartPosition = initialFileChannelPosition + expectedFcWrittenBytesLength;

                Assertions.assertEquals(expectedPosition, bc.getPosition());
                Assertions.assertEquals(expectedUnpersistedBytes, bc.getUnpersistedBytes());
                Assertions.assertEquals(expectedWriteBufferStartPosition, bc.getWriteBufferStartPosition().get());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @AfterEach
    public void deleteTestFile() throws IOException {
        Path path = Paths.get(BC_TEST_FILE);
        if (Files.exists(path)) Files.delete(path);
    }
}

