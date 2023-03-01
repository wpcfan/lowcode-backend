package com.mooc.backend.entities;

import com.mooc.backend.entities.blocks.BlockData;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.util.Comparator;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mooc_page_block_data")
public class PageBlockDataEntity implements Comparator<PageBlockDataEntity>, Comparable<PageBlockDataEntity> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "sort", nullable = false)
    private Integer sort;

    @Type(JsonType.class)
    @Column(name = "content", nullable = false, columnDefinition = "json")
    private BlockData content;

    @ManyToOne
    @JoinColumn(name = "page_block_id")
    private PageBlockEntity pageBlock;

    @Override
    public int compare(PageBlockDataEntity o1, PageBlockDataEntity o2) {
        return o1.getSort() - o2.getSort();
    }

    @Override
    public int compareTo(PageBlockDataEntity o) {
        return this.getSort() - o.getSort();
    }
}
