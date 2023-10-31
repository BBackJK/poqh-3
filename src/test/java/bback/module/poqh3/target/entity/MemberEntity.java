package bback.module.poqh3.target.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member")
public class MemberEntity {

    @Id
    private String id;

    private String name;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "member")
    private List<ArticleEntity> articles = new ArrayList<>();

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<ArticleEntity> getArticles() {
        return articles;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addArticle(ArticleEntity articleEntity) {
        this.articles.add(articleEntity);
    }

    public MemberEntity() {}

    public MemberEntity(String id, String name) {
        this.id = id;
        this.name = name;
        this.createdAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "MemberEntity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", articles=" + articles + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
