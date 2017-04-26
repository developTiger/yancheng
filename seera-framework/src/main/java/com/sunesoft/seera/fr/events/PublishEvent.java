package com.sunesoft.seera.fr.events;

/**
 * Created by zhouz on 2016/5/17.
 */
public interface PublishEvent {
   public  <T extends BaseEvent> void Publish(T t);
 }
