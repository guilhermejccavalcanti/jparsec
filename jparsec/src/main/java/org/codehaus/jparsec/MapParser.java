/*****************************************************************************
 * Copyright (C) Codehaus.org                                                *
 * ------------------------------------------------------------------------- *
 * Licensed under the Apache License, Version 2.0 (the "License");           *
 * you may not use this file except in compliance with the License.          *
 * You may obtain a copy of the License at                                   *
 *                                                                           *
 * http://www.apache.org/licenses/LICENSE-2.0                                *
 *                                                                           *
 * Unless required by applicable law or agreed to in writing, software       *
 * distributed under the License is distributed on an "AS IS" BASIS,         *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *
 * See the License for the specific language governing permissions and       *
 * limitations under the License.                                            *
 *****************************************************************************/
package org.codehaus.jparsec;

import org.codehaus.jparsec.functors.Map;

final class MapParser<From, To> extends Parser<To> {
  private final Parser<From> parser;
  private final Map<? super From, ? extends To> m;

  MapParser(Parser<From> parser, Map<? super From, ? extends To> m) {
    this.parser = parser;
    this.m = m;
  }

  @Override boolean apply(final ParseContext ctxt) {
    final boolean r = parser.apply(ctxt);
    if (r) {
      ctxt.result = m.map(parser.getReturn(ctxt));
    }
    return r;
  }
  
  @Override public String toString() {
    return m.toString();
  }
}