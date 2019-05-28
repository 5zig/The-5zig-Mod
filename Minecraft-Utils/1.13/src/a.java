//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import com.google.common.collect.Lists;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

// TODO: remove
public enum a {
    a("BLACK", '0', 0, 0),
    b("DARK_BLUE", '1', 1, 170),
    c("DARK_GREEN", '2', 2, 43520),
    d("DARK_AQUA", '3', 3, 43690),
    e("DARK_RED", '4', 4, 11141120),
    f("DARK_PURPLE", '5', 5, 11141290),
    g("GOLD", '6', 6, 16755200),
    h("GRAY", '7', 7, 11184810),
    i("DARK_GRAY", '8', 8, 5592405),
    j("BLUE", '9', 9, 5592575),
    k("GREEN", 'a', 10, 5635925),
    l("AQUA", 'b', 11, 5636095),
    m("RED", 'c', 12, 16733525),
    n("LIGHT_PURPLE", 'd', 13, 16733695),
    o("YELLOW", 'e', 14, 16777045),
    p("WHITE", 'f', 15, 16777215),
    q("OBFUSCATED", 'k', true),
    r("BOLD", 'l', true),
    s("STRIKETHROUGH", 'm', true),
    t("UNDERLINE", 'n', true),
    u("ITALIC", 'o', true),
    v("RESET", 'r', -1, (Integer)null);

    private static final Map<String, a> w = Arrays.stream(values()).collect(Collectors.toMap((var0) -> d(var0.y), (var0) -> var0));
    private static final Pattern x = Pattern.compile("(?i)\u00a7[0-9A-FK-OR]");
    private final String y;
    private final char z;
    private final boolean A;
    private final String B;
    private final int C;
    private final Integer D;

    private static String d(String var0) {
        return var0.toLowerCase(Locale.ROOT).replaceAll("[^a-z]", "");
    }

    private a(String var3, char var4, int var5, Integer var6) {
        this(var3, var4, false, var5, var6);
    }

    private a(String var3, char var4, boolean var5) {
        this(var3, var4, var5, -1, (Integer)null);
    }

    private a(String var3, char var4, boolean var5, int var6, Integer var7) {
        this.y = var3;
        this.z = var4;
        this.A = var5;
        this.C = var6;
        this.D = var7;
        this.B = "\u00a7" + var4;
    }

    public static String a(String var0) {
        StringBuilder var1 = new StringBuilder();
        int var2 = -1;
        int var3 = var0.length();

        while((var2 = var0.indexOf(167, var2 + 1)) != -1) {
            if (var2 < var3 - 1) {
                a var4 = a(var0.charAt(var2 + 1));
                if (var4 != null) {
                    if (var4.f()) {
                        var1.setLength(0);
                    }

                    if (var4 != v) {
                        var1.append(var4);
                    }
                }
            }
        }

        return var1.toString();
    }

    public int b() {
        return this.C;
    }

    public boolean c() {
        return this.A;
    }

    public boolean d() {
        return !this.A && this != v;
    }

    public Integer e() {
        return this.D;
    }

    public boolean f() {
        return !this.A;
    }

    public String g() {
        return this.name().toLowerCase(Locale.ROOT);
    }

    public String toString() {
        return this.B;
    }

    public static String b(String var0) {
        return var0 == null ? null : x.matcher(var0).replaceAll("");
    }

    public static a c(String var0) {
        return var0 == null ? null : (a)w.get(d(var0));
    }

    public static a a(int var0) {
        if (var0 < 0) {
            return v;
        } else {
            a[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                a var4 = var1[var3];
                if (var4.b() == var0) {
                    return var4;
                }
            }

            return null;
        }
    }

    public static a a(char var0) {
        char var1 = Character.toString(var0).toLowerCase(Locale.ROOT).charAt(0);
        a[] var2 = values();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            a var5 = var2[var4];
            if (var5.z == var1) {
                return var5;
            }
        }

        return null;
    }

    public static Collection<String> a(boolean var0, boolean var1) {
        List<String> var2 = Lists.newArrayList();
        a[] var3 = values();
        int var4 = var3.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            a var6 = var3[var5];
            if ((!var6.d() || var0) && (!var6.c() || var1)) {
                var2.add(var6.g());
            }
        }

        return var2;
    }
}
