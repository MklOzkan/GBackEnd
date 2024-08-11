package com.project.payload.response.user;

import com.project.payload.response.abstracts.BaseUserResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class UserUpdateResponse extends BaseUserResponse {

}
