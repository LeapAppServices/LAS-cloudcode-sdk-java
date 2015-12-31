package com.maxleap.code;

import com.maxleap.code.impl.GlobalConfig;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * load all the define.
 * Created by stream.
 */
public interface Loader {

  Map<String, Definer> definers();

  /**
   * user have to implements this method for own code.
   *
   * @param globalConfig user's cloudcode config
   */
  void main(ApplicationContext context,GlobalConfig globalConfig);

}
