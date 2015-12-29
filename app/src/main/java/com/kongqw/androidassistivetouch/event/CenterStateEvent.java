package com.kongqw.androidassistivetouch.event;

/**
 * Created by kongqw on 2015/12/29.
 */
public class CenterStateEvent {
    private boolean isOpen;

    public CenterStateEvent(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }
}
