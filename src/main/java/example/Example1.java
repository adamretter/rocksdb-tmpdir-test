package example;

import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Example1 {

    public static void main(final String args[]) throws IOException, RocksDBException {
        final Path dataDir = Files.createTempDirectory("example1").toAbsolutePath();
        new Example1().run(dataDir);
    }

    public byte[] run(final Path dataDir) throws RocksDBException {
        RocksDB.loadLibrary();

        try (final Options options = new Options().setCreateIfMissing(true);
                final RocksDB db = RocksDB.open(dataDir.toString())) {

            final byte[] key = "key1".getBytes(UTF_8);
            db.put(key, ("time: " + System.currentTimeMillis()).getBytes(UTF_8));

            return db.get(key);
        }
    }
}
