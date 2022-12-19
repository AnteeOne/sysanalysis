package dz4;

public class Log {

    static void info(Object... args) {
        String res = null;
        for (Object arg : args) {
            if (res == null) {
                res = (String) arg;
            } else {
                res = res.replaceFirst("\\{}", arg.toString());
            }
        }
        System.out.print(res);
    }

    static void infoLine(Object... args) {
        String res = null;
        for (Object arg : args) {
            if (res == null) {
                res = (String) arg;
            } else {
                res = res.replaceFirst("\\{}", arg.toString());
            }
        }
        System.out.println(res);
    }
}
