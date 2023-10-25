package bback.module.poqh3.target.domain;

public class EmptyConstructorArticle {

    private long id;
    private String title;
    private String contents;
    private String memberName;

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
