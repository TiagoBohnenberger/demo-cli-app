package io.github.tiagobohnenberger.cli.util;

import java.io.IOException;
import java.text.MessageFormat;

import com.github.lalyos.jfiglet.FigletFont;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;

@UtilityClass
public class Functions {

    public static <T> void println(T obj, Object... args) {
        if (ArrayUtils.isNotEmpty(args) && obj != null) {
            System.out.println(
                    MessageFormat.format(ObjectUtils.toString(obj, () -> ""), args));
            return;
        }

        System.out.println(ObjectUtils.defaultIfNull(obj, ""));
    }

    public static void print(String msg) {
        System.out.print(ObjectUtils.defaultIfNull(msg, ""));
    }


    public static void println() {
        System.out.println();
    }

    public static void printAnsiArt(String msg) throws IOException {
        System.out.println(FigletFont.convertOneLine(msg));
    }
}