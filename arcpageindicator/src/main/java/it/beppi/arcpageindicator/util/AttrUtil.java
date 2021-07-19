package it.beppi.arcpageindicator.util;

import it.beppi.arcpageindicator.ArcPageIndicator;
import java.io.IOException;
import java.util.Optional;
import ohos.agp.components.Attr;
import ohos.agp.components.AttrSet;
import ohos.app.Context;
import ohos.global.resource.NotExistException;
import ohos.global.resource.WrongTypeException;

/**
 * class for Util functions.
 */
public final class AttrUtil {

    // Default Constants.
    public static final int DEFAULT_PAGE_SLIDER_ID = 0;
    public static final int DEFAULT_SPOTS_COLOR = 0x88CCCCCC;
    public static final int DEFAULT_SELECTED_SPOT_COLOR = 0xFFCCCCCC;
    public static final int DEFAULT_SPOTS_RADIUS = 10;
    public static final boolean DEFAULT_INTERVAL_MEASURE_ANGLE = false;
    public static final boolean DEFAULT_INVERT_DIRECTION = false;
    public static final boolean DEFAULT_HAND_ENABLED = false;
    public static final int DEFAULT_HAND_COLOR = DEFAULT_SPOTS_COLOR;
    public static final int DEFAULT_HAND_WIDTH = 8;
    public static final float DEFAULT_HAND_RELATIVE_LENGTH = 0.8f;
    public static final ArcPageIndicator.AnimationType DEFAULT_ANIMATION_TYPE = ArcPageIndicator.AnimationType.COLOR;
    public static final ArcPageIndicator.ArcOrientation DEFAULT_ARC_ORIENTATION = ArcPageIndicator.ArcOrientation.TO_UP;
    public static final ArcPageIndicator.SpotShape DEFAULT_SPOT_SHAPE = ArcPageIndicator.SpotShape.CIRCLE;

    private AttrUtil() {
    }

    public static int getColorValue(AttrSet attrSet, String key, int defValue) {
        Optional<Attr> temp = attrSet.getAttr(key);
        return temp.map(attr -> attr.getColorValue().getValue()).orElse(defValue);
    }

    public static boolean getBooleanValue(AttrSet attrSet, String key, boolean isDefValue) {
        Optional<Attr> temp = attrSet.getAttr(key);
        return temp.map(Attr::getBoolValue).orElse(isDefValue);
    }

    public static float getFloatValue(AttrSet attrSet, String key, float defValue) {
        Optional<Attr> temp = attrSet.getAttr(key);
        return temp.map(Attr::getFloatValue).orElse(defValue);
    }

    public static String getStringValue(AttrSet attrSet, String key, String defValue) {
        Optional<Attr> temp = attrSet.getAttr(key);
        return temp.map(Attr::getStringValue).orElse(defValue);
    }

    public static int getIntegerValue(AttrSet attrSet, String key, int defValue) {
        Optional<Attr> temp = attrSet.getAttr(key);
        return temp.map(Attr::getIntegerValue).orElse(defValue);
    }

    /**
     * Obtains a string value based on this Resource value.
     *
     * @param context context.
     * @param i resource value.
     * @return String value based on this Resource value.
     */
    public static String getString(Context context, int i) {
        try {
            return context.getResourceManager().getElement(i).getString();
        } catch (IOException | WrongTypeException | NotExistException e) {
            e.printStackTrace();
        }
        return null;
    }
}