package manualtest;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.apache.bookkeeper.bookie.BufferedChannel;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static customutils.Utils.*;

/**
 * Unit testing for {@link BufferedChannel}. class <br>
 * Tested method: {@link BufferedChannel#read(ByteBuf, long, int)}
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BufferedChannelReadTest {

    private static Stream<Arguments> data() {
        try {

            /*
            BufferedChannelState t2Invalid = new BufferedChannelState(invalidByteBufAllocator(),  validFileChannel(),100, 100, 1);
             */
            BufferedChannelState t7Invalid = new BufferedChannelState(unpooledByteBufAllocator(), invalidPositionFileChannel(), 100, 100, 1);
            BufferedChannelState t5Invalid = new BufferedChannelState(unpooledByteBufAllocator(), writeOnlyFileChannel(),       100, 100, 1);
            BufferedChannelState t13Invalid = new BufferedChannelState(invalidByteBufAllocator(),  validFileChannel(),           100,   0, 1);
            BufferedChannelState valid     = new BufferedChannelState(unpooledByteBufAllocator(), validFileChannel(), 100, 100, 1);

            // fix test T2 dinamicamente
            int length = BC_FC_CONTENT.length() / 2;
            ByteBuf semi = semiFullByteBuf();
            int expected = Math.min(length, semi.writableBytes());


            return Stream.of(

                    /*
                    // test R1; test fallito
                    Arguments.of(t2Invalid, null, emptyByteBuf(), 0, BC_FC_CONTENT.length(), Exception.class, -1)
                    */

                    // test R2; test passato
                    Arguments.of(t7Invalid, null, emptyByteBuf(), 0, BC_FC_CONTENT.length(), Exception.class, -1),

                    // test R3; test passato
                    Arguments.of(t5Invalid, null, emptyByteBuf(), 0, BC_FC_CONTENT.length(), Exception.class, -1),

                    // test R4; test passato
                    Arguments.of(t13Invalid, null, emptyByteBuf(), 0, BC_FC_CONTENT.length(), Exception.class, -1),

                    // test T1; test passato
                    Arguments.of(valid, null, emptyByteBuf(), 0, BC_FC_CONTENT.length(), null, BC_FC_CONTENT.length()),

                    // test T2; test passato
                    Arguments.of(valid, null, semi, 0, length, null, expected),

                    //test T3; test passato
                    Arguments.of(valid, null, fullByteBuf(), 0, BC_FC_CONTENT.length(), Exception.class, -1),

                    //test T4; test passato
                    Arguments.of(valid, null, invalidWriteIndexByteBuf(), 0, BC_FC_CONTENT.length(), Exception.class, -1),

                    //test T5; test passato
                    Arguments.of(valid, null, deallocatedByteBuf(), 0, BC_FC_CONTENT.length(), Exception.class, -1),

                    //test T6; test passato
                    Arguments.of(valid, null, null, 0, BC_FC_CONTENT.length(), Exception.class, -1),

                    //test T7; test passato
                    Arguments.of(valid, BC_BB_CONTENT, emptyByteBuf(), -1, 1, Exception.class, -1),

                     /*
                    //test T8; test fallito
                    Arguments.of(valid, BC_BB_CONTENT, emptyByteBuf(), 1, -1, Exception.class, -1),
                    */

                    //test T9; test passato
                    Arguments.of(valid, BC_BB_CONTENT, emptyByteBuf(), 0, 0, null, 0),

                    //test T10; test passato
                    Arguments.of(valid, BC_BB_CONTENT, emptyByteBuf(), 0, 1, null, BC_FC_CONTENT.length()),

                    //test T11; test passato
                    Arguments.of(valid, BC_BB_CONTENT, emptyByteBuf(), 0, BC_FC_CONTENT.length()-1, null, BC_FC_CONTENT.length()),

                    //test T12; test passato
                    Arguments.of(valid, BC_BB_CONTENT, emptyByteBuf(), 0, BC_FC_CONTENT.length(), null, BC_FC_CONTENT.length()),

                    //test T13; test passato
                    Arguments.of(valid, BC_BB_CONTENT, emptyByteBuf(), 1, BC_FC_CONTENT.length() - 1, null, BC_FC_CONTENT.length()-1),

                    //test T14; test passato
                    Arguments.of(valid, BC_BB_CONTENT, emptyByteBuf(), BC_FC_CONTENT.length(), 1, null, BC_BB_CONTENT.length()),

                    //test T15; test passato
                    Arguments.of(valid, BC_BB_CONTENT, emptyByteBuf(), BC_FC_CONTENT.length()+BC_BB_CONTENT.length()-1, 1, null, 1),

                    //test T16; test passato
                    Arguments.of(valid, BC_BB_CONTENT, emptyByteBuf(), 0, BC_FC_CONTENT.length()+1, null, BC_FC_CONTENT.length()+BC_BB_CONTENT.length()),

                    //test T17; test passato
                    Arguments.of(valid, BC_BB_CONTENT, emptyByteBuf(), 0, BC_FC_CONTENT.length()+BC_BB_CONTENT.length(), null, BC_FC_CONTENT.length()+BC_BB_CONTENT.length()),

                    //test T18; test passato
                    Arguments.of(valid, BC_BB_CONTENT, emptyByteBuf(), 0, BC_FC_CONTENT.length()+BC_BB_CONTENT.length()+1, Exception.class, -1),

                    // //test T19; test passato
                    Arguments.of(valid, BC_BB_CONTENT, emptyByteBuf(), BC_FC_CONTENT.length()+BC_BB_CONTENT.length(), 1, Exception.class, -1)
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @ParameterizedTest
    @MethodSource("data")
    @Timeout(value = 5, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    void read(BufferedChannelState state, String wbContent, ByteBuf dest, long pos,
              int length, Class<Exception> expectedException, int expectedReturn) {

        if(expectedException != null && expectedReturn > 0)
            throw new RuntimeException("Invalid test configuration");

        BufferedChannel bc;
        int destStartingWritePos = dest != null ? dest.writerIndex() : 0;

        try {
            bc = new BufferedChannel(state.allocator, state.fc, state.writeCapacity, state.readCapacity, state.unpersistedBytesBound);
            Assertions.assertNotNull(bc);
            if(wbContent != null)
                bc.getWriteBuffer().writeBytes(wbContent.getBytes(StandardCharsets.UTF_8));

            clearReadBuffer(bc); // invalida il readBuffer per forzare lettura da FileChannel
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (expectedException != null) {
            Assertions.assertThrows(expectedException, () -> bc.read(dest, pos, length));
        }
        else {
            try {
                assert dest != null;
                int actualReturn = bc.read(dest, pos, length);

                // Costruiamo la stringa effettivamente scritta nel buffer di destinazione
                ByteBuf actualWrittenBuffer = dest.slice(destStartingWritePos, actualReturn);
                String actualWrittenString = actualWrittenBuffer.toString(StandardCharsets.UTF_8);

                // Costruzione expected: parte da `pos` e legge `actualReturn` byte
                String expectedTotalContent = BC_FC_CONTENT + BC_BB_CONTENT;

                String expectedWrittenString = expectedTotalContent.substring((int) pos, (int) pos + actualReturn);

                // Asserzioni
                Assertions.assertEquals(expectedWrittenString, actualWrittenString);
                Assertions.assertEquals(expectedReturn, actualReturn);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @AfterEach
    public void deleteTestFile() throws IOException {
        Path path = Paths.get(BC_TEST_FILE);
        if (Files.exists(path))
            Files.delete(path);
    }

    private static final class BufferedChannelState {
        private final ByteBufAllocator allocator;
        private final FileChannel fc;
        private final int writeCapacity;
        private final int readCapacity;
        private final long unpersistedBytesBound;

        BufferedChannelState(ByteBufAllocator allocator, FileChannel fc, int writeCapacity, int readCapacity,
                             long unpersistedBytesBound) {
            this.allocator = allocator;
            this.fc = fc;
            this.writeCapacity = writeCapacity;
            this.readCapacity = readCapacity;
            this.unpersistedBytesBound = unpersistedBytesBound;
        }
    }
}
