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
package com.google.gapid.service.snippets;

import com.google.gapid.rpclib.binary.BinaryClass;
import com.google.gapid.rpclib.binary.BinaryObject;
import com.google.gapid.rpclib.binary.Decoder;
import com.google.gapid.rpclib.binary.Encoder;
import com.google.gapid.rpclib.binary.Namespace;
import com.google.gapid.rpclib.schema.Constant;
import com.google.gapid.rpclib.schema.Entity;
import com.google.gapid.rpclib.schema.Field;
import com.google.gapid.rpclib.schema.Interface;
import com.google.gapid.rpclib.schema.Method;
import com.google.gapid.rpclib.schema.Primitive;
import com.google.gapid.rpclib.schema.Slice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public final class Labels extends KindredSnippets implements BinaryObject {
  //<<<Start:Java.ClassBody:1>>>
  private Pathway myPath;
  private String[] myLabels;

  // Constructs a default-initialized {@link Labels}.
  public Labels() {}


  public Pathway getPath() {
    return myPath;
  }

  public Labels setPath(Pathway v) {
    myPath = v;
    return this;
  }

  public String[] getLabels() {
    return myLabels;
  }

  public Labels setLabels(String[] v) {
    myLabels = v;
    return this;
  }

  @Override
  public BinaryClass klass() { return Klass.INSTANCE; }


  private static final Entity ENTITY = new Entity("snippets", "Labels", "", "");

  static {
    ENTITY.setFields(new Field[]{
      new Field("Path", new Interface("Pathway")),
      new Field("Labels", new Slice("", new Primitive("string", Method.String))),
    });
    Namespace.register(Klass.INSTANCE);
  }
  public static void register() {}
  //<<<End:Java.ClassBody:1>>>


  @Override
  public String toString() {
    return myPath + " = " + Arrays.asList(myLabels).toString();
  }

  /**
   * find the labels in the snippets.
   * @param snippets any kind of snippets.
   * @return the labels maybe null.
   */

  public static Labels fromSnippets(KindredSnippets[] snippets) {
    for (KindredSnippets obj : snippets) {
      if (obj instanceof Labels) {
        return (Labels)obj;
      }
    }
    return null;
  }

  /**
   * filter a list of constants down to those present in the snippet.
   * @param constants a list of constants to filter.
   * @return the constants to prefer.
   */

  public List<Constant> preferred(Collection<Constant> constants) {
    List<Constant> preferred = new ArrayList<Constant>();
    for (Constant c : constants) {
      for (int i = 0; i < myLabels.length; i++) {
         if (c.getName().equals(myLabels[i])) {
           preferred.add(c);
         }
      }
    }
    return preferred;
  }

  public enum Klass implements BinaryClass {
    //<<<Start:Java.KlassBody:2>>>
    INSTANCE;

    @Override
    public Entity entity() { return ENTITY; }

    @Override
    public BinaryObject create() { return new Labels(); }

    @Override
    public void encode(Encoder e, BinaryObject obj) throws IOException {
      Labels o = (Labels)obj;
      e.object(o.myPath == null ? null : o.myPath.unwrap());
      e.uint32(o.myLabels.length);
      for (int i = 0; i < o.myLabels.length; i++) {
        e.string(o.myLabels[i]);
      }
    }

    @Override
    public void decode(Decoder d, BinaryObject obj) throws IOException {
      Labels o = (Labels)obj;
      o.myPath = Pathway.wrap(d.object());
      o.myLabels = new String[d.uint32()];
      for (int i = 0; i <o.myLabels.length; i++) {
        o.myLabels[i] = d.string();
      }
    }
    //<<<End:Java.KlassBody:2>>>
  }
}
