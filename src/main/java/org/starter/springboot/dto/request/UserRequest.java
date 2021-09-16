package org.starter.springboot.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class UserRequest {

    @Email
    @NotBlank
    @ApiModelProperty(example = "neo@matrix.com")
    private String email;

    @NotBlank
    @ApiModelProperty(example = "Thomas")
    private String firstName;

    @NotBlank
    @ApiModelProperty(example = "Anderson")
    private String lastName;

    @NotNull
    @ApiModelProperty(example = "37")
    private Integer age;
}
