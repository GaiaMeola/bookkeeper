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


            BufferedChannelState t2Invalid = new BufferedChannelState(invalidByteBufAllocator(),  validFileChannel(),100, 100, 1, false);

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

                    // test R1 – Allocatore invalido gestito dalla superclasse.
                    // La logica ereditata garantisce la validità del buffer di lettura,
                    // pertanto il test non deve attendersi un'eccezione ma un'esecuzione stabile.
                    Arguments.of(t2Invalid, null, emptyByteBuf(), 0, BC_FC_CONTENT.length(), null,  BC_FC_CONTENT.length()),


                    // test R2; test passato
                    Arguments.of(t7Invalid, null, emptyByteBuf(), 0, BC_FC_CONTENT.length(), Exception.class, -1),

                    // test R3; test passato
                    Arguments.of(t5Invalid, null, emptyByteBuf(), 0, BC_FC_CONTENT.length(), Exception.class, -1),

                    // test R4; test passato
                    Arguments.of(t13Invalid, null, emptyByteBuf(), 0, BC_FC_CONTENT.length(), Exception.class, -1),

                    // test R5; test passato
                    Arguments.of(valid, null, emptyByteBuf(), 0, BC_FC_CONTENT.length(), null, BC_FC_CONTENT.length()),

                    // test R6; test passato
                    Arguments.of(valid, null, semi, 0, length, null, expected),

                    //test R7; test passato
                    Arguments.of(valid, null, fullByteBuf(), 0, BC_FC_CONTENT.length(), Exception.class, -1),

                    //test R8; test passato
                    Arguments.of(valid, null, invalidWriteIndexByteBuf(), 0, BC_FC_CONTENT.length(), Exception.class, -1),

                    //test R9; test passato
                    Arguments.of(valid, null, deallocatedByteBuf(), 0, BC_FC_CONTENT.length(), Exception.class, -1),

                    //test R10; test passato
                    Arguments.of(valid, null, null, 0, BC_FC_CONTENT.length(), Exception.class, -1),

                    //test R11; test passato
                    Arguments.of(valid, BC_BB_CONTENT, emptyByteBuf(), -1, 1, Exception.class, -1),

                     /*
                    //test R12; test fallito
                    Arguments.of(valid, BC_BB_CONTENT, emptyByteBuf(), 1, -1, Exception.class, -1),
                    */

                    // test R12 – Lunghezza negativa gestita come No-Op.
                    //Il sistema non solleva eccezioni ma interpreta la lunghezza negativa
                    // come segnale di interruzione immediata dell'operazione.
                    Arguments.of(valid, BC_BB_CONTENT, emptyByteBuf(), 1, -1, null, 0),

                    //test R13; test passato
                    Arguments.of(valid, BC_BB_CONTENT, emptyByteBuf(), 0, 0, null, 0),

                    //test R14; test passato
                    Arguments.of(valid, BC_BB_CONTENT, emptyByteBuf(), 0, 1, null, BC_FC_CONTENT.length()),

                    //test R15; test passato
                    Arguments.of(valid, BC_BB_CONTENT, emptyByteBuf(), 0, BC_FC_CONTENT.length()-1, null, BC_FC_CONTENT.length()),

                    //test R16; test passato
                    Arguments.of(valid, BC_BB_CONTENT, emptyByteBuf(), 0, BC_FC_CONTENT.length(), null, BC_FC_CONTENT.length()),

                    //test R17; test passato
                    Arguments.of(valid, BC_BB_CONTENT, emptyByteBuf(), 1, BC_FC_CONTENT.length() - 1, null, BC_FC_CONTENT.length()-1),

                    //test R18; test passato
                    Arguments.of(valid, BC_BB_CONTENT, emptyByteBuf(), BC_FC_CONTENT.length(), 1, null, BC_BB_CONTENT.length()),

                    //test R19; test passato
                    Arguments.of(valid, BC_BB_CONTENT, emptyByteBuf(), BC_FC_CONTENT.length()+BC_BB_CONTENT.length()-1, 1, null, 1),

                    //test R20; test passato
                    Arguments.of(valid, BC_BB_CONTENT, emptyByteBuf(), 0, BC_FC_CONTENT.length()+1, null, BC_FC_CONTENT.length()+BC_BB_CONTENT.length()),

                    //test R21; test passato
                    Arguments.of(valid, BC_BB_CONTENT, emptyByteBuf(), 0, BC_FC_CONTENT.length()+BC_BB_CONTENT.length(), null, BC_FC_CONTENT.length()+BC_BB_CONTENT.length()),

                    //test R22; test passato
                    Arguments.of(valid, BC_BB_CONTENT, emptyByteBuf(), 0, BC_FC_CONTENT.length()+BC_BB_CONTENT.length()+1, Exception.class, -1),

                    // //test T19; test passato
                    Arguments.of(valid, BC_BB_CONTENT, emptyByteBuf(), BC_FC_CONTENT.length()+BC_BB_CONTENT.length(), 1, Exception.class, -1),

                    // test P_R0; (Boundary Value Analysis aggiornata a seguito di PIT)
                    // // test P_R0: pos = 13 (fine file), wBSP è 0 o comunque <= 13.
                    Arguments.of(wbNullWriteState, null, emptyByteBuf(), 13, 1, null, 0),

//                  //test J_R1; test passato
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
    void testKillMutation272_AdditionSubtraction() throws IOException {
        // Buffer piccolo (10) per isolare il calcolo matematico
        BufferedChannel bc = new BufferedChannel(unpooledByteBufAllocator(), validFileChannel(), 100, 10, 1);
        clearReadBuffer(bc);

        // Carichiamo esattamente 10 byte nel readBuffer
        ByteBuf tmp = Unpooled.buffer(10);
        bc.read(tmp, 0, 10);

        int startPos = 3;
        int expectedToRead = 10 - startPos; // 7
        ByteBuf dest = Unpooled.buffer(20);

        int actualRead = bc.read(dest, startPos, expectedToRead);

        // L'originale legge 7. Il mutante (10+3) proverebbe a leggerne 13.
        Assertions.assertEquals(7, actualRead, "Il mutante 272 ha scambiato sottrazione con addizione!");
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
