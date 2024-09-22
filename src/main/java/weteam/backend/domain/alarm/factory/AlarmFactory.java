package weteam.backend.domain.alarm.factory;

import org.springframework.stereotype.Component;
import weteam.backend.application.handler.exception.CustomException;
import weteam.backend.application.handler.exception.ErrorCode;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class AlarmFactory {
  private final Map<AlarmType, AlarmStrategy<?>> map;

  private AlarmFactory(List<AlarmStrategy<?>> alarmStrategies) {
    this.map = alarmStrategies.stream().collect(Collectors.toMap(AlarmStrategy::alarmType, Function.identity()));
  }

  public <T> AlarmStrategy<T> getCreator(final String name) {
    final AlarmType alarmType = AlarmType.valueOf(name);
    final AlarmStrategy<?> creator = map.get(alarmType);
    if (creator == null) {
      throw new CustomException(ErrorCode.INVALID_TYPE);
    }
    return (AlarmStrategy<T>) creator;
  }
}
