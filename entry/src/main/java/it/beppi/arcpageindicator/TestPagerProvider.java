/*
 * Copyright (C) 2020-21 Application Library Engineering Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package it.beppi.arcpageindicator;

import java.io.IOException;
import java.util.List;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.PageSliderProvider;
import ohos.agp.components.StackLayout;
import ohos.agp.components.Text;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.agp.utils.TextAlignment;
import ohos.app.Context;
import ohos.global.resource.NotExistException;
import ohos.global.resource.WrongTypeException;

/**
 * Custom PageSliderProvider.
 */
public class TestPagerProvider extends PageSliderProvider {

    // Data source. Each page maps to an item on the list.
    private final List<DataItem> list;
    private final Context context;

    public TestPagerProvider(List<DataItem> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object createPageInContainer(ComponentContainer componentContainer, int i) {
        final DataItem data = list.get(i);
        Text label = new Text(null);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setLayoutConfig(
                new StackLayout.LayoutConfig(
                        ComponentContainer.LayoutConfig.MATCH_PARENT,
                        ComponentContainer.LayoutConfig.MATCH_PARENT
                ));
        label.setText(data.text);
        label.setTextColor(Color.WHITE);
        label.setTextSize(150);
        ShapeElement element = new ShapeElement();
        String colorStr = "#FFAFEEEE";

        switch (String.valueOf(i)) {
            case "0":
                colorStr = getColorString(ResourceTable.String_color_0);
                break;
            case "1":
                colorStr = getColorString(ResourceTable.String_color_1);
                break;
            case "2":
                colorStr = getColorString(ResourceTable.String_color_2);
                break;
            case "3":
                colorStr = getColorString(ResourceTable.String_color_3);
                break;
            case "4":
                colorStr = getColorString(ResourceTable.String_color_4);
                break;
            case "5":
                colorStr = getColorString(ResourceTable.String_color_5);
                break;
            case "6":
                colorStr = getColorString(ResourceTable.String_color_6);
                break;
            case "7":
                colorStr = getColorString(ResourceTable.String_color_7);
                break;
            case "8":
                colorStr = getColorString(ResourceTable.String_color_8);
                break;
            case "9":
                colorStr = getColorString(ResourceTable.String_color_9);
                break;
            case "10":
                colorStr = getColorString(ResourceTable.String_color_10);
                break;
            case "11":
                colorStr = getColorString(ResourceTable.String_color_11);
                break;
            default:
                break;
        }

        element.setRgbColor(RgbColor.fromArgbInt(Color.getIntColor(colorStr)));
        label.setBackground(element);
        componentContainer.addComponent(label);
        return label;
    }

    private String getColorString(int i) {
        try {
            return context.getResourceManager().getElement(i).getString();
        } catch (IOException | WrongTypeException | NotExistException e) {
            e.printStackTrace();
        }
        return "#FFAFEEEE";
    }

    @Override
    public void destroyPageFromContainer(ComponentContainer componentContainer, int i, Object o) {
        componentContainer.removeComponent((Component) o);
    }

    @Override
    public boolean isPageMatchToObject(Component component, Object o) {
        return true;
    }

    /**
     * Data Entity.
     */
    public static class DataItem {
        String text;

        public DataItem(String txt) {
            text = txt;
        }
    }
}