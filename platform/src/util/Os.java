package util;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by nwilson on 2/23/15.
 */
// TODO fix this up if we want any sort of portability
public abstract class Os {
    public static final int INTERRUPTED_STATUS = Integer.MIN_VALUE + 2;
    public static final int IO_FAILURE_STATUS  = Integer.MIN_VALUE + 3;

    public static Os current = GetCurrent();

    // Inspect the runtime environment and set up os interface properly
    private static Os GetCurrent() {
        return new UnixOs(); // TODO
    }

    /**
     * Returns exit statis of cmd
     * @param cmdAndArgs: The cmd and arguments as would be typed into a shell
     * @return
     */
    public abstract int Exec(String cmdAndArgs);

    private static class UnixOs extends Os
    {
        public UnixOs() { }

        @Override
        public int Exec(String cmdAndArgs) {
            try {
                ProcessBuilder pb = new ProcessBuilder(cmdAndArgs);
                pb.redirectError(ProcessBuilder.Redirect.INHERIT);
                pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
                return pb.start().waitFor();
            } catch (IOException ex) {
                return IO_FAILURE_STATUS;
            } catch (InterruptedException ex) {
                return INTERRUPTED_STATUS;
            }
        }
    }

    // TODO, etc. etc.
    private static class WindowsOS extends Os {

        @Override
        public int Exec(String cmdAndArgs) {
            throw new NotImplementedException();
        }
    }
}

