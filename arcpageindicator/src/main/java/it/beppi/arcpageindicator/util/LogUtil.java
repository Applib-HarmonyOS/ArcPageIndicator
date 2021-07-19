package it.beppi.arcpageindicator.util;

import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

/**
 * LogUtil Class.
 */
public class LogUtil {
    private static final String TAG_LOG = "systembartint";

    private static final int DOMAIN_ID = 0xD000F00;

    private static final HiLogLabel LABEL_LOG = new HiLogLabel(3, DOMAIN_ID, LogUtil.TAG_LOG);

    private static final String LOG_FORMAT = "%{public}s: %{public}s";

    private LogUtil() {
        /* Do nothing */
    }

    public static void debug(String tag, String msg) {
        HiLog.debug(LABEL_LOG, LOG_FORMAT, tag, msg);
    }

    public static void info(String tag, String msg) {
        HiLog.info(LABEL_LOG, LOG_FORMAT, tag, msg);
    }

    public static void warn(String tag, String msg) {
        HiLog.warn(LABEL_LOG, LOG_FORMAT, tag, msg);
    }

    public static void error(String tag, String msg) {
        HiLog.error(LABEL_LOG, LOG_FORMAT, tag, msg);
    }
}