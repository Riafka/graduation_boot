package com.github.riafka.graduation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "restaurant", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}, name = "restaurant_unique_name_idx"))
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant extends NamedEntity {
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @JsonManagedReference
    @OnDelete(action = OnDeleteAction.CASCADE) //https://stackoverflow.com/a/44988100/548473
    @JsonIgnore
    private List<MenuItem> menuItems;

    public Restaurant(Restaurant r) {
        this(r.id, r.name, r.menuItems);
    }

    public Restaurant(Integer id, String name, Collection<MenuItem> items) {
        super(id, name);
        setMenuItems(items);
    }

    public void setMenuItems(Collection<MenuItem> items) {
        this.menuItems = CollectionUtils.isEmpty(items) ? Collections.emptyList() : new ArrayList<>(items);
    }
}
