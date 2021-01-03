package br.com.springessentials2.demo.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnimePutRequestBody {
    private String name;
    private long id;
}
