package eu.the5zig.mod.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static eu.the5zig.mod.util.ReflectionUtil.invoke;
import static eu.the5zig.mod.util.ReflectionUtil.newInstance;

public class CrashReportUtil {

	private static final Method createCrashReport;
	private static final Method createCrashCategory;
	private static final Constructor<?> reportedException;

	static {
		try {
			Class<?> crashReportClass = Class.forName("b");
			createCrashReport = crashReportClass.getMethod("a", Throwable.class, String.class);
			createCrashCategory = crashReportClass.getMethod("a", String.class);
			reportedException = Class.forName("e").getConstructor(crashReportClass);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private CrashReportUtil() {
	}

	public static void makeCrashReport(Throwable throwable, String reason) {
		Object crashReport = invoke(createCrashReport, throwable, reason);
		invoke(crashReport, createCrashCategory, "The 5zig Mod");
		throw (RuntimeException) newInstance(reportedException, crashReport);
	}

}
