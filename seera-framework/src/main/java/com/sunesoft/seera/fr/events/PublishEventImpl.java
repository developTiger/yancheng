package com.sunesoft.seera.fr.events;


import com.google.common.eventbus.EventBus;

import com.sunesoft.seera.fr.annotations.EventListener;
import com.sunesoft.seera.fr.matcher.ClassesFactory;
import com.sunesoft.seera.fr.matcher.Matcher;
import com.sunesoft.seera.fr.matcher.Matchers;
import org.springframework.stereotype.Service;

/**
 * Created by zhouz on 2016/5/17.
 */
@Service("publishEvent")
public class PublishEventImpl implements PublishEvent {
    private static EventBus eventBus = new EventBus("WorkBus");


    static {
      Matcher matchs = Matchers.annotatedWith(EventListener.class);
       ClassesFactory classes=ClassesFactory.getInstance();
        for (Class<?> aClass : classes.getAllClass()) {
            matchs.matches(aClass);
            try {
                eventBus.register(aClass.newInstance()) ;
            }catch (Exception ex){
            }
        }
    }

    @Override
    public <T extends BaseEvent> void Publish(T t) {try {
          eventBus.post(t);
        } catch (Exception ex) {
           // exceptionPolicy.HandleException(this,ex);
        }
    }

}
