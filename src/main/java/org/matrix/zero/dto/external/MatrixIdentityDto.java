package org.matrix.zero.dto.external;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MatrixIdentityDto {

    @ApiModelProperty(example = "d41d8cd98f00b204e9800998ecf8427e")
    private String tokenMatrix;

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
