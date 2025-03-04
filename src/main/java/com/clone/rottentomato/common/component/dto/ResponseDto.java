package com.clone.rottentomato.common.component.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseDto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String resultMsg;   // 응답 값으로 사용할때, 각 개별 메세지
    private boolean success = true;

    public void setSuccess(String resultMsg){
        this.success = true;
        this.resultMsg = resultMsg;
    }
    public void setFail(String resultMsg){
        this.success = false;
        this.resultMsg = resultMsg;
    }

    public void setResult(boolean success, String resultMsg){
        this.success = success;
        this.resultMsg = resultMsg;
    }
}
