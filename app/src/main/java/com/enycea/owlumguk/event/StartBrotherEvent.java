package com.enycea.owlumguk.event;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by scene on 16/6/30.
 */
public class StartBrotherEvent {
    public SupportFragment targetFragment;

    public StartBrotherEvent(SupportFragment targetFragment) {
        this.targetFragment = targetFragment;
    }
}
