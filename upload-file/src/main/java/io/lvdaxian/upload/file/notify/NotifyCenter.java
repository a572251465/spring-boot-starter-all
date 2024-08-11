package io.lvdaxian.upload.file.notify;

import io.lvdaxian.upload.file.notify.entity.Event;
import io.lvdaxian.upload.file.notify.enumeration.PublisherTypeEnum;
import io.lvdaxian.upload.file.notify.listener.Subscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class NotifyCenter {
  private final static ConcurrentMap<PublisherTypeEnum, List<Subscriber>> subscriberMap = new ConcurrentHashMap<>();
  
  public static void publishEvent(final PublisherTypeEnum key, final Event event) {
    List<Subscriber> subscriberList = subscriberMap.getOrDefault(key, new ArrayList<>());
    if (subscriberList.isEmpty()) return;
    
    for (Subscriber fn : subscriberList)
      fn.onEvent(event);
  }
  
  public static void removeSubscriber(final PublisherTypeEnum key) {
    subscriberMap.remove(key);
  }
  
  public static void registerSubscriber(PublisherTypeEnum key, Subscriber value) {
    List<Subscriber> subscriberList = subscriberMap.getOrDefault(key, new ArrayList<>());
    
    if (subscriberList.isEmpty())
      subscriberMap.put(key, (subscriberList = new ArrayList<>()));
    
    subscriberList.add(value);
  }
}
