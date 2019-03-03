package com.teqtrue;

import java.util.HashMap;

public class GameMap {
  private HashMap<Integer, HashMap<Integer, Integer>> state = new HashMap<Integer, HashMap<Integer, Integer>>();

  public void set(int x, int y, int val) {
    if (this.state.containsKey(x)) {
      HashMap<Integer, Integer> column = this.state.get(x);
      column.put(y, val);
    } else {
      HashMap<Integer, Integer> newColumn = new HashMap<Integer, Integer>();
      newColumn.put(y, val);
      this.state.put(x, newColumn);
    }
  }

  public boolean remove(int x, int y) {
    if (!this.state.containsKey(x)) {
      return false;
    }
    HashMap<Integer, Integer> column = this.state.get(x);
    if (!column.containsKey(y)) {
      return false;
    }
    if (column.size() == 1) {
      this.state.remove(x);
    } else {
      column.remove(y);
    }
    return true;
  }

  public int get(int x, int y) {
    if (!this.state.containsKey(x)) {
      return -1;
    }
    HashMap<Integer, Integer> column = this.state.get(x);
    if (!column.containsKey(y)) {
      return -1;
    }
    return column.get(y);
  }
}