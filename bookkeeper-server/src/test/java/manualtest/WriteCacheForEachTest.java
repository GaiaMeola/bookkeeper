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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static customutils.Utils.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WriteCacheForEachTest {

    private enum CacheState {
        EMPTY,
        WRITTEN,
        WRITTEN_WITH_DELETED_LEDGER
    }

    private enum ConsumerType {
        VALID,
        INVALID,
        NULL
    }

    private static Stream<Arguments> data() {
        WriteCache.EntryConsumer validConsumer = (ledgerId, entryId, entry) -> {
            // consumer che fa nulla, simula il caso normale
        };

        WriteCacheState validState = new WriteCacheState(unpooledByteBufAllocator(), 512, 128);
        WriteCacheState zeroCacheSizeState = new WriteCacheState(unpooledByteBufAllocator(), 0, 1);
       /* WriteCacheState nullAllocatorState = new WriteCacheState(null, 512, 128); */
       /* WriteCacheState invalidAllocatorState = new WriteCacheState(invalidByteBufAllocator(), 512, 128);*/

        return Stream.of(

                /*test F1; test fallito*/
                //Arguments.of(invalidAllocatorState, CacheState.WRITTEN, ConsumerType.VALID, validConsumer, Exception.class),

                /*test F2; test fallito */
                //Arguments.of(nullAllocatorState, CacheState.WRITTEN, ConsumerType.VALID, validConsumer, Exception.class)

                /*test F3; test passato */
                Arguments.of(zeroCacheSizeState, CacheState.EMPTY, ConsumerType.VALID, null, null),

                /*test t1; test passato*/
                Arguments.of(validState, CacheState.WRITTEN, ConsumerType.VALID, validConsumer, null),

                /*test t2; test passato*/
                Arguments.of(validState, CacheState.WRITTEN, ConsumerType.INVALID, null, Exception.class),

                /*test t3; test passato*/
                Arguments.of(validState, CacheState.WRITTEN, ConsumerType.NULL, null, Exception.class),

                /*test t4; test passato */
                Arguments.of(validState, CacheState.EMPTY, ConsumerType.VALID, null, null),

                /*test t5 test passato*/
                Arguments.of(validState, CacheState.WRITTEN_WITH_DELETED_LEDGER, ConsumerType.VALID, validConsumer, null)
        );
    }

    @ParameterizedTest
    @MethodSource("data")
    @Timeout(5)
    void testForEachWithCacheStates(
            WriteCacheState state,
            CacheState cacheState,
            ConsumerType consumerType,
            WriteCache.EntryConsumer consumer,
            Class<? extends Exception> expectedException) throws IOException {

        WriteCache writeCache = null;
        Exception constructionException = null;

        try {
            writeCache = new WriteCache(state.allocator, state.maxCacheSize, state.maxSegmentSize);
        } catch (Exception e) {
            constructionException = e;
        }

        if (expectedException != null && constructionException != null) {
            Assertions.assertTrue(expectedException.isInstance(constructionException),
                    "Expected exception " + expectedException + " but got " + constructionException);
            return;
        } else if (constructionException != null) {
            throw new RuntimeException(constructionException);
        }

        // Mappa il tipo di consumer al vero consumer
        switch (consumerType) {
            case INVALID:
                consumer = invalidConsumer(); // da Utils
                break;
            case NULL:
                consumer = null;
                break;
            case VALID:
                if (consumer == null) {
                    consumer = (ledgerId, entryId, entryBuf) -> { /* no-op */ };
                }
                break;
        }

        switch (cacheState) {
            case WRITTEN:
                ByteBuf entry = lenFullByteBuf(16);
                writeCache.put(1L, 1L, entry);
                break;

            case WRITTEN_WITH_DELETED_LEDGER:
                long ledgerToDelete = 2L;
                ByteBuf entry2 = lenFullByteBuf(16);
                writeCache.put(ledgerToDelete, 1L, entry2);

                writeCache.deleteLedger(ledgerToDelete);

                writeCache.put(1L, 1L, lenFullByteBuf(16));
                break;

            case EMPTY:
                // Nessuna entry inserita
                break;
        }

        if (expectedException != null) {
            WriteCache finalWriteCache = writeCache;
            WriteCache.EntryConsumer finalConsumer1 = consumer;
            Exception ex = Assertions.assertThrows(expectedException, () -> finalWriteCache.forEach(finalConsumer1));
            System.out.println("Expected exception for consumer " + consumerType + " on cache state " + cacheState + ": " + ex);
            return;
        }

        if (consumerType == ConsumerType.VALID) {
            List<String> consumedEntries = new ArrayList<>();

            WriteCache.EntryConsumer trackingConsumer = (ledgerId, entryId, entryBuf) -> consumedEntries.add(ledgerId + ":" + entryId);

            writeCache.forEach(trackingConsumer);

            switch (cacheState) {
                case WRITTEN:
                    Assertions.assertEquals(1, consumedEntries.size(), "Expected exactly one entry consumed");
                    Assertions.assertTrue(consumedEntries.contains("1:1"), "Expected entry 1:1");
                    break;

                case WRITTEN_WITH_DELETED_LEDGER:
                    Assertions.assertEquals(1, consumedEntries.size(), "Expected exactly one entry consumed after deleting ledger");
                    Assertions.assertTrue(consumedEntries.contains("1:1"), "Expected entry 1:1 only");
                    Assertions.assertFalse(consumedEntries.stream().anyMatch(s -> s.startsWith("2:")),
                            "Deleted ledger's entries should not be iterated");
                    break;

                case EMPTY:
                    Assertions.assertTrue(consumedEntries.isEmpty(), "Expected no entries consumed on empty cache");
                    break;
            }

        } else {
            WriteCache finalWriteCache1 = writeCache;
            WriteCache.EntryConsumer finalConsumer = consumer;
            Assertions.assertDoesNotThrow(() -> finalWriteCache1.forEach(finalConsumer));
        }
    }

//    //introdotti a seguito dei miglioramenti per Jacoco
//    @Test
//    void testForEach_F2_SortedEntriesSufficientSize() throws Exception {
//        // Crea WriteCache con dimensioni sufficienti
//        ByteBufAllocator allocator = Unpooled.buffer().alloc();
//        WriteCache writeCache = new WriteCache(allocator, 512, 128);
//
//        // Inserisci più entry di quelle allocate nella prima chiamata
//        for (long i = 1; i <= 10; i++) {
//            ByteBuf entry = Unpooled.wrappedBuffer(("entry-" + i).getBytes());
//            writeCache.put(1L, i, entry);
//        }
//
//        // Prepariamo un consumer no-op
//        WriteCache.EntryConsumer consumer = (ledgerId, entryId, buf) -> { /* no-op */ };
//
//        // Prima chiamata: allocazione iniziale sortedEntries
//        writeCache.forEach(consumer);
//
//        // Seconda chiamata: sortedEntries già allocato e dimensione >= arrayLen
//        Assertions.assertDoesNotThrow(() -> writeCache.forEach(consumer),
//                "Il metodo forEach dovrebbe usare l'array sortedEntries esistente senza riallocarlo");
//    }
//
//    //introdotti a seguito dei miglioramenti per Jacoco
//    @Test
//    void testForEach_FT_SortedEntriesTooSmall() throws Exception {
//        ByteBufAllocator allocator = Unpooled.buffer().alloc();
//        WriteCache writeCache = new WriteCache(allocator, 512, 128);
//
//        WriteCache.EntryConsumer consumer = (ledgerId, entryId, buf) -> { /* no-op */ };
//
//        // Prima chiamata con 2 entry → alloca un array piccolo
//        for (long i = 1; i <= 2; i++) {
//            ByteBuf entry = Unpooled.wrappedBuffer(("entry-" + i).getBytes());
//            writeCache.put(1L, i, entry);
//        }
//        writeCache.forEach(consumer);
//
//        // Poi aggiungiamo molte più entry → arrayLen > sortedEntries.length
//        for (long i = 3; i <= 20; i++) {
//            ByteBuf entry = Unpooled.wrappedBuffer(("entry-" + i).getBytes());
//            writeCache.put(1L, i, entry);
//        }
//
//        // Seconda chiamata → deve riallocare sortedEntries
//        Assertions.assertDoesNotThrow(() -> writeCache.forEach(consumer),
//                "Il metodo forEach dovrebbe riallocare sortedEntries se troppo piccolo");
//    }

    private static class WriteCacheState {
        final ByteBufAllocator allocator;
        final long maxCacheSize;
        final int maxSegmentSize;

        WriteCacheState(ByteBufAllocator allocator, long maxCacheSize, int maxSegmentSize) {
            this.allocator = allocator;
            this.maxCacheSize = maxCacheSize;
            this.maxSegmentSize = maxSegmentSize;
        }
    }
}
