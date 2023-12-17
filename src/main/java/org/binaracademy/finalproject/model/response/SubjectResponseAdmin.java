package org.binaracademy.finalproject.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubjectResponseAdmin {

    private String id;
    private String title;
    private String code;
    private String link;
    private String description;
    private Boolean isPremium;
}