package weteam.backend.domain.alarm.strategy;

import org.springframework.stereotype.Component;
import weteam.backend.application.handler.exception.CustomException;
import weteam.backend.application.handler.exception.ErrorCode;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class AlarmContext {
  private final Map<AlarmType, AlarmStrategy<?>> map;

  public AlarmContext(List<AlarmStrategy<?>> alarmStrategies) {
    this.map = alarmStrategies.stream().collect(Collectors.toMap(AlarmStrategy::alarmType, Function.identity()));
  }

  //  valueOf를 사용한 전략 확인으로 인한 경고 제거
  @SuppressWarnings("unchecked")
  public <T> AlarmStrategy<T> getCreator(final String name) {
    final AlarmType alarmType = AlarmType.valueOf(name.toUpperCase());
    final AlarmStrategy<?> creator = map.get(alarmType);
    if (creator == null) {
      throw new CustomException(ErrorCode.INVALID_TYPE);
    }
    return (AlarmStrategy<T>) creator;
  }
}
