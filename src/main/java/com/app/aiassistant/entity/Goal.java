package com.app.aiassistant.entity;

import com.app.aiassistant.enums.GoalPeriod;
import com.app.aiassistant.enums.GoalTargetUnit;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name="tb_goal")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private GoalPeriod period;
    private BigDecimal targetValue;
    private GoalTargetUnit targetUnit;

    public Goal(Long id, String title, String description, GoalPeriod period, BigDecimal targetValue, GoalTargetUnit targetUnit) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.period = period;
        this.targetValue = targetValue;
        this.targetUnit = targetUnit;
    }
}
