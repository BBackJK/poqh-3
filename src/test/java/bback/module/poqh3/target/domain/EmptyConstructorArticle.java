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

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }
}
