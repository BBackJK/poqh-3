package bback.module.poqh3.target.domain;

public class HasAllConstructorArticle {

    private long id;
    private String title;
    private String contents;
    private String memberName;

    public HasAllConstructorArticle(long id, String title, String contents, String memberName) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.memberName = memberName;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", memberName='" + memberName + '\'' +
                '}';
    }
}
