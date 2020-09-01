package io.javable.jpaapplication.core.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<CellPhone> cellPhones = new ArrayList<>();

    public Member(final String name) {
        this.name = name;
    }

    public void buyCellphone(final CellPhone cellPhone) {
        this.cellPhones.add(cellPhone);
        cellPhone.placeMember(this);
    }
}
