package sjs.assignment.sjsassignment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sjs.assignment.sjsassignment.model.ScrapEntity;

@Getter
@Setter
@NoArgsConstructor
public class ScrapRequestDto {

    /** json 데이터 저장 */
    private String scarpData;

    @Builder
    private ScrapRequestDto(String scarpData) {
        this.scarpData = scarpData;
    }

    public ScrapEntity toEntity() {
        return ScrapEntity.builder()
        .scarpData(scarpData)
        .build();
    }
}
