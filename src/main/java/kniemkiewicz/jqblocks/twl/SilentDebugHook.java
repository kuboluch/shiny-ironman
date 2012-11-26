package kniemkiewicz.jqblocks.twl;

import de.matthiasmann.twl.DebugHook;

/**
 * User: krzysiek
 * Date: 25.11.12
 */
public class SilentDebugHook extends DebugHook {

  public void beforeApplyTheme(de.matthiasmann.twl.Widget widget) {  }

  public void afterApplyTheme(de.matthiasmann.twl.Widget widget) {  }

  public void missingTheme(java.lang.String themePath) {  }

  public void missingChildTheme(de.matthiasmann.twl.ThemeInfo parent, java.lang.String theme) {  }

  public void missingParameter(de.matthiasmann.twl.ParameterMap map, java.lang.String paramName, java.lang.String parentDescription, java.lang.Class<?> dataType) {  }

  public void wrongParameterType(de.matthiasmann.twl.ParameterMap map, java.lang.String paramName, java.lang.Class<?> expectedType, java.lang.Class<?> foundType, java.lang.String parentDescription) {  }

  public void wrongParameterType(de.matthiasmann.twl.ParameterList map, int idx, java.lang.Class<?> expectedType, java.lang.Class<?> foundType, java.lang.String parentDescription) {  }

  public void replacingWithDifferentType(de.matthiasmann.twl.ParameterMap map, java.lang.String paramName, java.lang.Class<?> oldType, java.lang.Class<?> newType, java.lang.String parentDescription) {  }

  public void missingImage(java.lang.String name) {  }

  public void guiLayoutValidated(int iterations, java.util.Collection<de.matthiasmann.twl.Widget> loop) {  }

  public void usingFallbackTheme(java.lang.String themePath) {  }
}
