package platform;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name="CODES")
public class    Code {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @NotBlank
    @Lob
    @Column(name = "CODE")
    private String code;
    @NotBlank
    @Column(name = "DATEX")
    private String date;
    @NotBlank
    @Column(name = "TOKEN")
    private String uuid;
    @Column(name = "TIMEX")
    private Long time;
    @Column(name = "VIEWS")
    private Integer views;
    @Column(name = "SHOWTIME")
    private Long showTime;
    @Column(name = "DATETIMEX")
    private LocalDateTime startTime;
    @Column(name = "TIMELIMIT")
    private Boolean timeLimited;
    @Column(name = "VIEWLIMIT")
    private Boolean viewLimited;
}
