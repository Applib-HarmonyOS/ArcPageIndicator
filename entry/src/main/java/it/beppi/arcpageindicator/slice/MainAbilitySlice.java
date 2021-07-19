package it.beppi.arcpageindicator.slice;

import it.beppi.arcpageindicator.ArcPageIndicator;
import it.beppi.arcpageindicator.ResourceTable;
import it.beppi.arcpageindicator.TestPagerProvider;
import java.util.ArrayList;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.PageSlider;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.miscservices.timeutility.Time;

/**
 * MainAbilitySlice Class for testing ArcPageIndicator library.
 */
public class MainAbilitySlice extends AbilitySlice {

    ArcPageIndicator arcPageIndicator;
    PageSlider pageSlider;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        initPageSlider();
        // for manual page sliding comment the below line.
        startAnimationThreadStuff(delay);
    }

    private void initPageSlider() {
        pageSlider = (PageSlider) findComponentById(ResourceTable.Id_page_slider);
        pageSlider.setProvider(new TestPagerProvider(getData(), getContext()));
        arcPageIndicator = (ArcPageIndicator) findComponentById(ResourceTable.Id_arc_pi_1);
        arcPageIndicator.setPageSlider(pageSlider);
    }

    private ArrayList<TestPagerProvider.DataItem> getData() {
        ArrayList<TestPagerProvider.DataItem> dataItems = new ArrayList<>();
        for (int i = 1; i < totalPages + 1; i++) {
            dataItems.add(new TestPagerProvider.DataItem("Page " + i));
        }
        return dataItems;
    }

    int pageNumber = 0;
    int totalPages = 12;
    boolean towardsRight = true;
    int times = 36;
    long delay = 1000;
    Thread updateThread;

    private void startAnimationThreadStuff(long delay) {
        if (updateThread != null && updateThread.isAlive()) {
            updateThread.interrupt();
        }
        // Start animation after a delay so there's no missed frames while the app loads up
        EventRunner eventRunner = EventRunner.getMainEventRunner();
        EventHandler eventHandler = new EventHandler(eventRunner);
        eventHandler.postTask(() -> {
            // Run thread to update
            updateThread = new Thread(() -> {
                while (times > 0) {
                    // Must set progress in UI thread
                    eventHandler.postTask(() -> pageSlider.setCurrentPage(pageNumber, true));
                    modifications();
                    Time.sleep(1000);
                }
            });
            updateThread.start();
        }, delay);
    }

    private void modifications() {
        if (towardsRight) {
            pageNumber++;
        } else {
            pageNumber--;
        }
        if (pageNumber == totalPages - 1) {
            towardsRight = false;
        }
        if (pageNumber < 0) {
            towardsRight = true;
            pageNumber = 0;
        }
        times--;
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
