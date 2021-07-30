package it.beppi.arcpageindicator;

import it.beppi.arcpageindicator.util.AttrUtil;
import ohos.aafwk.ability.Ability;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.PageSlider;
import ohos.agp.components.PageSliderProvider;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.utils.Color;
import ohos.app.Context;

//Created by Beppi on 30/12/2016.

/**
 * ArcPageIndicator is a custom page indicator with stunning animations. Needs a very small screen, prefect when
 * many pages need to be shown and reached in a small time.
 */
public class ArcPageIndicator extends Component implements PageSlider.PageChangedListener,
        Component.DrawTask, Component.EstimateSizeListener, Component.BindStateChangedListener {

    /**
     * Possible Animations.
     */
    public enum AnimationType {
        NONE,
        COLOR,
        SLIDE,
        PINCH,
        BUMP,
        ROTATE,
        ROTATE_PINCH,
        NECKLACE,
        NECKLACE2,
        COVER,
        FILL,
        SURROUND
    }

    /**
     * Possible Arc Orientations.
     */
    public enum ArcOrientation {
        TO_UP,
        TO_DOWN,
        TO_RIGHT,
        TO_LEFT,
        TO_UP_RIGHT,
        TO_UP_LEFT,
        TO_DOWN_RIGHT,
        TO_DOWN_LEFT
    }

    /**
     * Possible Spot Shapes.
     */
    public enum SpotShape {
        CIRCLE,
        ROUNDED_SQUARE,
        SQUARE
    }

    /**
     * The PageSlider associated to the indicator.
     */
    private int pageSliderId;

    /**
     * Color of the displayed Spots.
     */
    private int spotsColor;

    /**
     * Color of the selected Spot referring to current page.
     */
    private int selectedSpotColor;

    /**
     * Radius of the spots.
     */
    private int spotsRadius;

    /**
     * If true, inverts the direction of selected spot movement.
     */
    private boolean invertDirection;

    /**
     * Displays the hand to the indicator.
     */
    private boolean handEnabled;

    /**
     * Color of the hand.
     */
    private int handColor;

    /**
     * Width of the hand.
     */
    private int handWidth;

    /**
     * Hand's relative length starting from center to edges.
     */
    private float handRelativeLength;

    /**
     * How spots are distributed on the circumference: constant angle or constant arc length. With
     * constant angle, the spots will not be distributed evenly, because of ellipse's eccentricity.
     * Normally constant arc length is used.
     * If true, constant angle is used.
     */
    private boolean intervalMeasureAngle;

    /**
     * Type of Animation as per ArcPageIndicator.AnimationType.
     */
    private AnimationType animationType;

    /**
     * Arc orientation as per ArcPageIndicator.ArcOrientation.
     */
    private ArcOrientation arcOrientation;

    /**
     * Spot shape as per ArcPageIndicator.SpotShape.
     */
    private SpotShape spotShape;

    // PageSlider in entry module
    PageSlider pageSlider = null;
    // pageProvider of above Page Slider
    PageSliderProvider pagerProvider = null;
    float verticalRadius;
    float horizontalRadius;
    float centerX;
    float centerY;
    private Paint paint;
    private Context context;

    /**
     * Constructor.
     *
     * @param context Context.
     * @param attrSet AttributeSet.
     */
    public ArcPageIndicator(Context context, AttrSet attrSet) {
        super(context, attrSet);
        init(context, attrSet);
    }

    /**
     * Adds all the listeners, DrawTask and Loads the Attributes.
     *
     * @param context Context.
     * @param attrSet AttributeSet.
     */
    private void init(Context context, AttrSet attrSet) {
        this.context = context;
        loadAttributes(attrSet);
        initTools();
        setBindStateChangedListener(this);
        setEstimateSizeListener(this);
        addDrawTask(this);
    }

    /**
     * Loads all the Attributes.
     *
     * @param attrSet AttributeSet.
     */
    private void loadAttributes(AttrSet attrSet) {
        if (attrSet == null) {
            return;
        }

        pageSliderId = AttrUtil.getIntegerValue(attrSet, AttrUtil
                .getString(context, ResourceTable.String_apiPageSliderId), AttrUtil.DEFAULT_PAGE_SLIDER_ID);

        spotsColor = AttrUtil.getColorValue(attrSet, AttrUtil
                .getString(context, ResourceTable.String_apiSpotsColor), AttrUtil.DEFAULT_SPOTS_COLOR);

        selectedSpotColor = AttrUtil.getColorValue(attrSet, AttrUtil
                .getString(context, ResourceTable.String_apiSelectedSpotColor), AttrUtil.DEFAULT_SELECTED_SPOT_COLOR);

        spotsRadius = AttrUtil.getIntegerValue(attrSet, AttrUtil
                .getString(context, ResourceTable.String_apiSpotsRadius), AttrUtil.DEFAULT_SPOTS_RADIUS);

        invertDirection = AttrUtil.getBooleanValue(attrSet, AttrUtil
                .getString(context, ResourceTable.String_apiInvertDirection), AttrUtil.DEFAULT_INVERT_DIRECTION);

        handEnabled = AttrUtil.getBooleanValue(attrSet, AttrUtil
                .getString(context, ResourceTable.String_apiHandEnabled), AttrUtil.DEFAULT_HAND_ENABLED);

        handColor = AttrUtil.getColorValue(attrSet, AttrUtil
                .getString(context, ResourceTable.String_apiHandColor), AttrUtil.DEFAULT_HAND_COLOR);

        handWidth = AttrUtil.getIntegerValue(attrSet, AttrUtil
                .getString(context, ResourceTable.String_apiHandWidth), AttrUtil.DEFAULT_HAND_WIDTH);

        handRelativeLength = AttrUtil.getFloatValue(attrSet, AttrUtil
                .getString(context, ResourceTable.String_apiHandRelativeLength), AttrUtil.DEFAULT_HAND_RELATIVE_LENGTH);

        intervalMeasureAngle = AttrUtil.getBooleanValue(attrSet, AttrUtil.getString(context,
                ResourceTable.String_apiIntervalMeasureAngle), AttrUtil.DEFAULT_INTERVAL_MEASURE_ANGLE);

        String animationTypeString = AttrUtil.getStringValue(attrSet, AttrUtil
                .getString(context, ResourceTable.String_apiAnimationType), "null");

        switch (animationTypeString) {
            case "none":
                animationType = AnimationType.NONE;
                break;
            case "color":
                animationType = AnimationType.COLOR;
                break;
            case "slide":
                animationType = AnimationType.SLIDE;
                break;
            case "pinch":
                animationType = AnimationType.PINCH;
                break;
            case "bump":
                animationType = AnimationType.BUMP;
                break;
            case "rotate":
                animationType = AnimationType.ROTATE;
                break;
            case "rotate_pinch":
                animationType = AnimationType.ROTATE_PINCH;
                break;
            case "necklace":
                animationType = AnimationType.NECKLACE;
                break;
            case "necklace2":
                animationType = AnimationType.NECKLACE2;
                break;
            case "cover":
                animationType = AnimationType.COVER;
                break;
            case "fill":
                animationType = AnimationType.FILL;
                break;
            case "surround":
                animationType = AnimationType.SURROUND;
                break;
            case "null":
                animationType = AttrUtil.DEFAULT_ANIMATION_TYPE;
                break;
            default:
                break;
        }

        String orientation = AttrUtil.getStringValue(attrSet, AttrUtil
                .getString(context, ResourceTable.String_apiArcOrientation), "null");

        switch (orientation) {
            case "toUp":
                arcOrientation = ArcOrientation.TO_UP;
                break;
            case "toDown":
                arcOrientation = ArcOrientation.TO_DOWN;
                break;
            case "toRight":
                arcOrientation = ArcOrientation.TO_RIGHT;
                break;
            case "toLeft":
                arcOrientation = ArcOrientation.TO_LEFT;
                break;
            case "toUpRight":
                arcOrientation = ArcOrientation.TO_UP_RIGHT;
                break;
            case "toUpLeft":
                arcOrientation = ArcOrientation.TO_UP_LEFT;
                break;
            case "toDownRight":
                arcOrientation = ArcOrientation.TO_DOWN_RIGHT;
                break;
            case "toDownLeft":
                arcOrientation = ArcOrientation.TO_DOWN_LEFT;
                break;
            case "null":
                arcOrientation = AttrUtil.DEFAULT_ARC_ORIENTATION;
                break;
            default:
                break;
        }

        String spotShapeString = AttrUtil.getStringValue(attrSet, AttrUtil
                .getString(context, ResourceTable.String_apiSpotShape), "null");

        switch (spotShapeString) {
            case "circle":
                spotShape = SpotShape.CIRCLE;
                break;
            case "roundedSquare":
                spotShape = SpotShape.ROUNDED_SQUARE;
                break;
            case "square":
                spotShape = SpotShape.SQUARE;
                break;
            case "null":
                spotShape = AttrUtil.DEFAULT_SPOT_SHAPE;
                break;
            default:
                break;
        }
    }

    /**
     * Initialise Paint.
     */
    private void initTools() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.StrokeCap.ROUND_CAP);
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.FILL_STYLE);
    }

    // *********************************** Overridden functions *************************************************

    @Override
    public boolean onEstimateSize(int widthEstimateConfig, int heightEstimateConfig) {
        int width = Component.EstimateSpec.getSize(widthEstimateConfig);
        int height = Component.EstimateSpec.getSize(heightEstimateConfig);
        setEstimatedSize(
                Component.EstimateSpec.getChildSizeWithMode(width, width, Component.EstimateSpec.NOT_EXCEED),
                Component.EstimateSpec.getChildSizeWithMode(height, height, Component.EstimateSpec.NOT_EXCEED));
        return true;
    }

    @Override
    public void onDraw(Component component, Canvas canvas) {
        calcValues();
        paintSpotsAndHand(canvas);
    }

    @Override
    public void onPageSliding(int position, float positionOffset, int positionOffsetPixels) {
        // positionOffsetPixels is negative if sliding if from right to left
        currentPosition = positionOffsetPixels >= 0 ? position : position - 1;
        currentPositionOffset = positionOffsetPixels >= 0 ? positionOffset : 1 - positionOffset;
        correctedCurrentPositionOffset = currentDirection ? currentPositionOffset : -currentPositionOffset;
        invalidate();
    }

    @Override
    public void onPageChosen(int i) {
        invalidate();
    }

    @Override
    public void onPageSlideStateChanged(int i) {
        // Do nothing
    }

    @Override
    public void onComponentBoundToWindow(Component component) {
        findPageSlider();
        calcDirection();
        calcRadiusAndCenter();
        prepareFormulaForConstantArcData();
    }

    @Override
    public void onComponentUnboundFromWindow(Component component) {
        // Do nothing
    }

    /**
     * Finds the PageSlider.
     */
    private void findPageSlider() {
        if (pageSlider != null) {
            return;
        }
        if (pageSliderId == 0) {
            return;
        }
        Context ctx = getContext();
        if (ctx instanceof Ability) {
            Ability ability = (Ability) getContext();
            Component component = ability.findComponentById(pageSliderId);
            if (component instanceof PageSlider) {
                configurePageSlider((PageSlider) component);
            }
        }
    }

    /**
     * Finds PageProvider, adds PageChangedListener to the found pageSlider.
     *
     * @param pageSlider1 PageSlider Component found in the Layout.
     */
    private void configurePageSlider(PageSlider pageSlider1) {
        releasePageSlider();
        this.pageSlider = pageSlider1;
        pageSlider.addPageChangedListener(this);
        pagerProvider = pageSlider.getProvider();
        currentPosition = pageSlider.getCurrentPage();
    }

    /**
     * Releases the Page Slider.
     */
    public void releasePageSlider() {
        if (pageSlider != null) {
            pageSlider.removePageChangedListener(this);
            pageSlider = null;
        }
    }

    // *************************** Colors, shapes and animations *******************************

    boolean currentDirection = true;

    /**
     * Decides the direction of spot movement when pages are sliding.
     */
    private void calcDirection() {
        currentDirection = arcOrientation != ArcOrientation.TO_LEFT && arcOrientation != ArcOrientation.TO_DOWN
                && arcOrientation != ArcOrientation.TO_DOWN_LEFT;
        if (animationType == AnimationType.ROTATE || animationType == AnimationType.ROTATE_PINCH
                || animationType == AnimationType.NECKLACE || animationType == AnimationType.NECKLACE2) {
            currentDirection = !currentDirection;
        }
        if (invertDirection) {
            currentDirection = !currentDirection;
        }
    }

    /**
     * Calculate radius and center of the portion of ellipse to be drawn, based on orientation and size of the window.
     */
    void calcRadiusAndCenter() {
        final int width = getWidth();
        final int height = getHeight();

        // horizontal
        if (arcOrientation == ArcOrientation.TO_UP) {
            verticalRadius = (float) (height - spotsRadius * 2.0);
            horizontalRadius = (float) (width / 2.0 - spotsRadius);
            centerX = (float) (width / 2.0);
            centerY = height - (float) spotsRadius;
        } else if (arcOrientation == ArcOrientation.TO_DOWN) {
            verticalRadius = (float) (height - spotsRadius * 2.0);
            horizontalRadius = (float) (width / 2.0 - spotsRadius);
            centerX = (float) (width / 2.0);
            centerY = spotsRadius;
        } else if (arcOrientation == ArcOrientation.TO_RIGHT) { // vertical
            verticalRadius = (float) (height / 2.0 - spotsRadius);
            horizontalRadius = width - (float) spotsRadius * 2;
            centerX = spotsRadius;
            centerY = (float) (height / 2.0);
        } else if (arcOrientation == ArcOrientation.TO_LEFT)  {
            verticalRadius = (float) (height / 2.0 - spotsRadius);
            horizontalRadius = width - (float) spotsRadius * 2;
            centerX = width - (float) spotsRadius;
            centerY = (float) (height / 2.0);
        } else if (arcOrientation == ArcOrientation.TO_UP_RIGHT) { // corners
            verticalRadius = height - 2 * (float) spotsRadius;
            horizontalRadius = width - 2 * (float) spotsRadius;
            centerX = spotsRadius;
            centerY = height - (float) spotsRadius;
        } else if (arcOrientation == ArcOrientation.TO_UP_LEFT) {
            verticalRadius = height - 2 * (float) spotsRadius;
            horizontalRadius = width - 2 * (float) spotsRadius;
            centerX = width - (float) spotsRadius;
            centerY = height - (float) spotsRadius;
        } else if (arcOrientation == ArcOrientation.TO_DOWN_RIGHT) {
            verticalRadius = height - 2 * (float) spotsRadius;
            horizontalRadius = width - 2 * (float) spotsRadius;
            centerX = spotsRadius;
            centerY = spotsRadius;
        } else if (arcOrientation == ArcOrientation.TO_DOWN_LEFT) {
            verticalRadius = height - 2 * (float) spotsRadius;
            horizontalRadius = width - 2 * (float) spotsRadius;
            centerX = width - (float) spotsRadius;
            centerY = spotsRadius;
        }
    }

    /**
     * Depending upon animation Type calculates Selected spot color.
     *
     * @return Selected spot color.
     */
    private int calcSelectedSpotColor() {
        if (animationType == AnimationType.COLOR) {
            return proportionalScaleColor(selectedSpotColor, spotsColor, currentPositionOffset);
        }
        if (animationType == AnimationType.SLIDE || animationType == AnimationType.PINCH
                || animationType == AnimationType.BUMP) {
            return (currentPositionOffset == 0 ? 0 : spotsColor);
        }
        if (animationType == AnimationType.SURROUND) {
            return spotsColor;
        }
        return selectedSpotColor;
    }

    /**
     * Depending upon animationType calculates spotsColor.
     *
     * @return spots color.
     */
    private int calcCurrentSpotsColor() {
        if (animationType == AnimationType.COLOR) {
            return proportionalScaleColor(spotsColor, selectedSpotColor, currentPositionOffset);
        }
        return spotsColor;
    }

    /**
     * Paint the spot number n with that color!.
     *
     * @param canvas Current canvas object to draw 2D graphics.
     * @param n Current page number or spot number.
     * @param color color of the shape.
     */
    void paintSpot(Canvas canvas, int n, float positionOffset, int color, float radius, boolean stroke) {
        paint.setStyle(stroke ? Paint.Style.STROKE_STYLE : Paint.Style.FILL_STYLE);
        paint.setColor(new Color(color));
        double angle = calcAngle(n, positionOffset);
        double x = centerX + horizontalRadius * Math.sin(angle);
        double y = centerY - verticalRadius * Math.cos(angle);
        if (spotShape == SpotShape.CIRCLE) {
            canvas.drawCircle((float) x, (float) y, radius, paint);
        } else if (spotShape == SpotShape.ROUNDED_SQUARE) {
            float roundRadius = 2f * radius / 3f;
            canvas.drawRoundRect(new RectF((float) x - radius, (float) y - radius,
                    (float) x + radius, (float) y + radius), roundRadius, roundRadius, paint);
        } else if (spotShape == SpotShape.SQUARE) {
            canvas.drawRect(new RectF((float) x - radius, (float) y - radius,
                    (float) x + radius, (float) y + radius), paint);
        }
    }

    /**
     * Paints the handle.
     *
     * @param canvas Current canvas object to draw 2D graphics
     * @param n spot number or page number
     * @param positionOffset fraction of page slided
     */
    void paintHand(Canvas canvas, int n, float positionOffset) {
        paint.setColor(new Color(handColor));
        paint.setStrokeWidth(handWidth);
        double angle = calcAngle(n, positionOffset);
        double x = centerX + horizontalRadius * Math.sin(angle) * handRelativeLength;
        double y = centerY - verticalRadius * Math.cos(angle) * handRelativeLength;
        canvas.drawLine(centerX, centerY, (float) x, (float) y, paint);
    }

    /**
     * When rotating one extra spot must be added.
     *
     * @param canvas Current canvas object to draw 2D graphics.
     */
    void paintLeftMostSpot(Canvas canvas) {
        if (animationType != AnimationType.ROTATE && animationType != AnimationType.ROTATE_PINCH
                && animationType != AnimationType.NECKLACE && animationType != AnimationType.NECKLACE2) {
            return;
        }
        if (currentPositionOffset == 0) {
            return;
        }
        paintSpot(canvas, -1, correctedCurrentPositionOffset, spotsColor, (float) calcSpotsRadius(0), false);
    }

    /**
     * Calculates the spot radius depending upon type of animation.
     *
     * @param n Current page number or Spot number.
     * @return spot radius.
     */
    private double calcSpotsRadius(int n) {
        if (animationType == AnimationType.ROTATE_PINCH) {
            return calcPinchRadius();
        } else if (animationType == AnimationType.SURROUND) {
            return calcSurroundRadius();
        } else if (animationType == AnimationType.NECKLACE) {
            return calcNecklaceRadius(n);
        } else if (animationType == AnimationType.NECKLACE2) {
            return calcNecklace2Radius(n);
        } else {
            return spotsRadius;
        }
    }

    /**
     * Calculates the selected spot radius depending upon type of animation.
     *
     * @return spot radius
     */
    private double calcSelectedSpotRadius() {
        if (animationType == AnimationType.SURROUND) {
            return calcSurroundRadius();
        } else if (animationType == AnimationType.NECKLACE) {
            return calcNecklaceRadius(currentPosition);
        } else {
            return spotsRadius;
        }
    }

    /**
     * certain animations need an additional selected spot to be moving over the normal spots.
     *
     * @param canvas Current canvas object to draw 2D graphics.
     */
    void paintMovingSpot(Canvas canvas) {
        if (animationType == AnimationType.PINCH) {
            paintSpot(canvas, currentPosition, correctedCurrentPositionOffset, selectedSpotColor,
                    (float) calcPinchRadius(), false);
        } else if (animationType == AnimationType.BUMP) {
            paintSpot(canvas, currentPosition, correctedCurrentPositionOffset, selectedSpotColor,
                    (float) calcBumpRadius(), false);
        } else if (animationType == AnimationType.SLIDE) {
            paintSpot(canvas, currentPosition, correctedCurrentPositionOffset, selectedSpotColor,
                    spotsRadius, false);
        } else if (animationType == AnimationType.SURROUND) {
            paintSpot(canvas, currentPosition, correctedCurrentPositionOffset, selectedSpotColor,
                    spotsRadius, true);
        }
    }

    /**
     * Specific for animations Cover && Fill, the selected spot reduces its radius and increases back.
     *
     * @param canvas Current canvas object to draw 2D graphics.
     */
    void paintFillSpot(Canvas canvas) {
        if (animationType != AnimationType.COVER && animationType != AnimationType.FILL) {
            return;
        }
        paint.setStyle(Paint.Style.FILL_STYLE);
        if (currentPositionOffset < 0.5) {
            paintSpot(canvas, currentPosition, 0, selectedSpotColor, (float) calcFillRadius(), false);
        } else {
            paintSpot(canvas, currentPosition + 1, 0, selectedSpotColor, (float) calcFillRadius(), false);
        }
    }

    /**
     * used for Pinch animation.
     *
     * @return spot radius.
     */
    double calcPinchRadius() {
        double radius;
        if (currentPositionOffset == 0) {
            radius = spotsRadius;
        } else if (currentPositionOffset < 0.5) {
            radius = mapValueFromRangeToRange(currentPositionOffset, 0, 0.5, spotsRadius, 2.0 * spotsRadius / 3.0);
        } else {
            radius = mapValueFromRangeToRange(currentPositionOffset, 0.5, 1, 2.0 * spotsRadius / 3.0, spotsRadius);
        }
        return radius;
    }

    /**
     * used for Bump animation.
     *
     * @return spot radius.
     */
    double calcBumpRadius() {
        double radius;
        if (currentPositionOffset == 0) {
            radius = spotsRadius;
        } else if (currentPositionOffset < 0.5) {
            radius = mapValueFromRangeToRange(currentPositionOffset, 0, 0.5, spotsRadius, 3.0 * spotsRadius / 2.0);
        } else {
            radius = mapValueFromRangeToRange(currentPositionOffset, 0.5, 1, 3.0 * spotsRadius / 2.0, spotsRadius);
        }
        return radius;
    }

    /**
     * Radius of the smaller spots for Surround animation.
     *
     * @return spot radius.
     */
    double calcSurroundRadius() {
        return 0.75d * spotsRadius;
    }

    /**
     * used to change radius of the spots with Necklace animation.
     *
     * @param n current page number or spot number.
     * @return current spot radius.
     */
    double calcNecklaceRadius(int n) {
        double pos = n + currentPositionOffset;
        double p = (pages - 1d) / 2;
        if (pos < p) {
            return mapValueFromRangeToRange(pos, 0, p, spotsRadius / 3.0, spotsRadius);
        } else {
            return mapValueFromRangeToRange(pos, p, pages - 1.0, spotsRadius, spotsRadius / 3.0);
        }
    }

    /**
     * used to change radius of the spots with Necklace2 animation.
     *
     * @param n current spot number or page number.
     * @return spot radius.
     */
    double calcNecklace2Radius(int n) {
        double p = Math.abs(n - currentPosition);
        return mapValueFromRangeToRange(p, pages - 1.0, 0, spotsRadius / 4.0, spotsRadius);
    }

    /**
     * used for Cover && Fill animations.
     */
    double calcFillRadius() {
        double radius;
        if (currentPositionOffset == 0) {
            radius = spotsRadius;
        } else if (currentPositionOffset < 0.5) {
            radius = mapValueFromRangeToRange(currentPositionOffset, 0, 0.5, spotsRadius, 0);
        } else {
            radius = mapValueFromRangeToRange(currentPositionOffset, 0.5, 1, 0, spotsRadius);
        }
        return radius;
    }

    /**
     * calc offset when all spots must be rotated for the animation.
     *
     * @return amount of rotation.
     */
    float calcAllSpotsOffset() {
        if (animationType != AnimationType.ROTATE && animationType != AnimationType.ROTATE_PINCH
                && animationType != AnimationType.NECKLACE && animationType != AnimationType.NECKLACE2) {
            return 0;
        }
        return correctedCurrentPositionOffset;
    }

    /**
     * Paints all the spots, considering animations and the rest.
     *
     * @param canvas Current canvas object to draw 2D graphics.
     */
    private void paintSpotsAndHand(Canvas canvas) {
        if (animationType == AnimationType.FILL) {
            paint.setStyle(Paint.Style.STROKE_STYLE);
        } else {
            paint.setStyle(Paint.Style.FILL_STYLE);
        }
        for (int w = 0; w < pages; w++) {
            if (w != currentPosition && w != currentPosition + 1) {
                paintSpot(canvas, w, calcAllSpotsOffset(), spotsColor, (float) calcSpotsRadius(w),
                        animationType == AnimationType.FILL);
            }
        }
        paintLeftMostSpot(canvas);
        paintTheHand(canvas);
        if (animationType == AnimationType.COLOR) {
            if (currentPositionOffset < 0.5) {
                paintSpot(canvas, currentPosition + 1, calcAllSpotsOffset(), calcCurrentSpotsColor(),
                        (float) calcSpotsRadius(currentPosition + 1), false);
                // the selected spot is painted as last one to be over the others
                paintSpot(canvas, currentPosition, calcAllSpotsOffset(), calcSelectedSpotColor(),
                        spotsRadius, false);
            } else {
                // the selected spot is painted as last one to be over the others
                paintSpot(canvas, currentPosition, calcAllSpotsOffset(), calcSelectedSpotColor(),
                        spotsRadius, false);
                paintSpot(canvas, currentPosition + 1, calcAllSpotsOffset(), calcCurrentSpotsColor(),
                        (float) calcSpotsRadius(currentPosition), false);
            }
        } else if (animationType == AnimationType.COVER) {
            paintSpot(canvas, currentPosition + 1, calcAllSpotsOffset(), calcCurrentSpotsColor(),
                    spotsRadius, false);
            // the selected spot is painted as last one to be over the others
            paintSpot(canvas, currentPosition, calcAllSpotsOffset(), calcCurrentSpotsColor(), spotsRadius, false);
        } else if (animationType == AnimationType.FILL) {
            paintSpot(canvas, currentPosition + 1, calcAllSpotsOffset(), calcCurrentSpotsColor(), spotsRadius, true);
            // the selected spot is painted as last one to be over the others
            paintSpot(canvas, currentPosition, calcAllSpotsOffset(), calcCurrentSpotsColor(), spotsRadius, true);
        } else {
            paintSpot(canvas, currentPosition + 1, calcAllSpotsOffset(), calcCurrentSpotsColor(),
                    (float) calcSpotsRadius(currentPosition + 1), false);
            // the selected spot is painted as last one to be over the others
            paintSpot(canvas, currentPosition, calcAllSpotsOffset(), calcSelectedSpotColor(),
                    (float) calcSelectedSpotRadius(), false);
        }
        paintMovingSpot(canvas);
        paintFillSpot(canvas);
    }

    /**
     * Calculate the value that stands linearly between From and To in a proportion between 0 (From) and 1 (To).
     *
     * @param valueFrom low end value
     * @param valueTo high end value
     * @param proportion01 proportion
     * @return proportional value
     */
    int proportionalScaleValue(int valueFrom, int valueTo, float proportion01) {
        return ((int) (valueFrom * (1 - proportion01) + valueTo * proportion01));
    }

    /**
     * Map a value within a given range to another range.
     *
     * @param value the value to map.
     * @param fromLow the low end of the range the value is within
     * @param fromHigh the high end of the range the value is within
     * @param toLow the low end of the range to map to
     * @param toHigh the high end of the range to map to
     * @return the mapped value
     */
    public static double mapValueFromRangeToRange(
            double value,
            double fromLow,
            double fromHigh,
            double toLow,
            double toHigh) {
        double fromRangeSize = fromHigh - fromLow;
        double toRangeSize = toHigh - toLow;
        double valueScale = (value - fromLow) / fromRangeSize;
        return toLow + (valueScale * toRangeSize);
    }

    /**
     * Calculate the color that stands linearly between From and To in a proportion between 0 (From) and 1 (To).
     *
     * @param colorFrom low end color.
     * @param colorTo high end color.
     * @param proportion01 proportion.
     * @return proportional color.
     */
    private int proportionalScaleColor(int colorFrom, int colorTo, float proportion01) {
        return Color.argb(
                proportionalScaleValue(Color.alpha(colorFrom), Color.alpha(colorTo), proportion01),
                proportionalScaleValue((colorFrom >> 16) & 0xFF, (colorTo >> 16) & 0xFF, proportion01),
                proportionalScaleValue((colorFrom >> 8) & 0xFF, (colorTo >> 8) & 0xFF, proportion01),
                proportionalScaleValue(colorFrom & 0xFF, colorTo & 0xFF, proportion01)
        );
    }

    /**
     * Paints the handle.
     *
     * @param canvas Current canvas object to draw 2D graphics.
     */
    private void paintTheHand(Canvas canvas) {
        if (!handEnabled) {
            return;
        }
        paintHand(canvas, currentPosition, correctedCurrentPositionOffset);
    }

    // ***************************** Ellipses (and every other maths) Management ********************************

    int pages;

    int currentPosition;

    // value between 0 to 1 tells what fraction of page slided.
    float currentPositionOffset = 0;

    // sign change to the currentPositionOffset depending upon direction of sliding.
    float correctedCurrentPositionOffset = 0;

    double itemAngle;

    double rotationConstant = -Math.PI / 2;

    // 2 for half ellipse, 4 for a quarter of ellipse and so on
    double arcRate;

    // used to rotate an ellipse
    double rotationConstant2;

    // used in the calculations for the constant arc distribution
    double arcFormulaE2;
    double arcFormulaFactor1;
    double arcFormulaFactor2;
    double arcFormulaFactor3;

    /**
     * Calc all values needed to draw, runtime.
     */
    void calcValues() {
        if (arcOrientation == ArcOrientation.TO_UP) {
            rotationConstant2 = 0;
            arcRate = 2;
        } else if (arcOrientation == ArcOrientation.TO_DOWN) {
            rotationConstant2 = Math.PI;
            arcRate = 2;
        } else if (arcOrientation == ArcOrientation.TO_RIGHT) {
            rotationConstant2 = Math.PI / 2;
            arcRate = 2;
        } else if (arcOrientation == ArcOrientation.TO_LEFT) {
            rotationConstant2 = -Math.PI / 2;
            arcRate = 2;
        } else if (arcOrientation == ArcOrientation.TO_UP_RIGHT) {
            rotationConstant2 = Math.PI / 2;
            arcRate = 4;
        } else if (arcOrientation == ArcOrientation.TO_UP_LEFT) {
            rotationConstant2 = 0;
            arcRate = 4;
        } else if (arcOrientation == ArcOrientation.TO_DOWN_RIGHT) {
            rotationConstant2 = Math.PI;
            arcRate = 4;
        } else if (arcOrientation == ArcOrientation.TO_DOWN_LEFT) {
            rotationConstant2 = 3 * Math.PI / 2;
            arcRate = 4;
        }
        if (pagerProvider != null) {
            pages = pagerProvider.getCount();
            if (pages > 1) {
                itemAngle = Math.PI * 2 / ((pages - 1) * arcRate);
            } else {
                itemAngle = 0;
            }
        } else {
            pages = 3;
        }
    }

    /**
     * check if position must be inverted to keep the browsing direction consistent (top->down and left->right).
     * apply also the invertDirection attribute.
     *
     * @param position indicating the current page.
     * @return inverted or normal position.
     */
    private int invertPosition(int position) {
        if (!currentDirection) {
            return pages - 1 - position;
        } else {
            return position;
        }
    }

    /**
     * Calculates angle at which spot has to be drawn depending on constant arc length
     * or constant angle between the spots.
     *
     * @param position current page number or spot number.
     * @param positionOffset fraction of page slided.
     * @return angle of the spot.
     */
    private double calcAngle(int position, float positionOffset) {
        position = invertPosition(position);
        if (intervalMeasureAngle) {
            // constant angle
            return itemAngle * position + rotationConstant + rotationConstant2;
        } else {
            return calcAngleConstantArc(position, positionOffset);
        }
    }

    /**
     * Calculates approximate angle from the center to the point on the half-ellipses in the desired direction
     * accordingly to the formula<a href="http://math.stackexchange.com/questions/2093569/points-on-an-ellipse#2093586">here described</a>.
     *
     * @param position position number.
     * @return approximate angle.
     */
    private double calcAngleConstantArc(int position, float positionOffset) {
        double t;
        double angle;
        // calculates horizontals with rotationConstant and with rotationConstant2 rotates them to vertical
        t = itemAngle * (position + positionOffset) + rotationConstant;
        angle = t
                - arcFormulaFactor1 * Math.sin(t * 2)
                + arcFormulaFactor2 * Math.sin(t * 4)
                + arcFormulaFactor3 * Math.sin(t * 6)
                + rotationConstant2;
        return angle;
    }

    /**
     * Calculates all values that are needed to draw, and that don't depend from position.
     * Recall this when layout or the number of values change.
     */
    private void prepareFormulaForConstantArcData() {
        if (horizontalRadius < verticalRadius) {
            // inverts axis for verticals
            arcFormulaE2 = 1 - (horizontalRadius * horizontalRadius) / (verticalRadius * verticalRadius);
        } else {
            arcFormulaE2 = 1 - (verticalRadius * verticalRadius) / (horizontalRadius * horizontalRadius);
        }
        arcFormulaFactor1 = arcFormulaE2 / 8 + arcFormulaE2 * arcFormulaE2 / 16
                + 71 * arcFormulaE2 * arcFormulaE2 * arcFormulaE2 / 2048;
        arcFormulaFactor2 = 5 * arcFormulaE2 * arcFormulaE2 / 256
                + 5 * arcFormulaE2 * arcFormulaE2 * arcFormulaE2 / 256;
        arcFormulaFactor3 = 29 * arcFormulaE2 * arcFormulaE2 * arcFormulaE2 / 6144;
    }

    // ******************************** Getters and setters *************************************

    /**
     * Gets the page slider Id.
     *
     * @return page slider Id.
     */
    public int getPageSliderId() {
        return pageSliderId;
    }

    /**
     * Sets the page slider Id.
     *
     * @param pageSliderId Id of the Page slider.
     */
    public void setPageSliderId(int pageSliderId) {
        this.pageSliderId = pageSliderId;
        invalidate();
    }

    /**
     * Sets the Page slider.
     *
     * @param pageSlider Page Slider to which Arc Page Indicator has to been attached.
     */
    public void setPageSlider(PageSlider pageSlider) {
        // useful when the view pager is created dynamically and there is no res
        configurePageSlider(pageSlider);
        invalidate();
    }

    /**
     * Gets the Spots Color.
     *
     * @return Color of the spots.
     */
    public int getSpotsColor() {
        return spotsColor;
    }

    /**
     * Sets the Color to the spots.
     *
     * @param spotsColor Color to been assigned to spots.
     */
    public void setSpotsColor(int spotsColor) {
        this.spotsColor = spotsColor;
        invalidate();
    }

    /**
     * Gets the color of the selected spots.
     *
     * @return Color of the selected spots.
     */
    public int getSelectedSpotColor() {
        return selectedSpotColor;
    }

    /**
     * Sets the Color to the selected spots.
     *
     * @param selectedSpotColor Color to been assigned to selected spots.
     */
    public void setSelectedSpotColor(int selectedSpotColor) {
        this.selectedSpotColor = selectedSpotColor;
        invalidate();
    }

    /**
     * Gets the radius of the spots.
     *
     * @return Radius of the spots.
     */
    public int getSpotsRadius() {
        return spotsRadius;
    }

    /**
     * Sets the radius of the spots.
     *
     * @param spotsRadius Radius of the spots.
     */
    public void setSpotsRadius(int spotsRadius) {
        this.spotsRadius = spotsRadius;
        invalidate();
    }

    /**
     * Tells Whether same Arc length is used or same angle.
     *
     * @return true if same angle between spots.
     */
    public boolean isIntervalMeasureAngle() {
        return intervalMeasureAngle;
    }

    /**
     * Sets the Interval Measure Angle.
     *
     * @param intervalMeasureAngle Yes if same angle between spots.
     */
    public void setIntervalMeasureAngle(boolean intervalMeasureAngle) {
        this.intervalMeasureAngle = intervalMeasureAngle;
        invalidate();
    }

    /**
     * Gets the type of animation.
     *
     * @return Type of animation.
     */
    public AnimationType getAnimationType() {
        return animationType;
    }

    /**
     * Sets the animation type.
     *
     * @param animationType Type of animation needed.
     */
    public void setAnimationType(AnimationType animationType) {
        this.animationType = animationType;
        calcDirection();
        invalidate();
    }

    /**
     * Gets Arc Orientation.
     *
     * @return Arc Orientation.
     */
    public ArcOrientation getArcOrientation() {
        return arcOrientation;
    }

    /**
     * Sets the Arc Orientation.
     *
     * @param arcOrientation Orientation of arc needed.
     */
    public void setArcOrientation(ArcOrientation arcOrientation) {
        this.arcOrientation = arcOrientation;
        invalidate();
    }

    /**
     * Yes if direction is inverted.
     *
     * @return Whether the direction is inverted or not.
     */
    public boolean isInvertDirection() {
        return invertDirection;
    }

    /**
     * If Yes the inverts the Direction of spot movement.
     *
     * @param invertDirection Whether to invert the spot movement direction or not.
     */
    public void setInvertDirection(boolean invertDirection) {
        this.invertDirection = invertDirection;
        calcDirection();
        invalidate();
    }

    /**
     * Whether the hand is Enabled or not.
     *
     * @return Yes, if hand is enabled.
     */
    public boolean isHandEnabled() {
        return handEnabled;
    }

    /**
     * If yes enables the hand.
     *
     * @param handEnabled Whether to enable the hand or not.
     */
    public void setHandEnabled(boolean handEnabled) {
        this.handEnabled = handEnabled;
        invalidate();
    }

    /**
     * Gets the hand color.
     *
     * @return color of the hand.
     */
    public int getHandColor() {
        return handColor;
    }

    /**
     * Sets the hand color.
     *
     * @param handColor color of the hand needed.
     */
    public void setHandColor(int handColor) {
        this.handColor = handColor;
        invalidate();
    }

    /**
     * Sets the width of the hand.
     *
     * @return Hand Width.
     */
    public int getHandWidth() {
        return handWidth;
    }

    /**
     * Sets the width of the hand.
     *
     * @param handWidth Width of the hand needed.
     */
    public void setHandWidth(int handWidth) {
        this.handWidth = handWidth;
        invalidate();
    }

    /**
     * Gets the Length of the hand taking radius of the arc as 1.
     *
     * @return hand Relative Length.
     */
    public float getHandRelativeLength() {
        return handRelativeLength;
    }

    /**
     * Sets the Length of the hand taking radius of the arc as 1.
     *
     * @param handRelativeLength Fraction of radius needed as hand length.
     */
    public void setHandRelativeLength(float handRelativeLength) {
        this.handRelativeLength = handRelativeLength;
        invalidate();
    }

    /**
     * Gets the Shape of the spots.
     *
     * @return spot Shape.
     */
    public SpotShape getSpotShape() {
        return spotShape;
    }

    /**
     * Sets the Shape of the spots.
     *
     * @param spotShape shape of the spot needed.
     */
    public void setSpotShape(SpotShape spotShape) {
        this.spotShape = spotShape;
        invalidate();
    }
}