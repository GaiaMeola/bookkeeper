package manualtest;

import customutils.TestBookieImpl;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.apache.bookkeeper.bookie.BookieImpl;
import org.apache.bookkeeper.bookie.LedgerDirsManager;
import org.apache.bookkeeper.bookie.storage.EntryLogger;
import org.apache.bookkeeper.bookie.storage.ldb.DbLedgerStorage;
import org.apache.bookkeeper.bookie.storage.ldb.SingleDirectoryDbLedgerStorage;
import org.apache.bookkeeper.bookie.storage.ldb.WriteCache;
import org.apache.bookkeeper.conf.ServerConfiguration;
import org.apache.bookkeeper.meta.LedgerManager;
import org.apache.bookkeeper.stats.StatsLogger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import customutils.TestBKConfiguration;

import java.io.File;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class SingleDirectoryDbLedgerStorageWriteCacheIT {

    private DbLedgerStorage storage; //istanza di DbLedgerStorage
    private File tmpDir;
    public static WriteCache currentWriteCache; //spy di Mockito

    // =========================
    // Ledger "mocked" con WriteCache spy
    // =========================
    private static class MockedDbLedgerStorage extends DbLedgerStorage {
        protected SingleDirectoryDbLedgerStorage newSingleDirectoryDbLedgerStorage(
                ServerConfiguration conf,
                LedgerManager ledgerManager,
                LedgerDirsManager ledgerDirsManager,
                LedgerDirsManager indexDirsManager,
                EntryLogger entryLogger,
                StatsLogger statsLogger,
                long writeCacheSize, long readCacheSize,
                int readAheadCacheBatchSize, long readAheadCacheBatchBytesSize)
                throws IOException {

            return new MockedSingleDirectoryDbLedgerStorage(
                    conf, ledgerManager, ledgerDirsManager, indexDirsManager,
                    entryLogger, statsLogger, allocator, writeCacheSize,
                    readCacheSize, readAheadCacheBatchSize, readAheadCacheBatchBytesSize);
        }

        private static class MockedSingleDirectoryDbLedgerStorage extends SingleDirectoryDbLedgerStorage {
            public MockedSingleDirectoryDbLedgerStorage(ServerConfiguration conf,
                                                        LedgerManager ledgerManager,
                                                        LedgerDirsManager ledgerDirsManager,
                                                        LedgerDirsManager indexDirsManager,
                                                        EntryLogger entryLogger,
                                                        StatsLogger statsLogger,
                                                        io.netty.buffer.ByteBufAllocator allocator,
                                                        long writeCacheSize, long readCacheSize,
                                                        int readAheadCacheBatchSize, long readAheadCacheBatchBytesSize)
                    throws IOException {
                super(conf, ledgerManager, ledgerDirsManager, indexDirsManager, entryLogger,
                        statsLogger, allocator, writeCacheSize, readCacheSize, readAheadCacheBatchSize,
                        readAheadCacheBatchBytesSize);

                // Spy della WriteCache per verificare le interazioni, mentre SingleDirectoryDbLedgerStorage reale
                currentWriteCache = spy(this.writeCache);
                this.writeCache = currentWriteCache;
            }
        }
    }

    private ByteBuf customByteBuf(long entryId) {
        //metodo helper, per costruire un'entry
        ByteBuf entry = Unpooled.buffer(512 + 2 * 8);
        entry.writeLong(1);
        entry.writeLong(entryId);
        entry.writeZero(512);
        return entry;
    }

    @Before
    public void setup() throws Exception {
        tmpDir = File.createTempFile("bkTest", ".dir");
        if (!tmpDir.delete()) {
            System.err.println("Warning: unable to delete temp file " + tmpDir);
        }
        if (!tmpDir.mkdir()) {
            throw new IOException("Unable to create temp directory " + tmpDir);
        } //directory temporanea per simulare il file system di un Bookie

        File curDir = BookieImpl.getCurrentDirectory(tmpDir);
        BookieImpl.checkDirectoryStructure(curDir);

        ServerConfiguration conf = TestBKConfiguration.newServerConfiguration();
        conf.setLedgerStorageClass(MockedDbLedgerStorage.class.getName());
        conf.setLedgerDirNames(new String[]{tmpDir.toString()});

        // IMPORTANT: forza dimensione cache bassa (MB) per rendere la rotazione deterministica
        conf.setProperty(DbLedgerStorage.WRITE_CACHE_MAX_SIZE_MB, 1); // 1 MB

        // Usa il nome host per costruire il BookieId (evita IPv6 link-local con %zone)
        conf.setUseHostNameAsBookieID(true);

        // Crea TestBookieImpl (usando il builder di test che inizializza le dipendenze)
        TestBookieImpl bookie = new TestBookieImpl(conf);

        storage = (DbLedgerStorage) bookie.getLedgerStorage();

        assert storage != null;
    }

    @After
    public void teardown() throws Exception {
        if (storage != null) {
            storage.shutdown();
        }

        if (!tmpDir.delete()) {
            System.err.println("Warning: unable to delete temp directory " + tmpDir);
        }
    }


    // =========================
    // Fase 1: Stub (isolamento)
    // =========================
    @Test
    public void testAddEntry_stubbed() throws Exception {
        ByteBuf entry = customByteBuf(2);

        // Stub: la cache risponde sempre true; in questo modo non eseguo il metodo reale
        doReturn(true).when(currentWriteCache).put(anyLong(), anyLong(), any());

        storage.addEntry(entry);

        // Assertion reale sul comportamento della cache
        verify(currentWriteCache, times(1)).put(1, 2, entry);
    }


    // =========================
    // Fase 2: Mock + Verification (interazione)
    // =========================
    @Test
    public void testAddEntry_mocked_interaction() throws Exception {
        ByteBuf entry = customByteBuf(3);

        // Simula fallimento della prima put e successo della seconda
        doReturn(false, true).when(currentWriteCache).put(anyLong(), anyLong(), any());

        storage.addEntry(entry);

        verify(currentWriteCache, times(2)).put(1, 3, entry);
    }

    @Test
    public void testWriteCacheRotation() throws Exception {
        // Inserimento di abbastanza entry per riempire la cache
        for (int i = 0; i < 5; i++) {
            ByteBuf entry = Unpooled.buffer(100 * 1024 + 2 * 8); // ~100 KB
            entry.writeLong(4);
            entry.writeLong(i);
            entry.writeZero(100 * 1024);
            storage.addEntry(entry);
        }

        // Inserimento successivo dovrebbe triggerare la rotazione
        ByteBuf entry = Unpooled.buffer(100 * 1024 + 2 * 8);
        entry.writeLong(4);
        entry.writeLong(22);
        entry.writeZero(100 * 1024);
        storage.addEntry(entry);

        verify(currentWriteCache, atLeast(2)).put(4, 22, entry);
    }
}