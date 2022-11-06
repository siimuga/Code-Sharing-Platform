package platform;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface CodeMapper {
    @Mapping(ignore = true, target = "uuid")
    @Mapping(ignore = true, target = "startTime")
    @Mapping(ignore = true, target = "timeLimited")
    @Mapping(ignore = true, target = "viewLimited")
    @Mapping(ignore = true, target = "showTime")
    Code codeInfoToCode(CodeInfo codeInfo);

    @Mapping(source = "showTime", target = "time")
    CodeInfo codeToCodeInfo(Code code);


}
