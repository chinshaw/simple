package com.simple.original.client.view.widgets;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public class TitledTextCell extends TextCell {
    
    private String maxWidth;
    
    public TitledTextCell() {
        this("100%");
    }
    
    public TitledTextCell(String maxWidth) {
        this.maxWidth = maxWidth;
    }
    
    public void render(Context context, SafeHtml value, SafeHtmlBuilder sb) {
        if (value != null) {
            sb.appendHtmlConstant("<div style=\"max-width:" + maxWidth + "\" title=\"" + value.asString() + "\">");
            sb.append(value);
            sb.appendHtmlConstant("</div>");
        }
    }
}
