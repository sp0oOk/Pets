package org.github.spook.pets;

import lombok.AllArgsConstructor;

@AllArgsConstructor(staticName = "create")
@SuppressWarnings("unused")
public class AbilityArgs {

  private final Object[] args;
  private int step;

  /**
   * Reads Argument at current position, and increments position in array if next argument exists
   *
   * @return "null" if no other arguments exist, otherwise arg.
   * @param <T> _
   */
  public <T> T get() {
    return args.length != step ? (T) args[step++] : null;
  }

  /**
   * Reads an Argument at a specifically passed index in the arguments array
   *
   * @param idx index of argument
   * @return Argument at position, else null
   * @param <T> _
   */
  public <T> T getAt(int idx) {
    return args.length > idx ? (T) args[idx] : null;
  }

  /**
   * Read Argument at current position and increments position in array if next argument exists,
   * assigns default value if none
   *
   * @param def Default value
   * @return Argument at next position, else default value
   * @param <T> _
   */
  public <T> T get(T def) {
    return args.length != step ? (T) args[step++] : def;
  }

  /**
   * Reads an Argument at a specifically passed index in the arguments array
   *
   * @param idx index of argument
   * @param def Default value
   * @return Argument at position else default value
   * @param <T> _
   */
  public <T> T get(int idx, T def) {
    return args.length > idx ? (T) args[idx] : def;
  }

  public String getAll() {
    final StringBuilder sb = new StringBuilder();
    for (Object arg : args) {
      sb.append(arg);
    }
    return sb.toString();
  }
}
