package com.example.dainq.smilenotes.ui.common.spinner;

import java.util.ArrayList;

public class SpinnerItem implements ISpinnerItem {
    final String spinnerLabel;
    String labelId;

    public SpinnerItem(String label) {
        this.spinnerLabel = label;
    }

    public SpinnerItem(String label, String labelId) {
        this.spinnerLabel = label;
        this.labelId = labelId;
    }

    public static ArrayList<SpinnerItem> getSpinnerItem(String[] objects) {
        ArrayList<SpinnerItem> flexibleItems = new ArrayList<>();
        for (String object : objects) {
            flexibleItems.add(new SpinnerItem(object));
        }

        return flexibleItems;
    }

    @Override
    public String getSingleSpinnerLabel() {
        return spinnerLabel;
    }

    @Override
    public String getSingleLabelId() {
        return labelId;
    }
}
