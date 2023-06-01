package com.ll.olol.boundedContext.recruitment;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateForm {
    //        @NotBlank
//        @Size(min = 3, max = 65)
    @NotBlank(message = "제목은 필수항목입니다.") // 아래의 변수는 비어있으면 안된다.
    @Size(max = 65, message = "제목을 200자 이하로 설정해주세요.") // 최대 200까지 가능하다.
    private String title;

    @NotBlank(message = "내용은 필수항목입니다.")
    @Size(max = 20000, message = "제목을 20000자 이하로 설정해주세요.")
    private String content;


    @NotNull(message = "타입은 필수항목입니다.")
    @Min(value = 1, message = "올바른 타입 값을 선택해주세요.")
    @Max(value = 2, message = "올바른 타입 값을 선택해주세요.")
    private Integer typeValue;

    @NotNull(message = "타입은 필수항목입니다.")
    @Min(value = 1, message = "올바른 타입 값을 선택해주세요.")
    @Max(value = 2, message = "올바른 타입 값을 선택해주세요.")
    private Integer dayNight;

    private String mountainName;

    @NotNull(message = "모집인원은 필수항목입니다.")
    @Min(value = 0, message = "올바른 인원수를 입력해주세요.")
    private Long recruitsNumber;

//        private LocalDateTime startTime;

//        private LocalDateTime courseTime;

    private Long ageRange;

    @NotBlank(message = "내용은 필수항목입니다.")
    private String connectType;

}
