package io.github.geertbraakman.api.messaging;

public enum DefaultMessage {
  PREFIX("prefix", ""),
  INTERNAL_ERROR("internal-error", "%prefix%&cSomething went wrong!"),
  DEFAULT("default", "%prefix%&cThere is no default message available for &7%key%&c!"),
  NOT_ENOUGH_SPACE("not-enough-space", "%prefix%&cYou don't have enough space for &7%amount%&c items in your inventory, they will be dropped on the ground!"),
  NO_MESSAGE("none", ""),
  RELOAD_FAILED("reload-success", "%prefix%&aThe reload was successfully!"),
  RELOAD_SUCCESS("reload-failed", "%prefix%&cThe reload failed!")
  ;

  public final String key;
  public final String message;

  DefaultMessage(String key, String message) {
    this.key = key;
    this.message = message;
  }

  public static DefaultMessage fromKey(String key) {
    for (DefaultMessage defaultMessage: DefaultMessage.values()) {
      if(defaultMessage.key.equals(key)) {
        return defaultMessage;
      }
    }
    return null;
  }

}
