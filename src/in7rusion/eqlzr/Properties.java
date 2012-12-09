package in7rusion.eqlzr;

import android.media.audiofx.Visualizer;

public class Properties {

    public static final String defaultUrl = "http://192.168.1.66:8080/";
    public static final int initialBars = 8;
    public static final int maxBars = 32;
    public static final int initialSensitivity = 20;
    public static final int maxSensitivity = 300;
    public static final int initialCaptureRate = 11000;
    public static final int maxCaptureRate = Visualizer.getMaxCaptureRate();

}
