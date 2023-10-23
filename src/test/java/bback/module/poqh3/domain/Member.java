package bback.module.poqh3.domain;

public class Member {

    private final String id;
    private final String name;
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Member(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

}
