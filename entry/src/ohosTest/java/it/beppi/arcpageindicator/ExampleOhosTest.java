package it.beppi.arcpageindicator;

import it.beppi.arcpageindicator.util.AttrUtil;
import ohos.aafwk.ability.delegation.AbilityDelegatorRegistry;
import ohos.agp.components.Attr;
import ohos.agp.components.AttrSet;
import ohos.app.Context;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ExampleOhosTest {

    private ArcPageIndicator arcPageIndicator;

    @Before
    public void setUp() {
        Context context = AbilityDelegatorRegistry.getAbilityDelegator().getAppContext();
        AttrSet attrSet = new AttrSet() {
            @Override
            public Optional<String> getStyle() {
                return Optional.empty();
            }

            @Override
            public int getLength() {
                return 0;
            }

            @Override
            public Optional<Attr> getAttr(int i) {
                return Optional.empty();
            }

            @Override
            public Optional<Attr> getAttr(String s) {
                return Optional.empty();
            }
        };
        arcPageIndicator = new ArcPageIndicator(context, attrSet);
    }

    @Test
    public void testSetPageSliderId() {
        arcPageIndicator.setPageSliderId(1111);
        assertEquals(1111, arcPageIndicator.getPageSliderId());
    }

    @Test
    public void testSetSpotsColor() {
        arcPageIndicator.setSpotsColor(0x99CCCCCC);
        assertEquals(0x99CCCCCC, arcPageIndicator.getSpotsColor());
    }

    @Test
    public void testSetSelectedSpotsColor() {
        arcPageIndicator.setSelectedSpotColor(0x99CCCCCC);
        assertEquals(0x99CCCCCC, arcPageIndicator.getSelectedSpotColor());
    }

    @Test
    public void testSetSpotRadius() {
        arcPageIndicator.setSpotsRadius(9);
        assertEquals(9, arcPageIndicator.getSpotsRadius());
    }

    @Test
    public void testSetIntervalMeasureAngle() {
        arcPageIndicator.setIntervalMeasureAngle(true);
        assertTrue(arcPageIndicator.isIntervalMeasureAngle());
    }

    @Test
    public void testSetAnimationType() {
        arcPageIndicator.setAnimationType(ArcPageIndicator.AnimationType.BUMP);
        assertEquals(ArcPageIndicator.AnimationType.BUMP, arcPageIndicator.getAnimationType());
    }

    @Test
    public void testSetArcOrientation() {
        arcPageIndicator.setArcOrientation(ArcPageIndicator.ArcOrientation.TO_DOWN);
        assertEquals(ArcPageIndicator.ArcOrientation.TO_DOWN, arcPageIndicator.getArcOrientation());
    }

    @Test
    public void testSetInvertDirection() {
        arcPageIndicator.setInvertDirection(true);
        assertTrue(arcPageIndicator.isInvertDirection());
    }

    @Test
    public void testSetHandEnabled() {
        arcPageIndicator.setHandEnabled(true);
        assertTrue(arcPageIndicator.isHandEnabled());
    }

    @Test
    public void testSetHandColor() {
        arcPageIndicator.setHandColor(0x99CCCCCC);
        assertEquals(0x99CCCCCC, arcPageIndicator.getHandColor());
    }

    @Test
    public void testSetHandWidth() {
        arcPageIndicator.setHandWidth(6);
        assertEquals(6, arcPageIndicator.getHandWidth());
    }

    @Test
    public void testSetHandRelativeLength() {
        arcPageIndicator.setHandRelativeLength(0.6f);
        assertEquals(0.6f, arcPageIndicator.getHandRelativeLength(),0.0001f);
    }

    @Test
    public void testSetSpotShape() {
        arcPageIndicator.setSpotShape(ArcPageIndicator.SpotShape.SQUARE);
        assertEquals(ArcPageIndicator.SpotShape.SQUARE, arcPageIndicator.getSpotShape());
    }

    @Test
    public void testGetPageSliderId() {
        assertEquals(AttrUtil.DEFAULT_PAGE_SLIDER_ID, arcPageIndicator.getPageSliderId());
    }

    @Test
    public void testGetSpotsColor() {
        assertEquals(AttrUtil.DEFAULT_SPOTS_COLOR, arcPageIndicator.getSpotsColor());
    }

    @Test
    public void testGetSelectedSpotsColor() {
        assertEquals(AttrUtil.DEFAULT_SELECTED_SPOT_COLOR, arcPageIndicator.getSelectedSpotColor());
    }

    @Test
    public void testGetSpotRadius() {
        assertEquals(AttrUtil.DEFAULT_SPOTS_RADIUS, arcPageIndicator.getSpotsRadius());
    }

    @Test
    public void testIsIntervalMeasureAngle() {
        assertEquals(AttrUtil.DEFAULT_INTERVAL_MEASURE_ANGLE, arcPageIndicator.isIntervalMeasureAngle());
    }

    @Test
    public void testGetAnimationType() {
        assertEquals(AttrUtil.DEFAULT_ANIMATION_TYPE,arcPageIndicator.getAnimationType());
    }

    @Test
    public void testGetArcOrientation() {
        assertEquals(AttrUtil.DEFAULT_ARC_ORIENTATION,arcPageIndicator.getArcOrientation());
    }

    @Test
    public void testGetSpotShape() {
        assertEquals(AttrUtil.DEFAULT_SPOT_SHAPE,arcPageIndicator.getSpotShape());
    }

    @Test
    public void testIsInvertDirection() {
        assertEquals(AttrUtil.DEFAULT_INVERT_DIRECTION, arcPageIndicator.isInvertDirection());
    }

    @Test
    public void testIsHandEnabled() {
        assertEquals(AttrUtil.DEFAULT_HAND_ENABLED, arcPageIndicator.isHandEnabled());
    }

    @Test
    public void testGetHandColor() {
        assertEquals(AttrUtil.DEFAULT_HAND_COLOR, arcPageIndicator.getHandColor());
    }

    @Test
    public void testGetHandWidth() {
        assertEquals(AttrUtil.DEFAULT_HAND_WIDTH, arcPageIndicator.getHandWidth());
    }

    @Test
    public void testGetHandRelativeLength() {
        double delta = 0.0001f;
        assertEquals(AttrUtil.DEFAULT_HAND_RELATIVE_LENGTH, arcPageIndicator.getHandRelativeLength(),delta);
    }
}