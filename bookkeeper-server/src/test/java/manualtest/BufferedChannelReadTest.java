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
            BufferedChannelState t7Invalid = new BufferedChannelState(unpooledByteBufAllocator(), invalidPositionFileChannel(), 100, 100, 1, false);
            BufferedChannelState t5Invalid = new BufferedChannelState(unpooledByteBufAllocator(), writeOnlyFileChannel(),       100, 100, 1, false);
            BufferedChannelState t13Invalid = new BufferedChannelState(invalidByteBufAllocator(),  validFileChannel(),           100,   0, 1, false);
            BufferedChannelState valid     = new BufferedChannelState(unpooledByteBufAllocator(), validFileChannel(), 100, 100, 1, false);
            //aggiunto a seguito dell'utilizzo di Jacoco
            BufferedChannelState wbNullWriteState = new BufferedChannelState(unpooledByteBufAllocator(), validFileChannel(), 100, 100, 1, true);


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
                    Arguments.of(valid, BC_BB_CONTENT, emptyByteBuf(), BC_FC_CONTENT.length()+BC_BB_CONTENT.length(), 1, Exception.class, -1),

                    //test J_R1; test passato
                    Arguments.of(wbNullWriteState, BC_BB_CONTENT, emptyByteBuf(),  BC_FC_CONTENT.length() + BC_BB_CONTENT.length() - 1, 1, null, 0)
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @ParameterizedTest
    @MethodSource("data")
    @Timeout(value = 5, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    void read(BufferedChannelState state, String wbContent, ByteBuf dest, long pos,
              int length, Class<? extends Throwable> expectedException, int expectedReturn) {

        if(expectedException != null && expectedReturn > 0)
            throw new RuntimeException("Invalid test configuration");

        BufferedChannel bc;
        int destStartingWritePos = dest != null ? dest.writerIndex() : 0;

        try {
            // Se il test richiede un writeBuffer nullo, usiamo il metodo speciale di Utils
            if (state.useNullWriteBuffer) {
                bc = bufferedChannelWithNullWriteBuffer(
                        state.allocator, state.fc, state.writeCapacity, state.readCapacity, state.unpersistedBytesBound
                );
            } else {
                bc = new BufferedChannel(
                        state.allocator, state.fc, state.writeCapacity, state.readCapacity, state.unpersistedBytesBound
                );
            }

            Assertions.assertNotNull(bc);

            // Se wbContent è valorizzato, scriviamo sul writeBuffer
            if (wbContent != null && bc.getWriteBuffer() != null) {
                bc.getWriteBuffer().writeBytes(wbContent.getBytes(StandardCharsets.UTF_8));
            }

            clearReadBuffer(bc); // invalida il readBuffer per forzare lettura da FileChannel

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        if (expectedException != null) {
            try {
                bc.read(dest, pos, length);
                Assertions.fail("Expected exception was not thrown");
            } catch (Throwable t) {
                Assertions.assertTrue(expectedException.isInstance(t),
                        () -> "Expected " + expectedException.getName() + " but got " + t.getClass().getName());
            }
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

    @Test
    void testJR2_ReadBufferConditionCoverage() throws IOException {
        // Prepariamo il BufferedChannel normale
        BufferedChannel bc = new BufferedChannel(
                unpooledByteBufAllocator(),
                validFileChannel(),
                100, // writeCapacity
                100, // readCapacity
                1    // unpersistedBytesBound
        );

        // Clear del readBuffer per forzare la lettura da file
        clearReadBuffer(bc);

        // Primo read: legge i primi 5 byte
        ByteBuf dest1 = Unpooled.buffer(5); // buffer almeno grande quanto il length
        int read1 = bc.read(dest1, 0, 5);
        Assertions.assertEquals(5, read1);

        // Il readBufferStartPosition ora è stato aggiornato a pos=0
        // Secondo read: leggiamo in una posizione più piccola per far entrare il ramo readBufferStartPosition > pos
        ByteBuf dest2 = Unpooled.buffer(5); // di nuovo, almeno grande quanto length
        int read2 = bc.read(dest2, 0, 5);

        // Verifica solo che ritorni la dimensione letta corretta
        Assertions.assertEquals(5, read2);

        String expectedContent = (BC_FC_CONTENT + BC_BB_CONTENT).substring(0, 5);
        String actualContent = dest2.toString(StandardCharsets.UTF_8);
        Assertions.assertEquals(expectedContent, actualContent);
    }

    @Test
    void testJR3_ReadWithinBufferedRange() throws IOException {
        // BufferedChannel con file di almeno 10 byte
        BufferedChannel bc = new BufferedChannel(
                unpooledByteBufAllocator(),
                validFileChannel(), // assicurati che contenga almeno 10 byte
                100,
                100,
                1
        );

        clearReadBuffer(bc); //invalido il read buffer

        // Prima lettura: carichiamo i primi 10 byte nel buffer
        ByteBuf dest1 = Unpooled.buffer(10);
        int read1 = bc.read(dest1, 0, 10);
        Assertions.assertEquals(10, read1);

        // Seconda lettura: pos=0, length=5 (tutto nel buffer già caricato)
        ByteBuf dest2 = Unpooled.buffer(5);
        int read2 = bc.read(dest2, 0, 5);
        Assertions.assertEquals(5, read2);

        String expected = (BC_FC_CONTENT + BC_BB_CONTENT).substring(0, 5);
        String actual = dest2.toString(StandardCharsets.UTF_8);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testJR4_ReadFullyWithinBuffer() throws IOException {
        BufferedChannel bc = new BufferedChannel(
                unpooledByteBufAllocator(),
                validFileChannel(),
                100,
                100,
                1
        );

        clearReadBuffer(bc);

        // Carico 10 byte nel readBuffer
        ByteBuf tmp = Unpooled.buffer(10);
        int readTmp = bc.read(tmp, 0, 10);
        Assertions.assertEquals(10, readTmp);

        // Leggo nuovamente, questa volta completamente nel buffer: pos=2, length=5
        ByteBuf dest = Unpooled.buffer(5);
        int read = bc.read(dest, 2, 5);
        Assertions.assertEquals(5, read);

        // Controllo contenuto letto
        String expected = (BC_FC_CONTENT + BC_BB_CONTENT).substring(2, 7);
        String actual = dest.toString(StandardCharsets.UTF_8);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testReadPartialWithinReadBuffer() throws IOException {
        BufferedChannel bc = new BufferedChannel(
                unpooledByteBufAllocator(),
                validFileChannel(),
                100,
                100,
                1
        );

        clearReadBuffer(bc);

        // Popola il buffer con i primi 10 byte
        ByteBuf tmp = Unpooled.buffer(10);
        int readTmp = bc.read(tmp, 0, 10);
        Assertions.assertEquals(10, readTmp);

        // Leggi parzialmente all’interno del buffer: pos=3, length=5
        ByteBuf dest = Unpooled.buffer(5);
        int read = bc.read(dest, 3, 5);
        Assertions.assertEquals(5, read);

        String expected = (BC_FC_CONTENT + BC_BB_CONTENT).substring(3, 8);
        String actual = dest.toString(StandardCharsets.UTF_8);
        Assertions.assertEquals(expected, actual);
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
        private final boolean useNullWriteBuffer;

        BufferedChannelState(ByteBufAllocator allocator, FileChannel fc, int writeCapacity, int readCapacity,
                             long unpersistedBytesBound, boolean useNullWriteBuffer) {
            this.allocator = allocator;
            this.fc = fc;
            this.writeCapacity = writeCapacity;
            this.readCapacity = readCapacity;
            this.unpersistedBytesBound = unpersistedBytesBound;
            this.useNullWriteBuffer = useNullWriteBuffer;
        }
    }
}
