package br.com.alura.util;

import java.io.IOException;

import br.com.alura.command.CommandOption;
import com.github.lalyos.jfiglet.FigletFont;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Functions {

    public static void println(String msg) {
        System.out.println(msg);
    }

    public static void print(String msg) {
        System.out.print(msg);
    }


    public static void println() {
        System.out.println();
    }

    public static void printAnsiArt(String msg) throws IOException {
        System.out.println(FigletFont.convertOneLine(msg));
    }

    public static boolean isClassCommandOption(Object classCommand) {
        if (classCommand == null) {
            return false;
        }
        return classCommand.getClass().isAnnotationPresent(CommandOption.class);
    }

    public static void systemExit(int status) {
        println("Até mais!");
        System.exit(status);
    }
}