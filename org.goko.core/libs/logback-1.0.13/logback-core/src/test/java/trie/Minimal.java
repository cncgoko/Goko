/**
 * Logback: the reliable, generic, fast and flexible logging framework.
 * Copyright (C) 1999-2013, QOS.ch. All rights reserved.
 *
 * This program and the accompanying materials are dual-licensed under
 * either the terms of the Eclipse Public License v1.0 as published by
 * the Eclipse Foundation
 *
 *   or (per the licensee's choosing)
 *
 * under the terms of the GNU Lesser General Public License version 2.1
 * as published by the Free Software Foundation.
 */
package trie;


public class Minimal {



  public Node findValue(Node startNode, String value) {
    Node current = startNode;
    for (char c : value.toCharArray()) {
      if (current.containsChildValue(c)) {
        current = current.getChild(c);
      } else {
        current = Node.EMPTY_NODE;
        break;
      }
    }
    return current;
  }
}
