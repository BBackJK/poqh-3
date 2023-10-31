package bback.module.poqh3.target.domain;

import java.time.LocalDateTime;

public class Member {

    private final String id;
    private final String name;
    private final LocalDateTime createdAt;

    private final int year;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Member(String id, String name, LocalDateTime createdAt, int year) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.year = year;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", year='" + year + '\'' +
                '}';
    }
}
