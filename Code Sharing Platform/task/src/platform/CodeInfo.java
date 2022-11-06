package platform;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CodeInfo {
    private String code;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String date;
    private Long time;
    private Integer views;
}
