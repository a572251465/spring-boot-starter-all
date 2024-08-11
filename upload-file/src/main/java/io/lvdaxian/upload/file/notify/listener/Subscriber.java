package io.lvdaxian.upload.file.notify.listener;

import io.lvdaxian.upload.file.notify.entity.Event;

public abstract class Subscriber<T extends Event> {
  public abstract void onEvent(T event);
}
