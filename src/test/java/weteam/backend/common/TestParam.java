package weteam.backend.common;

import lombok.Getter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Getter
public class TestParam {
  public final MultiValueMap<String, String> param = new LinkedMultiValueMap<>();

  private TestParam() {
    this.param.add("page", String.valueOf(0));
    this.param.add("size", String.valueOf(10));
  }

  public static MultiValueMap<String, String> make() {
    return new TestParam().getParam();
  }
}
