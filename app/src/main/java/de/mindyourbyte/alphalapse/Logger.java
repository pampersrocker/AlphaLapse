package de.mindyourbyte.alphalapse;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class Logger {

    public static File getFile(){
        return new File(Environment.getExternalStorageDirectory(), "ALPHALOG.TXT");
    }

    protected static void log(String msg) {
        try {
            Log.e(null, msg);
            getFile().getParentFile().mkdirs();
            BufferedWriter writer = new BufferedWriter(new FileWriter(getFile(), true));
            writer.append(msg);
            writer.newLine();
            writer.close();
        } catch (IOException e) {}
    }
    protected static void log(String type, String msg) { log("[" + type + "] " + msg); }

    public static void info(String msg) { log("INFO", msg); }
    public static void error(String msg) { log("ERROR", msg); }
    public static void exception(Throwable e) {
        StringWriter writer = new StringWriter();
        writer.append(e.getMessage());
        writer.append("\n");
        e.printStackTrace(new PrintWriter(writer));
        log("EXC", writer.toString().trim());
    }
}
