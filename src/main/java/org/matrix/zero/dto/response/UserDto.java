package org.matrix.zero.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.matrix.zero.dto.external.MatrixIdentityDto;

import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class UserDto {

    @ApiModelProperty(example = "neo@matrix.com")
    private String email;

    @ApiModelProperty(example = "Thomas")
    private String firstName;

    @ApiModelProperty(example = "Anderson")
    private String lastName;

    @ApiModelProperty(example = "37")
    private Integer age;

    @ApiModelProperty(example = "CL")
    private String country;

    private List<MatrixIdentityDto> matrixIdentities;
}