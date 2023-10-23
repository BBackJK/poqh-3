package bback.module.poqh3.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "article")
public class ArticleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity member;

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public MemberEntity getMember() {
        return member;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public void setMember(MemberEntity member) {
        this.member = member;
    }

    public ArticleEntity() {}

    public ArticleEntity(Long id, String title, String contents, MemberEntity member) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.member = member;
    }
}
