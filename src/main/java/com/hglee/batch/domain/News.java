package com.hglee.batch.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class News {

    private String id;
    private String title;
    private String content;
    private String created_at;
    private String updated_at;
}
