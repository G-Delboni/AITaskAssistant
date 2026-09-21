package com.app.aiassistant.entity;

import com.app.aiassistant.enums.GoalPeriod;
import com.app.aiassistant.enums.GoalStatus;
import com.app.aiassistant.enums.GoalTargetUnit;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

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
    @Enumerated(EnumType.STRING)
    private GoalTargetUnit targetUnit;
    private Double targetValue;
    @Enumerated(EnumType.STRING)
    private GoalStatus status;

    public Goal(String title, String description, GoalPeriod period, Double targetValue, GoalTargetUnit targetUnit) {
        this.title = title;
        this.description = description;
        this.period = period;
        this.targetUnit = targetUnit;
        this.targetValue = targetValue;
        this.status = GoalStatus.PENDING;
    }

    public void completeGoal() {
        this.status = GoalStatus.COMPLETED;
    }

    public void updateGoal(String title, String description, GoalPeriod period, Double targetValue, GoalTargetUnit targetUnit) {
        this.title = title;
        this.description = description;
        this.period = period;
        this.targetValue = targetValue;
    }
}
