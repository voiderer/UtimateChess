package edu.ncepu.yutao.PublicComponent.Event;

import java.util.EventListener;

/**
 * Created by AUTOY on 2016/6/5.
 */
public interface ChatPanelListener extends EventListener {
    void messageSent(String message);
}
