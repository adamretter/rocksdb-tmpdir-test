package example;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.EnvironmentVariables;
import org.junit.rules.TemporaryFolder;
import org.rocksdb.RocksDBException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.Assert.*;

public class Example1Test {

    @Rule
    public TemporaryFolder dbFolder = new TemporaryFolder();

    @Rule
    public final EnvironmentVariables environmentVariables = new EnvironmentVariables();


    //@Test
    public void run() throws IOException, RocksDBException {
        final Path dataDir = dbFolder.newFolder().toPath().toAbsolutePath();

        final byte[] value = new Example1().run(dataDir);

        assertNotNull(value);
    }

    @Test
    public void runWithBadEnvVar() throws IOException, RocksDBException {
        final Path noSuchPath = Paths.get("no-such-path").toAbsolutePath();
        assertFalse(Files.exists(noSuchPath));

        // set env var to a non-existent directory
        final String prevEnvVarValue = System.getenv().get("ROCKSDB_SHAREDLIB_DIR");
        environmentVariables.set("ROCKSDB_SHAREDLIB_DIR", noSuchPath.toString());
        assertEquals(noSuchPath.toString(), System.getenv().get("ROCKSDB_SHAREDLIB_DIR"));
        try {

            // perform test
            run();

        } finally {
            // restore env var
            if (prevEnvVarValue == null) {
                environmentVariables.clear("ROCKSDB_SHAREDLIB_DIR");
            } else {
                environmentVariables.set("ROCKSDB_SHAREDLIB_DIR", prevEnvVarValue);
            }
        }
    }
}
