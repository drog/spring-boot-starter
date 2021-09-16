package org.starter.springboot.dto.external;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MatrixIdentityDto {

    @ApiModelProperty(example = "Thomas")
    private String firstName;

    @ApiModelProperty(example = "Anderson")
    private String lastName;

    @ApiModelProperty(example = "31")
    private Integer age;

    @ApiModelProperty(example = "278-52-94")
    private String phone;

    @ApiModelProperty(example = "Neo")
    private String nick;
}
