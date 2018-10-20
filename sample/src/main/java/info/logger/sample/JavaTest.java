package info.logger.sample;

import java.io.File;

import info.logger.fast.L;

public class JavaTest {

    public JavaTest() {
        L.d("test");
        File logFile = L.getLogFile();
    }
}
