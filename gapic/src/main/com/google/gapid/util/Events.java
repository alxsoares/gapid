/*
 * Copyright (C) 2017 Google Inc.
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
package com.google.gapid.util;

import static java.util.logging.Level.FINE;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Widget;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.logging.Logger;

public class Events {
  protected static final Logger LOG = Logger.getLogger(Events.class.getName());

  public static final int Loading = 0x7f000001;
  public static final int Loaded  = 0x7f000002;
  public static final int Updated = 0x7f000003;
  public static final int Search  = 0x7f000004;

  public static final int REGEX = 1 << 10; // Used in the Search event.

  public static Event newSearchEvent(Widget source, String text, boolean regex) {
    Event event = new Event();
    event.widget = source;
    event.text = text;
    event.detail = regex ? REGEX : 0;
    return event;
  }

  public static interface Listener {
    // Empty tagging interface.
  }

  public static interface ListenerCollection<T extends Listener> {
    public void addListener(T listener);
    public void removeListener(T listener);
    public T fire();
  }

  @SuppressWarnings("unchecked")
  public static <T extends Listener> ListenerCollection<T> listeners(Class<T> listenerInterface) {
    ListenerCollectionImpl<T> result = new ListenerCollectionImpl<T>();
    return result.withProxy((T)Proxy.newProxyInstance(
        Events.class.getClassLoader(), new Class<?>[] { listenerInterface }, result));
  }


  private static class ListenerCollectionImpl<T extends Listener> extends ArrayList<T>
      implements ListenerCollection<T>, InvocationHandler {

    private T proxy;

    public ListenerCollectionImpl() {
    }

    public ListenerCollectionImpl<T> withProxy(T newProxy) {
      proxy = newProxy;
      return this;
    }

    @Override
    public T fire() {
     return proxy;
    }

    @Override
    public synchronized void addListener(T listener) {
      super.add(listener);
    }

    @Override
    public synchronized void removeListener(T listener) {
      super.remove(listener);
    }

    @Override
    public Object invoke(Object me, Method method, Object[] args) throws Throwable {
      if (LOG.isLoggable(FINE)) {
        StringBuilder msg = new StringBuilder()
            .append("Firing ").append(method.getName()).append('(');
        if (args != null && args.length > 0) {
          msg.append("{0}");
          for (int i = 1; i < args.length; i++) {
            msg.append(", {").append(i).append('}');
          }
        }
        LOG.log(FINE, msg.append(')').toString(), args);
      }

      Object[] listeners;
      synchronized (this) {
        listeners = toArray();
      }
      for (Object listener : listeners) {
        method.invoke(listener, args);
      }
      return null;
    }
  }
}
