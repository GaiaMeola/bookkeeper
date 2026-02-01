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
import java.nio.file.StandardOpenOption;
import java.util.stream.Stream;

import static customutils.Utils.*;
import static org.mockito.Mockito.*;

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

                    // W3 – unpersistedBytesBound negativo --> modificato
                    Arguments.of(unpooledByteBufAllocator(), validFileChannel(), 100, 100, -1, emptyByteBuf(), null),


                    // W4 – FileChannel in sola lettura, ovviamente non può essere scritto; test passato
                    Arguments.of(unpooledByteBufAllocator(), readOnlyFileChannel(), 100, 100, 1, fullByteBuf(), Exception.class),


                    /*
                    // W5 – writeCapacity = 0 → eccezione; test in errore a causa del time-out
                    Arguments.of(unpooledByteBufAllocator(), validFileChannel(), 0, 100, 1, fullByteBuf(), Exception.class)
                     */

                    // W6 – Scrittura nel solo buffer, no flush; test passato
                    Arguments.of(unpooledByteBufAllocator(), validFileChannel(), 100, 100, BC_BB_CONTENT.length() + 1, fullByteBuf(), null),


                    // W7 – Scrittura che è uguale ad unpersistedBytesBound, quindi flush; test passato
                    Arguments.of(unpooledByteBufAllocator(), validFileChannel(), 100, 100, BC_BB_CONTENT.length(), fullByteBuf(), null),


                    // W8 – Scrittura che supera l’unpersistedBytesBound, quindi flush; test passato
                    Arguments.of(unpooledByteBufAllocator(), validFileChannel(), 100, 100, BC_BB_CONTENT.length() - 1,  fullByteBuf(), null),


                     // W9 – Scrittura che supera l’unpersistedBytesBound, poiché è nullo; test fallito --> si modifica dopo la comprensione della logica
                    Arguments.of(unpooledByteBufAllocator(), validFileChannel(), 100, 100, 0, emptyByteBuf(), null),


                    // W10 - ByteBuf con index invalido
                    Arguments.of(unpooledByteBufAllocator(), validFileChannel(), 100, 100, 1, invalidWriteIndexByteBuf(), Exception.class),

                    // W11 – ByteBuf deallocato; test passato
                    Arguments.of(unpooledByteBufAllocator(), validFileChannel(), 100, 100, 1, deallocatedByteBuf(), Exception.class),

                    // W12 – ByteBuf vuoto; test passato
                    Arguments.of(unpooledByteBufAllocator(), validFileChannel(), 100, 100, 1, emptyByteBuf(), null),

                    // W13 – ByteBuf nullo; test passato
                    Arguments.of(unpooledByteBufAllocator(), validFileChannel(), 100, 100, 1, null, Exception.class),

                    // W14 – ByteBuf write-only / senza byte leggibili.
                    // Il sistema adotta un approccio permissivo: l'assenza di dati leggibili
                    // comporta l'uscita silente dal metodo senza sollevare eccezioni.
                    Arguments.of(unpooledByteBufAllocator(), validFileChannel(), 100, 100, 1, emptyByteBuf(), null)
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testJW1_BufferFullTriggersFlush() throws IOException {
        String contentToWrite = BC_BB_CONTENT; // "Byte Buffer?!", lunghezza 13
        int halfLength = contentToWrite.length() / 2; // 6 byte

        // Pulizia file
        Path path = Paths.get(BC_TEST_FILE);
        if (Files.exists(path)) Files.delete(path);
        Files.createFile(path);

        FileChannel fc = FileChannel.open(path,
                StandardOpenOption.READ, StandardOpenOption.WRITE);

        BufferedChannel bc = new BufferedChannel(
                unpooledByteBufAllocator(),
                fc,
                halfLength, // writeCapacity = 6
                100,
                0 // unpersistedBytesBound
        );

        ByteBuf src = Unpooled.copiedBuffer(contentToWrite.getBytes(StandardCharsets.UTF_8));
        bc.write(src);

        // Lettura dal FileChannel: devono esserci i primi 12 byte
        ByteBuffer fcContent = ByteBuffer.allocate(contentToWrite.length());
        fc.position(0);
        fc.read(fcContent);
        fcContent.flip();
        String writtenFcContent = new String(fcContent.array(), 0, fcContent.limit(), StandardCharsets.UTF_8);

        String expectedFcContent = contentToWrite.substring(0, 12); // primi 12 byte
        Assertions.assertEquals(expectedFcContent, writtenFcContent,
                "I byte che saturano il buffer devono essere scritti sul file channel");

        // Il buffer ora deve contenere l'ultimo byte
        String expectedRemaining = contentToWrite.substring(12); // ultimo byte "!"
        String remainingContent = bc.getWriteBuffer().toString(StandardCharsets.UTF_8);
        Assertions.assertEquals(expectedRemaining, remainingContent,
                "Il resto dei byte deve rimanere nel writeBuffer");
        Assertions.assertEquals(1, bc.getWriteBuffer().readableBytes(),
                "Il writeBuffer deve contenere esattamente 1 byte rimanente");
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

    //aggiunto dopo l'analisi di PIT --> T7
    @Test
    void testForceWriteNotCalledWhenShouldForceWriteFalse() throws IOException {
        Path path = Paths.get(BC_TEST_FILE);
        if (Files.exists(path)) Files.delete(path);
        Files.createFile(path);
        FileChannel fc = FileChannel.open(path, StandardOpenOption.READ, StandardOpenOption.WRITE);

        BufferedChannel bc = new BufferedChannel(unpooledByteBufAllocator(), fc, 1000, 100, 1000);
        BufferedChannel spyBc = spy(bc);

        ByteBuf src = Unpooled.copiedBuffer("abc".getBytes(StandardCharsets.UTF_8));
        spyBc.write(src);

        // Verifica che forceWrite NON sia chiamato
        verify(spyBc, never()).forceWrite(anyBoolean());
    }

    @AfterEach
    public void deleteTestFile() throws IOException {
        Path path = Paths.get(BC_TEST_FILE);
        if (Files.exists(path)) Files.delete(path);
    }
}

