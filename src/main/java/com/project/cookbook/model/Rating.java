package com.project.cookbook.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "RATINGS")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RT_ID", nullable = false)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RT_USER_ID", referencedColumnName = "USR_ID")
    private User author;

    @Column(name = "RT_RATE", nullable = false)
    private int rate;

    @Column(name = "RT_COMMENT")
    private String comment;

    @ManyToOne
    @JoinColumn(name = "RT_RECIPE", referencedColumnName = "RCP_ID")
    private Recipe recipe;
}
